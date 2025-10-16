package com.pinapp.challenge.infrastructure.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utility class to generate BCrypt password hashes.
 * 
 * Usage:
 * Run this class's main method to generate BCrypt hashes for your passwords.
 * 
 * Example:
 * - Input: "password123"
 * - Output: $2a$10$XvDvZX8.6aF8xB3h7xGQOe2Z7l9sKkJdB6WPZQKfvqYQfYWKJVJYW
 */
public class BCryptPasswordGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Generate hashes for common passwords
        System.out.println("=== BCrypt Password Hash Generator ===\n");
        
        String[] passwords = {
            "password123",
            "user123",
            "admin123",
            "secretpassword"
        };
        
        for (String password : passwords) {
            String hash = encoder.encode(password);
            System.out.println("Password: " + password);
            System.out.println("BCrypt Hash: " + hash);
            System.out.println("Verification: " + encoder.matches(password, hash));
            System.out.println();
        }
        
        // Interactive mode (optional)
        if (args.length > 0) {
            System.out.println("=== Custom Password Hash ===");
            String customPassword = args[0];
            String customHash = encoder.encode(customPassword);
            System.out.println("Your Password: " + customPassword);
            System.out.println("BCrypt Hash: " + customHash);
        } else {
            System.out.println("To hash a custom password, run:");
            System.out.println("mvn exec:java -Dexec.mainClass=\"com.pinapp.challenge.infrastructure.security.BCryptPasswordGenerator\" -Dexec.args=\"your_password\"");
        }
    }
}

