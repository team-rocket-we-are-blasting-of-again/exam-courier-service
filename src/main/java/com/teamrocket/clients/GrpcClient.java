package com.teamrocket.clients;

import com.teamrocket.entity.Courier;
import com.teamrocket.model.CustomerDeliveryData;
import com.teamrocket.proto.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class GrpcClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(GrpcClient.class);

    @Value("${auth-grpc-service.host}")
    private String AUTH_gRPC_Host;

    @Value("${auth-grpc-service.port}")
    private int AUTH_gRPC_Port;

    @Value("${cust-grpc-service.host}")
    private String CUST_gRPC_Host;

    @Value("${cust-grpc-service.port}")
    private int CUST_gRPC_Port;

    @Autowired
    ManagedChannelBuilder managedChannelBuilder;


    private UserGrpc.UserBlockingStub userService = userBlockingStub();
    private CustomerServiceGrpc.CustomerServiceBlockingStub customerService = customerServiceBlockingStub();


    public CustomerDeliveryData getCustomerDeliveryData(int orderId) {
        LOGGER.info("CustomerDataProcess started for systemOrderId {}", orderId);

        DeliveryData grpcResponse = customerService.getDeliveryData(
                SystemOrderId.
                        newBuilder().
                        setSystemOrderId(orderId).
                        build());

        CustomerDeliveryData response = new CustomerDeliveryData(grpcResponse);
        return response;
    }

    public int registerCourierUser(Courier courier) {
        try {
            CreateUserResponse response = userService.createUser(
                    CreateUserRequest
                            .newBuilder()
                            .setRole(Role.COURIER)
                            .setRoleId(courier.getId())
                            .setEmail(courier.getEmail())
                            .build());

            LOGGER.info("Response: ", response);
            LOGGER.info("Response user id: ", response.getId());
            return response.getId();
        } catch (Exception e) {

            LOGGER.error(e.getClass().toString());
            LOGGER.error(e.getLocalizedMessage());
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    private UserGrpc.UserBlockingStub userBlockingStub() {
        LOGGER.info("build User Stub");
        ManagedChannel channel = ManagedChannelBuilder.forAddress(AUTH_gRPC_Host, AUTH_gRPC_Port).usePlaintext().build();
        return UserGrpc.newBlockingStub(channel);
    }


    private CustomerServiceGrpc.CustomerServiceBlockingStub customerServiceBlockingStub() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(CUST_gRPC_Host, CUST_gRPC_Port).usePlaintext().build();
        return CustomerServiceGrpc.newBlockingStub(channel);
    }
}
