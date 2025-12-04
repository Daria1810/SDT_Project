package com.example.recommendationservice.strategy;

import java.util.List;

/**
 * Strategy Pattern Interface for Recommendation Algorithms.
 * 
 * Different implementations provide recommendations based on:
 * - TrendingStrategy: Most-viewed content for new users
 * - HistoryBasedStrategy: Content from preferred genres for users with watch history
 * - RatingBasedStrategy: Highly-rated content in preferred genres for users with ratings
 * 
 * This is the STRATEGY PATTERN interface.
 */
public interface RecommendationStrategy {
    
    /**
     * Generate content recommendations for a user.
     * 
     * @param userId The ID of the user to generate recommendations for
     * @param limit Maximum number of recommendations to return
     * @return List of content IDs recommended for the user
     */
    List<Long> recommend(Long userId, int limit);
    
    /**
     * Get the name of this recommendation strategy.
     * Used for logging and debugging.
     * 
     * @return Strategy name (e.g., "Trending", "History-Based", "Rating-Based")
     */
    String getStrategyName();
}
