package com.teamrocket.service;

import com.google.gson.Gson;
import com.teamrocket.clients.CustomerClient;
import com.teamrocket.entity.CamundaOrderTask;
import com.teamrocket.entity.Delivery;
import com.teamrocket.enums.DeliveryStatus;
import com.teamrocket.enums.Topic;
import com.teamrocket.exceptions.ResourceException;
import com.teamrocket.model.CustomerDeliveryData;
import com.teamrocket.model.DeliveryRequest;
import com.teamrocket.model.camunda.*;
import com.teamrocket.model.kafka.OrderKafkaMsg;
import com.teamrocket.repository.CamundaRepo;
import com.teamrocket.repository.DeliveryRepository;
import com.teamrocket.service.interfaces.IDeliveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class DeliveryService implements IDeliveryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryService.class);

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private CamundaRepo camundaRepo;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CustomerClient customerClient;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private Gson GSON;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("${camunda.bpm.client.base-url}")
    private String restEngine;

    @Override
    public DeliveryTask claimDeliveryTask(DeliveryRequest request, int couríerId) {

        LOGGER.info("Courier {} claims delivery  {}", couríerId, request.getDeliveryId());

        Delivery delivery = deliveryRepository.findById(request.getDeliveryId())
                .orElseThrow(() -> new NoSuchElementException("No delivery with id " + request.getDeliveryId()));

        delivery.setCourierId(couríerId);
        delivery.setStatus(DeliveryStatus.ON_THE_WAY);
        delivery = deliveryRepository.save(delivery);
        DeliveryTask deliveryTask = new DeliveryTask(delivery);
        kafkaTemplate.send(Topic.ORDER_CLAIMED.toString(), new OrderKafkaMsg(delivery.getOrderId()));
        completeCamundaPickupTask(deliveryTask);
        sendNewDeliveryTasksToArea(delivery.getAreaId());
        return deliveryTask;
    }

    @Override
    public DeliveryTask saveAndPublishNewDeliveryTask(DeliveryTask deliveryTask) {
        LOGGER.info(" Delivery task to be published: {}", deliveryTask);
        deliveryTask = saveNewDelivery(deliveryTask);
        sendNewDeliveryTasksToArea(deliveryTask.getAreaId());
        LOGGER.info("Delivery task sent to websocket {}", deliveryTask);
        return deliveryTask;
    }

    @Override
    public void sendNewDeliveryTasksToArea(String areaId) {
        Set<DeliveryTask> tasks = new HashSet<>();
        deliveryRepository.findAllByAreaIdAndStatus(areaId, DeliveryStatus.NEW).forEach(d -> {
            tasks.add(new DeliveryTask(d));
        });
        simpMessagingTemplate
                .convertAndSend("/delivery/" + areaId.toLowerCase(), tasks);
    }

    @Override
    public void handleOrderReadyEvent(int orderId) {

        List<Delivery> readyToPickup = deliveryRepository.findAllByOrderIdAndStatus(orderId, DeliveryStatus.NEW);
        if (readyToPickup.size() < 1) {
            throw new NoSuchElementException("No delivery task for order id " + orderId);
        }
        if (readyToPickup.size() > 1) {
            LOGGER.warn("More than one delivery for given order id {} ", orderId);
            throw new ResourceException("More than one delivery for order id "
                    + orderId);
        }
        Delivery delivery = readyToPickup.get(0);
        simpMessagingTemplate
                .convertAndSend("/delivery/" + delivery.getCourierId() + "ready", new DeliveryTask(delivery));
    }

    @Override
    public DeliveryTask handleDropOff(DeliveryRequest request, int courierId) {
        Delivery delivery = deliveryRepository.findById(request.getDeliveryId()).
                orElseThrow(() -> new NoSuchElementException("No delivery with id " + request.getDeliveryId()));
        if (delivery.getCourierId() != courierId) {
            throw new ResourceException(String
                    .format("Courier with courierId %d is not assigned to the delivery " +
                                    "with deliveryId: %d. Assigned courier is courierID %d",
                            courierId, request.getDeliveryId(), delivery.getCourierId()));
        }
        if (!delivery.getStatus().equals(DeliveryStatus.ON_THE_WAY)) {
            throw new ResourceException(String
                    .format("Delivery status with deliveryId %d is not 'ON_THE_WAY', actual status is '%s'",
                            delivery.getId(), delivery.getStatus().toString()));
        }

        delivery.setStatus(DeliveryStatus.COMPLETED);
        delivery = deliveryRepository.save(delivery);
        kafkaTemplate.send(Topic.ORDER_DELIVERED.toString(), new OrderKafkaMsg(delivery.getOrderId()));
        return new DeliveryTask(delivery);
    }

    private DeliveryTask saveNewDelivery(DeliveryTask deliveryTask) {
        deliveryTask = collectDropOffData(deliveryTask);
        Delivery delivery = new Delivery(deliveryTask);
        delivery.setStatus(DeliveryStatus.NEW);
        try {
            LOGGER.info("New Delivery to be saved in DB: {}", delivery);

            delivery = deliveryRepository.save(delivery);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error(e.getMessage());
            throw new ResourceException("Delivery could not be saved - Delivery for order id "
                    + delivery.getOrderId() + " already exists");
        }
        LOGGER.info("New delivery saved {} ", delivery);
        return new DeliveryTask(delivery);
    }

    private DeliveryTask collectDropOffData(DeliveryTask deliveryTask) {
        CustomerDeliveryData customerData = customerClient.
                getCustomerDeliveryData(deliveryTask.getOrderId());

        deliveryTask.setCustomerName(customerData.getCustomerName());
        deliveryTask.setCustomerPhone(customerData.getCustomerPhone());
        deliveryTask.setDropOffAddressId(customerData.getDropOfAddressId());
        LOGGER.info("Collected data from Customer Service: {}", deliveryTask);
        return deliveryTask;
    }

    private void completeCamundaPickupTask(DeliveryTask deliveryTask) {
        int orderId = deliveryTask.getOrderId();

        Optional<CamundaOrderTask> optionalTask = camundaRepo.findById(orderId);
        if (!optionalTask.isPresent()) {
            LOGGER.error("No Pickup task defined for orderId: " + orderId);
            return;
        }
        CamundaOrderTask task = optionalTask.get();

        String url = new StringBuilder(restEngine)
                .append("/external-task/")
                .append(task.getTaskId())
                .append("/complete")
                .toString();

        String requestBody = buildTaskVariables(task.getWorkerId(), deliveryTask);

        LOGGER.info("FIRE TASK URL: {}", url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<MediaType> mediaTypeList = new ArrayList();
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        headers.setAccept(mediaTypeList);
        HttpEntity<String> request =
                new HttpEntity<>(requestBody, headers);

        restTemplate.postForEntity(url, request, String.class);
        LOGGER.info("completeCamundaPickupTask with variables: {}", requestBody);
    }

    private String buildTaskVariables(String workerId, DeliveryTask deliveryTask) {
        DeliveryTaskHolder taskHolder = new DeliveryTaskHolder(deliveryTask.toJsonString());
        OrderIdHolder orderIdHolder = new OrderIdHolder(deliveryTask.getOrderId());
        Variables variables = new Variables(taskHolder, orderIdHolder);
        TaskVariables taskVariables = new TaskVariables(workerId, variables);
        return GSON.toJson(taskVariables, TaskVariables.class);
    }

    public List<DeliveryTask> getClaimedTasks(int courierId) {
        List<DeliveryTask> claimedTasks = new ArrayList<>();
        List<Delivery> claimedDeliveries = deliveryRepository.findAllByCourierIdAndStatus(courierId, DeliveryStatus.ON_THE_WAY);
        claimedDeliveries.forEach(d -> claimedTasks.add(new DeliveryTask(d)));
        return claimedTasks;
    }

}
