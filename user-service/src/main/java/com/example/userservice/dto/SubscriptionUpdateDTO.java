package com.example.userservice.dto;

import com.example.userservice.model.User.SubscriptionTier;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating user subscription tier
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionUpdateDTO {
    
    @NotNull(message = "Subscription tier is required")
    private SubscriptionTier subscriptionTier;
}
