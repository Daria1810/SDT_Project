package com.example.observer;

//import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

//observer that updates recommendations based on user activity

public class RecommendationObserver implements EventObserver {
    private NamedParameterJdbcTemplate jdbcTemplate;
    
    public RecommendationObserver(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public void update(Event event) {
        if (event instanceof VideoWatchedEvent) {
            VideoWatchedEvent videoEvent = (VideoWatchedEvent) event;
            updateUserPreferences(videoEvent);
        } else if (event instanceof ContentRatedEvent) {
            ContentRatedEvent ratingEvent = (ContentRatedEvent) event;
            updateRatingPreferences(ratingEvent);
        }
    }
    
    private void updateUserPreferences(VideoWatchedEvent event) {
        //in real system, would update ML model or preference weights
        System.out.println("[RecommendationObserver] Updated preferences for user " + event.getUserId() + 
                          " based on watching content " + event.getContentId());
    }
    
    private void updateRatingPreferences(ContentRatedEvent event) {
        System.out.println("[RecommendationObserver] Updated preferences for user " + event.getUserId() + 
                          " based on rating content " + event.getContentId() + " with " + event.getRating() + " stars");
    }
    
    @Override
    public String getObserverName() {
        return "RecommendationObserver";
    }
}
