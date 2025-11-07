package com.example.controller;

import com.example.model.Content;
import com.example.model.User;
import com.example.service.RecommendationService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


//REST endpoints for generating content recommendations.
 
@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {
    @Autowired
    private RecommendationService recommendationService;
    
    @Autowired
    private UserService userService;
    
    @RequestMapping(value = "/{userId}", method = org.springframework.web.bind.annotation.RequestMethod.GET)
    public Map<String, Object> getRecommendations(@PathVariable Long userId, 
                                                  @RequestParam(defaultValue = "10") int limit) {
        User user = userService.getUserById(userId);
        
        Map<String, Object> response = new HashMap<>();
        if (user == null) {
            response.put("success", false);
            response.put("message", "User not found");
            return response;
        }
        
        List<Content> recommendations = recommendationService.getRecommendations(user, limit);

        response.put("success", true);
        response.put("recommendations", recommendations);
        response.put("count", recommendations.size());
        response.put("strategyUsed", recommendationService.getCurrentStrategyName());
        response.put("message", "Recommendations generated using Strategy Pattern");
        return response;
    }
}
