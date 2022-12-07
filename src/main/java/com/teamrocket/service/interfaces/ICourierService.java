package com.teamrocket.service.interfaces;

import com.teamrocket.entity.Courier;
import com.teamrocket.exceptions.ResourceException;
import com.teamrocket.model.ClaimRequest;
import com.teamrocket.model.camunda.DeliveryTask;

public interface ICourierService {

    Courier registerCourier(Courier courier) throws ResourceException;

    String chooseArea(String area);

    DeliveryTask claimDeliveryTask(ClaimRequest claimRequest, int courierId);

    void sendNewDeliveryTasksToArea(String area);
}
