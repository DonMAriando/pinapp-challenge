package com.pinapp.challenge.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

/**
 * Security configuration for the application.
 * 
 * Features:
 * - BCrypt password encryption
 * - Multiple users with different roles (configured via properties)
 * - HTTP Basic Authentication
 * - Public access to Swagger, H2 console, and health endpoints
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityProperties securityProperties;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                // Public endpoints - Swagger and H2 console
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/api-docs/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/actuator/health/**").permitAll()  // Health checks should be public
                // All API endpoints require authentication
                .requestMatchers("/api/**").authenticated()
                .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> httpBasic.realmName("Client API"))
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));  // For H2 console

        return http.build();
    }

    /**
     * Configures user details service with users from application properties.
     * Passwords must be BCrypt encrypted.
     * 
     * @return UserDetailsService with configured users
     */
    @Bean
    public UserDetailsService userDetailsService() {
        List<UserDetails> users = new ArrayList<>();
        
        // Load users from properties
        for (SecurityProperties.UserCredentials userConfig : securityProperties.getUsers()) {
            UserDetails user = User.builder()
                    .username(userConfig.getUsername())
                    .password(userConfig.getPassword()) // Already BCrypt hashed
                    .roles(userConfig.getRoles().split(","))
                    .build();
            users.add(user);
        }
        
        // Fallback user if no users are configured
        if (users.isEmpty()) {
            UserDetails defaultUser = User.builder()
                    .username("admin")
                    .password(passwordEncoder().encode("password"))
                    .roles("ADMIN", "USER")
                    .build();
            users.add(defaultUser);
        }

        return new InMemoryUserDetailsManager(users);
    }

    /**
     * BCrypt password encoder bean.
     * Strength: 10 (default)
     * 
     * @return BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
