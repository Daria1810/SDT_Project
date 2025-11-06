package com.example.strategy;

import com.example.model.Content;
import com.example.model.User;
import java.util.List;

/**
 * Contract for recommendation algorithms.
 */
public interface RecommendationStrategy {
    /**
     * Generate recommendations.
     * @param user the user to recommend to
     * @param limit max items to return
     * @return recommended content
     */
    List<Content> recommend(User user, int limit);
    
    /**
     * @return human-friendly name of this strategy
     */
    String getStrategyName();
}
