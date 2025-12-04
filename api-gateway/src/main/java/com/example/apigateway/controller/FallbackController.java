package com.example.apigateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Fallback Controller for Circuit Breaker
 * 
 * Provides fallback responses when backend services are unavailable or experiencing issues.
 * This controller is triggered by the circuit breaker when a service fails to respond
 * within the configured timeout or when the failure rate threshold is exceeded.
 * 
 * Each fallback endpoint returns a 503 Service Unavailable status with details about
 * which service is down and suggested actions for the client.
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    /**
     * Fallback for User Service
     * Triggered when user-service:8081 is unavailable
     */
    @GetMapping("/user-service")
    @PostMapping("/user-service")
    public ResponseEntity<Map<String, Object>> userServiceFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "User Service Unavailable");
        response.put("message", "The User Service is currently unavailable. Please try again later.");
        response.put("service", "user-service");
        response.put("port", 8081);
        response.put("timestamp", LocalDateTime.now());
        response.put("suggestion", "Check if user-service container is running and healthy");
        
        return ResponseEntity
            .status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(response);
    }

    /**
     * Fallback for Content Service
     * Triggered when content-service:8082 is unavailable
     */
    @GetMapping("/content-service")
    @PostMapping("/content-service")
    public ResponseEntity<Map<String, Object>> contentServiceFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Content Service Unavailable");
        response.put("message", "The Content Service is currently unavailable. Please try again later.");
        response.put("service", "content-service");
        response.put("port", 8082);
        response.put("timestamp", LocalDateTime.now());
        response.put("suggestion", "Check if content-service container is running and healthy");
        
        return ResponseEntity
            .status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(response);
    }

    /**
     * Fallback for Video Service
     * Triggered when video-service:8083 is unavailable
     */
    @GetMapping("/video-service")
    @PostMapping("/video-service")
    public ResponseEntity<Map<String, Object>> videoServiceFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Video Service Unavailable");
        response.put("message", "The Video Service is currently unavailable. Please try again later.");
        response.put("service", "video-service");
        response.put("port", 8083);
        response.put("timestamp", LocalDateTime.now());
        response.put("suggestion", "Check if video-service container is running and healthy");
        
        return ResponseEntity
            .status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(response);
    }

    /**
     * Fallback for Recommendation Service
     * Triggered when recommendation-service:8084 is unavailable
     */
    @GetMapping("/recommendation-service")
    @PostMapping("/recommendation-service")
    public ResponseEntity<Map<String, Object>> recommendationServiceFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Recommendation Service Unavailable");
        response.put("message", "The Recommendation Service is currently unavailable. Please try again later.");
        response.put("service", "recommendation-service");
        response.put("port", 8084);
        response.put("timestamp", LocalDateTime.now());
        response.put("suggestion", "Check if recommendation-service container is running and healthy");
        
        return ResponseEntity
            .status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(response);
    }
}
