package com.example.recommendationservice.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Message DTO for user events received from RabbitMQ.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEventMessage implements Serializable {
    
    private Long userId;
    private String eventType;
    private String username;
    private String email;
    private String tier;
}

