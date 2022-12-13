package com.teamrocket.integration.repository;

import com.teamrocket.entity.Courier;
import com.teamrocket.repository.CourierRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource(locations = {"classpath:applicationtest.properties"})
class CourierRepositoryTest {
    @Autowired
    private CourierRepository sut;

    @BeforeEach
    void setup() {
        sut.deleteAll();
    }

    @AfterEach
    void tearDown() {
        sut.deleteAll();
    }

    @Test
    void givenCourierWithValidDataWhenSaveCourierThenReturnsCourier() {
        Courier courier = Courier.builder().firstName("Magdalena").lastName("Wawrzak").email("magda@mail.com").phone("123456").build();
        courier = sut.save(courier);
        assertEquals(courier.getFirstName(), "Magdalena");
    }

    @Test
    void givenCourierWithExistingEmailWhenSaveCourierThenReturnsCourierThenThrowException() {
        Courier courier =  Courier.builder().firstName("Magdalena").lastName("Wawrzak").email("magda@mail.com").phone("123456").build();
        sut.save(courier);
        assertThrows(DataIntegrityViolationException.class, () -> {
            sut.save( Courier.builder().firstName("Magdalena").lastName("Wawrzak").email("magda@mail.com").phone("123456").build());
        }, "DataIntegrityViolationException was expected");
    }

}