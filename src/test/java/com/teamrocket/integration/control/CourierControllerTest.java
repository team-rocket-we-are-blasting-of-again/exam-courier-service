package com.teamrocket.integration.control;

import com.google.gson.Gson;
import com.teamrocket.clients.AuthClient;
import com.teamrocket.control.CourierController;
import com.teamrocket.entity.Courier;
import com.teamrocket.repository.CourierRepository;
import com.teamrocket.service.CourierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = {"classpath:applicationtest.properties"})

public class CourierControllerTest {
    @LocalServerPort
    private int port;
    private String URL;

    @Autowired
    private CourierRepository courierRepository;
    @Mock
    private AuthClient authClient;
    @MockBean
    KafkaTemplate kafkaTemplate;

    @Autowired
    private CourierController sut;
    @Autowired
    @InjectMocks
    private CourierService courierService;
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private Gson GSON;

    private MockMvc mvc;
    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        courierRepository.deleteAll();
        URL = "http://localhost:" + port;
        MockitoAnnotations.openMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(sut)
                .build();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        when(authClient.registerCourierUser(any())).thenReturn(888);
        when(kafkaTemplate.send(ArgumentMatchers.anyString(),any())).thenReturn(null);
    }

    @Test
    void registerUserWithUniqEmail() throws Exception {

        Courier courier = Courier.builder().firstName("Magda").lastName("W").email("mail@mail.dk").phone("789789789").build();

        String c = GSON.toJson(courier);
        MockHttpServletResponse response = mvc.perform(post("/register")
                        .content(c)
                        .headers(headers)
                )
                .andReturn().getResponse();
        assertTrue("Response status is ok", (response.getStatus() == HttpStatus.OK.value()));
    }

    @Test
    void registerUserWithExistingEmail() throws Exception {

        Courier courier = Courier.builder().firstName("Magda").lastName("W").email("mail@mail.dk").phone("7878").build();
        courierRepository.save(Courier.builder().firstName("Magda").lastName("W").email("mail@mail.dk").phone("787979").build());
        String c = GSON.toJson(courier);
        MockHttpServletResponse response = mvc.perform(post("/register")
                        .content(c)
                        .headers(headers)
                )
                .andReturn().getResponse();

        assertTrue("Response status is not ok", (response.getStatus() == HttpStatus.BAD_REQUEST.value()));
    }
}
