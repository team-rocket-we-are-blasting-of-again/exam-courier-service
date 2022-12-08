package com.teamrocket.service;

import com.teamrocket.clients.GrpcClient;
import com.teamrocket.entity.Courier;
import com.teamrocket.enums.Topic;
import com.teamrocket.exceptions.ResourceException;
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
    GrpcClient grpcClient;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Override
    public Courier registerCourier(Courier courier) throws ResourceException {
        LOGGER.info("New register courier request {}", courier);
        try {
            int userId = grpcClient.registerCourierUser(courier);
//            int userId = authClient.registerCourierUser(courier);
            courier.setUserId(userId);
        }catch (Exception e) {

            LOGGER.error(e.getClass().toString());
            LOGGER.error(e.getLocalizedMessage());
            LOGGER.error(e.getMessage());
            throw e;
        }


        try {
            courier = courierRepository.save(courier);
            kafkaTemplate.send(Topic.NEW_COURIER.toString(), courier);
            return courier;
        } catch (DataIntegrityViolationException e) {
            LOGGER.error(e.getMessage());
            throw new ResourceException("Courier could not be saved due to an incorrect data");
        }
    }

    @Override
    public String chooseArea(String area) {
        return null;
    }


}
