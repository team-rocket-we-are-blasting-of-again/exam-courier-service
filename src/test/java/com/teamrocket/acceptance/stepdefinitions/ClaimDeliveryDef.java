package com.teamrocket.acceptance.stepdefinitions;

import com.google.gson.Gson;
import com.teamrocket.clients.CustomerClient;
import com.teamrocket.control.CourierController;
import com.teamrocket.entity.Courier;
import com.teamrocket.entity.Delivery;
import com.teamrocket.enums.DeliveryStatus;
import com.teamrocket.model.ClaimRequest;
import com.teamrocket.model.CustomerDeliveryData;
import com.teamrocket.repository.CourierRepository;
import com.teamrocket.repository.DeliveryRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class ClaimDeliveryDef {

    private Delivery delivery;
    private Courier courier;
    private int orderId;

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private CourierController courierController;

    @Autowired
    CustomerClient customerClient;

    @Autowired
    private Gson GSON;

    private MockMvc mvc;
    private HttpHeaders headers = new HttpHeaders();
    private MockHttpServletResponse response;

    @Before("@cleanUpDb")
    public void cleanupDb() {
        courierRepository.deleteAll();
        deliveryRepository.deleteAll();
        when(customerClient.getCustomerDeliveryData(orderId)).thenReturn(customerdata());

    }

    @Given("a delivery received from restaurant with area {string}")
    public void a_delivery_task_received_from_restaurant_in_area(String area) {
        delivery = deliveryRepository.save(Delivery
                .builder()
                .areaId(area)
                .orderId(orderId)
                .build());
    }

    @Given("a courier that is online and assigned to area {string}")
    public void a_courier_that_is_online_and_assigned_to_area(String area) {
        courier = Courier.builder()
                .areaId(area)
                .phone("123474")
                .firstName("Magda")
                .lastName("Wawrzak")
                .email("clim@mail")
                .online(true)
                .userId(77)
                .build();
        courier = courierRepository.save(courier);
    }

    @When("courier claims a task")
    public void courier_claims_a_task() throws Exception {

        MockitoAnnotations.openMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(courierController)
                .build();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("role_id", courier.getId().toString());
        ClaimRequest request = new ClaimRequest(delivery.getId());
        String requestBody = GSON.toJson(request);
        response = mvc.perform(post("/claim")
                        .content(requestBody)
                        .headers(headers)
                )
                .andReturn().getResponse();
    }

    @Then("Delivery is assigned to courier and has status ON_THE_WAY")
    public void delivery_is_assigned_to_courier_and_has_status_in_progress() throws IOException {
        delivery = deliveryRepository.findById(delivery.getId()).get();
        assertTrue("delivery has courier Id and status ON_THE_WAY",
                delivery.getCourierId() == courier.getId()
                        && delivery.getStatus().equals(DeliveryStatus.ON_THE_WAY));
    }

    @Then("Response status is OK")
    public void response_status_is_ok() {
        assertTrue("Response status is ok", (response.getStatus() == HttpStatus.OK.value()));

    }

    private CustomerDeliveryData customerdata() {
        return CustomerDeliveryData
                .builder()
                .customerName("Magda")
                .customerPhone("89797845")
                .dropOfAddressId(78797)
                .build();
    }

}
