package com.example.videoservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating watch events.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchEventCreateDTO {
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Content ID is required")
    private Long contentId;
    
    @NotNull(message = "Progress is required")
    @Min(value = 0, message = "Progress must be non-negative")
    private Integer progress;
    
    @NotNull(message = "Completed status is required")
    private Boolean completed;
}
