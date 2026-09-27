package com.fu.productservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ServiceInfoController {

    @GetMapping("/")
    public Map<String, String> info() {
        return Map.of(
                "service", "product-service",
                "role", "Quan ly sach, gia va ton kho",
                "status", "skeleton"
        );
    }

}
