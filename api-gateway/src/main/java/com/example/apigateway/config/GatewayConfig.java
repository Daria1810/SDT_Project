package com.example.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

/**
 * Gateway Configuration for CORS and additional gateway settings
 * 
 * Configures Cross-Origin Resource Sharing (CORS) to allow frontend applications
 * to make requests to the API Gateway from different origins.
 * 
 * CORS Policy:
 * - Allowed Origins: All (*)
 * - Allowed Methods: GET, POST, PUT, DELETE, OPTIONS
 * - Allowed Headers: All (*)
 * - Allow Credentials: true
 * - Max Age: 3600 seconds (1 hour)
 */
@Configuration
public class GatewayConfig {

    /**
     * Configure CORS filter for the gateway
     * 
     * This is needed for reactive applications (Spring Cloud Gateway uses WebFlux)
     * to handle preflight OPTIONS requests and CORS headers properly.
     */
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        
        // Allow all origins (for development - restrict in production)
        corsConfig.setAllowedOriginPatterns(Collections.singletonList("*"));
        
        // Allow common HTTP methods
        corsConfig.setAllowedMethods(Arrays.asList(
            "GET", 
            "POST", 
            "PUT", 
            "DELETE", 
            "OPTIONS",
            "PATCH"
        ));
        
        // Allow all headers
        corsConfig.setAllowedHeaders(Collections.singletonList("*"));
        
        // Allow credentials (cookies, authorization headers)
        corsConfig.setAllowCredentials(true);
        
        // Cache preflight response for 1 hour
        corsConfig.setMaxAge(3600L);
        
        // Expose common headers to clients
        corsConfig.setExposedHeaders(Arrays.asList(
            "Authorization",
            "X-Gateway-Request",
            "Content-Type"
        ));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        
        return new CorsWebFilter(source);
    }
}
