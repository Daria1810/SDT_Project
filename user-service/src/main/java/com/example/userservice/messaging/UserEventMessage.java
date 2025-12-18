package com.example.userservice.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Message DTO for user events published to RabbitMQ.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEventMessage implements Serializable {
    
    private Long userId;
    private String eventType; // "USER_CREATED", "SUBSCRIPTION_UPDATED"
    private String username;
    private String email;
    private String tier;
    
    public static UserEventMessage forUserCreated(Long userId, String username, String email, String tier) {
        return new UserEventMessage(userId, "USER_CREATED", username, email, tier);
    }
    
    public static UserEventMessage forSubscriptionUpdated(Long userId, String tier) {
        return new UserEventMessage(userId, "SUBSCRIPTION_UPDATED", null, null, tier);
    }
}

