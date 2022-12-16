package com.teamrocket.acceptance.stepdefinitions;

import com.google.gson.Gson;
import com.teamrocket.control.CourierController;
import com.teamrocket.entity.Courier;
import com.teamrocket.entity.Delivery;
import com.teamrocket.enums.DeliveryStatus;
import com.teamrocket.model.DeliveryRequest;
import com.teamrocket.model.camunda.DeliveryTask;
import com.teamrocket.repository.CourierRepository;
import com.teamrocket.repository.DeliveryRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class DropOffOrderDef {
    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private CourierController courierController;

    @Autowired
    KafkaTemplate kafkaTemplate;

    @Autowired
    private Gson GSON;

    private Courier courier;
    private Delivery delivery;

    private DeliveryTask deliveryTask;
    private int orderId;
    private final String custName = "Hanna";
    private final String custPhone = "1213";
    private final String area = "CPH";
    private final int pickupTime = 12344566;
    private final int restAddId = 2468;
    private final int dropAddId = 369;
    private final String restName = "Pizzza Mario";

    private MockMvc mvc;
    private HttpHeaders headers = new HttpHeaders();
    private MockHttpServletResponse response;

    @Before("@cleanUpDb")
    public void cleanupDb() {
        courierRepository.deleteAll();
        deliveryRepository.deleteAll();
        when(kafkaTemplate.send(ArgumentMatchers.anyString(), any())).thenReturn(null);
    }

    @Given("A courier and a delivery task that has been picked up by that courier")
    public void a_courier_and_a_delivery_task_that_has_been_picked_up_by_that_courier() {
        courier = courierRepository.save(Courier
                .builder()
                .email("mail")
                .firstName("Magda")
                .lastName("Wawrzak")
                .email("mail")
                .phone("532765327")
                .build());

        delivery = deliveryRepository.save(Delivery
                .builder()
                .courierId(courier.getId())
                .status(DeliveryStatus.ON_THE_WAY)
                .orderId(orderId)
                .restaurantName(restName)
                .restaurantAddressId(restAddId)
                .dropOffAddressId(dropAddId)
                .areaId(area)
                .pickupTime(pickupTime)
                .customerName(custName)
                .customerPhone(custPhone)
                .build());
    }

    @When("Courier drops of the order of that task")
    public void courier_drops_of_the_order_of_that_task() throws Exception {

        MockitoAnnotations.openMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(courierController)
                .build();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("role_id", courier.getId().toString());
        DeliveryRequest request = new DeliveryRequest(delivery.getId());
        String requestBody = GSON.toJson(request);
        response = mvc.perform(post("/drop")
                        .content(requestBody)
                        .headers(headers)
                )
                .andReturn().getResponse();

        deliveryTask = GSON.fromJson(response.getContentAsString(), DeliveryTask.class);
    }

    @Then("Delivery status is sat to COMPLETED and system is notified")
    public void delivery_status_is_sat_to_completed_and_system_is_notified() {
        verify(kafkaTemplate, times(1)).send(ArgumentMatchers.anyString(), any());
        DeliveryStatus newStatus = deliveryRepository.findById(delivery.getId()).get().getStatus();
        assertEquals("Delivery status should be COMPLETED", DeliveryStatus.COMPLETED, newStatus);
    }

    @Then("DeliveryTask has customer data, restaurant data and order id")
    public void delivery_task_has_data() {
        assertTrue("Delivery task should have data of delivery saved in db",
                deliveryTask.getAreaId().equals(area) &&
                        deliveryTask.getOrderId() == orderId &&
                        deliveryTask.getDropOffAddressId() == dropAddId &&
                        deliveryTask.getPickupTime() == pickupTime &&
                        deliveryTask.getCustomerName().equals(custName) &&
                        deliveryTask.getRestaurantName().equals(restName) &&
                        deliveryTask.getCustomerPhone().equals(custPhone));
    }

}
