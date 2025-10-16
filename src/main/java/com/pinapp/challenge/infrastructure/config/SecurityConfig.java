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
import org.springframework.web.cors.CorsConfigurationSource;


/**
 * Security configuration for the application.
 *
 * Features:
 * - BCrypt password encryption
 * - Single user authentication (configured via properties)
 * - HTTP Basic Authentication
 * - Public access to Swagger, H2 console, and health endpoints
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityProperties securityProperties;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        http
            // Enable CORS BEFORE any other security config
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            // Disable CSRF (not needed for stateless API with Basic Auth)
            .csrf(csrf -> csrf.disable())
            // Configure authorization
            .authorizeHttpRequests(authz -> authz
                // Public endpoints - Swagger and H2 console
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/api-docs/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/actuator/health/**").permitAll()  // Health checks should be public
                // All API endpoints require authentication
                .requestMatchers("/api/**").authenticated()
                .anyRequest().authenticated()
            )
            // HTTP Basic Auth
            .httpBasic(httpBasic -> httpBasic.realmName("Client API"))
            // Headers config
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())  // For H2 console
                .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'; frame-ancestors 'self'; form-action 'self'"))
            );

        return http.build();
    }

    /**
     * Configures user details service with user from application properties.
     * Password must be BCrypt encrypted.
     *
     * @return UserDetailsService with configured user
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username(securityProperties.getUsername())
                .password(securityProperties.getPassword()) // Already BCrypt hashed
                .roles(securityProperties.getRole())
                .build();

        return new InMemoryUserDetailsManager(user);
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
