package com.teamrocket.control;

import com.teamrocket.entity.Courier;
import com.teamrocket.model.ClaimRequest;
import com.teamrocket.service.CourierService;
import com.teamrocket.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("")
public class CourierController {


    @Autowired
    CourierService courierServiceService;

    @Autowired
    DeliveryService deliveryService;

    @PostMapping("register")
    public ResponseEntity createNew(@RequestBody Courier request) {
        return ResponseEntity.ok(courierServiceService.registerCourier(request));
    }

    @PostMapping("claim")
    public ResponseEntity claimDeliveryTask(@RequestBody ClaimRequest request,
                                            @RequestHeader("role_id") int courierId) {
        return ResponseEntity.ok(deliveryService.claimDeliveryTask(request, courierId));
    }
}
