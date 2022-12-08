package com.teamrocket.service;

import com.teamrocket.entity.Courier;
import com.teamrocket.proto.CreateUserRequest;
import com.teamrocket.proto.CreateUserResponse;
import com.teamrocket.proto.Role;
import com.teamrocket.proto.UserGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthClient.class);

    @Value("${auth-grpc-service.host}")
    private String AUTH_gRPC_Host;

    @Value("${auth-grpc-service.port}")
    private int AUTH_gRPC_Port;

    @Autowired
    private UserGrpc.UserBlockingStub userBlockingStub;

    private ManagedChannel managedChannel;

    public int registerCourierUser(Courier courier) {
        LOGGER.info("gRPC Channel {} ", managedChannel.toString());
        buildChannel();
        CreateUserResponse response = userBlockingStub.createUser(
                CreateUserRequest
                        .newBuilder()
                        .setRole(Role.RESTAURANT)
                        .setRoleId(courier.getId())
                        .setEmail(courier.getEmail())
                        .build());


        LOGGER.info("Response user id: ", response.getId());
        return response.getId();
    }

    private void buildChannel() {
        if (managedChannel == null) {
            managedChannel = ManagedChannelBuilder.
                    forAddress(AUTH_gRPC_Host, AUTH_gRPC_Port)
                    .usePlaintext()
                    .build();
        }
    }
}