package com.example.contentservice.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Message DTO for content events published to RabbitMQ.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentEventMessage implements Serializable {
    
    private Long contentId;
    private String eventType; // "CONTENT_CREATED"
    private String title;
    private String type; // "MOVIE" or "TV_SERIES"
    private String genre;
    private Integer releaseYear;
    
    public static ContentEventMessage forContentCreated(Long contentId, String title, String type, String genre, Integer releaseYear) {
        return new ContentEventMessage(contentId, "CONTENT_CREATED", title, type, genre, releaseYear);
    }
}

