package com.teamrocket.acceptance.config;

import com.teamrocket.clients.AuthClient;
import com.teamrocket.clients.CustomerClient;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.TestPropertySource;

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
