package com.teamrocket.unit.control;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("unit")
@SpringBootTest//(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(locations = { "classpath:applicationtest.properties"})
class TempControllerTest {

//    @MockBean
//    private TemplateService templateService;
//
//    @Autowired
//    private TempController tempController;
//
//    private TemplateDTO output;
//
//    @BeforeEach
//    void setUp() {
//        output = new TemplateDTO(); // to make the coverage happy :)
//        output = new TemplateDTO(88, "Goodbye");
//        when(templateService.hello(any())).thenReturn(output);
//    }
//
//    @AfterEach
//    void tearDown() {
//        reset(templateService);
//    }
//
//    @Test
//    void getHello() {
//        TemplateDTO actual = tempController.getHello();
//        assertNotNull(actual);
//        assertEquals(actual, output);
//
//    }
}