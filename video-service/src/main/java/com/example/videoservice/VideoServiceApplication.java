package com.example.videoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for Video Service.
 * 
 * This microservice implements the OBSERVER PATTERN for event-driven notifications.
 * 
 * Design Pattern: OBSERVER PATTERN
 * - VideoEvent (Subject/Event interface)
 * - VideoWatchedEvent, ContentRatedEvent (Concrete Events)
 * - EventObserver (Observer interface)
 * - RecommendationUpdateObserver, AnalyticsObserver (Concrete Observers)
 * - VideoEventPublisher (Publisher/Subject that maintains observer list)
 * - VideoService (Client that publishes events)
 * 
 * Observer Pattern Adaptation for Microservices:
 * - Traditional Observer: direct method calls within same process
 * - Microservices Observer: REST calls to remote services
 * - Demonstrates graceful degradation: observer failures don't fail main operation
 * - Spring auto-discovery: all EventObserver beans are automatically registered
 * 
 * Inter-Service Communication:
 * - Validates content with Content Service before recording events
 * - Notifies Recommendation Service via REST when events occur
 * - Logs analytics locally via AnalyticsObserver
 * 
 * Port: 8083
 * Database: PostgreSQL on port 5434 (videodb)
 */
@SpringBootApplication
public class VideoServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(VideoServiceApplication.class, args);
    }
}
