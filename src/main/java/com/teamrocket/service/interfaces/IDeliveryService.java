package com.teamrocket.service.interfaces;

import com.teamrocket.model.DeliveryRequest;
import com.teamrocket.model.camunda.DeliveryTask;

public interface IDeliveryService {
    DeliveryTask claimDeliveryTask(DeliveryRequest request, int couríerId);

    DeliveryTask saveAndPublishNewDeliveryTask(DeliveryTask orderId);

    void sendNewDeliveryTasksToArea(String area);

    void handleOrderReadyEvent(int orderId);

    DeliveryTask handleDropOff(DeliveryRequest deliveryId, int courierId);
}
