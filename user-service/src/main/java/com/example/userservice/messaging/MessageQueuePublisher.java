package com.example.userservice.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.userservice.config.RabbitMQConfig;

/**
 * Message Queue Publisher for User Service.
 * Publishes user events to RabbitMQ.
 */
@Component
public class MessageQueuePublisher {
    
    private static final Logger logger = LoggerFactory.getLogger(MessageQueuePublisher.class);
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    public void publishUserEvent(UserEventMessage message) {
        try {
            logger.info("Publishing user event to queue: userId={}, eventType={}", 
                       message.getUserId(), message.getEventType());
            
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.USER_EVENTS_QUEUE,
                message
            );
            
            logger.info("Successfully published user event for user {}", message.getUserId());
            
        } catch (Exception e) {
            logger.error("Failed to publish user event to queue for user {}: {}", 
                        message.getUserId(), e.getMessage(), e);
        }
    }
}

