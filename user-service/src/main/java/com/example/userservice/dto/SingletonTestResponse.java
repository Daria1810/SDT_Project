package com.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for Singleton pattern demonstration
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingletonTestResponse {
    private boolean isSingleton;
    private String message;
    private String instance1HashCode;
    private String instance2HashCode;
    private String instance3HashCode;
    private boolean allInstancesEqual;
    private String configurationDetails;
}
