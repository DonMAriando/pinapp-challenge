package com.pinapp.challenge.infrastructure.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for BCryptPasswordGenerator utility
 */
public class BCryptPasswordGeneratorTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void main_WithNoArguments_ShouldGenerateDefaultPasswords() {
        // When
        BCryptPasswordGenerator.main(new String[]{});

        // Then
        String output = outContent.toString();
        
        // Verify output contains the default passwords
        assertThat(output).contains("Password: password123");
        assertThat(output).contains("Password: user123");
        assertThat(output).contains("Password: admin123");
        assertThat(output).contains("Password: secretpassword");
        
        // Verify all outputs contain BCrypt hashes and verification
        assertThat(output).contains("BCrypt Hash:");
        assertThat(output).contains("Verification: true");
        
        // Verify usage message
        assertThat(output).contains("To hash a custom password, run:");
    }

    @Test
    void main_WithCustomPassword_ShouldGenerateHashForThatPassword() {
        // Given
        String customPassword = "myCustomPassword123";

        // When
        BCryptPasswordGenerator.main(new String[]{customPassword});

        // Then
        String output = outContent.toString();
        
        // Verify output contains the custom password section
        assertThat(output).contains("=== Custom Password Hash ===");
        assertThat(output).contains("Your Password: " + customPassword);
        assertThat(output).contains("BCrypt Hash:");
        
        // Extract the hash using regex
        Pattern pattern = Pattern.compile("=== Custom Password Hash ===\\s+Your Password: " + Pattern.quote(customPassword) + "\\s+BCrypt Hash: (\\$2[aby]\\$\\d{2}\\$.{53})");
        Matcher matcher = pattern.matcher(output);
        
        assertThat(matcher.find()).isTrue();
        String hash = matcher.group(1);
        
        // Verify hash format
        assertThat(hash).matches("\\$2[aby]\\$\\d{2}\\$.{53}");
        
        // Verify the hash can validate the password
        assertThat(encoder.matches(customPassword, hash)).isTrue();
        
        // Verify wrong password doesn't match
        assertThat(encoder.matches("wrongPassword", hash)).isFalse();
    }

    @Test
    void main_WithMultipleArguments_ShouldOnlyUseFirstArgument() {
        // Given
        String firstPassword = "first";
        String secondPassword = "second";

        // When
        BCryptPasswordGenerator.main(new String[]{firstPassword, secondPassword});

        // Then
        String output = outContent.toString();
        
        // Should contain first password in custom section
        assertThat(output).contains("Your Password: " + firstPassword);
        // Second password should not appear
        assertThat(output).doesNotContain("Your Password: " + secondPassword);
    }

    @Test
    void main_VerifiesGeneratedHashes() {
        // When
        BCryptPasswordGenerator.main(new String[]{});

        // Then
        String output = outContent.toString();
        
        // All default passwords should show verification: true
        String[] passwords = {"password123", "user123", "admin123", "secretpassword"};
        
        for (String password : passwords) {
            // Find the password section
            assertThat(output).contains("Password: " + password);
            
            // After each password, there should be a hash and verification
            int passwordIndex = output.indexOf("Password: " + password);
            int nextPasswordIndex = output.indexOf("Password:", passwordIndex + 1);
            if (nextPasswordIndex == -1) nextPasswordIndex = output.length();
            
            String section = output.substring(passwordIndex, nextPasswordIndex);
            assertThat(section).contains("BCrypt Hash: $2a$10$");
            assertThat(section).contains("Verification: true");
        }
    }

    @Test
    void main_GeneratesBCryptFormatHashes() {
        // Given
        String password = "testPassword";

        // When
        BCryptPasswordGenerator.main(new String[]{password});

        // Then
        String output = outContent.toString();
        
        // Extract hash from custom password section
        Pattern pattern = Pattern.compile("BCrypt Hash: (\\$2[aby]\\$\\d{2}\\$.{53})");
        Matcher matcher = pattern.matcher(output);
        
        // Should have at least one hash in custom section
        boolean foundCustomHash = false;
        while (matcher.find()) {
            String hash = matcher.group(1);
            // BCrypt strength 10 should start with $2a$10$
            if (output.indexOf(hash) > output.indexOf("=== Custom Password Hash ===")) {
                assertThat(hash).startsWith("$2a$10$");
                foundCustomHash = true;
                break;
            }
        }
        
        assertThat(foundCustomHash).isTrue();
    }
}

