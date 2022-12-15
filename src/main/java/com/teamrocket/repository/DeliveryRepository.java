package com.teamrocket.repository;

import com.teamrocket.entity.Delivery;
import com.teamrocket.enums.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {
    List<Delivery> findAllByAreaIdAndStatus(String areaId, DeliveryStatus status);

    List<Delivery> findAllByOrderIdAndStatus(int orderId, DeliveryStatus status);
    List<Delivery> findAllByCourierIdAndStatus(int courierId, DeliveryStatus status);
}
