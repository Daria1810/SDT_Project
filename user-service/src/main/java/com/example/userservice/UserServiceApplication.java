package com.example.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * StreamFlix User Service - Microservice for user management
 * Demonstrates: Singleton Pattern (ConfigurationManager)
 * Port: 8081
 * Database: PostgreSQL (userdb)
 */
@SpringBootApplication
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
        System.out.println("==============================================");
        System.out.println("   User Service Started Successfully!");
        System.out.println("   Port: 8081");
        System.out.println("   Pattern: Singleton (ConfigurationManager)");
        System.out.println("==============================================");
    }
}
