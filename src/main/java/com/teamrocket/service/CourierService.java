package com.teamrocket.service;

import com.teamrocket.entity.Courier;
import com.teamrocket.repository.CourierRepository;
import com.teamrocket.service.interfaces.ICourierService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CourierService implements ICourierService {
    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private AuthClient authClient;


    @Override
    public Courier registerCourier(Courier courier) {
        int userId = authClient.registerCourierUser(courier);
        courier.setGetUserId(userId);
        return courierRepository.save(courier);
    }
}
