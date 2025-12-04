package com.example.videoservice.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * RecommendationUpdateObserver - REST-based Observer for updating Recommendation Service.
 * 
 * This is a CONCRETE OBSERVER in the Observer Pattern adapted for microservices.
 * Instead of calling a local method, it makes a REST call to the Recommendation Service.
 * 
 * Key Responsibilities:
 * - Listens for video watch events
 * - Makes HTTP POST to Recommendation Service to update user preferences
 * - Handles network failures gracefully (logs error but doesn't fail the watch event)
 * 
 * This demonstrates how the Observer pattern adapts from monolithic to microservices architecture.
 */
@Component
public class RecommendationUpdateObserver implements EventObserver {
    
    private static final Logger logger = LoggerFactory.getLogger(RecommendationUpdateObserver.class);
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${recommendation.service.url}")
    private String recommendationServiceUrl;
    
    @Override
    public void update(VideoEvent event) {
        logger.info("RecommendationUpdateObserver: Processing {} event for user {} and content {}", 
                   event.getEventType(), event.getUserId(), event.getContentId());
        
        try {
            String url = recommendationServiceUrl + "/api/recommendations/update";
            
            // Build request based on event type
            Map<String, Object> request = new HashMap<>();
            request.put("userId", event.getUserId());
            
            if (event instanceof VideoWatchedEvent) {
                VideoWatchedEvent watchEvent = (VideoWatchedEvent) event;
                
                // For watch events, increment watch count and update preferred genres
                request.put("watchCount", 1); // Will be aggregated on the recommendation service side
                
                if (watchEvent.getGenre() != null) {
                    request.put("preferredGenres", watchEvent.getGenre());
                }
                
                logger.debug("RecommendationUpdateObserver: Sending watch event update - userId={}, genre={}", 
                           event.getUserId(), watchEvent.getGenre());
                
            } else if (event instanceof ContentRatedEvent) {
                ContentRatedEvent ratedEvent = (ContentRatedEvent) event;
                
                // For rating events, update average rating
                request.put("averageRating", ratedEvent.getScore());
                
                if (ratedEvent.getGenre() != null) {
                    request.put("preferredGenres", ratedEvent.getGenre());
                }
                
                logger.debug("RecommendationUpdateObserver: Sending rating update - userId={}, score={}, genre={}", 
                           event.getUserId(), ratedEvent.getScore(), ratedEvent.getGenre());
            }
            
            // Make REST call to Recommendation Service
            logger.info("RecommendationUpdateObserver: Calling {} with request: {}", url, request);
            
            Object response = restTemplate.postForObject(url, request, Object.class);
            
            logger.info("RecommendationUpdateObserver: Successfully notified Recommendation Service for user {}", 
                       event.getUserId());
            logger.debug("RecommendationUpdateObserver: Response: {}", response);
            
        } catch (RestClientException e) {
            // IMPORTANT: Don't fail the watch event if recommendation service is down
            // This demonstrates graceful degradation in microservices
            logger.error("RecommendationUpdateObserver: Failed to notify Recommendation Service for user {}: {}", 
                        event.getUserId(), e.getMessage());
            logger.debug("RecommendationUpdateObserver: Full error: ", e);
        } catch (Exception e) {
            logger.error("RecommendationUpdateObserver: Unexpected error processing event for user {}: {}", 
                        event.getUserId(), e.getMessage(), e);
        }
    }
    
    @Override
    public String getObserverName() {
        return "RecommendationUpdateObserver";
    }
}
