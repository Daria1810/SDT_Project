package com.example.videoservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ Configuration for Video Service.
 * 
 * Configures:
 * - Queue for user preference updates
 * - JSON message converter for serialization
 * - RabbitTemplate for publishing messages
 * 
 * This enables asynchronous communication with Recommendation Service,
 * improving scalability and fault tolerance.
 */
@Configuration
public class RabbitMQConfig {
    
    public static final String USER_PREFERENCE_QUEUE = "user.preference.updates";
    public static final String CONTENT_EVENTS_QUEUE = "content.events";
    
    /**
     * Create queue for user preference updates.
     */
    @Bean
    public Queue userPreferenceQueue() {
        return new Queue(USER_PREFERENCE_QUEUE, true);
    }
    
    /**
     * Create queue for content events (consumes from Content Service).
     */
    @Bean
    public Queue contentEventsQueue() {
        return new Queue(CONTENT_EVENTS_QUEUE, true);
    }
    
    /**
     * Configure RabbitTemplate with JSON message converter.
     * This allows sending Java objects as JSON messages.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }
    
    /**
     * JSON message converter for serializing/deserializing messages.
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

