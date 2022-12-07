package com.teamrocket.model.camunda;

import com.teamrocket.entity.Delivery;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryTask {
    int orderId;
    String restaurantName;
    int restaurantAddressId;
    String areaId;
    long pickupTime;
    String customerName;
    String customerPhone;
    int dropOffAddressId;

    public DeliveryTask(Delivery delivery) {
        this.orderId = delivery.getOrderId();
        this.restaurantName = delivery.getRestaurantName();
        this.restaurantAddressId = delivery.getRestaurantAddressId();
        this.areaId = delivery.getAreaId();
        this.pickupTime = delivery.getPickupTime();
        this.customerName = delivery.getCustomerName();
        this.customerPhone = delivery.getCustomerPhone();
        this.dropOffAddressId = delivery.getDropOffAddressId();
    }
}
