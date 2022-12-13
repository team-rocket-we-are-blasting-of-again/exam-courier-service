package com.teamrocket.model.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class OrderKafkaMsg {
    int systemOrderId;
}
