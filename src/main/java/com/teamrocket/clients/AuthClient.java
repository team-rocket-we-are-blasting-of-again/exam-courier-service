package com.teamrocket.clients;

import com.teamrocket.entity.Courier;
import com.teamrocket.proto.CreateUserRequest;
import com.teamrocket.proto.CreateUserResponse;
import com.teamrocket.proto.Role;
import com.teamrocket.proto.UserGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthClient.class);

    @Autowired
    private UserGrpc.UserBlockingStub userBlockingStub;

    public int registerCourierUser(Courier courier) {
        LOGGER.info("gRPC Channel {} ", userBlockingStub.getChannel());

        CreateUserResponse response = userBlockingStub.createUser(
                CreateUserRequest
                        .newBuilder()
                        .setRole(Role.COURIER)
                        .setRoleId(courier.getId())
                        .setEmail(courier.getEmail())
                        .build());
        return response.getId();
    }
}