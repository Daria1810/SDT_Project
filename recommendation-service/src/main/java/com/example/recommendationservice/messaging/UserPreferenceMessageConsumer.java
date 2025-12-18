package com.example.recommendationservice.messaging;

import com.example.recommendationservice.model.UserPreference;
import com.example.recommendationservice.repository.RecommendationRepository;
import com.example.recommendationservice.service.RecommendationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Message Queue Consumer for Recommendation Service.
 * 
 * Consumes user preference update messages from RabbitMQ queue.
 * This replaces synchronous REST calls with asynchronous message queue communication.
 * 
 * Benefits:
 * - Decoupling: Recommendation Service doesn't need to be available when Video Service publishes
 * - Scalability: Messages are queued and processed asynchronously, can handle bursts
 * - Fault Tolerance: If Recommendation Service is down, messages are queued and processed when it's back
 * - Performance: Non-blocking, doesn't slow down Video Service operations
 */
@Component
public class UserPreferenceMessageConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(UserPreferenceMessageConsumer.class);
    
    @Autowired
    private RecommendationRepository recommendationRepository;
    
    @Autowired
    private RecommendationService recommendationService;
    
    /**
     * Consume user preference update messages from RabbitMQ queue.
     * 
     * This method is called automatically when a message arrives in the queue.
     * 
     * @param message User preference update message
     */
    @RabbitListener(queues = "user.preference.updates")
    public void handleUserPreferenceUpdate(UserPreferenceMessage message) {
        try {
            logger.info("=== Received user preference update from queue ===");
            logger.info("Message: userId={}, eventType={}, contentId={}, genre={}", 
                       message.getUserId(), message.getEventType(), 
                       message.getContentId(), message.getGenre());
            
            // Load or create user preference
            UserPreference preference = recommendationRepository.findByUserId(message.getUserId())
                    .orElseGet(() -> {
                        logger.info("Creating new preference record for user {}", message.getUserId());
                        return new UserPreference(message.getUserId());
                    });
            
            // Update preference based on event type
            if ("WATCH".equals(message.getEventType())) {
                // Increment watch count
                preference.setWatchCount(preference.getWatchCount() + 
                    (message.getWatchCount() != null ? message.getWatchCount() : 1));
                
                // Update preferred genres
                if (message.getGenre() != null && !message.getGenre().isEmpty()) {
                    String currentGenres = preference.getPreferredGenres();
                    if (currentGenres == null || currentGenres.isEmpty()) {
                        preference.setPreferredGenres(message.getGenre());
                    } else if (!currentGenres.contains(message.getGenre())) {
                        preference.setPreferredGenres(currentGenres + "," + message.getGenre());
                    }
                }
                
                logger.info("Updated watch count for user {}: {}", 
                           message.getUserId(), preference.getWatchCount());
                
            } else if ("RATE".equals(message.getEventType())) {
                // Update average rating
                if (message.getAverageRating() != null) {
                    preference.setAverageRating(message.getAverageRating());
                }
                
                // Update preferred genres
                if (message.getGenre() != null && !message.getGenre().isEmpty()) {
                    String currentGenres = preference.getPreferredGenres();
                    if (currentGenres == null || currentGenres.isEmpty()) {
                        preference.setPreferredGenres(message.getGenre());
                    } else if (!currentGenres.contains(message.getGenre())) {
                        preference.setPreferredGenres(currentGenres + "," + message.getGenre());
                    }
                }
                
                logger.info("Updated average rating for user {}: {}", 
                           message.getUserId(), preference.getAverageRating());
            }
            
            // Save updated preference
            recommendationRepository.save(preference);
            
            logger.info("=== Successfully processed user preference update for user {} ===", 
                       message.getUserId());
            
        } catch (Exception e) {
            // Log error but don't throw - RabbitMQ will retry if needed
            logger.error("Failed to process user preference update for user {}: {}", 
                        message.getUserId(), e.getMessage(), e);
            throw e; // Re-throw to trigger retry mechanism
        }
    }
}

