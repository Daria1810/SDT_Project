package com.example.observer;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * Saves watch progress when a user watches a video.
 */
public class WatchHistoryObserver implements EventObserver {
    private NamedParameterJdbcTemplate jdbcTemplate;
    
    public WatchHistoryObserver(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public void update(Event event) {
        if (event instanceof VideoWatchedEvent) {
            VideoWatchedEvent videoEvent = (VideoWatchedEvent) event;
            saveWatchHistory(videoEvent);
        }
    }
    
    private void saveWatchHistory(VideoWatchedEvent event) {
        String sql = "MERGE INTO watch_history (user_id, content_id, progress_seconds, last_watched, completed) " +
                    "KEY (user_id, content_id) " +
                    "VALUES (:userId, :contentId, :progressSeconds, CURRENT_TIMESTAMP, :completed)";
        
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("userId", event.getUserId())
            .addValue("contentId", event.getContentId())
            .addValue("progressSeconds", event.getProgressSeconds())
            .addValue("completed", event.isCompleted());
        
        jdbcTemplate.update(sql, params);
        System.out.println("[WatchHistoryObserver] Saved watch progress for user " + event.getUserId() + 
                          ", content " + event.getContentId() + ": " + event.getProgressSeconds() + "s");
    }
    
    @Override
    public String getObserverName() {
        return "WatchHistoryObserver";
    }
}
