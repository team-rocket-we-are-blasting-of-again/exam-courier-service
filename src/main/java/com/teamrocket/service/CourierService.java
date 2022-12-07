package com.teamrocket.service;

import com.teamrocket.entity.Courier;
import com.teamrocket.entity.Delivery;
import com.teamrocket.enums.DeliveryStatus;
import com.teamrocket.enums.Topic;
import com.teamrocket.exceptions.ResourceException;
import com.teamrocket.model.ClaimRequest;
import com.teamrocket.model.CustomerDeliveryResponse;
import com.teamrocket.model.camunda.DeliveryTask;
import com.teamrocket.repository.CourierRepository;
import com.teamrocket.repository.DeliveryRepository;
import com.teamrocket.service.interfaces.ICourierService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class CourierService implements ICourierService {
    @Autowired
    private CourierRepository courierRepository;
    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private AuthClient authClient;

    @Autowired
    private CustomerClient customerClient;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Override
    public Courier registerCourier(Courier courier) throws ResourceException {
        int userId = authClient.registerCourierUser(courier);
        courier.setUserId(userId);

        try {
            courier = courierRepository.save(courier);
            kafkaTemplate.send(Topic.NEW_COURIER.toString(), courier);
            return courier;
        } catch (DataIntegrityViolationException e) {
            throw new ResourceException("Courier could not be saved due to an incorrect email");
        }
    }


    @Override
    public String chooseArea(String area) {
        return null;
    }

    @Override
    public DeliveryTask claimDeliveryTask(ClaimRequest request, int couríerId) {

        Delivery delivery = deliveryRepository.findById(request.getDeliveryId())
                .orElseThrow(() -> new NoSuchElementException("No delivery with id " + request.getDeliveryId()));
        System.out.println("in claimDeliveryTask");
        System.out.println(delivery);
        delivery.setCourierId(couríerId);
        delivery.setStatus(DeliveryStatus.ON_THE_WAY);
        CustomerDeliveryResponse customerData = customerClient.getCustomerDeliveryData(delivery.getOrderId());
        delivery.setCustomerName(customerData.getCustomerName());
        delivery.setCustomerPhone(customerData.getCustomerPhone());
        delivery.setDropOffAddressId(customerData.getDropOfAddressId());

        delivery = deliveryRepository.save(delivery);
        return new DeliveryTask(delivery);
    }

    @Override
    public void sendNewDeliveryTasksToArea(String area) {

    }

}
