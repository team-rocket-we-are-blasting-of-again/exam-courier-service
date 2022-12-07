package com.teamrocket.model.camunda;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryTask {
    int orderId;
    String restaurantName;
    int restaurantAddressId;
    String areaId;
    long pickupTime;
    String customerName;
    String customerPhone;
    int dropOffAddressId;
    int courierId;
}
