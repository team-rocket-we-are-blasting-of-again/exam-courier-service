//package com.teamrocket.acceptance.stepdefinitions;
//
//import com.teamrocket.entity.Courier;
//import com.teamrocket.service.AuthClient;
//import com.teamrocket.service.CourierService;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import org.mockito.ArgumentMatchers;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.when;
//
//public class CourierManagementDef {
//    Courier courier;
//    @Autowired
//    @MockBean
//    private AuthClient authClient;
//    @Autowired
//    CourierService courierService;
//
//
//    @Given("a Courier with first name {string}, last name {string} and uniq email {string}")
//    public void a_courier_with_first_name_last_name_and_uniq_email(String firstName, String lastName, String email) {
//        courier = new Courier(firstName, lastName, email);
//
//    }
//
//    @When("Courier registers in the Service")
//    public void courier_registers_in_the_service() {
//        when(authClient.registerCourierUser(ArgumentMatchers.any())).thenReturn(999);
//
//        courier = courierService.registerCourier(courier);
//
//    }
//
//    @Then("New Courier is created with with first name {string}, last name {string}, email {string}")
//    public void new_courier_is_created_with_with_first_name_last_name_email(String firstName, String lastName, String email) {
//        assertTrue(
//                courier.getFirstName().equals(firstName)
//                        && courier.getLastName().equals(lastName)
//                        && courier.getEmail().equals(email)
//        );
//    }
//
//    @Then("New Courier has courier id and user id")
//    public void new_courier_has_courier_id_and_user_id() {
//        assertTrue(
//                courier.getId() > 0 //&& courier.getUserId > 0
//        );
//    }
//}
