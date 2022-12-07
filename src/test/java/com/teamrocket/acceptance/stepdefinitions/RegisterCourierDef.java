package com.teamrocket.acceptance.stepdefinitions;

import com.teamrocket.entity.Courier;
import com.teamrocket.exceptions.ResourceException;
import com.teamrocket.repository.CourierRepository;
import com.teamrocket.repository.DeliveryRepository;
import com.teamrocket.service.AuthClient;
import com.teamrocket.service.CourierService;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class RegisterCourierDef {
    private Courier courier;
    private int userID = 888;
    private String firstName = "Magda";
    private String lastName = "WAWRZAK";
    private String email = "my@mail.com";
    private String phone = "0700";
    private Courier courierEM;
    private Courier courierPH;
    private Exception em;
    private Exception ph;

    @Autowired
    private AuthClient authClient;

    @Autowired
    private CourierService courierService;

    @Autowired
    private CourierRepository courierRepository;

    @Before("@cleanUpDb")
    public void cleanupDb() {
        when(authClient.registerCourierUser(ArgumentMatchers.any())).thenReturn(userID);
        courierRepository.deleteAll();
    }

    @Given("a Courier with first name, last name, uniq email and uniq phone")
    public void a_courier_with_first_name_last_name_uniq_email_and_uniq_phone() {
        courier = Courier.builder().firstName(firstName).lastName(lastName).email(email).phone(phone).build();
    }

    @Then("New Courier is created with first name last name, email, phone")
    public void new_courier_is_created_with_first_name_last_name_email_phone() {
        courier = courierService.registerCourier(courier);
        assertTrue(
                courier.getFirstName().equals(firstName)
                        && courier.getLastName().equals(lastName)
                        && courier.getEmail().equals(email)
                        && courier.getPhone().equals(phone)
        );
    }

    @Given("a Courier with existing email")
    public void a_courier_with_existing_email() {
        courierEM = Courier.builder().firstName("").lastName("").email(email).phone("phone1").build();
    }

    @Given("a Courier with existing phone")
    public void a_courier_with_existing_phone() {
        courierPH = Courier.builder().firstName("").lastName("").email("mail987").phone(phone).build();
    }

    @When("Courier registers in the Service")
    public void courier_registers_in_the_service() {
        courier = courierService.registerCourier(courier);
    }

    @Then("New Courier has courier id and user id")
    public void new_courier_has_courier_id_and_user_id() {
        assertTrue(
                courier.getId() > 0 && courier.getUserId() == userID
        );
    }

    @When("Courier registers in the Service with invalid phone")
    public void courier_registers_in_the_service_with_invalid_phone() {
        courier = Courier.builder().firstName(firstName).lastName(lastName).email(email).phone(phone).build();
        courierService.registerCourier(courier);
        try {
            courierService.registerCourier(courierPH);
        } catch (ResourceException e) {
            ph = e;
        }
    }

    @When("Courier registers in the Service with invalid email")
    public void courier_registers_in_the_service_with_invalid_email() {
        courier = Courier.builder().firstName(firstName).lastName(lastName).email(email).phone(phone).build();
        courierService.registerCourier(courier);
        try {
            courierService.registerCourier(courierEM);
        } catch (ResourceException e) {
            em = e;
        }
    }

    @Then("ResourceException em is thrown")
    public void resource_exception_em_is_thrown() {
        assertTrue(em.getClass().equals(ResourceException.class));
    }

    @Then("ResourceException ph is thrown")
    public void resource_exception_ph_is_thrown() {
        assertTrue(ph.getClass().equals(ResourceException.class));
    }
}

