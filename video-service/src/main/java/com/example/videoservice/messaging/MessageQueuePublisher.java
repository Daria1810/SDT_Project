package com.example.videoservice.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.videoservice.config.RabbitMQConfig;

/**
 * Message Queue Publisher for Video Service.
 * 
 * Publishes user preference update messages to RabbitMQ queue.
 * This replaces synchronous REST calls with asynchronous message queue communication.
 * 
 * Benefits:
 * - Decoupling: Video Service doesn't need to know Recommendation Service details
 * - Scalability: Messages are queued and processed asynchronously
 * - Fault Tolerance: If Recommendation Service is down, messages are queued and processed later
 * - Performance: Non-blocking, doesn't slow down watch/rating operations
 */
@Component
public class MessageQueuePublisher {
    
    private static final Logger logger = LoggerFactory.getLogger(MessageQueuePublisher.class);
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    /**
     * Publish user preference update message to RabbitMQ queue.
     * 
     * @param message User preference update message
     */
    public void publishUserPreferenceUpdate(UserPreferenceMessage message) {
        try {
            logger.info("Publishing user preference update to queue: userId={}, eventType={}, contentId={}", 
                       message.getUserId(), message.getEventType(), message.getContentId());
            
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.USER_PREFERENCE_QUEUE,
                message
            );
            
            logger.info("Successfully published message to queue for user {}", message.getUserId());
            
        } catch (Exception e) {
            // Log error but don't fail the main operation
            // This ensures watch/rating events succeed even if queue is temporarily unavailable
            logger.error("Failed to publish message to queue for user {}: {}", 
                        message.getUserId(), e.getMessage(), e);
        }
    }
}

