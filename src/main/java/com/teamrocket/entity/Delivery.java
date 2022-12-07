package com.teamrocket.entity;

import com.teamrocket.enums.DeliveryStatus;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    int orderId;
    String restaurantName;
    int restaurantAddressId;
    String areaId;
    long pickupTime;
    String customerName;
    String customerPhone;
    int dropOffAddressId;
    int courierId;
    DeliveryStatus status;

    @Override
    public String toString() {
        return "Delivery{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", restaurantName='" + restaurantName + '\'' +
                ", restaurantAddressId=" + restaurantAddressId +
                ", areaId='" + areaId + '\'' +
                ", pickupTime=" + pickupTime +
                ", customerName='" + customerName + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                ", dropOffAddressId=" + dropOffAddressId +
                ", courierId=" + courierId +
                ", status=" + status +
                '}';
    }
}
