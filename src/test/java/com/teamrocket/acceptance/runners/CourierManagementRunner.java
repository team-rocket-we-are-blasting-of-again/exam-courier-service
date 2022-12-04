package com.teamrocket.acceptance.runners;

import com.teamrocket.service.AuthClient;
import com.teamrocket.service.CourierService;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.Mockito.when;

@CucumberContextConfiguration

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features/CourierManagement.feature")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(locations = {"classpath:applicationtest.properties"})

public class CourierManagementRunner {

    @MockBean
    private AuthClient authClient;



}
