package com.example.userservice.controller;

import com.example.userservice.dto.*;
import com.example.userservice.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User Controller - REST API endpoints for user management
 * 
 * Base URL: /api/users
 * Port: 8081
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;
    
    /**
     * Health check endpoint
     * GET /api/users/health
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("User Service is running");
    }
    
    /**
     * Register a new user
     * POST /api/users/register
     * 
     * Request Body:
     * {
     *   "username": "john_doe",
     *   "email": "john@example.com",
     *   "password": "password123"
     * }
     * 
     * Response: 201 Created with UserDTO
     */
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterDTO registerDTO) {
        logger.info("POST /api/users/register - Username: {}", registerDTO.getUsername());
        UserDTO user = userService.registerUser(registerDTO);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
    
    /**
     * Login user
     * POST /api/users/login
     * 
     * Request Body:
     * {
     *   "email": "john@example.com",
     *   "password": "password123"
     * }
     * 
     * Response: 200 OK with JWT token and user data
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        logger.info("POST /api/users/login - Email: {}", loginDTO.getEmail());
        LoginResponseDTO response = userService.loginUser(loginDTO);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get user by ID
     * GET /api/users/{id}
     * 
     * Response: 200 OK with UserDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        logger.info("GET /api/users/{}", id);
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    /**
     * Get all users
     * GET /api/users
     * 
     * Response: 200 OK with List<UserDTO>
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        logger.info("GET /api/users");
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    /**
     * Update user profile
     * PUT /api/users/{id}
     * 
     * Request Body:
     * {
     *   "username": "john_updated",
     *   "email": "john_new@example.com",
     *   "password": "newpassword123"
     * }
     * 
     * Response: 200 OK with updated UserDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody RegisterDTO updateDTO) {
        logger.info("PUT /api/users/{}", id);
        UserDTO user = userService.updateUser(id, updateDTO);
        return ResponseEntity.ok(user);
    }
    
    /**
     * Get user subscription
     * GET /api/users/{id}/subscription
     * 
     * Response: 200 OK with subscription tier
     */
    @GetMapping("/{id}/subscription")
    public ResponseEntity<SubscriptionResponse> getSubscription(@PathVariable Long id) {
        logger.info("GET /api/users/{}/subscription", id);
        UserDTO user = userService.getUserById(id);
        SubscriptionResponse response = new SubscriptionResponse(user.getSubscriptionTier());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Update user subscription tier
     * PUT /api/users/{id}/subscription
     * 
     * Request Body:
     * {
     *   "subscriptionTier": "PREMIUM"
     * }
     * 
     * Response: 200 OK with updated UserDTO
     */
    @PutMapping("/{id}/subscription")
    public ResponseEntity<UserDTO> updateSubscription(
            @PathVariable Long id,
            @Valid @RequestBody SubscriptionUpdateDTO updateDTO) {
        logger.info("PUT /api/users/{}/subscription - New tier: {}", id, updateDTO.getSubscriptionTier());
        UserDTO user = userService.updateSubscription(id, updateDTO);
        return ResponseEntity.ok(user);
    }
    
    /**
     * Delete user
     * DELETE /api/users/{id}
     * 
     * Response: 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logger.info("DELETE /api/users/{}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Singleton pattern demonstration endpoint
     * GET /api/users/demo/singleton-test
     * 
     * Demonstrates that ConfigurationManager is a true singleton
     * by getting the instance multiple times and comparing
     * 
     * Response: 200 OK with singleton verification
     */
    @GetMapping("/demo/singleton-test")
    public ResponseEntity<SingletonTestResponse> singletonTest() {
        logger.info("GET /api/users/demo/singleton-test - Running singleton pattern verification");
        SingletonTestResponse response = userService.verifySingletonPattern();
        return ResponseEntity.ok(response);
    }
    
    /**
     * Inner class for subscription response
     */
    public static class SubscriptionResponse {
        private String subscriptionTier;
        
        public SubscriptionResponse(com.example.userservice.model.User.SubscriptionTier tier) {
            this.subscriptionTier = tier.name();
        }
        
        public String getSubscriptionTier() {
            return subscriptionTier;
        }
        
        public void setSubscriptionTier(String subscriptionTier) {
            this.subscriptionTier = subscriptionTier;
        }
    }
}
