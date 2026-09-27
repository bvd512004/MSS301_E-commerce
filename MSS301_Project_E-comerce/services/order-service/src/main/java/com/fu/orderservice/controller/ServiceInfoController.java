package com.fu.orderservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ServiceInfoController {

    @GetMapping("/")
    public Map<String, String> info() {
        return Map.of(
                "service", "order-service",
                "role", "Quan ly don hang va trang thai don",
                "status", "skeleton"
        );
    }

}
