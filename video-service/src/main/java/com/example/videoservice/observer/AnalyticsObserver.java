package com.example.videoservice.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * AnalyticsObserver - Local Observer for analytics tracking.
 * 
 * This is a CONCRETE OBSERVER in the Observer Pattern.
 * Processes events locally for analytics purposes without external service calls.
 * 
 * Key Responsibilities:
 * - Logs analytics data for watch events and ratings
 * - Can be extended to store aggregated metrics in a local database
 * - Provides insights into user behavior
 * 
 * This demonstrates the flexibility of the Observer pattern:
 * - Some observers make REST calls (RecommendationUpdateObserver)
 * - Some observers process data locally (AnalyticsObserver)
 */
@Component
public class AnalyticsObserver implements EventObserver {
    
    private static final Logger logger = LoggerFactory.getLogger(AnalyticsObserver.class);
    
    @Override
    public void update(VideoEvent event) {
        logger.info("AnalyticsObserver: Processing {} event", event.getEventType());
        
        try {
            if (event instanceof VideoWatchedEvent) {
                VideoWatchedEvent watchEvent = (VideoWatchedEvent) event;
                
                logger.info("ANALYTICS: User {} watched content {} - Progress: {}s, Completed: {}, Genre: {}", 
                           watchEvent.getUserId(),
                           watchEvent.getContentId(),
                           watchEvent.getProgress(),
                           watchEvent.getCompleted(),
                           watchEvent.getGenre());
                
                // In a real system, this would:
                // 1. Store metrics in a time-series database
                // 2. Update real-time dashboards
                // 3. Trigger alerts for unusual patterns
                // 4. Calculate engagement metrics
                
                if (watchEvent.getCompleted()) {
                    logger.info("ANALYTICS: Content {} completion by user {} - positive engagement signal", 
                               watchEvent.getContentId(), watchEvent.getUserId());
                }
                
            } else if (event instanceof ContentRatedEvent) {
                ContentRatedEvent ratedEvent = (ContentRatedEvent) event;
                
                logger.info("ANALYTICS: User {} rated content {} with score {} - Genre: {}", 
                           ratedEvent.getUserId(),
                           ratedEvent.getContentId(),
                           ratedEvent.getScore(),
                           ratedEvent.getGenre());
                
                // Analytics for ratings
                if (ratedEvent.getScore() >= 4.0) {
                    logger.info("ANALYTICS: High rating ({}) for content {} - quality signal", 
                               ratedEvent.getScore(), ratedEvent.getContentId());
                } else if (ratedEvent.getScore() <= 2.0) {
                    logger.info("ANALYTICS: Low rating ({}) for content {} - may need review", 
                               ratedEvent.getScore(), ratedEvent.getContentId());
                }
            }
            
            logger.debug("AnalyticsObserver: Successfully processed event at {}", event.getTimestamp());
            
        } catch (Exception e) {
            logger.error("AnalyticsObserver: Error processing analytics for event: {}", e.getMessage(), e);
        }
    }
    
    @Override
    public String getObserverName() {
        return "AnalyticsObserver";
    }
}
