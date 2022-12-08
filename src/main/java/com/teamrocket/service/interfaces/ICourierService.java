package com.teamrocket.service.interfaces;

import com.teamrocket.exceptions.ResourceException;
import com.teamrocket.model.CourierDTO;
import com.teamrocket.model.RegisterCourierRequest;

public interface ICourierService {

    CourierDTO registerCourier(RegisterCourierRequest courier) throws ResourceException;

    String chooseArea(String area);

}
