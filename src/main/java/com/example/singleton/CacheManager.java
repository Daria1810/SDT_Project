package com.example.singleton;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

//imple in-memory cache shared across the app.
 
public class CacheManager {
    private static volatile CacheManager instance;
    private Map<String, Object> cache;
    private Map<String, Long> expiryTimes;
    private final long defaultTTL = 300000; // 5 minutes in milliseconds
    
    private CacheManager() {
        this.cache = new ConcurrentHashMap<>();
        this.expiryTimes = new ConcurrentHashMap<>();
        System.out.println("[CacheManager] Initialized cache");
    }
    
    public static CacheManager getInstance() {
        if (instance == null) {
            synchronized (CacheManager.class) {
                if (instance == null) {
                    instance = new CacheManager();
                }
            }
        }
        return instance;
    }
    
    public void put(String key, Object value) {
        put(key, value, defaultTTL);
    }
    
    public void put(String key, Object value, long ttlMillis) {
        cache.put(key, value);
        expiryTimes.put(key, System.currentTimeMillis() + ttlMillis);
        System.out.println("[CacheManager] Cached: " + key);
    }
    
    public Object get(String key) {
        // Check if expired
        Long expiryTime = expiryTimes.get(key);
        if (expiryTime != null && System.currentTimeMillis() > expiryTime) {
            cache.remove(key);
            expiryTimes.remove(key);
            System.out.println("[CacheManager] Cache expired: " + key);
            return null;
        }
        
        Object value = cache.get(key);
        if (value != null) {
            System.out.println("[CacheManager] Cache hit: " + key);
        }
        return value;
    }
    
    public void invalidate(String key) {
        cache.remove(key);
        expiryTimes.remove(key);
        System.out.println("[CacheManager] Invalidated cache: " + key);
    }
    
    public void clear() {
        cache.clear();
        expiryTimes.clear();
        System.out.println("[CacheManager] Cleared all cache");
    }
    
    public int size() {
        return cache.size();
    }
}
