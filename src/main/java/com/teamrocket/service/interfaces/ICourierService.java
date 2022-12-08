package com.teamrocket.service.interfaces;

import com.teamrocket.entity.Courier;
import com.teamrocket.exceptions.ResourceException;

public interface ICourierService {

    Courier registerCourier(Courier courier) throws ResourceException;

    String chooseArea(String area);

}
