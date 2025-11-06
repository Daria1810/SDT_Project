package com.example.singleton;

import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Shared database connection holder.
 * Provides a single, lazily initialized DataSource the whole app can reuse.
 * Thread-safe with double-checked locking.
 */
public class DatabaseConnectionPool {
    // Volatile ensures visibility across threads
    private static volatile DatabaseConnectionPool instance;
    private DataSource dataSource;
    private final int maxConnections = 20;
    // Optional external configuration
    private static volatile String configuredDriver;
    private static volatile String configuredUrl;
    private static volatile String configuredUsername;
    private static volatile String configuredPassword;
    
    // Private constructor prevents external instantiation
    private DatabaseConnectionPool() {
        initializeDataSource();
        System.out.println("[DatabaseConnectionPool] Initialized with max " + maxConnections + " connections");
    }
    
    /**
     * Double-checked locking for thread-safe lazy initialization
     */
    public static DatabaseConnectionPool getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnectionPool.class) {
                if (instance == null) {
                    instance = new DatabaseConnectionPool();
                }
            }
        }
        return instance;
    }
    
    /**
     * Configure the underlying DataSource settings before first use.
     * Safe to call multiple times; only takes effect on first initialization.
     */
    public static void configure(String driver, String url, String username, String password) {
        configuredDriver = driver;
        configuredUrl = url;
        configuredUsername = username;
        configuredPassword = password;
    }
    
    private void initializeDataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        String driver = configuredDriver != null ? configuredDriver : "org.h2.Driver";
        String url = configuredUrl != null ? configuredUrl : "jdbc:h2:mem:streamflix;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL";
        String username = configuredUsername != null ? configuredUsername : "sa";
        String password = configuredPassword != null ? configuredPassword : "";
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        this.dataSource = ds;
    }
    
    public DataSource getDataSource() {
        return dataSource;
    }
    
    public int getMaxConnections() {
        return maxConnections;
    }
}
