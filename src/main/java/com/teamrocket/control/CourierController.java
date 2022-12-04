package com.teamrocket.control;

import com.teamrocket.entity.Courier;
import com.teamrocket.exceptions.ResourceException;
import com.teamrocket.service.CourierService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("")
@RequiredArgsConstructor

public class CourierController {


    @Autowired
    CourierService courierServiceService;


    @PostMapping("register")
    public ResponseEntity createNew(@RequestBody Courier request) throws ResourceException {
        return ResponseEntity.ok(courierServiceService.registerCourier(request));
    }
}
