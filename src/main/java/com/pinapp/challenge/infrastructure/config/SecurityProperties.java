package com.pinapp.challenge.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration properties for application security.
 * Binds to properties with prefix "app.security"
 */
@Component
@ConfigurationProperties(prefix = "app.security")
@Data
public class SecurityProperties {
    
    private List<UserCredentials> users = new ArrayList<>();
    
    /**
     * User credentials configuration
     */
    @Data
    public static class UserCredentials {
        private String username;
        private String password; // BCrypt hashed password
        private String roles;    // Comma-separated roles
    }
}

