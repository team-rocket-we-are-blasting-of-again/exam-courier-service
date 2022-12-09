package com.teamrocket.integration.service;

import com.teamrocket.entity.Courier;
import com.teamrocket.proto.CreateUserResponse;
import com.teamrocket.proto.UserGrpc;
import com.teamrocket.clients.AuthClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(locations = {"classpath:applicationtest.properties"})
@RunWith(MockitoJUnitRunner.class)
class AuthClientTest {
    @MockBean
    private UserGrpc.UserBlockingStub userBlockingStub;

    @Autowired
    private AuthClient sut;
    private int userID;

    @BeforeEach
    void setUp() {
        userID = 888;
        CreateUserResponse response = CreateUserResponse.newBuilder().setId(userID).build();
        when(userBlockingStub.createUser(any())).thenReturn(response);
    }

    @AfterEach
    void tearDown() {
        reset(userBlockingStub);
    }

    @Test
    void registerCourierUser() {
        Courier courier = Courier.builder()
                .id(789789)
                .email("mail")
                .build();

        assertEquals(userID, sut.registerCourierUser(courier));
    }
}