package com.teamrocket.integration.repository;

import com.teamrocket.entity.Courier;
import com.teamrocket.repository.CourierRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@TestPropertySource(locations = { "classpath:applicationtest.properties"})
class CourierRepositoryTest {
    @Autowired
    private CourierRepository sut;

    @Test
    void givenCourierWhenSaveCourierThenReturnsCourier(){
        Courier courier = sut.save(new Courier("Magdalena", "Wawrzak", "magda@mail.com"));
        assertEquals(courier.getFirstName(),"Magdalena");
    }

}