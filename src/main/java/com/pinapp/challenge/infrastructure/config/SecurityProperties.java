package com.pinapp.challenge.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for application security.
 * Binds to properties with prefix "app.security"
 */
@Component
@ConfigurationProperties(prefix = "app.security")
@Data
public class SecurityProperties {
    private String username;
    private String password; // BCrypt hashed password
    private String role;     // User role (e.g., "USER")
}

