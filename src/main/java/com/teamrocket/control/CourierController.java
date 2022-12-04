package com.teamrocket.control;

import com.teamrocket.entity.Courier;
import com.teamrocket.service.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("")
public class CourierController {


    @Autowired
    CourierService courierServiceService;


    @PostMapping("register")
    public ResponseEntity createNew(@RequestBody Courier request) {
        return ResponseEntity.ok(courierServiceService.registerCourier(request));
    }
}
