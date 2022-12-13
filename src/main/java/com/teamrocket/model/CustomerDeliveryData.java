package com.teamrocket.model;

import com.teamrocket.proto.DeliveryData;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CustomerDeliveryData {
    private String customerName;
    private String customerPhone;
    private int dropOfAddressId;

    public CustomerDeliveryData(DeliveryData response) {
        this.customerName = response.getCustomerName();
        this.customerPhone = response.getCustomerPhone();
        this.dropOfAddressId = response.getDropOfAddressId();
    }
}
