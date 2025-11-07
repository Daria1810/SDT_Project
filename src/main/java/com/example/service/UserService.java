package com.example.service;

import com.example.model.User;
import com.example.singleton.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//creates and loads users
@Service
public class UserService {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    
    private CacheManager cacheManager = CacheManager.getInstance();
    
    public User createUser(String email, String password, User.SubscriptionTier tier) {
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(hashPassword(password));
        user.setSubscriptionTier(tier);
        user.setNew(true);
        
        String sql = "INSERT INTO users (email, password_hash, subscription_tier, is_new) " +
                    "VALUES (:email, :passwordHash, :subscriptionTier, :isNew)";
        
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("email", email)
            .addValue("passwordHash", user.getPasswordHash())
            .addValue("subscriptionTier", tier.name())
            .addValue("isNew", true);
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        
        cacheManager.put("user:" + user.getId(), user);
        
        System.out.println("[UserService] Created user: " + email);
        return user;
    }
    
    public User getUserById(Long id) {
    // Fast path: check the cache first
        Object cached = cacheManager.get("user:" + id);
        if (cached != null) {
            return (User) cached;
        }
        
        String sql = "SELECT * FROM users WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        
        List<User> results = jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setEmail(rs.getString("email"));
            user.setPasswordHash(rs.getString("password_hash"));
            user.setSubscriptionTier(User.SubscriptionTier.valueOf(rs.getString("subscription_tier")));
            user.setNew(rs.getBoolean("is_new"));
            return user;
        });
        
        if (!results.isEmpty()) {
            User user = results.get(0);
            cacheManager.put("user:" + id, user);
            return user;
        }
        return null;
    }
    
    private String hashPassword(String password) {
        // Simplified; a real system would use BCrypt or similar
        return "hashed_" + password;
    }
}
