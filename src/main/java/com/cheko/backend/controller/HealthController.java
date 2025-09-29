package com.cheko.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Health & System", description = "System health check and application information")
public class HealthController {

    @Operation(summary = "Health check", description = "Check if the Cheko Backend service is running")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service is healthy and running")
    })
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Cheko Backend is running successfully!");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> welcome() {
        Map<String, String> response = new HashMap<>();
        response.put("application", "Cheko Backend");
        response.put("version", "1.0.0");
        response.put("message", "Welcome to Cheko Restaurant Backend API!");
        response.put("description", "Backend service for Cheko restaurant with menu management and location services");
        return ResponseEntity.ok(response);
    }
}