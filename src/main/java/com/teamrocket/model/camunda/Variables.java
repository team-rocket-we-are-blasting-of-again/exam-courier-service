package com.teamrocket.model.camunda;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Variables {
    DeliveryTaskHolder delivery_task;
    OrderIdHolder systemOrderId;
}