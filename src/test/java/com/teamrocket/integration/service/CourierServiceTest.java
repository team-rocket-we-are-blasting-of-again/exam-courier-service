package com.teamrocket.integration.service;

import com.teamrocket.entity.Courier;
import com.teamrocket.repository.CourierRepository;
import com.teamrocket.service.AuthClient;
import com.teamrocket.service.CourierService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(locations = {"classpath:applicationtest.properties"})
@RunWith(MockitoJUnitRunner.class)

public class CourierServiceTest {

    private final int userID = 999;

    @MockBean
    private AuthClient authClient;

    @Autowired
    private CourierService sut;
    @Autowired
    CourierRepository repository;

    @BeforeEach
    void setUp() {
        when(authClient.registerCourierUser(ArgumentMatchers.any())).thenReturn(userID);
    }

    @AfterEach
    void tearDown() {
        reset(authClient);
        repository.deleteAll();
    }

    @Test
    void GivenCourierWhenRegisterCourierThenNewCourierReturned() {
        Courier courier = sut.registerCourier(Courier.builder().firstName("Magdalena").lastName("Wawrzak").email("magdalena@mail.com").build());
        assertEquals(courier.getFirstName(), "Magdalena");
    }

}
