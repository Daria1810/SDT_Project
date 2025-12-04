package com.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for login responses
 * Contains JWT token and user information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {
    
    private String token;
    
    @Builder.Default
    private String tokenType = "Bearer";
    
    private UserDTO user;
    
    public LoginResponseDTO(String token, UserDTO user) {
        this.token = token;
        this.user = user;
    }
}
