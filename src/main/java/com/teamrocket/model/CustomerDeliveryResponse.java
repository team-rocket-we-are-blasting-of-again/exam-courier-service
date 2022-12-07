package com.teamrocket.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDeliveryResponse {
    private String customerName;
    private String customerPhone;
    private int dropOfAddressId;
}
