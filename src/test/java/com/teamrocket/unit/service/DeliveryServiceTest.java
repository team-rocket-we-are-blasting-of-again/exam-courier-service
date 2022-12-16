package com.teamrocket.unit.service;

import com.google.gson.Gson;
import com.teamrocket.clients.CustomerClient;
import com.teamrocket.entity.CamundaOrderTask;
import com.teamrocket.entity.Delivery;
import com.teamrocket.enums.DeliveryStatus;
import com.teamrocket.enums.Topic;
import com.teamrocket.exceptions.ResourceException;
import com.teamrocket.model.CustomerDeliveryData;
import com.teamrocket.model.DeliveryRequest;
import com.teamrocket.model.camunda.DeliveryTask;
import com.teamrocket.repository.CamundaRepo;
import com.teamrocket.repository.DeliveryRepository;
import com.teamrocket.service.DeliveryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(locations = {"classpath:applicationtest.properties"})
@RunWith(MockitoJUnitRunner.class)
class DeliveryServiceTest {
    @Autowired
    private DeliveryService sut;

    @MockBean
    private DeliveryRepository deliveryRepository;

    @MockBean
    private CamundaRepo camundaRepo;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private CustomerClient customerClient;

    @MockBean
    private SimpMessagingTemplate simpMessagingTemplate;

    @MockBean
    private KafkaTemplate kafkaTemplate;


    @Autowired
    private Gson GSON;

    private Delivery delivery;
    private final String area = "CPH";
    private int orderId = 9898;
    private final String custName = "Hanna";
    private final String custPhone = "1213";
    private final int pickupTime = 12344566;
    private final int restAddId = 2468;
    private final int dropAddId = 369;
    private final String restName = "Pizzza Mario";
    private final int deliveryId = -2;
    private final int courierId = 2345;
    private final int multipleDeliveriesId = -555;
    private CustomerDeliveryData deliveryData;

    @BeforeEach
    void setUp() {
        delivery = Delivery
                .builder()
                .courierId(courierId)
                .status(DeliveryStatus.ON_THE_WAY)
                .orderId(orderId)
                .restaurantName(restName)
                .restaurantAddressId(restAddId)
                .dropOffAddressId(dropAddId)
                .areaId(area)
                .pickupTime(pickupTime)
                .customerName(custName)
                .customerPhone(custPhone)
                .id(deliveryId)
                .build();

        deliveryData = CustomerDeliveryData
                .builder()
                .customerName(custName)
                .customerPhone(custPhone)
                .customerPhone(custPhone)
                .build();
        List<Delivery> oneDelivery = new ArrayList();
        oneDelivery.add(delivery);
        List<Delivery> multipleDeliveries = new ArrayList();
        multipleDeliveries.add(Delivery.builder().id(12312).build());
        multipleDeliveries.add(Delivery.builder().id(12313).build());

        when(deliveryRepository.findAllByAreaIdAndStatus(area, DeliveryStatus.NEW)).thenReturn(oneDelivery);
        when(deliveryRepository.findAllByOrderIdAndStatus(orderId, DeliveryStatus.NEW)).thenReturn(oneDelivery);
        when(deliveryRepository.findAllByOrderIdAndStatus(eq(multipleDeliveriesId), any())).thenReturn(multipleDeliveries);
        when(deliveryRepository.findById(deliveryId)).thenReturn(Optional.of(delivery));
        when(deliveryRepository.save(any())).thenReturn((delivery));
        doNothing().when(simpMessagingTemplate).convertAndSend(ArgumentMatchers.anyString(), ArgumentMatchers.anyCollection());
        when(kafkaTemplate.send(eq(Topic.ORDER_CLAIMED.toString()), ArgumentMatchers.any())).thenReturn(null);
        when(kafkaTemplate.send(eq(Topic.ORDER_DELIVERED.toString()), ArgumentMatchers.any())).thenReturn(null);
        when(restTemplate.postForEntity(anyString(), any(), any())).thenReturn(null);
        when(customerClient.getCustomerDeliveryData(orderId)).thenReturn(deliveryData);
    }

    @AfterEach
    void tearDown() {
        reset(deliveryRepository);
        reset(simpMessagingTemplate);
        reset(kafkaTemplate);
        reset(restTemplate);
        reset(customerClient);
    }

    @Test
    void claimDeliveryTask_postsCompleteTask() {
        CamundaOrderTask orderTask = new CamundaOrderTask(
                orderId, "processID",
                "defKey",
                "taskId",
                "WorkerId");
        when(camundaRepo.findById(any())).thenReturn(Optional.of(orderTask));

        DeliveryRequest delReq = new DeliveryRequest(delivery.getId());
        DeliveryTask deliveryTask = sut.claimDeliveryTask(delReq, courierId);

        verify(restTemplate, times(1)).postForEntity(anyString(), any(), any());

    }

    @Test
    void claimDeliveryTask() {
        when(camundaRepo.findById(any())).thenReturn(Optional.empty());

        DeliveryRequest delReq = new DeliveryRequest(delivery.getId());
        DeliveryTask deliveryTask = sut.claimDeliveryTask(delReq, courierId);
        assertTrue("Delivery task should have data of delivery saved in db",
                deliveryTask.getAreaId().equals(area) &&
                        deliveryTask.getOrderId() == orderId &&
                        deliveryTask.getDropOffAddressId() == dropAddId &&
                        deliveryTask.getPickupTime() == pickupTime &&
                        deliveryTask.getCustomerName().equals(custName) &&
                        deliveryTask.getRestaurantName().equals(restName) &&
                        deliveryTask.getCustomerPhone().equals(custPhone));

        verify(deliveryRepository, times(1)).findById(deliveryId);
        verify(deliveryRepository, times(1)).save(delivery);

    }

    @Test
    void saveAndPublishNewDeliveryTask() {
        DeliveryTask deliveryTask = DeliveryTask
                .builder()
                .orderId(orderId)
                .restaurantName(restName)
                .restaurantAddressId(restAddId)
                .areaId(area)
                .pickupTime(pickupTime)
                .build();

        deliveryTask = sut.saveAndPublishNewDeliveryTask(deliveryTask);
        assertTrue("Delivery task should have data of delivery saved in db",
                deliveryTask.getAreaId().equals(area) &&
                        deliveryTask.getOrderId() == orderId &&
                        deliveryTask.getDropOffAddressId() == dropAddId &&
                        deliveryTask.getPickupTime() == pickupTime &&
                        deliveryTask.getCustomerName().equals(custName) &&
                        deliveryTask.getRestaurantName().equals(restName) &&
                        deliveryTask.getCustomerPhone().equals(custPhone));

        verify(simpMessagingTemplate, times(1))
                .convertAndSend(ArgumentMatchers.anyString(), ArgumentMatchers.anyCollection());

        verify(deliveryRepository, times(1)).save(any());
        verify(deliveryRepository, times(1))
                .findAllByAreaIdAndStatus(area, DeliveryStatus.NEW);
    }

    @Test
    void saveAndPublishNewDeliveryTask_throwsResourceException() {
        int doubleOrderId = 787;
        DeliveryTask deliveryTask = DeliveryTask
                .builder()
                .orderId(doubleOrderId)
                .restaurantName(restName)
                .restaurantAddressId(restAddId)
                .areaId(area)
                .pickupTime(pickupTime)
                .build();

        when(deliveryRepository.save(any()))
                .thenThrow(new DataIntegrityViolationException("Delivery with order id " + multipleDeliveriesId + "already exists"));
        when(customerClient.getCustomerDeliveryData(doubleOrderId)).thenReturn(deliveryData);


        assertThrows(ResourceException.class, () -> {
            sut.saveAndPublishNewDeliveryTask(deliveryTask);
        }, "Delivery with order id " + multipleDeliveriesId + "already exists");
    }


    @Test
    void sendNewDeliveryTasksToArea() {
        sut.sendNewDeliveryTasksToArea(area);
        verify(simpMessagingTemplate, times(1))
                .convertAndSend(ArgumentMatchers.anyString(), ArgumentMatchers.anyCollection());
        verify(deliveryRepository, times(1))
                .findAllByAreaIdAndStatus(area, DeliveryStatus.NEW);
    }

    @Test
    void handleOrderReadyEvent() {
        sut.handleOrderReadyEvent(orderId);
        verify(deliveryRepository, times(1))
                .findAllByOrderIdAndStatus(orderId, DeliveryStatus.NEW);
    }

    @Test
    void handleOrderReadyEvent_throwsNoSuchElementException() {
        assertThrows(NoSuchElementException.class, () -> {
            sut.handleOrderReadyEvent(-777);
        }, "A delivery task with order id -777 was found, but was not expected");
    }

    @Test
    void handleOrderReadyEvent_throwsResourceException() {
        assertThrows(ResourceException.class, () -> {
            sut.handleOrderReadyEvent(multipleDeliveriesId);
        }, "There was not more than one delivery for given order id "
                + multipleDeliveriesId
                + ", but expected two");
    }

    @Test
    void handleDropOff() {
        DeliveryRequest request = new DeliveryRequest(deliveryId);
        DeliveryTask deliveryTask = sut.handleDropOff(request, courierId);

        assertTrue("Delivery task should have data of delivery saved in db",
                deliveryTask.getAreaId().equals(area) &&
                        deliveryTask.getOrderId() == orderId &&
                        deliveryTask.getDropOffAddressId() == dropAddId &&
                        deliveryTask.getPickupTime() == pickupTime &&
                        deliveryTask.getCustomerName().equals(custName) &&
                        deliveryTask.getRestaurantName().equals(restName) &&
                        deliveryTask.getCustomerPhone().equals(custPhone));

        verify(deliveryRepository, times(1)).save(any());
        verify(deliveryRepository, times(1))
                .findById(deliveryId);
    }

    @Test
    void handleDropOff_whenIncorrectCourierId_throwsResourceException() {
        DeliveryRequest request = new DeliveryRequest(deliveryId);
        assertThrows(ResourceException.class, () -> {
            sut.handleDropOff(request, courierId - 100);
        }, "Courier id on deliver should not much provided courier id but it does");
    }

    @Test
    void handleDropOff_whenIncorrectStatus_throwsResourceException() {
        delivery.setStatus(DeliveryStatus.COMPLETED);
        when(deliveryRepository.findById(deliveryId)).thenReturn(Optional.of(delivery));
        DeliveryRequest request = new DeliveryRequest(deliveryId);
        assertThrows(ResourceException.class, () -> {
            sut.handleDropOff(request, courierId);
        }, "Delivery Status should expected not to be ON_THE_WAY");
    }

}