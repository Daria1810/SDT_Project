package com.example.videoservice.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumer for content events from RabbitMQ.
 * Video Service reacts to new content creation for validation purposes.
 */
@Component
public class ContentEventMessageConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(ContentEventMessageConsumer.class);
    
    @RabbitListener(queues = "content.events")
    public void handleContentEvent(ContentEventMessage message) {
        try {
            logger.info("=== Received content event from queue ===");
            logger.info("Event: contentId={}, eventType={}, title={}", 
                       message.getContentId(), message.getEventType(), message.getTitle());
            
            if ("CONTENT_CREATED".equals(message.getEventType())) {
                logger.info("New content available for watch events: {} (ID: {})", 
                           message.getTitle(), message.getContentId());
                // Video Service now knows about new content without REST call
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

