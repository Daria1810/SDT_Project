package com.example.recommendationservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * Configuration for RestTemplate used for inter-service communication.
 * Configures timeouts for HTTP calls to Content Service.
 */
@Configuration
public class RestTemplateConfig {
    
    @Value("${rest.connection-timeout:5000}")
    private int connectionTimeout;
    
    @Value("${rest.read-timeout:10000}")
    private int readTimeout;
    
    /**
     * Create RestTemplate bean with configured timeouts.
     * Used for making HTTP calls to Content Service.
     * 
     * @param builder RestTemplateBuilder provided by Spring Boot
     * @return Configured RestTemplate instance
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofMillis(connectionTimeout))
                .setReadTimeout(Duration.ofMillis(readTimeout))
                .build();
    }
}
