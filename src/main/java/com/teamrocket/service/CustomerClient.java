package com.teamrocket.service;

import com.teamrocket.model.CustomerDeliveryData;
import com.teamrocket.proto.CustomerServiceGrpc;
import com.teamrocket.proto.DeliveryData;
import com.teamrocket.proto.SystemOrderId;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CustomerClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerClient.class);

    @Value("${cust-grpc-service.host}")
    private String CUST_gRPC_Host;

    @Value("${cust-grpc-service.port}")
    private int CUST_gRPC_Port;

    @Autowired
    private CustomerServiceGrpc.CustomerServiceBlockingStub customerServiceBlockingStub;

    private ManagedChannel managedChannel;

    public CustomerDeliveryData getCustomerDeliveryData(int orderId) {
        buildChannel();
        LOGGER.info("CustomerDataProcess started for systemOrderId {}", orderId);

        DeliveryData grpcResponse = customerServiceBlockingStub.getDeliveryData(
                SystemOrderId.
                        newBuilder().
                        setSystemOrderId(orderId).
                        build());

        CustomerDeliveryData response = new CustomerDeliveryData(grpcResponse);
        return response;
    }

    private void buildChannel() {
        if (managedChannel == null) {
            managedChannel = ManagedChannelBuilder.
                    forAddress(CUST_gRPC_Host, CUST_gRPC_Port)
                    .usePlaintext()
                    .build();
        }
    }

}
