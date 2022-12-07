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
