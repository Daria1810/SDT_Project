package com.example.controller;

import com.example.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

//REST endpoints for recording watches and ratings.

@RestController
@RequestMapping("/api/video")
public class VideoController {
    @Autowired
    private VideoService videoService;
    
    @RequestMapping(value = "/watch", method = org.springframework.web.bind.annotation.RequestMethod.POST)
    public Map<String, Object> recordWatchEvent(@RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        Long contentId = Long.valueOf(request.get("contentId").toString());
        int progressSeconds = (Integer) request.get("progressSeconds");
        boolean completed = (Boolean) request.get("completed");
        
        videoService.recordWatchEvent(userId, contentId, progressSeconds, completed);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Watch event recorded - all observers notified (Observer Pattern)");
        return response;
    }
    
    @RequestMapping(value = "/rate", method = org.springframework.web.bind.annotation.RequestMethod.POST)
    public Map<String, Object> rateContent(@RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        Long contentId = Long.valueOf(request.get("contentId").toString());
        int rating = (Integer) request.get("rating");
        
        if (rating < 1 || rating > 5) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Rating must be between 1 and 5");
            return response;
        }
        
        videoService.recordRating(userId, contentId, rating);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Rating recorded - all observers notified (Observer Pattern)");
        return response;
    }
}
