package com.teamrocket.entity;

import com.teamrocket.enums.DeliveryStatus;
import com.teamrocket.model.camunda.DeliveryTask;
import lombok.*;

import javax.persistence.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private int orderId;
    private String restaurantName;
    private int restaurantAddressId;
    private String areaId;
    private long pickupTime;
    private String customerName;
    private String customerPhone;
    private int dropOffAddressId;
    private int courierId;
    private DeliveryStatus status;

    public Delivery(DeliveryTask deliveryTask) {
        this.orderId = deliveryTask.getOrderId();
        this.restaurantName = deliveryTask.getRestaurantName();
        this.restaurantAddressId = deliveryTask.getRestaurantAddressId();
        this.areaId = deliveryTask.getAreaId();
        this.pickupTime = deliveryTask.getPickupTime();
        this.customerName = deliveryTask.getCustomerName();
        this.customerPhone = deliveryTask.getCustomerPhone();
        this.dropOffAddressId = deliveryTask.getDropOffAddressId();
    }
}
