package com.teamrocket.service;

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
        try {
            CreateUserResponse response = userBlockingStub.createUser(
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


}