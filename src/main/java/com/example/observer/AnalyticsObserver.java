package com.example.observer;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


//updates analytics like view counts and average ratings

public class AnalyticsObserver implements EventObserver {
    private NamedParameterJdbcTemplate jdbcTemplate;
    
    public AnalyticsObserver(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public void update(Event event) {
        if (event instanceof VideoWatchedEvent) {
            VideoWatchedEvent videoEvent = (VideoWatchedEvent) event;
            if (videoEvent.isCompleted()) {
                incrementViewCount(videoEvent.getContentId());
            }
        } else if (event instanceof ContentRatedEvent) {
            ContentRatedEvent ratingEvent = (ContentRatedEvent) event;
            updateAverageRating(ratingEvent.getContentId());
        }
    }
    
    private void incrementViewCount(Long contentId) {
        String sql = "UPDATE content SET view_count = view_count + 1 WHERE id = :contentId";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("contentId", contentId);
        jdbcTemplate.update(sql, params);
        System.out.println("[AnalyticsObserver] Incremented view count for content " + contentId);
    }
    
    private void updateAverageRating(Long contentId) {
        String sql = "UPDATE content SET average_rating = (" +
                    "SELECT AVG(rating) FROM rating WHERE content_id = :contentId" +
                    ") WHERE id = :contentId";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("contentId", contentId);
        jdbcTemplate.update(sql, params);
        System.out.println("[AnalyticsObserver] Updated average rating for content " + contentId);
    }
    
    @Override
    public String getObserverName() {
        return "AnalyticsObserver";
    }
}
