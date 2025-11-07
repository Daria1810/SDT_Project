package com.example;

import com.example.singleton.DatabaseConnectionPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class AppConfig {
    
    
    //DataSource bean backed by the shared connection pool.
     
    @Bean
    public DataSource dataSource(
            @Value("${spring.datasource.driverClassName:org.h2.Driver}") String driver,
            @Value("${spring.datasource.url:jdbc:h2:mem:streamflix;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL}") String url,
            @Value("${spring.datasource.username:sa}") String username,
            @Value("${spring.datasource.password:}") String password) {
    //configure the shared pool from application properties before first use
        DatabaseConnectionPool.configure(driver, url, username, password);
        DatabaseConnectionPool connectionPool = DatabaseConnectionPool.getInstance();
        return connectionPool.getDataSource();
    }
    
   
    //NamedParameterJdbcTemplate bean using the shared DataSource
    
    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}