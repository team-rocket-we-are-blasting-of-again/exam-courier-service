package com.teamrocket.acceptance.config;

import com.teamrocket.repository.CourierRepository;
import com.teamrocket.service.AuthClient;
import io.cucumber.java.AfterAll;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.Mockito.reset;

@SpringBootTest
@AutoConfigureMockMvc
@CucumberContextConfiguration
@TestPropertySource(locations = {"classpath:applicationtest.properties"})

public class CucumberSpringContextConfig {
    @MockBean
    private AuthClient authClient;
    @Autowired
    CourierRepository courierRepository;
    @AfterAll
    void cleanUp() {
        reset(authClient);
        courierRepository.deleteAll();
    }

}
