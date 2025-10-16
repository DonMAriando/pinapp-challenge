package com.pinapp.challenge.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for SecurityProperties configuration and BCrypt password encoding
 */
@SpringBootTest
@ActiveProfiles("dev")
public class SecurityPropertiesTest {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void securityProperties_ShouldLoadUserFromConfiguration() {
        // When - Properties are loaded from application-dev.properties
        String username = securityProperties.getUsername();
        String password = securityProperties.getPassword();
        String role = securityProperties.getRole();

        // Then - Should have user configured
        assertThat(username).isNotNull().isEqualTo("admin");
        assertThat(password).isNotNull().startsWith("$2a$10$");
        assertThat(role).isNotNull().isEqualTo("USER");
    }

    @Test
    void securityProperties_ShouldHaveBCryptHashedPassword() {
        // When
        String password = securityProperties.getPassword();

        // Then - Password should be BCrypt hashed
        assertThat(password).startsWith("$2a$10$");
        assertThat(password).matches("\\$2[aby]\\$\\d{2}\\$.{53}");
    }

    @Test
    void bcryptEncoder_ShouldValidateConfiguredPassword() {
        // Given
        String correctPassword = "password123";
        String hashedPassword = securityProperties.getPassword();

        // When
        boolean matches = passwordEncoder.matches(correctPassword, hashedPassword);

        // Then
        assertThat(matches).isTrue();
    }

    @Test
    void bcryptEncoder_ShouldRejectWrongPassword() {
        // Given
        String wrongPassword = "wrongpassword";
        String hashedPassword = securityProperties.getPassword();

        // When
        boolean matches = passwordEncoder.matches(wrongPassword, hashedPassword);

        // Then
        assertThat(matches).isFalse();
    }

    @Test
    void bcryptEncoder_ShouldRejectOldPassword() {
        // Given - Old password that was used before
        String oldPassword = "password";
        String hashedPassword = securityProperties.getPassword();

        // When
        boolean matches = passwordEncoder.matches(oldPassword, hashedPassword);

        // Then - Should not match the new BCrypt hash
        assertThat(matches).isFalse();
    }

    @Test
    void bcryptEncoder_ShouldGenerateDifferentHashesForSamePassword() {
        // Given
        String password = "testpassword";

        // When - Encode the same password twice
        String hash1 = passwordEncoder.encode(password);
        String hash2 = passwordEncoder.encode(password);

        // Then - Hashes should be different (due to salt)
        assertThat(hash1).isNotEqualTo(hash2);
        
        // But both should match the original password
        assertThat(passwordEncoder.matches(password, hash1)).isTrue();
        assertThat(passwordEncoder.matches(password, hash2)).isTrue();
    }

    @Test
    void bcryptEncoder_ShouldUseBCryptFormat() {
        // Given
        String password = "testpassword";

        // When
        String hash = passwordEncoder.encode(password);

        // Then - Should follow BCrypt format: $2a$rounds$salt+hash
        assertThat(hash).matches("\\$2[aby]\\$\\d{2}\\$.{53}");
        assertThat(hash).startsWith("$2a$10$"); // Default strength 10
    }
}

