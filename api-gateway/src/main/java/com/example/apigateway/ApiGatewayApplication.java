package com.example.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

/**
 * API Gateway Application for StreamFlix Microservices
 * 
 * This Spring Cloud Gateway serves as the single entry point for all microservices,
 * providing routing, load balancing, circuit breaking, and request filtering.
 * 
 * Key Features:
 * - Routes requests to 4 backend services (User, Content, Video, Recommendation)
 * - Circuit breaker pattern with fallback responses
 * - CORS configuration for cross-origin requests
 * - Request header injection (X-Gateway-Request)
 * - Health checks and actuator endpoints
 * - Comprehensive logging for debugging
 * 
 * Port: 8080
 * 
 * Backend Services:
 * - User Service: http://user-service:8081
 * - Content Service: http://content-service:8082
 * - Video Service: http://video-service:8083
 * - Recommendation Service: http://recommendation-service:8084
 * 
 * @author StreamFlix Team
 * @version 1.0.0
 */
@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
        System.out.println("========================================");
        System.out.println("StreamFlix API Gateway Started on Port 8080");
        System.out.println("========================================");
        System.out.println("Routes configured:");
        System.out.println("  - /api/users/** -> user-service:8081");
        System.out.println("  - /api/content/** -> content-service:8082");
        System.out.println("  - /api/videos/** -> video-service:8083");
        System.out.println("  - /api/recommendations/** -> recommendation-service:8084");
        System.out.println("========================================");
        System.out.println("Gateway Endpoints:");
        System.out.println("  - GET /api/gateway/health - Gateway health check");
        System.out.println("  - GET /api/gateway/routes - List all routes");
        System.out.println("  - GET /actuator/health - Actuator health");
        System.out.println("  - GET /actuator/gateway/routes - Gateway routes");
        System.out.println("========================================");
    }
}
