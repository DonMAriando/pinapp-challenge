package com.pinapp.challenge.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void apiEndpoint_WithoutAuth_ShouldReturnUnauthorized() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/clients")
                        .contentType("application/json")
                        .content("{\"firstName\":\"Test\",\"lastName\":\"User\",\"age\":30,\"birthDate\":\"1994-01-15\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void apiEndpoint_WithValidAuth_ShouldSucceed() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/clients")
                        .with(httpBasic("admin", "password"))
                        .contentType("application/json")
                        .content("{\"firstName\":\"Test\",\"lastName\":\"User\",\"age\":30,\"birthDate\":\"1994-01-15\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void apiEndpoint_GetWithoutAuth_ShouldReturnUnauthorized() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void apiEndpoint_GetWithValidAuth_ShouldSucceed() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/clients")
                        .with(httpBasic("admin", "password")))
                .andExpect(status().isOk());
    }

    @Test
    void swaggerEndpoints_ShouldBeAccessibleWithoutAuth() throws Exception {
        // When & Then - Swagger UI redirects, so we expect a 3xx status or 200
        mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void healthEndpoint_ShouldBeAccessibleWithoutAuth() throws Exception {
        // When & Then
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }
}
