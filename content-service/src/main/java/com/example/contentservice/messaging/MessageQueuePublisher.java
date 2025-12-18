package com.example.contentservice.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.contentservice.config.RabbitMQConfig;

/**
 * Message Queue Publisher for Content Service.
 * Publishes content events to RabbitMQ.
 */
@Component
public class MessageQueuePublisher {
    
    private static final Logger logger = LoggerFactory.getLogger(MessageQueuePublisher.class);
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    public void publishContentEvent(ContentEventMessage message) {
        try {
            logger.info("Publishing content event to queue: contentId={}, eventType={}, title={}", 
                       message.getContentId(), message.getEventType(), message.getTitle());
            
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.CONTENT_EVENTS_QUEUE,
                message
            );
            
            logger.info("Successfully published content event for content {}", message.getContentId());
            
        } catch (Exception e) {
            logger.error("Failed to publish content event to queue for content {}: {}", 
                        message.getContentId(), e.getMessage(), e);
        }
    }
}

