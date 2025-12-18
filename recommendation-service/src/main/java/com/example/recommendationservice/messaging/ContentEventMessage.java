package com.example.recommendationservice.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Message DTO for content events received from RabbitMQ.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentEventMessage implements Serializable {
    
    private Long contentId;
    private String eventType;
    private String title;
    private String type;
    private String genre;
    private Integer releaseYear;
}

