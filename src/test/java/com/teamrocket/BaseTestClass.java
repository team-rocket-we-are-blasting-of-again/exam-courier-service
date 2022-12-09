package com.teamrocket;

import com.teamrocket.control.CourierController;
import com.teamrocket.entity.Courier;
import com.teamrocket.repository.CourierRepository;
import com.teamrocket.clients.AuthClient;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
@AutoConfigureMessageVerifier
@TestPropertySource(locations = {"classpath:applicationtest.properties"})

public class BaseTestClass {
    @MockBean
    private AuthClient authClient;
    @Autowired
    private CourierController courierController;
    @Autowired
    CourierRepository courierRepository;

    @Before
    public void setup() {
        when(authClient.registerCourierUser(ArgumentMatchers.any())).thenReturn(888);
        courierRepository.deleteAll();
        courierRepository.save(Courier.builder().firstName("Anna").lastName("Panna").email("used@mail.com").userId(999999999).phone("contract").build());
        StandaloneMockMvcBuilder standaloneMockMvcBuilder
                = MockMvcBuilders.standaloneSetup(courierController);
        RestAssuredMockMvc.standaloneSetup(standaloneMockMvcBuilder);
    }
}