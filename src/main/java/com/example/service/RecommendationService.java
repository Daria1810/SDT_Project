package com.example.service;

import com.example.model.Content;
import com.example.model.User;
import com.example.strategy.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

//service that provides content recommendations using different strategies

@Service
public class RecommendationService {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    
    private RecommendationStrategy strategy;
    
    //get recommendations for a user
    public List<Content> getRecommendations(User user, int limit) {
        selectStrategy(user);
    List<Content> results = strategy.recommend(user, limit);
    System.out.println("[RecommendationService] Using " + strategy.getStrategyName() +
        " strategy for user " + user.getId() + "; got " + results.size() + " items");

        // Fallbacks: if the chosen strategy returns nothing, try alternates
        if (results.isEmpty()) {
            if (strategy instanceof RatingBasedStrategy) {
                System.out.println("[RecommendationService] Fallback to History-Based");
                strategy = new HistoryBasedStrategy(jdbcTemplate);
                results = strategy.recommend(user, limit);
                if (results.isEmpty()) {
                    System.out.println("[RecommendationService] Fallback to Trending");
                    strategy = new TrendingStrategy(jdbcTemplate);
                    results = strategy.recommend(user, limit);
                }
            } else if (strategy instanceof HistoryBasedStrategy) {
                System.out.println("[RecommendationService] Fallback to Rating-Based");
                strategy = new RatingBasedStrategy(jdbcTemplate);
                results = strategy.recommend(user, limit);
                if (results.isEmpty()) {
                    System.out.println("[RecommendationService] Fallback to Trending");
                    strategy = new TrendingStrategy(jdbcTemplate);
                    results = strategy.recommend(user, limit);
                }
            }
        }

        return results;
    }

    /** Exposes the active strategy name for debugging/UI. */
    public String getCurrentStrategyName() {
        return strategy != null ? strategy.getStrategyName() : "Unknown";
    }
    
    //choose the best strategy based on user data

    private void selectStrategy(User user) {
        if (hasRatings(user)) {
            // Users who rate content get rating-based recommendations
            strategy = new RatingBasedStrategy(jdbcTemplate);
        } else if (hasWatchHistory(user)) {
            // Users with watch history get history-based recommendations
            strategy = new HistoryBasedStrategy(jdbcTemplate);
        } else {
            // No signals yet: trending
            strategy = new TrendingStrategy(jdbcTemplate);
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
    
    //optional manual override
    public void setStrategy(RecommendationStrategy strategy) {
        this.strategy = strategy;
    }
}
