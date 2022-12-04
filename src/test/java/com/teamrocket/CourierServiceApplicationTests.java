package com.teamrocket;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = {"classpath:applicationtest.properties"})
class CourierServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}

