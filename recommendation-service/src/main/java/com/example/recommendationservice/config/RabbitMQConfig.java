package com.example.recommendationservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ Configuration for Recommendation Service.
 * 
 * Configures:
 * - Queue for receiving user preference updates
 * - JSON message converter for deserialization
 * 
 * This enables asynchronous communication with Video Service,
 * improving scalability and fault tolerance.
 */
@Configuration
public class RabbitMQConfig {
    
    public static final String USER_PREFERENCE_QUEUE = "user.preference.updates";
    public static final String USER_EVENTS_QUEUE = "user.events";
    public static final String CONTENT_EVENTS_QUEUE = "content.events";
    
    /**
     * Create queue for user preference updates.
     */
    @Bean
    public Queue userPreferenceQueue() {
        return new Queue(USER_PREFERENCE_QUEUE, true);
    }
    
    /**
     * Create queue for user events.
     */
    @Bean
    public Queue userEventsQueue() {
        return new Queue(USER_EVENTS_QUEUE, true);
    }
    
    /**
     * Create queue for content events.
     */
    @Bean
    public Queue contentEventsQueue() {
        return new Queue(CONTENT_EVENTS_QUEUE, true);
    }
    
    /**
     * JSON message converter for deserializing messages.
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

