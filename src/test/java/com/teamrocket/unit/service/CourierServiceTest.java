package com.teamrocket.unit.service;

import com.teamrocket.clients.AuthClient;
import com.teamrocket.entity.Courier;
import com.teamrocket.exceptions.ResourceException;
import com.teamrocket.model.courier.CourierDTO;
import com.teamrocket.model.courier.RegisterCourierRequest;
import com.teamrocket.repository.CourierRepository;
import com.teamrocket.service.CourierService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(locations = {"classpath:applicationtest.properties"})
@RunWith(MockitoJUnitRunner.class)
class CourierServiceTest {

    @Mock
    private KafkaTemplate kafkaMock;

    @MockBean
    private CourierRepository repositoryMock;

    @MockBean
    private AuthClient authClientMock;


    @Autowired
    private CourierService sut;

    private Courier courier;
    private final String fname = "A";
    private final String lname = "B";
    private final String phone = "C";
    private final String email = "D";
    private final int id = 999;
    private final int userID = 99;
    private RegisterCourierRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = RegisterCourierRequest.builder()
                .email(email)
                .firstName(fname)
                .lastName(lname)
                .phone(phone)
                .build();
        courier = Courier.builder()
                .email(email)
                .phone(phone)
                .firstName(fname)
                .lastName(lname)
                .id(id)
                .build();

        when(kafkaMock.send(ArgumentMatchers.anyString(), ArgumentMatchers.any())).thenReturn(null);
        when(authClientMock.registerCourierUser(anyString(), anyInt(), ArgumentMatchers.anyString())).thenReturn(userID);

    }

    @AfterEach
    void tearDown() {
        reset(kafkaMock);
        reset(repositoryMock);
        reset(authClientMock);
    }

    @Test
    void registerCourier() {
        when(repositoryMock.save(ArgumentMatchers.any())).thenReturn(courier);

        CourierDTO created = sut.registerCourier(registerRequest);
        assertTrue(
                email.equals(created.getEmail()) &&
                        phone.equals(registerRequest.getPhone()) &&
                        fname.equals(created.getFirstName()) &&
                        lname.equals(registerRequest.getLastName()),
                "Properties of returned DTO equal with returned Entity");
        reset(repositoryMock);
    }

    @Test
    void registerCourierThrowsException() {

        when(repositoryMock.save(ArgumentMatchers.any(Courier.class))).thenThrow(new DataIntegrityViolationException(""));

        assertThrows(ResourceException.class, () -> {
            sut.registerCourier(registerRequest);
        }, "Courier could not be saved due to an incorrect data");
    }
}