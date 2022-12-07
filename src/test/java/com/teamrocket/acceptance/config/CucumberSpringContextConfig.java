package com.teamrocket.acceptance.config;

import com.teamrocket.repository.CourierRepository;
import com.teamrocket.repository.DeliveryRepository;
import com.teamrocket.service.AuthClient;
import com.teamrocket.service.CustomerClient;
import io.cucumber.java.BeforeAll;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@CucumberContextConfiguration
@TestPropertySource(locations = {"classpath:applicationtest.properties"})

public class CucumberSpringContextConfig {
    @MockBean
    private AuthClient authClient;
    @MockBean
    private KafkaTemplate kafkaTemplate;
    @MockBean
    private CustomerClient customerClient;

}
