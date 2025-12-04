package com.example.videoservice.controller;

import com.example.videoservice.dto.*;
import com.example.videoservice.service.VideoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Video Service.
 * Provides endpoints for:
 * - Recording watch events
 * - Submitting ratings
 * - Retrieving watch history and ratings
 * - Health check
 */
@RestController
@RequestMapping("/api/videos")
public class VideoController {
    
    private static final Logger logger = LoggerFactory.getLogger(VideoController.class);
    
    @Autowired
    private VideoService videoService;
    
    /**
     * Record a watch event.
     * Triggers Observer Pattern notification to Recommendation Service and Analytics.
     * 
     * @param createDTO Watch event data
     * @return Watch event response with ID
     */
    @PostMapping("/watch")
    public ResponseEntity<WatchEventResponseDTO> recordWatchEvent(
            @Valid @RequestBody WatchEventCreateDTO createDTO) {
        
        logger.info("POST /api/videos/watch - userId={}, contentId={}, progress={}, completed={}", 
                   createDTO.getUserId(), createDTO.getContentId(), 
                   createDTO.getProgress(), createDTO.getCompleted());
        
        WatchEventResponseDTO response = videoService.recordWatchEvent(createDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Submit a rating for content.
     * Triggers Observer Pattern notification to update recommendations.
     * 
     * @param createDTO Rating data
     * @return Rating response with ID
     */
    @PostMapping("/rate")
    public ResponseEntity<RatingResponseDTO> submitRating(
            @Valid @RequestBody RatingCreateDTO createDTO) {
        
        logger.info("POST /api/videos/rate - userId={}, contentId={}, score={}", 
                   createDTO.getUserId(), createDTO.getContentId(), createDTO.getScore());
        
        RatingResponseDTO response = videoService.submitRating(createDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Get watch history for a user.
     * 
     * @param userId User ID
     * @return List of watch events
     */
    @GetMapping("/watch/user/{userId}")
    public ResponseEntity<List<WatchEventResponseDTO>> getUserWatchHistory(@PathVariable Long userId) {
        logger.info("GET /api/videos/watch/user/{}", userId);
        
        List<WatchEventResponseDTO> history = videoService.getUserWatchHistory(userId);
        
        return ResponseEntity.ok(history);
    }
    
    /**
     * Get all ratings by a user.
     * 
     * @param userId User ID
     * @return List of ratings
     */
    @GetMapping("/rate/user/{userId}")
    public ResponseEntity<List<RatingResponseDTO>> getUserRatings(@PathVariable Long userId) {
        logger.info("GET /api/videos/rate/user/{}", userId);
        
        List<RatingResponseDTO> ratings = videoService.getUserRatings(userId);
        
        return ResponseEntity.ok(ratings);
    }
    
    /**
     * Get user's average rating.
     * 
     * @param userId User ID
     * @return Average rating
     */
    @GetMapping("/rate/user/{userId}/average")
    public ResponseEntity<Map<String, Object>> getUserAverageRating(@PathVariable Long userId) {
        logger.info("GET /api/videos/rate/user/{}/average", userId);
        
        Double average = videoService.getUserAverageRating(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("averageRating", average);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get content's average rating.
     * 
     * @param contentId Content ID
     * @return Average rating
     */
    @GetMapping("/rate/content/{contentId}/average")
    public ResponseEntity<Map<String, Object>> getContentAverageRating(@PathVariable Long contentId) {
        logger.info("GET /api/videos/rate/content/{}/average", contentId);
        
        Double average = videoService.getContentAverageRating(contentId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("contentId", contentId);
        response.put("averageRating", average);
        
        return ResponseEntity.ok(response);
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
        health.put("service", "video-service");
        health.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(health);
    }
}
