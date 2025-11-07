package com.example.controller;

import com.example.model.Content;
import com.example.model.User;
import com.example.service.*;
import com.example.singleton.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


//demo endpoints that walk through how the pieces work together.
 
@RestController
@RequestMapping("/api/demo")
public class DemoController {
    @Autowired
    private ContentService contentService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RecommendationService recommendationService;
    
    @Autowired
    private VideoService videoService;
    
    /**
     * Complete demonstration of all patterns
     */
    @RequestMapping(value = "/full", method = org.springframework.web.bind.annotation.RequestMethod.GET)
    public Map<String, Object> fullDemo() {
        Map<String, Object> response = new HashMap<>();
        
        // 1. SINGLETON PATTERN - Show singleton instances
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();
        ConfigurationManager config = ConfigurationManager.getInstance();
        CacheManager cache = CacheManager.getInstance();
        
        response.put("1_singleton_demo", Map.of(
            "pattern", "Singleton",
            "connectionPoolMaxConnections", pool.getMaxConnections(),
            "cacheSize", cache.size(),
            "configProperty", config.getProperty("streamflix.recommendation.default-limit", "10"),
            "explanation", "All three singletons initialized once, shared across entire application"
        ));
        
        // 2. FACTORY PATTERN - Create content objects
        Content movie = contentService.createMovie(
            "Interstellar", 
            "A team of explorers travel through a wormhole in space",
            "Sci-Fi", 
            2014, 
            169
        );
        
        Content series = contentService.createTVSeries(
            "The Crown",
            "Historical drama about Queen Elizabeth II",
            "Drama",
            2016,
            6,
            60,
            58
        );
        
        response.put("2_factory_demo", Map.of(
            "pattern", "Factory Method",
            "movieCreated", movie.getTitle() + " (" + movie.getContentType() + ")",
            "seriesCreated", series.getTitle() + " (" + series.getContentType() + ")",
            "explanation", "Different factories created Movie and TVSeries objects without conditional logic"
        ));
        
        // 3. OBSERVER PATTERN - Record video watch event
        User user = userService.getUserById(1L);
        if (user != null) {
            videoService.recordWatchEvent(user.getId(), movie.getId(), 5040, false);
            videoService.recordRating(user.getId(), movie.getId(), 5);
            
            response.put("3_observer_demo", Map.of(
                "pattern", "Observer",
                "event", "User " + user.getId() + " watched and rated content " + movie.getId(),
                "observersNotified", "4 observers (WatchHistory, Recommendation, Analytics, Notification)",
                "explanation", "Single event triggered multiple independent observers automatically"
            ));
        }
        
        // 4. STRATEGY PATTERN - Get recommendations
        User newUser = userService.getUserById(2L);  // Jane - new user
        User existingUser = userService.getUserById(1L);  // John - has history
        
        List<Content> newUserRecs = recommendationService.getRecommendations(newUser, 3);
        List<Content> existingUserRecs = recommendationService.getRecommendations(existingUser, 3);
        
        response.put("4_strategy_demo", Map.of(
            "pattern", "Strategy",
            "newUserStrategy", "Trending (no history)",
            "newUserRecommendations", newUserRecs.size() + " items",
            "existingUserStrategy", "Rating-Based (has ratings)",
            "existingUserRecommendations", existingUserRecs.size() + " items",
            "explanation", "Different recommendation algorithms selected based on user state at runtime"
        ));
        
        response.put("summary", "All 4 design patterns demonstrated successfully!");
        return response;
    }
    
    /**
     * Test singleton instance uniqueness
     */
    @RequestMapping(value = "/singleton-test", method = org.springframework.web.bind.annotation.RequestMethod.GET)
    public Map<String, Object> testSingleton() {
        DatabaseConnectionPool pool1 = DatabaseConnectionPool.getInstance();
        DatabaseConnectionPool pool2 = DatabaseConnectionPool.getInstance();
        CacheManager cache1 = CacheManager.getInstance();
        CacheManager cache2 = CacheManager.getInstance();
        
        Map<String, Object> response = new HashMap<>();
        response.put("pattern", "Singleton");
        response.put("connectionPoolSameInstance", pool1 == pool2);
        response.put("cacheSameInstance", cache1 == cache2);
        response.put("explanation", "Multiple getInstance() calls return the exact same object reference");
        return response;
    }
}
