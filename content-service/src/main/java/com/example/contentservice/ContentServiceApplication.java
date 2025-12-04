package com.example.contentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Content Service - Main Application
 * 
 * Microservice for managing streaming content (Movies and TV Series)
 * 
 * Design Pattern: Factory Method Pattern
 * - ContentFactory (interface)
 * - MovieFactory and TVSeriesFactory (implementations)
 * - Used for creating different types of content with proper validation
 * 
 * Port: 8082
 */
@SpringBootApplication
public class ContentServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ContentServiceApplication.class, args);
        
        System.out.println("==============================================");
        System.out.println("   Content Service Started Successfully!");
        System.out.println("   Port: 8082");
        System.out.println("   Pattern: Factory Method (MovieFactory, TVSeriesFactory)");
        System.out.println("==============================================");
    }
}
