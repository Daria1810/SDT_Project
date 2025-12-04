package com.example.userservice.service;

import com.example.userservice.config.ConfigurationManager;
import com.example.userservice.dto.*;
import com.example.userservice.exception.*;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User Service - Business logic for user management
 * 
 * DEMONSTRATES SINGLETON PATTERN USAGE:
 * - Uses ConfigurationManager.getInstance() to get configuration
 * - All requests use the SAME ConfigurationManager instance
 * - Ensures consistent JWT secrets, rate limits, etc.
 */
@Service
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    private final BCryptPasswordEncoder passwordEncoder;
    
    // SINGLETON PATTERN: Get single instance of ConfigurationManager
    private final ConfigurationManager config = ConfigurationManager.getInstance();
    
    public UserService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
        logger.info("UserService initialized with ConfigurationManager: {}", config.getConfigSummary());
    }
    
    /**
     * Register a new user
     * 
     * @param registerDTO registration data
     * @return created user DTO
     * @throws DuplicateEmailException if email already exists
     * @throws DuplicateUsernameException if username already exists
     */
    @Transactional
    public UserDTO registerUser(RegisterDTO registerDTO) {
        logger.info("Attempting to register user with email: {}", registerDTO.getEmail());
        
        // Check for duplicate email
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            logger.warn("Registration failed: Email already exists: {}", registerDTO.getEmail());
            throw new DuplicateEmailException(registerDTO.getEmail());
        }
        
        // Check for duplicate username
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            logger.warn("Registration failed: Username already exists: {}", registerDTO.getUsername());
            throw new DuplicateUsernameException(registerDTO.getUsername());
        }
        
        // Create new user with encrypted password
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        
        // SINGLETON USAGE: Use ConfigurationManager's password salt for additional security
        String hashedPassword = passwordEncoder.encode(
            registerDTO.getPassword() + config.getPasswordSalt()
        );
        user.setPassword(hashedPassword);
        
        // Set subscription tier from DTO, default to BASIC if not provided
        if (registerDTO.getTier() != null && !registerDTO.getTier().isEmpty()) {
            try {
                user.setSubscriptionTier(User.SubscriptionTier.valueOf(registerDTO.getTier()));
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid tier provided: {}, defaulting to BASIC", registerDTO.getTier());
                user.setSubscriptionTier(User.SubscriptionTier.BASIC);
            }
        } else {
            user.setSubscriptionTier(User.SubscriptionTier.BASIC); // Default tier
        }
        
        User savedUser = userRepository.save(user);
        logger.info("User registered successfully with ID: {}", savedUser.getId());
        
        return convertToDTO(savedUser);
    }
    
    /**
     * Authenticate user and generate JWT token
     * 
     * @param loginDTO login credentials
     * @return login response with JWT token
     * @throws InvalidCredentialsException if credentials are invalid
     */
    public LoginResponseDTO loginUser(LoginDTO loginDTO) {
        logger.info("Login attempt for email: {}", loginDTO.getEmail());
        
        // Find user by email
        User user = userRepository.findByEmail(loginDTO.getEmail())
            .orElseThrow(() -> {
                logger.warn("Login failed: User not found with email: {}", loginDTO.getEmail());
                return new InvalidCredentialsException();
            });
        
        // SINGLETON USAGE: Use ConfigurationManager's password salt
        String passwordWithSalt = loginDTO.getPassword() + config.getPasswordSalt();
        
        // Verify password
        if (!passwordEncoder.matches(passwordWithSalt, user.getPassword())) {
            logger.warn("Login failed: Invalid password for email: {}", loginDTO.getEmail());
            throw new InvalidCredentialsException();
        }
        
        // SINGLETON USAGE: Generate JWT token using ConfigurationManager settings
        String token = generateJwtToken(user);
        
        logger.info("User logged in successfully: {}", user.getId());
        
        return LoginResponseDTO.builder()
            .token(token)
            .user(convertToDTO(user))
            .build();
    }
    
    /**
     * Get user by ID
     * 
     * @param userId the user ID
     * @return user DTO
     * @throws UserNotFoundException if user not found
     */
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
        return convertToDTO(user);
    }
    
    /**
     * Get all users (for admin purposes)
     * 
     * @return list of all users
     */
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Update user subscription tier
     * 
     * @param userId the user ID
     * @param updateDTO subscription update data
     * @return updated user DTO
     * @throws UserNotFoundException if user not found
     */
    @Transactional
    public UserDTO updateSubscription(Long userId, SubscriptionUpdateDTO updateDTO) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
        
        logger.info("Updating subscription for user {} from {} to {}", 
            userId, user.getSubscriptionTier(), updateDTO.getSubscriptionTier());
        
        user.setSubscriptionTier(updateDTO.getSubscriptionTier());
        User updatedUser = userRepository.save(user);
        
        return convertToDTO(updatedUser);
    }
    
    /**
     * Update user profile information
     * 
     * @param userId the user ID
     * @param updateDTO user update data
     * @return updated user DTO
     * @throws UserNotFoundException if user not found
     */
    @Transactional
    public UserDTO updateUser(Long userId, RegisterDTO updateDTO) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
        
        // Check if new email is already taken by another user
        if (!user.getEmail().equals(updateDTO.getEmail()) && 
            userRepository.existsByEmail(updateDTO.getEmail())) {
            throw new DuplicateEmailException(updateDTO.getEmail());
        }
        
        // Check if new username is already taken by another user
        if (!user.getUsername().equals(updateDTO.getUsername()) && 
            userRepository.existsByUsername(updateDTO.getUsername())) {
            throw new DuplicateUsernameException(updateDTO.getUsername());
        }
        
        user.setUsername(updateDTO.getUsername());
        user.setEmail(updateDTO.getEmail());
        
        // Update password if provided
        if (updateDTO.getPassword() != null && !updateDTO.getPassword().isBlank()) {
            String hashedPassword = passwordEncoder.encode(
                updateDTO.getPassword() + config.getPasswordSalt()
            );
            user.setPassword(hashedPassword);
        }
        
        User updatedUser = userRepository.save(user);
        logger.info("User updated successfully: {}", userId);
        
        return convertToDTO(updatedUser);
    }
    
    /**
     * Delete user
     * 
     * @param userId the user ID
     * @throws UserNotFoundException if user not found
     */
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        
        userRepository.deleteById(userId);
        logger.info("User deleted successfully: {}", userId);
    }
    
    /**
     * Generate JWT token for authenticated user
     * SINGLETON USAGE: Uses ConfigurationManager for JWT secret and expiration
     * 
     * @param user the authenticated user
     * @return JWT token string
     */
    private String generateJwtToken(User user) {
        // In a real implementation, use a JWT library like jjwt
        // This is a simplified version for demonstration
        
        // SINGLETON PATTERN DEMONSTRATION:
        // All instances use the SAME secret from ConfigurationManager
        String secret = config.getJwtSecret();
        Long expiration = config.getJwtExpirationMs();
        
        // Simplified JWT generation (in production, use proper JWT library)
        String payload = String.format("%d:%s:%s:%d", 
            user.getId(), 
            user.getEmail(), 
            user.getSubscriptionTier(),
            System.currentTimeMillis() + expiration
        );
        
        // This would normally be properly signed with HMAC-SHA256
        return "streamflix." + java.util.Base64.getEncoder().encodeToString(payload.getBytes());
    }
    
    /**
     * Verify Singleton Pattern Implementation
     * 
     * Demonstrates that ConfigurationManager is a true singleton by:
     * 1. Getting the instance multiple times
     * 2. Comparing hash codes
     * 3. Verifying they all point to the same object in memory
     * 
     * @return SingletonTestResponse with verification results
     */
    public SingletonTestResponse verifySingletonPattern() {
        logger.info("=== SINGLETON PATTERN VERIFICATION ===");
        
        // Get ConfigurationManager instance multiple times
        ConfigurationManager instance1 = ConfigurationManager.getInstance();
        ConfigurationManager instance2 = ConfigurationManager.getInstance();
        ConfigurationManager instance3 = ConfigurationManager.getInstance();
        
        // Get hash codes (memory addresses)
        String hash1 = Integer.toHexString(System.identityHashCode(instance1));
        String hash2 = Integer.toHexString(System.identityHashCode(instance2));
        String hash3 = Integer.toHexString(System.identityHashCode(instance3));
        
        // Verify all instances are the same
        boolean allEqual = (instance1 == instance2) && (instance2 == instance3);
        
        String message = allEqual 
            ? "SUCCESS: All instances reference the SAME ConfigurationManager singleton object"
            : "FAILURE: Instances are different - Singleton pattern violated!";
        
        String configDetails = String.format(
            "JWT Secret: %s, JWT Expiration: %d ms, Password Salt: %s, Rate Limit: %d per minute",
            instance1.getJwtSecret(),
            instance1.getJwtExpirationMs(),
            instance1.getPasswordSalt(),
            instance1.getRateLimit()
        );
        
        logger.info("Singleton Test Result: {}", allEqual ? "PASS" : "FAIL");
        logger.info("Instance 1 HashCode: {}", hash1);
        logger.info("Instance 2 HashCode: {}", hash2);
        logger.info("Instance 3 HashCode: {}", hash3);
        
        return new SingletonTestResponse(
            allEqual,
            message,
            hash1,
            hash2,
            hash3,
            allEqual,
            configDetails
        );
    }
    
    /**
     * Convert User entity to UserDTO
     * 
     * @param user the user entity
     * @return user DTO
     */
    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .subscriptionTier(user.getSubscriptionTier())
            .createdAt(user.getCreatedAt())
            .build();
    }
}
