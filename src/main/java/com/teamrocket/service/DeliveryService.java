package com.teamrocket.service;

import com.teamrocket.entity.Delivery;
import com.teamrocket.enums.DeliveryStatus;
import com.teamrocket.model.ClaimRequest;
import com.teamrocket.model.CustomerDeliveryData;
import com.teamrocket.model.camunda.DeliveryTask;
import com.teamrocket.repository.DeliveryRepository;
import com.teamrocket.service.interfaces.IDeliveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class DeliveryService implements IDeliveryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryService.class);

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    CustomerClient customerClient;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public DeliveryTask claimDeliveryTask(ClaimRequest request, int couríerId) {

        LOGGER.info("Courier {} claims delivery of delivery {}", couríerId, request.getDeliveryId());

        Delivery delivery = deliveryRepository.findById(request.getDeliveryId())
                .orElseThrow(() -> new NoSuchElementException("No delivery with id " + request.getDeliveryId()));

        delivery.setCourierId(couríerId);
        delivery.setStatus(DeliveryStatus.ON_THE_WAY);
        delivery = deliveryRepository.save(delivery);

        //TODO execute camunda task
        return new DeliveryTask(delivery);
    }

    @Override
    public void publishNewDeliveryTask(DeliveryTask deliveryTask) {
        saveNewDelivery(deliveryTask);
        simpMessagingTemplate.convertAndSend(deliveryTask.getAreaId(), deliveryTask);
    }

    @Override
    public void sendNewDeliveryTasksToArea(String area) {
        //TODO once a courier assigns to an area, system should send all the available tasks
    }

    @Override
    public void monitOrderReadyToPickup() {
        /*
        TODO:
            listen on kafka event about orders ready to pickup

         */
    }

    private DeliveryTask saveNewDelivery(DeliveryTask deliveryTask) {
        deliveryTask = collectDropOffData(deliveryTask);

        Delivery delivery = new Delivery(deliveryTask);

        delivery.setStatus(DeliveryStatus.NEW);
        delivery = deliveryRepository.save(delivery);

        return new DeliveryTask(delivery);
    }

    private DeliveryTask collectDropOffData(DeliveryTask deliveryTask) {
        CustomerDeliveryData customerData = customerClient.
                getCustomerDeliveryData(deliveryTask.getOrderId());

        deliveryTask.setCustomerName(customerData.getCustomerName());
        deliveryTask.setCustomerPhone(customerData.getCustomerPhone());
        deliveryTask.setDropOffAddressId(customerData.getDropOfAddressId());

        return deliveryTask;
    }
}
