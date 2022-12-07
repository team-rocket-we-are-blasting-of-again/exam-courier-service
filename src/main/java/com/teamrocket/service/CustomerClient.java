package com.teamrocket.service;

import com.teamrocket.model.CustomerDeliveryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CustomerClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerClient.class);

    public CustomerDeliveryResponse getCustomerDeliveryData(int orderId) {
        LOGGER.info("CustomerDataProcess started for systemOrderId {}", orderId);
        CustomerDeliveryResponse response = new CustomerDeliveryResponse();
        //TODO set data on deliveryresponse
        return response;
    }

}
