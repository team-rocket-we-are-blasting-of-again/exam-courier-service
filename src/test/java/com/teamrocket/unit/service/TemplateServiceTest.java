package com.teamrocket.unit.service;

import com.teamrocket.service.TemplateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(locations = { "classpath:applicationtest.properties"})
class TemplateServiceTest {

    @Autowired
    TemplateService templateService;
    private String who = "Me";

    @Test
    void helloTest() {
        assertEquals(templateService.hello("Me").getMsg(), "Hello, " + who + "!");
        assertEquals(templateService.hello("Me").getId(), 99);
    }
}