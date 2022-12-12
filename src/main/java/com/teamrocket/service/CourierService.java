package com.teamrocket.service;

import com.teamrocket.clients.AuthClient;
import com.teamrocket.entity.Courier;
import com.teamrocket.enums.Topic;
import com.teamrocket.exceptions.ResourceException;
import com.teamrocket.model.courier.CourierDTO;
import com.teamrocket.model.courier.RegisterCourierRequest;
import com.teamrocket.repository.CourierRepository;
import com.teamrocket.service.interfaces.ICourierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CourierService implements ICourierService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CourierService.class);

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private AuthClient authClient;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Override
    public CourierDTO registerCourier(RegisterCourierRequest request) throws ResourceException {
        LOGGER.info("New register courier request {}", request);

        try {
            Courier courier = new Courier(request);
            courier = courierRepository.save(courier);
            int userId = authClient.registerCourierUser(courier);
            courier.setUserId(userId);
            courier = courierRepository.save(courier);
            CourierDTO registeredCourier = new CourierDTO(courier);
            kafkaTemplate.send(Topic.NEW_COURIER.toString(), registeredCourier);
            return registeredCourier;
        } catch (DataIntegrityViolationException e) {
            LOGGER.error(e.getMessage());
            throw new ResourceException("Courier could not be saved due to an incorrect data");
        }
    }


}
