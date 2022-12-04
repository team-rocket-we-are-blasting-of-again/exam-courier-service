package com.teamrocket.acceptance.config;

import com.teamrocket.service.AuthClient;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@AutoConfigureMockMvc
@CucumberContextConfiguration
@TestPropertySource(locations = {"classpath:applicationtest.properties"})

public class CucumberSpringContextConfig {
    @MockBean
    private AuthClient authClient;


}
