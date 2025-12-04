package com.example.userservice.dto;

import com.example.userservice.model.User.SubscriptionTier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for user responses (outgoing data)
 * Excludes sensitive information like password
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    
    private Long id;
    private String username;
    private String email;
    private SubscriptionTier subscriptionTier;
    private LocalDateTime createdAt;
}
