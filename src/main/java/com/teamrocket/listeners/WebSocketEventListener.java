package com.teamrocket.listeners;

import com.teamrocket.service.DeliveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class WebSocketEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketEventListener.class);
    private static Map<String, String> sessions = new HashMap();

    @Autowired
    DeliveryService deliveryService;

    @EventListener
    private void handleSessionConnected(SessionConnectedEvent event) {
        String sessionId = (String) event.getMessage().getHeaders().get("simpSessionId");
        GenericMessage msg = (GenericMessage) event.getMessage().getHeaders().get("simpConnectMessage");
        Map<String, ArrayList> map = (Map<String, ArrayList>) msg.getHeaders().get("nativeHeaders");
        String courierId = (String) map.get("role_id").get(0);
        sessions.put(sessionId, courierId);
        LOGGER.info("Courier with courierId: {} connected with sessionId: {}", courierId, sessionId);
    }

    @EventListener
    private void handleSessionSubscribe(SessionSubscribeEvent event) {
        String sessionId = (String) event.getMessage().getHeaders().get("simpSessionId");
        GenericMessage msg = (GenericMessage) event.getMessage();
        Map<String, ArrayList> map = (Map<String, ArrayList>) msg.getHeaders().get("nativeHeaders");
        String courierId = (String) map.get("role_id").get(0);
        String destination = (String) map.get("destination").get(0);
        String area = (String) map.get("area").get(0);
        LOGGER.info("Courier with courierId subscribed to {}", destination);
        if (courierId.equals(sessions.get(sessionId))) {
            deliveryService.sendNewDeliveryTasksToArea(area);
        } else {
            LOGGER.info("No session with given id");
        }
    }

    @EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) {
        String sessionId = (String) event.getMessage().getHeaders().get("simpSessionId");
        GenericMessage msg = (GenericMessage) event.getMessage().getHeaders().get("simpConnectMessage");
        LOGGER.info("Courier with courierId {} disconnected", sessions.get(sessionId));
        sessions.remove(sessionId);
    }
}
