package com.teamrocket.model;

import com.teamrocket.entity.Courier;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CourierDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private int userId;
    private int id;

    public CourierDTO(Courier courier) {
        this.firstName = courier.getFirstName();
        this.lastName = courier.getLastName();
        this.email = courier.getEmail();
        this.phone = courier.getPhone();
        this.userId = courier.getUserId();
        this.id = courier.getId();
    }

}