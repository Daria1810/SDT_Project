package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


//REST endpoints for registering and retrieving users.
 
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    
    @RequestMapping(value = "/register", method = org.springframework.web.bind.annotation.RequestMethod.POST)
    public Map<String, Object> registerUser(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String email = (String) request.get("email");
            String password = (String) request.get("password");
            // Accept either "tier" or "subscriptionTier" from the client
            String tierStr = (String) (request.containsKey("tier") ? request.get("tier") : request.get("subscriptionTier"));
            if (tierStr == null) tierStr = "BASIC"; // sensible default for POC
            User.SubscriptionTier tier = User.SubscriptionTier.valueOf(tierStr);

            User user = userService.createUser(email, password, tier);
            response.put("success", true);
            response.put("userId", user.getId());
            response.put("email", user.getEmail());
            response.put("tier", user.getSubscriptionTier());
        } catch (IllegalArgumentException ex) {
            response.put("success", false);
            response.put("message", "Invalid subscription tier");
        } catch (Exception ex) {
            response.put("success", false);
            response.put("message", ex.getMessage());
        }
        return response;
    }
    
    @RequestMapping(value = "/{id}", method = org.springframework.web.bind.annotation.RequestMethod.GET)
    public Map<String, Object> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        
        Map<String, Object> response = new HashMap<>();
        if (user != null) {
            response.put("success", true);
            response.put("user", user);
        } else {
            response.put("success", false);
            response.put("message", "User not found");
        }
        return response;
    }
}
