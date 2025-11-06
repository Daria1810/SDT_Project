package com.example.service;

import com.example.observer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Tracks watch and rating events and fans them out to interested handlers.
 * Multiple observers get notified when something happens.
 */
@Service
public class VideoService {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    
    private List<EventObserver> observers = new ArrayList<>();
    
    public VideoService() {
        // Initialize observers
    }
    
    /**
     * Register default observers once JDBC is available.
     */
    @Autowired
    public void initObservers(NamedParameterJdbcTemplate jdbcTemplate) {
        observers.add(new WatchHistoryObserver(jdbcTemplate));
        observers.add(new RecommendationObserver(jdbcTemplate));
        observers.add(new AnalyticsObserver(jdbcTemplate));
        observers.add(new NotificationObserver());
        
        System.out.println("[VideoService] Initialized " + observers.size() + " observers");
    }
    
    /**
     * Record a video watch event and notify observers.
     */
    public void recordWatchEvent(Long userId, Long contentId, int progressSeconds, boolean completed) {
        // Create event
        VideoWatchedEvent event = new VideoWatchedEvent(userId, contentId, progressSeconds, completed);
        
        System.out.println("\n[VideoService] Video watch event occurred - notifying " + observers.size() + " observers");
        
    // Notify all observers
        notifyObservers(event);
        
        System.out.println("[VideoService] All observers notified\n");
    }
    
    /**
     * Record a content rating event and notify observers.
     */
    public void recordRating(Long userId, Long contentId, int rating) {
        // Save rating to database
        String sql = "MERGE INTO rating (user_id, content_id, rating) " +
                    "KEY (user_id, content_id) " +
                    "VALUES (:userId, :contentId, :rating)";
        
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("userId", userId)
            .addValue("contentId", contentId)
            .addValue("rating", rating);
        
        jdbcTemplate.update(sql, params);
        
        // Create and notify observers
        ContentRatedEvent event = new ContentRatedEvent(userId, contentId, rating);
        
        System.out.println("\n[VideoService] Content rated event occurred - notifying observers");
        notifyObservers(event);
        System.out.println("[VideoService] All observers notified\n");
    }
    
    /**
     * Notify all registered observers of an event.
     */
    private void notifyObservers(Event event) {
        for (EventObserver observer : observers) {
            observer.update(event);
        }
    }
    
    /**
     * Add a new observer dynamically.
     */
    public void registerObserver(EventObserver observer) {
        observers.add(observer);
        System.out.println("[VideoService] Registered new observer: " + observer.getObserverName());
    }
    
    /**
     * Remove an observer.
     */
    public void unregisterObserver(EventObserver observer) {
        observers.remove(observer);
        System.out.println("[VideoService] Unregistered observer: " + observer.getObserverName());
    }
}
