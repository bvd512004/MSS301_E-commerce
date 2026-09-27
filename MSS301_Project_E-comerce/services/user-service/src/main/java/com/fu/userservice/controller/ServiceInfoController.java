package com.fu.userservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ServiceInfoController {

    @GetMapping("/")
    public Map<String, String> info() {
        return Map.of(
                "service", "user-service",
                "role", "Quan ly tai khoan va ho so khach hang",
                "status", "skeleton"
        );
    }

}
