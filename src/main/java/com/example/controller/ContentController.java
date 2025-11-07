package com.example.controller;

import com.example.model.Content;
import com.example.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


 //REST endpoints for creating and fetching content.
 
@RestController
@RequestMapping("/api/content")
public class ContentController {
    @Autowired
    private ContentService contentService;
    
    @RequestMapping(value = "/movie", method = org.springframework.web.bind.annotation.RequestMethod.POST)
    public Map<String, Object> createMovie(@RequestBody Map<String, Object> request) {
        Content movie = contentService.createMovie(
            (String) request.get("title"),
            (String) request.get("description"),
            (String) request.get("genre"),
            (Integer) request.get("releaseYear"),
            (Integer) request.get("durationMinutes")
        );
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("content", movie);
        response.put("message", "Movie created using Factory Pattern");
        return response;
    }
    
    @RequestMapping(value = "/tvseries", method = org.springframework.web.bind.annotation.RequestMethod.POST)
    public Map<String, Object> createTVSeries(@RequestBody Map<String, Object> request) {
        Content series = contentService.createTVSeries(
            (String) request.get("title"),
            (String) request.get("description"),
            (String) request.get("genre"),
            (Integer) request.get("releaseYear"),
            (Integer) request.get("seasons"),
            (Integer) request.get("totalEpisodes"),
            (Integer) request.get("averageEpisodeDuration")
        );
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("content", series);
        response.put("message", "TV Series created using Factory Pattern");
        return response;
    }
    
    @RequestMapping(value = "/{id}", method = org.springframework.web.bind.annotation.RequestMethod.GET)
    public Map<String, Object> getContent(@PathVariable Long id) {
        Content content = contentService.getContentById(id);
        
        Map<String, Object> response = new HashMap<>();
        if (content != null) {
            response.put("success", true);
            response.put("content", content);
            response.put("type", content.getContentType());
            response.put("durationInfo", content.getDurationInfo());
        } else {
            response.put("success", false);
            response.put("message", "Content not found");
        }
        return response;
    }
}
