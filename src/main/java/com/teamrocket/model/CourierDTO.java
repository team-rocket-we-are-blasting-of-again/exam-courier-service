package com.teamrocket.model;

import com.teamrocket.entity.Courier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourierDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public CourierDTO(Courier courier) {
        this.firstName = courier.getFirstName();
        this.lastName = courier.getLastName();
        this.email = courier.getEmail();
        this.phone = courier.getPhone();
    }

}
