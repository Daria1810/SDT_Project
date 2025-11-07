package com.example;

import org.h2.server.web.WebServlet;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//registers the H2 web console at /h2-console/** for Spring Boot 1.2.x.
 
@Configuration
public class H2ConsoleConfig {

    @Bean
    public ServletRegistrationBean h2ConsoleServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new WebServlet(), "/h2-console/*");
        bean.setName("H2Console");
        bean.setLoadOnStartup(1);
        return bean;
    }
}
