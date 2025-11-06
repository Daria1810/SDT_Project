package com.example.service;

import com.example.model.Content;
import com.example.model.User;
import com.example.strategy.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Generates content recommendations.
 * Picks the best algorithm at runtime based on what we know about the user.
 */
@Service
public class RecommendationService {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    
    private RecommendationStrategy strategy;
    
    /**
     * Get recommendations for a user.
     * The underlying algorithm is chosen automatically from user data.
     */
    public List<Content> getRecommendations(User user, int limit) {
        selectStrategy(user);
        
        System.out.println("[RecommendationService] Using " + strategy.getStrategyName() + 
                          " strategy for user " + user.getId());
        
        return strategy.recommend(user, limit);
    }
    
    /**
     * Choose a recommendation approach based on user state.
     */
    private void selectStrategy(User user) {
        if (user.isNew() || !hasWatchHistory(user)) {
            // New users get trending content
            strategy = new TrendingStrategy(jdbcTemplate);
        } else if (hasRatings(user)) {
            // Users who rate content get rating-based recommendations
            strategy = new RatingBasedStrategy(jdbcTemplate);
        } else {
            // Users with watch history get history-based recommendations
            strategy = new HistoryBasedStrategy(jdbcTemplate);
        }
    }
    
    private boolean hasWatchHistory(User user) {
        String sql = "SELECT COUNT(*) FROM watch_history WHERE user_id = :userId";
        java.util.Map<String, Object> params = new java.util.HashMap<>();
        params.put("userId", user.getId());
        Number count = jdbcTemplate.queryForObject(sql, params, Number.class);
        return count != null && count.longValue() > 0;
    }
    
    private boolean hasRatings(User user) {
        String sql = "SELECT COUNT(*) FROM rating WHERE user_id = :userId";
        java.util.Map<String, Object> params = new java.util.HashMap<>();
        params.put("userId", user.getId());
        Number count = jdbcTemplate.queryForObject(sql, params, Number.class);
        return count != null && count.longValue() > 0;
    }
    
    /**
     * Optional manual override (useful for tests or admin tools).
     */
    public void setStrategy(RecommendationStrategy strategy) {
        this.strategy = strategy;
    }
}
