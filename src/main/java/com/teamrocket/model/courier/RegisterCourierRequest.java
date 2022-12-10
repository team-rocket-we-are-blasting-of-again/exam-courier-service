package com.teamrocket.model.courier;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RegisterCourierRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

}
