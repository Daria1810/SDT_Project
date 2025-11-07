package com.example.singleton;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


//loads application configuration once and makes it available everywhere.
//reads application.properties on the classpath and overlays environment variables.
 
public class ConfigurationManager {
    private static volatile ConfigurationManager instance;
    private Properties properties;
    
    private ConfigurationManager() {
        loadConfiguration();
        System.out.println("[ConfigurationManager] Loaded configuration");
    }
    
    public static ConfigurationManager getInstance() {
        if (instance == null) {
            synchronized (ConfigurationManager.class) {
                if (instance == null) {
                    instance = new ConfigurationManager();
                }
            }
        }
        return instance;
    }
    
    private void loadConfiguration() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            System.err.println("Failed to load configuration: " + e.getMessage());
        }
        
        // Also load environment variables
        properties.putAll(System.getenv());
    }
    
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    public int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? Integer.parseInt(value) : defaultValue;
    }
}
