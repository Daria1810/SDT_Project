package com.example.userservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * SINGLETON PATTERN IMPLEMENTATION
 * 
 * Purpose: Manages service-wide configuration with single instance
 * 
 * Why Singleton is necessary:
 * - WITHOUT: Multiple instances would waste memory and cause inconsistent configuration
 * - WITH: Single instance ensures all components see same configuration
 * 
 * Thread-Safety: Uses double-checked locking with volatile
 */
public class ConfigurationManager {
    
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationManager.class);
    
    // Volatile ensures visibility across threads
    private static volatile ConfigurationManager instance;
    
    // Configuration storage
    private final Map<String, String> configMap;
    
    // Configuration keys
    private String jwtSecret;
    private Long jwtExpirationMs;
    private String passwordSalt;
    private Integer rateLimit;
    
    /**
     * Private constructor prevents external instantiation
     * This is KEY to Singleton pattern
     */
    private ConfigurationManager() {
        this.configMap = new HashMap<>();
        loadDefaultConfiguration();
        logger.info("[ConfigurationManager] Singleton instance initialized");
    }
    
    /**
     * Double-checked locking for thread-safe lazy initialization
     * 
     * First check: Avoid synchronized block if already initialized (performance)
     * Synchronized block: Only one thread can create instance
     * Second check: Ensure no other thread created instance while waiting
     */
    public static ConfigurationManager getInstance() {
        if (instance == null) {
            synchronized (ConfigurationManager.class) {
                if (instance == null) {
                    instance = new ConfigurationManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * Load default configuration values
     */
    private void loadDefaultConfiguration() {
        this.jwtSecret = System.getenv().getOrDefault("JWT_SECRET", 
            "streamflix-secret-key-change-in-production");
        this.jwtExpirationMs = Long.parseLong(
            System.getenv().getOrDefault("JWT_EXPIRATION_MS", "86400000")); // 24 hours
        this.passwordSalt = System.getenv().getOrDefault("PASSWORD_SALT", 
            "streamflix-salt");
        this.rateLimit = Integer.parseInt(
            System.getenv().getOrDefault("RATE_LIMIT_PER_MINUTE", "100"));
        
        configMap.put("service.name", "user-service");
        configMap.put("service.version", "1.0.0");
        configMap.put("service.port", "8081");
    }
    
    // Getters for configuration
    public String getJwtSecret() { return jwtSecret; }
    public Long getJwtExpirationMs() { return jwtExpirationMs; }
    public String getPasswordSalt() { return passwordSalt; }
    public Integer getRateLimit() { return rateLimit; }
    
    public String getConfig(String key) {
        return configMap.get(key);
    }
    
    public void setConfig(String key, String value) {
        configMap.put(key, value);
    }
    
    /**
     * Get configuration summary for logging
     */
    public String getConfigSummary() {
        return String.format(
            "ConfigurationManager[service=%s, version=%s, rateLimit=%d, jwtExpiration=%dms]",
            configMap.get("service.name"),
            configMap.get("service.version"),
            rateLimit,
            jwtExpirationMs
        );
    }
}
