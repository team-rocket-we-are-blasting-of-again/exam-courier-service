package com.teamrocket.clients;

import com.teamrocket.model.CustomerDeliveryData;
import com.teamrocket.proto.CustomerServiceGrpc;
import com.teamrocket.proto.DeliveryData;
import com.teamrocket.proto.SystemOrderId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerClient.class);

    @Autowired
    private CustomerServiceGrpc.CustomerServiceBlockingStub customerServiceBlockingStub;

    public CustomerDeliveryData getCustomerDeliveryData(int orderId) {

        LOGGER.info("CustomerDataProcess started for systemOrderId {}", orderId);

        DeliveryData grpcResponse = customerServiceBlockingStub.getDeliveryData(
                SystemOrderId.
                        newBuilder().
                        setSystemOrderId(orderId).
                        build());

        CustomerDeliveryData response = new CustomerDeliveryData(grpcResponse);
        return response;
    }

}
