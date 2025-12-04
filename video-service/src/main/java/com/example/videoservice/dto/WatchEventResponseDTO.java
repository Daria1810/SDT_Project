package com.example.videoservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for watch event responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchEventResponseDTO {
    
    private Long id;
    private Long userId;
    private Long contentId;
    private Integer progress;
    private Boolean completed;
    private LocalDateTime timestamp;
}
