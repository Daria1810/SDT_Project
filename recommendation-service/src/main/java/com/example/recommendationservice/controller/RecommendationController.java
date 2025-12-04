package com.example.recommendationservice.controller;

import com.example.recommendationservice.dto.RecommendationResponseDTO;
import com.example.recommendationservice.dto.UserPreferenceUpdateDTO;
import com.example.recommendationservice.model.UserPreference;
import com.example.recommendationservice.service.RecommendationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for Recommendation Service.
 * Provides endpoints for:
 * - Getting personalized recommendations
 * - Updating user preferences
 * - Health check
 */
@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {
    
    private static final Logger logger = LoggerFactory.getLogger(RecommendationController.class);
    
    @Autowired
    private RecommendationService recommendationService;
    
    /**
     * Get personalized recommendations for a user.
     * 
     * @param userId User ID to get recommendations for
     * @param limit Optional limit on number of recommendations (default: 10)
     * @return RecommendationResponseDTO with list of recommended content
     */
    @GetMapping("/{userId}")
    public ResponseEntity<RecommendationResponseDTO> getRecommendations(
            @PathVariable Long userId,
            @RequestParam(required = false) Integer limit) {
        
        logger.info("GET /api/recommendations/{} - limit={}", userId, limit);
        
        RecommendationResponseDTO response = recommendationService.getRecommendations(userId, limit);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Update user preferences.
     * This endpoint is called by Video Service when:
     * - User watches a video (update watch count and preferred genres)
     * - User rates content (update average rating)
     * 
     * @param updateDTO User preference update data
     * @return Updated UserPreference
     */
    @PostMapping("/update")
    public ResponseEntity<UserPreference> updateUserPreferences(
            @Valid @RequestBody UserPreferenceUpdateDTO updateDTO) {
        
        logger.info("POST /api/recommendations/update - userId={}", updateDTO.getUserId());
        
        UserPreference updated = recommendationService.updateUserPreferences(updateDTO);
        
        return ResponseEntity.ok(updated);
    }
    
    /**
     * Get user preferences.
     * 
     * @param userId User ID
     * @return UserPreference or 404 if not found
     */
    @GetMapping("/preferences/{userId}")
    public ResponseEntity<UserPreference> getUserPreferences(@PathVariable Long userId) {
        logger.info("GET /api/recommendations/preferences/{}", userId);
        
        UserPreference preferences = recommendationService.getUserPreferences(userId);
        
        if (preferences == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(preferences);
    }
    
    /**
     * Health check endpoint.
     * 
     * @return Health status
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "recommendation-service");
        health.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(health);
    }
}
