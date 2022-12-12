package com.teamrocket.unit.proto;

import com.teamrocket.proto.CreateUserRequest;
import com.teamrocket.proto.Role;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CreateUserRequestTest {

    private CreateUserRequest sut;
    private final String email = "mail";
    private final Role role= Role.COURIER;
    private final int id = 9;
    @Test
    void givenEmailIdRoleReturnsNewCreateUserRequestWithEmailIdRole() {
        CreateUserRequest request = CreateUserRequest
                .newBuilder()
                .setEmail(email)
                .setRoleId(id)
                .setRole(role)
                .build();

        assertTrue(

                request.getEmail().equals(email)
                && request.getRoleId() == id
                && request.getRole().equals(role)
        );
    }
}
