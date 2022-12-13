package com.teamrocket.unit.clients;

import com.teamrocket.clients.CustomerClient;
import com.teamrocket.model.CustomerDeliveryData;
import com.teamrocket.proto.CustomerServiceGrpc;
import com.teamrocket.proto.DeliveryData;
import com.teamrocket.proto.SystemOrderId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(locations = {"classpath:applicationtest.properties"})
class CustomerClientTest {

    @MockBean
    private CustomerServiceGrpc.CustomerServiceBlockingStub mock;

    @Autowired
    CustomerClient sut;

    private DeliveryData grpcResponse;
    private SystemOrderId systemOrderId;

    private int orderId = 9876;

    @BeforeEach
    void setUp() {
        grpcResponse = DeliveryData
                .newBuilder()
                .setCustomerName("Hanna")
                .setCustomerPhone("22233344")
                .setDropOfAddressId(-1213)
                .build();

        systemOrderId = SystemOrderId
                .newBuilder()
                .setSystemOrderId(orderId)
                .build();

        when(mock.getDeliveryData(systemOrderId)).thenReturn(grpcResponse);
    }

    @AfterEach
    void tearDown() {
        reset(mock);
    }

    @Test
    void getCustomerDeliveryData() {
        CustomerDeliveryData response = sut.getCustomerDeliveryData(orderId);
        assertTrue(
                grpcResponse.getCustomerName().equals(response.getCustomerName()) &&
                        grpcResponse.getCustomerPhone().equals(response.getCustomerPhone()) &&
                        grpcResponse.getDropOfAddressId() == response.getDropOfAddressId());

    }
}