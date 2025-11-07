package com.example.strategy;

import com.example.model.Content;
import com.example.model.User;
import java.util.List;


 //contract for recommendation algorithms.
 
public interface RecommendationStrategy {
    /**
     * generate recommendations.
     * @param user the user to recommend to
     * @param limit max items to return
     * @return recommended content
     */
    List<Content> recommend(User user, int limit);
    
    /**
     * @return the name of the strategy
     */
    String getStrategyName();
}
