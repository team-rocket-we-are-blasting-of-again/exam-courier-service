package com.teamrocket.service;

import com.teamrocket.entity.Courier;
import com.teamrocket.proto.CreateUserRequest;
import com.teamrocket.proto.CreateUserResponse;
import com.teamrocket.proto.Role;
import com.teamrocket.proto.UserGrpc;
import io.grpc.ManagedChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthClient.class);

    @Autowired
    UserGrpc.UserBlockingStub userBlockingStub;

    @Autowired
    ManagedChannel managedChannel;

    public int registerCourierUser(Courier courier) {
        LOGGER.info("gRPC Channel {} ", managedChannel.toString());
        System.out.println("HELLO FROM GRPC");
        System.out.println("HELLO FROM GRPC");
        System.out.println("HELLO FROM GRPC");
        System.out.println("HELLO FROM GRPC");
        CreateUserResponse response = userBlockingStub.createUser(CreateUserRequest
                .newBuilder()
                .setRole(Role.RESTAURANT)
                .setRoleId(courier.getId())
                .setEmail(courier.getEmail())
                .build());

        managedChannel.shutdown();
        LOGGER.info("Response user id: ", response.getId());
        return response.getId();
    }
}