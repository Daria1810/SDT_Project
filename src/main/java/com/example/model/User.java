package com.example.model;
import java.time.LocalDateTime;

//user of the streaming service

public class User {
    private Long id;
    private String email;
    private String passwordHash;
    private SubscriptionTier subscriptionTier;
    private LocalDateTime createdAt;
    private boolean isNew; // For strategy pattern selection
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public SubscriptionTier getSubscriptionTier() { return subscriptionTier; }
    public void setSubscriptionTier(SubscriptionTier subscriptionTier) { this.subscriptionTier = subscriptionTier; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public boolean isNew() { return isNew; }
    public void setNew(boolean aNew) { isNew = aNew; }
    
    public enum SubscriptionTier {
        BASIC,    // SD quality, 1 device
        STANDARD, // HD quality, 2 devices
        PREMIUM   // 4K quality, 4 devices
    }
}
