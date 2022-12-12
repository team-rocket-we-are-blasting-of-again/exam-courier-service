package com.teamrocket.unit.clients;

import com.teamrocket.clients.AuthClient;
import com.teamrocket.entity.Courier;
import com.teamrocket.proto.CreateUserRequest;
import com.teamrocket.proto.CreateUserResponse;
import com.teamrocket.proto.UserGrpc;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(locations = {"classpath:applicationtest.properties"})
class AuthClientTest {

    @MockBean
    private UserGrpc.UserBlockingStub mock;

    @Autowired
    private AuthClient sut;

    private final int userID = -88;


    @BeforeEach
    void setUp() {
        CreateUserResponse response = CreateUserResponse.newBuilder().setId(userID).build();
        when(mock.getChannel()).thenReturn(null);
        when(mock.createUser(ArgumentMatchers.any(CreateUserRequest.class))).thenReturn(response);
    }

    @AfterEach
    void tearDown() {
        reset(mock);
    }

    @Test
    void registerCourierUserTest() {
        assertEquals(userID, sut.registerCourierUser(Courier.builder().id(-2).email("mail").build()));
    }

    @Test
    void registerCourierUserWithNoEmailTest() {
        assertThrows(NullPointerException.class, () -> {
            sut.registerCourierUser(Courier.builder().id(-2).build());
        }, "NullPointerException was expected");
    }

    @Test
    void registerCourierUserWithNoIdTest() {
        assertThrows(NullPointerException.class, () -> {
            sut.registerCourierUser(Courier.builder().email("sas").build());
        }, "NullPointerException was expected");
    }
}