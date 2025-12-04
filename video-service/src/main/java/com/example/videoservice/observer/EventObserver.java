package com.example.videoservice.observer;

/**
 * EventObserver interface - Observer in the Observer Pattern.
 * 
 * Implementations of this interface will be notified when video events occur.
 * This is the OBSERVER INTERFACE in the Observer Pattern.
 * 
 * In a microservices architecture, observers can:
 * - Make REST calls to other services (RecommendationUpdateObserver)
 * - Store data locally (AnalyticsObserver)
 * - Send messages to queues/topics (future implementations)
 */
public interface EventObserver {
    
    /**
     * Called when a video event occurs.
     * Observers should handle failures gracefully to not block the main flow.
     * 
     * @param event The video event that occurred
     */
    void update(VideoEvent event);
    
    /**
     * Get the name of this observer (for logging and debugging)
     * 
     * @return Observer name
     */
    String getObserverName();
}
