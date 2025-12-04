package com.example.apigateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Health Controller for API Gateway
 * 
 * Provides endpoints to check the health status of the gateway and list all configured routes.
 * This is useful for debugging routing issues and monitoring the gateway's health.
 * 
 * Endpoints:
 * - GET /api/gateway/health - Check gateway health and all backend services
 * - GET /api/gateway/routes - List all configured routes
 */
@RestController
@RequestMapping("/api/gateway")
public class HealthController {

    @Autowired
    private RouteLocator routeLocator;

    private final WebClient webClient;

    public HealthController() {
        this.webClient = WebClient.builder()
            .build();
    }

    /**
     * Gateway Health Check
     * 
     * Returns the health status of the API Gateway and checks connectivity
     * to all backend services.
     * 
     * @return Health status including gateway and all backend services
     */
    @GetMapping("/health")
    public Mono<ResponseEntity<Map<String, Object>>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "api-gateway");
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("port", 8080);

        // Check backend services
        Map<String, Object> services = new HashMap<>();
        
        // Check User Service
        services.put("user-service", checkServiceHealth("http://user-service:8081/api/users/health"));
        
        // Check Content Service
        services.put("content-service", checkServiceHealth("http://content-service:8082/api/content/health"));
        
        // Check Video Service
        services.put("video-service", checkServiceHealth("http://video-service:8083/api/videos/health"));
        
        // Check Recommendation Service
        services.put("recommendation-service", checkServiceHealth("http://recommendation-service:8084/api/recommendations/health"));

        response.put("backend-services", services);

        return Mono.just(ResponseEntity.ok(response));
    }

    /**
     * List all configured routes
     * 
     * Returns a list of all routes configured in the gateway with their
     * IDs, URIs, and predicates. Useful for debugging routing issues.
     * 
     * @return List of all routes
     */
    @GetMapping("/routes")
    public Mono<ResponseEntity<Map<String, Object>>> listRoutes() {
        return Flux.from(routeLocator.getRoutes())
            .collectList()
            .map(routes -> {
                Map<String, Object> response = new HashMap<>();
                response.put("gateway", "api-gateway");
                response.put("totalRoutes", routes.size());
                response.put("timestamp", LocalDateTime.now());
                
                List<Map<String, Object>> routeList = routes.stream()
                    .map(route -> {
                        Map<String, Object> routeInfo = new HashMap<>();
                        routeInfo.put("id", route.getId());
                        routeInfo.put("uri", route.getUri().toString());
                        routeInfo.put("predicates", route.getPredicate().toString());
                        routeInfo.put("filters", route.getFilters().stream()
                            .map(filter -> filter.toString())
                            .collect(Collectors.toList()));
                        return routeInfo;
                    })
                    .collect(Collectors.toList());
                
                response.put("routes", routeList);
                
                return ResponseEntity.ok(response);
            });
    }

    /**
     * Check health of a backend service
     * 
     * @param healthUrl The health endpoint URL of the service
     * @return Status string (UP/DOWN)
     */
    private String checkServiceHealth(String healthUrl) {
        try {
            return webClient.get()
                .uri(healthUrl)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(5))
                .map(body -> "UP")
                .onErrorReturn("DOWN")
                .block();
        } catch (Exception e) {
            return "DOWN";
        }
    }
}
