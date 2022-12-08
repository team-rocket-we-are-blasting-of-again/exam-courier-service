package com.teamrocket.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RegisterCourierRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    @Override
    public String toString() {
        return "RegisterCourierRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
