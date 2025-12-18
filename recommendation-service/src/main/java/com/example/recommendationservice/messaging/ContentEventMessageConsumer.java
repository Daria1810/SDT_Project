package com.example.recommendationservice.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumer for content events from RabbitMQ.
 * Recommendation Service reacts to new content creation.
 */
@Component
public class ContentEventMessageConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(ContentEventMessageConsumer.class);
    
    @RabbitListener(queues = "content.events")
    public void handleContentEvent(ContentEventMessage message) {
        try {
            logger.info("=== Received content event from queue ===");
            logger.info("Event: contentId={}, eventType={}, title={}, genre={}", 
                       message.getContentId(), message.getEventType(), 
                       message.getTitle(), message.getGenre());
            
            if ("CONTENT_CREATED".equals(message.getEventType())) {
                logger.info("New content created: {} ({}) - {}", 
                           message.getTitle(), message.getType(), message.getGenre());
                // Could update trending content or refresh recommendations
            }
            
            logger.info("=== Successfully processed content event for content {} ===", 
                       message.getContentId());
            
        } catch (Exception e) {
            logger.error("Failed to process content event for content {}: {}", 
                        message.getContentId(), e.getMessage(), e);
            throw e;
        }
    }
}

