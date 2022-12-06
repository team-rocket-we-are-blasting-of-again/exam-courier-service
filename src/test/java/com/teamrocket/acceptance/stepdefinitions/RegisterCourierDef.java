package com.teamrocket.acceptance.stepdefinitions;

import com.teamrocket.entity.Courier;
import com.teamrocket.exceptions.ResourceException;
import com.teamrocket.repository.CourierRepository;
import com.teamrocket.service.AuthClient;
import com.teamrocket.service.CourierService;
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
    private Exception exception;

    @Autowired
    @MockBean
    private AuthClient authClient;
    @Autowired
    CourierService courierService;

    @Autowired
    CourierRepository courierRepository;

    @Given("a Courier with first name {string}, last name {string}, uniq email {string} and uniq phone {string}")
    public void a_courier_with_first_name_last_name_uniq_email_and_uniq_phone(String firstName, String lastName, String email, String phone) {
        courier = Courier.builder().firstName(firstName).lastName(lastName).email(email).phone(phone).build();
    }

    @Given("a Courier with first name {string}, last name {string}, existing email {string} or existing phone {string}")
    public void a_courier_with_first_name_last_name_existing_email_or_existing_phone(String firstName, String lastName, String email, String phone) {
        courierRepository.deleteAll();
        courierRepository.save(Courier.builder().firstName(firstName).lastName(lastName).email(email).phone("xoxo").userId(18).build());
        courier = courierRepository.save(Courier.builder().firstName(firstName).lastName(lastName).email("WOW").phone(phone).userId(7).build());
        try {
            courierService.registerCourier(Courier.builder().firstName(firstName).lastName(lastName).email(email).userId(9).build());
        } catch (ResourceException e) {
            exception = e;
        }
    }

    @When("Courier registers in the Service")
    public void courier_registers_in_the_service() {
        when(authClient.registerCourierUser(ArgumentMatchers.any())).thenReturn(888);
        courier = courierService.registerCourier(courier);
    }

    @Then("New Courier is created with first name {string}, last name {string}, email {string}, phone {string}")
    public void new_courier_is_created_with_first_name_last_name_email_phone(String firstName, String lastName, String email, String phone) {
        assertTrue(
                courier.getFirstName().equals(firstName)
                        && courier.getLastName().equals(lastName)
                        && courier.getEmail().equals(email)
                        && courier.getPhone().equals(phone)
        );
    }

    @Then("New Courier has courier id and user id")
    public void new_courier_has_courier_id_and_user_id() {
        assertTrue(
                courier.getId() > 0 && courier.getUserId() == 888
        );
    }

    @Then("ResourceException is thrown")
    public void resource_exception_is_thrown() {
        assertTrue(exception.getClass().equals(ResourceException.class));
    }

//    @Given("valid Courier")
//    public void valid_courier() {
//        courier = Courier.builder().firstName("Hanna").lastName("Wawrzak").email("hanna@mail.com").phone("99999").build();
//    }
//
//    @Then("Kafka event is emitted")
//    public void kafka_event_is_emitted() {
//        System.out.println("KAFKA EVENT EMITTED ???");
//        throw new io.cucumber.java.PendingException();
//    }

}
