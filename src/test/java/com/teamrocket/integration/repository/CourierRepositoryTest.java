package com.teamrocket.integration.repository;

import com.teamrocket.entity.Courier;
import com.teamrocket.repository.CourierRepository;
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

    @Test
    void givenCourierWithValidDataWhenSaveCourierThenReturnsCourier() {
        Courier courier = new Courier("Magdalena", "Wawrzak", "magda@mail.com");
        courier = sut.save(courier);
        assertEquals(courier.getFirstName(), "Magdalena");
    }

    @Test
    void givenCourierWithExistingEmailWhenSaveCourierThenReturnsCourierThenThrowException() {
        Courier courier = new Courier("Magda", "Wawrzak", "magda@mail.com");
        sut.save(courier);
        assertThrows(DataIntegrityViolationException.class, () -> {
            sut.save(new Courier("Magdalena", "Wawrzak", "magda@mail.com"));
        }, "DataIntegrityViolationException was expected");
    }

}