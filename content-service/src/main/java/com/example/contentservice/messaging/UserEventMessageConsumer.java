package com.example.contentservice.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumer for user events from RabbitMQ.
 * Content Service reacts to user events (for analytics).
 */
@Component
public class UserEventMessageConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(UserEventMessageConsumer.class);
    
    @RabbitListener(queues = "user.events")
    public void handleUserEvent(UserEventMessage message) {
        try {
            logger.info("=== Received user event from queue ===");
            logger.info("Event: userId={}, eventType={}, tier={}", 
                       message.getUserId(), message.getEventType(), message.getTier());
            
            if ("USER_CREATED".equals(message.getEventType())) {
                logger.info("New user registered: {} - could track for content analytics", 
                           message.getUsername());
            } else if ("SUBSCRIPTION_UPDATED".equals(message.getEventType())) {
                logger.info("User {} subscription changed to: {} - could adjust content recommendations", 
                           message.getUserId(), message.getTier());
            }
            
            logger.info("=== Successfully processed user event for user {} ===", 
                       message.getUserId());
            
        } catch (Exception e) {
            logger.error("Failed to process user event for user {}: {}", 
                        message.getUserId(), e.getMessage(), e);
            throw e;
        }
    }
}

