package com.example.videoservice.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * VideoEventPublisher - Subject/Publisher in the Observer Pattern.
 * 
 * This is the SUBJECT (or PUBLISHER) in the Observer Pattern.
 * Maintains a list of observers and notifies them when events occur.
 * 
 * Key Responsibilities:
 * - Maintains the list of observers (automatically injected by Spring)
 * - Notifies all observers when an event occurs
 * - Ensures one observer's failure doesn't affect others
 * 
 * Design Notes:
 * - Uses Spring's dependency injection to automatically discover all EventObserver beans
 * - Observers are executed synchronously in order
 * - Each observer is isolated with try-catch to prevent cascade failures
 * - Logs all notifications for debugging and monitoring
 */
@Component
public class VideoEventPublisher {
    
    private static final Logger logger = LoggerFactory.getLogger(VideoEventPublisher.class);
    
    private final List<EventObserver> observers;
    
    /**
     * Constructor with automatic observer discovery via Spring dependency injection.
     * All beans implementing EventObserver will be automatically injected.
     * 
     * @param observers List of all EventObserver beans in the application context
     */
    @Autowired
    public VideoEventPublisher(List<EventObserver> observers) {
        this.observers = new ArrayList<>(observers);
        logger.info("VideoEventPublisher initialized with {} observers: {}", 
                   observers.size(),
                   observers.stream().map(EventObserver::getObserverName).toList());
    }
    
    /**
     * Notify all observers about an event.
     * 
     * This is the core of the Observer Pattern implementation.
     * Each observer is notified in sequence, with error isolation.
     * 
     * @param event The video event to publish
     */
    public void notifyObservers(VideoEvent event) {
        logger.info("=== VideoEventPublisher: Publishing {} event for user {} and content {} ===", 
                   event.getEventType(), event.getUserId(), event.getContentId());
        
        if (observers.isEmpty()) {
            logger.warn("VideoEventPublisher: No observers registered!");
            return;
        }
        
        int successCount = 0;
        int failureCount = 0;
        
        // Notify each observer
        for (EventObserver observer : observers) {
            try {
                logger.debug("VideoEventPublisher: Notifying observer '{}'", observer.getObserverName());
                
                observer.update(event);
                
                successCount++;
                logger.debug("VideoEventPublisher: Observer '{}' completed successfully", 
                           observer.getObserverName());
                
            } catch (Exception e) {
                failureCount++;
                logger.error("VideoEventPublisher: Observer '{}' failed: {}", 
                           observer.getObserverName(), e.getMessage());
                logger.debug("VideoEventPublisher: Full error from observer '{}':", 
                           observer.getObserverName(), e);
                // Continue with other observers despite this failure
            }
        }
        
        logger.info("VideoEventPublisher: Notification complete - {} observers notified ({} succeeded, {} failed)", 
                   observers.size(), successCount, failureCount);
    }
    
    /**
     * Get the list of registered observers (for testing and debugging).
     * 
     * @return Unmodifiable list of observers
     */
    public List<EventObserver> getObservers() {
        return List.copyOf(observers);
    }
    
    /**
     * Get the count of registered observers.
     * 
     * @return Number of observers
     */
    public int getObserverCount() {
        return observers.size();
    }
}
