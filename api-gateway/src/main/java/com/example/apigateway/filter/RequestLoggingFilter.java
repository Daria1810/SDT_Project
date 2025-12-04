package com.example.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Request Logging Filter
 * 
 * Global filter that logs all incoming requests and outgoing responses.
 * This filter runs for every request passing through the gateway and logs:
 * - Request method and path
 * - Request headers
 * - Target service URI
 * - Response status code
 * - Request processing time
 * 
 * The filter implements Ordered with HIGHEST_PRECEDENCE to ensure it runs
 * before all other filters, allowing accurate timing measurements.
 */
@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        long startTime = System.currentTimeMillis();

        // Log incoming request
        logger.info("=".repeat(80));
        logger.info("INCOMING REQUEST at {}", LocalDateTime.now());
        logger.info("Method: {} | Path: {}", request.getMethod(), request.getPath());
        logger.info("URI: {}", request.getURI());
        logger.info("Remote Address: {}", request.getRemoteAddress());
        
        // Log important headers
        HttpHeaders headers = request.getHeaders();
        if (headers.containsKey("Authorization")) {
            logger.info("Authorization: [PRESENT]");
        }
        if (headers.containsKey("Content-Type")) {
            logger.info("Content-Type: {}", headers.getFirst("Content-Type"));
        }
        if (headers.containsKey("X-Gateway-Request")) {
            logger.info("X-Gateway-Request: {}", headers.getFirst("X-Gateway-Request"));
        }

        // Determine target service from path
        String path = request.getPath().value();
        String targetService = determineTargetService(path);
        logger.info("Target Service: {}", targetService);
        logger.info("-".repeat(80));

        // Continue with the filter chain
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            // Log response details
            logger.info("RESPONSE for {} {}", request.getMethod(), request.getPath());
            logger.info("Status Code: {}", response.getStatusCode());
            logger.info("Processing Time: {} ms", duration);
            logger.info("=".repeat(80));
        }));
    }

    /**
     * Determine which backend service will handle the request based on the path
     */
    private String determineTargetService(String path) {
        if (path.startsWith("/api/users")) {
            return "user-service:8081";
        } else if (path.startsWith("/api/content")) {
            return "content-service:8082";
        } else if (path.startsWith("/api/videos")) {
            return "video-service:8083";
        } else if (path.startsWith("/api/recommendations")) {
            return "recommendation-service:8084";
        } else if (path.startsWith("/api/gateway")) {
            return "api-gateway:8080 (internal)";
        } else {
            return "UNKNOWN";
        }
    }

    @Override
    public int getOrder() {
        // Run this filter first (highest precedence)
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
