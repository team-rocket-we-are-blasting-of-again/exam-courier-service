package com.teamrocket.acceptance.runners;

import com.teamrocket.service.AuthClient;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

@CucumberContextConfiguration

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features/CourierManagement.feature")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(locations = {"classpath:applicationtest.properties"})

public class CourierManagementRunner {

    @MockBean
    private AuthClient authClient;


}
