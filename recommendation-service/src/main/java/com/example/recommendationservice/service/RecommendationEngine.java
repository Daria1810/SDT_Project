package com.example.recommendationservice.service;

import com.example.recommendationservice.strategy.RecommendationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * RecommendationEngine - Context class in the Strategy Pattern.
 * 
 * This class maintains a reference to a RecommendationStrategy and delegates
 * recommendation generation to the current strategy.
 * 
 * The strategy can be changed at runtime based on user characteristics.
 * 
 * This is the CONTEXT CLASS in the Strategy Pattern.
 */
public class RecommendationEngine {
    
    private static final Logger logger = LoggerFactory.getLogger(RecommendationEngine.class);
    
    private RecommendationStrategy strategy;
    
    /**
     * Set the recommendation strategy to use.
     * This allows dynamic strategy selection at runtime.
     * 
     * @param strategy The strategy to use for recommendations
     */
    public void setStrategy(RecommendationStrategy strategy) {
        this.strategy = strategy;
        logger.info("RecommendationEngine: Strategy set to '{}'", strategy.getStrategyName());
    }
    
    /**
     * Get recommendations using the current strategy.
     * 
     * @param userId The user ID to generate recommendations for
     * @param limit Maximum number of recommendations
     * @return List of content IDs recommended by the current strategy
     * @throws IllegalStateException if no strategy has been set
     */
    public List<Long> getRecommendations(Long userId, int limit) {
        if (strategy == null) {
            logger.error("RecommendationEngine: No strategy set!");
            throw new IllegalStateException("No recommendation strategy has been set");
        }
        
        logger.debug("RecommendationEngine: Using '{}' strategy for user {}", 
                    strategy.getStrategyName(), userId);
        
        return strategy.recommend(userId, limit);
    }
    
    /**
     * Get the name of the current strategy.
     * 
     * @return Strategy name, or "None" if no strategy set
     */
    public String getCurrentStrategyName() {
        return strategy != null ? strategy.getStrategyName() : "None";
    }
}
