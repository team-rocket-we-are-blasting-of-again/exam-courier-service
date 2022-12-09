package com.teamrocket.service.interfaces;

import com.teamrocket.model.ClaimRequest;
import com.teamrocket.model.camunda.DeliveryTask;

public interface IDeliveryService {
    DeliveryTask claimDeliveryTask(ClaimRequest request, int couríerId);

    void publishNewDeliveryTask(DeliveryTask orderId);

    void sendNewDeliveryTasksToArea(String area);

    void monitOrderReadyToPickup();

}
