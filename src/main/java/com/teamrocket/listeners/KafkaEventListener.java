package com.teamrocket.listeners;

import com.teamrocket.model.kafka.OrderKafkaMsg;
import com.teamrocket.service.DeliveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class KafkaEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaEventListener.class);

    @Autowired
    DeliveryService deliveryService;


    @KafkaListener(id = "courier_service_order_ready", topics = "ORDER_READY")
    @KafkaHandler
    public void listenOnOrderReadyToPickup(@Payload OrderKafkaMsg orderMsg) {
        LOGGER.info("RECEIVED delivery: " + orderMsg.toString());
        deliveryService.handleOrderReadyEvent(orderMsg.getSystemOrderId());
    }

}
