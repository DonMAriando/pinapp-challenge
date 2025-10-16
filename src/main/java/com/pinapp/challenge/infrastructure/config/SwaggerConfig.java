package com.pinapp.challenge.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger/OpenAPI Configuration
 * Configures Swagger UI to use HTTPS for Cloud Run deployment
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // Use relative URL "/" to auto-detect the correct scheme (HTTP/HTTPS)
        // This allows Swagger to work in both local (HTTP) and Cloud Run (HTTPS) environments
        Server defaultServer = new Server()
                .url("/")
                .description("Current Server (auto-detects HTTP/HTTPS)");
        
        return new OpenAPI()
                .info(new Info()
                        .title("Challenge PinApp")
                        .version("1.0.0")
                        .description("Client Management API - Deployed on Google Cloud Run"))
                .servers(List.of(defaultServer))  // Auto-detect scheme!
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"))
                .components(new Components()
                        .addSecuritySchemes("basicAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")
                                .description("HTTP Basic Authentication. Default: 'admin'/'password123'")));
    }
}
