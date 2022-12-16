package com.teamrocket.clients;

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

    public int registerCourierUser(String email, int id, String password) {
        LOGGER.info("gRPC Channel {} ", userBlockingStub.getChannel());

        CreateUserResponse response = userBlockingStub.createUser(
                CreateUserRequest
                        .newBuilder()
                        .setRole(Role.COURIER)
                        .setEmail(email)
                        .setRoleId(id)
                        .setPassword(password)
                        .build());

        LOGGER.info("Registered user with userId {}", response.getId());

        return response.getId();
    }
}