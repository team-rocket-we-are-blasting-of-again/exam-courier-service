package com.teamrocket.service;

import com.teamrocket.entity.Courier;
import com.teamrocket.exceptions.ResourceException;
import com.teamrocket.repository.CourierRepository;
import com.teamrocket.service.interfaces.ICourierService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CourierService implements ICourierService {
    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private AuthClient authClient;


    @Override
    public Courier registerCourier(Courier courier) throws ResourceException {
        int userId = authClient.registerCourierUser(courier);
        courier.setUserId(userId);
        try{
            return courierRepository.save(courier);
        }
        catch (DataIntegrityViolationException e) {
            throw new ResourceException("Courier could not be saved due to an incorrect email");
        }
    }

}
