package com.mjanputra.tx.tx_engine.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    
    @GetMapping("/")
    public String home() {
        return "TX Engine is running...";
    }

    @GetMapping("/health")
    public String health() {
        return "ok";
    }
}
