package com.example.videoservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for rating responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingResponseDTO {
    
    private Long id;
    private Long userId;
    private Long contentId;
    private Double score;
    private LocalDateTime timestamp;
}
