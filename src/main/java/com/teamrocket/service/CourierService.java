package com.teamrocket.service;

import com.teamrocket.entity.Courier;
import com.teamrocket.enums.Topic;
import com.teamrocket.exceptions.ResourceException;
import com.teamrocket.model.CourierDTO;
import com.teamrocket.repository.CourierRepository;
import com.teamrocket.service.interfaces.ICourierService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CourierService implements ICourierService {
    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private AuthClient authClient;
    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Override
    public Courier registerCourier(Courier courier) throws ResourceException {
        int userId = authClient.registerCourierUser(courier);
        courier.setUserId(userId);

        try {
            courier = courierRepository.save(courier);
            CourierDTO courierDTO = new CourierDTO(courier);

            kafkaTemplate.send(Topic.NEW_COURIER.toString(), courierDTO);
            return courier;

        } catch (DataIntegrityViolationException e) {
            throw new ResourceException("Courier could not be saved due to an incorrect email");
        }
    }

}
