package com.pinapp.challenge.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pinapp.challenge.infrastructure.adapter.in.rest.dto.ClientMetricsResponse;
import com.pinapp.challenge.infrastructure.adapter.in.rest.dto.ClientResponse;
import com.pinapp.challenge.infrastructure.adapter.in.rest.dto.CreateClientRequest;
import com.pinapp.challenge.infrastructure.adapter.out.persistence.ClientJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * End-to-End tests for the Client Management API
 * Tests the complete flow from HTTP request through all layers to the database
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class ClientE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientJpaRepository clientRepository;

    @BeforeEach
    void setUp() {
        // Clean database before each test
        clientRepository.deleteAll();
    }

    @Test
    void e2e_CreateAndRetrieveClient_ShouldWorkEndToEnd() throws Exception {
        // Given - Create a client request
        CreateClientRequest request = CreateClientRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .age(30)
                .birthDate(LocalDate.of(1994, 1, 15))
                .build();

        // When - Create the client via POST endpoint
        MvcResult createResult = mockMvc.perform(post("/api/clients")
                        .with(httpBasic("admin", "password123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.birthDate").value("1994-01-15"))
                .andExpect(jsonPath("$.estimatedDeathDate").exists())
                .andReturn();

        // Extract the created client
        String responseJson = createResult.getResponse().getContentAsString();
        ClientResponse createdClient = objectMapper.readValue(responseJson, ClientResponse.class);

        // Then - Verify client is in database
        assertThat(clientRepository.findAll()).hasSize(1);

        // And - Retrieve all clients via GET endpoint
        MvcResult getAllResult = mockMvc.perform(get("/api/clients")
                        .with(httpBasic("admin", "password123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(createdClient.getId()))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andReturn();

        // Verify the retrieved list
        String getAllJson = getAllResult.getResponse().getContentAsString();
        List<ClientResponse> clients = objectMapper.readValue(
                getAllJson,
                objectMapper.getTypeFactory().constructCollectionType(List.class, ClientResponse.class)
        );
        assertThat(clients).hasSize(1);
        assertThat(clients.get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    void e2e_CreateMultipleClientsAndGetMetrics_ShouldCalculateCorrectly() throws Exception {
        // Given - Create multiple clients with different ages
        CreateClientRequest client1 = CreateClientRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .age(30)
                .birthDate(LocalDate.of(1994, 1, 15))
                .build();

        CreateClientRequest client2 = CreateClientRequest.builder()
                .firstName("Jane")
                .lastName("Smith")
                .age(25)
                .birthDate(LocalDate.of(1999, 5, 20))
                .build();

        CreateClientRequest client3 = CreateClientRequest.builder()
                .firstName("Bob")
                .lastName("Johnson")
                .age(35)
                .birthDate(LocalDate.of(1989, 3, 10))
                .build();

        // When - Create all clients
        mockMvc.perform(post("/api/clients")
                        .with(httpBasic("admin", "password123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/clients")
                        .with(httpBasic("admin", "password123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client2)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/clients")
                        .with(httpBasic("admin", "password123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client3)))
                .andExpect(status().isCreated());

        // Then - Verify all clients are in database
        assertThat(clientRepository.findAll()).hasSize(3);

        // And - Get metrics
        MvcResult metricsResult = mockMvc.perform(get("/api/clients/metrics")
                        .with(httpBasic("admin", "password123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageAge").value(30.0))
                .andExpect(jsonPath("$.standardDeviation").exists())
                .andExpect(jsonPath("$.totalClients").value(3))
                .andReturn();

        // Verify metrics calculation
        String metricsJson = metricsResult.getResponse().getContentAsString();
        ClientMetricsResponse metrics = objectMapper.readValue(metricsJson, ClientMetricsResponse.class);
        
        assertThat(metrics.getAverageAge()).isEqualTo(30.0);
        assertThat(metrics.getTotalClients()).isEqualTo(3L);
        assertThat(metrics.getStandardDeviation()).isGreaterThan(0.0);
    }

    @Test
    void e2e_WithoutAuthentication_ShouldReturnUnauthorized() throws Exception {
        // Given - No authentication

        // When/Then - All endpoints should return 401
        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Test\",\"lastName\":\"User\",\"age\":30,\"birthDate\":\"1994-01-15\"}"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/clients/metrics"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void e2e_InvalidClientData_ShouldReturnBadRequest() throws Exception {
        // Given - Invalid client (negative age)
        CreateClientRequest invalidRequest = CreateClientRequest.builder()
                .firstName("Invalid")
                .lastName("Client")
                .age(-5)
                .birthDate(LocalDate.of(1994, 1, 15))
                .build();

        // When/Then - Should return 400 Bad Request
        mockMvc.perform(post("/api/clients")
                        .with(httpBasic("admin", "password123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        // Verify no client was created
        assertThat(clientRepository.findAll()).isEmpty();
    }

    @Test
    void e2e_EmptyDatabase_ShouldReturnEmptyListAndZeroMetrics() throws Exception {
        // Given - Empty database (cleaned in setUp)

        // When/Then - Get all clients should return empty array
        mockMvc.perform(get("/api/clients")
                        .with(httpBasic("admin", "password123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        // And - Get metrics should return zeros
        mockMvc.perform(get("/api/clients/metrics")
                        .with(httpBasic("admin", "password123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageAge").value(0.0))
                .andExpect(jsonPath("$.standardDeviation").value(0.0))
                .andExpect(jsonPath("$.totalClients").value(0));
    }

    @Test
    void e2e_CompleteWorkflow_CreateRetrieveAndVerifyCalculations() throws Exception {
        // Given - A client with known birth date
        LocalDate birthDate = LocalDate.of(1990, 6, 15);
        int age = 34;
        
        CreateClientRequest request = CreateClientRequest.builder()
                .firstName("Alice")
                .lastName("Williams")
                .age(age)
                .birthDate(birthDate)
                .build();

        // When - Create client
        MvcResult createResult = mockMvc.perform(post("/api/clients")
                        .with(httpBasic("admin", "password123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        ClientResponse created = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                ClientResponse.class
        );

        // Then - Verify estimated death date is calculated (birthDate + 80 years)
        LocalDate expectedDeathDate = birthDate.plusYears(80);
        assertThat(created.getEstimatedDeathDate()).isEqualTo(expectedDeathDate);

        // And - Verify single client metrics
        MvcResult metricsResult = mockMvc.perform(get("/api/clients/metrics")
                        .with(httpBasic("admin", "password123")))
                .andExpect(status().isOk())
                .andReturn();

        ClientMetricsResponse metrics = objectMapper.readValue(
                metricsResult.getResponse().getContentAsString(),
                ClientMetricsResponse.class
        );

        assertThat(metrics.getAverageAge()).isEqualTo((double) age);
        assertThat(metrics.getStandardDeviation()).isEqualTo(0.0); // Single client has 0 std dev
        assertThat(metrics.getTotalClients()).isEqualTo(1L);
    }

    @Test
    void e2e_SwaggerEndpoints_ShouldBeAccessibleWithoutAuth() throws Exception {
        // Given/When/Then - Swagger endpoints should be public
        mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/api-docs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void e2e_HealthEndpoint_ShouldBeAccessibleWithoutAuth() throws Exception {
        // Given/When/Then - Health endpoint should be public
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void e2e_InvalidCredentials_ShouldReturnUnauthorized() throws Exception {
        // Given - Invalid credentials

        // When/Then - Should return 401
        mockMvc.perform(post("/api/clients")
                        .with(httpBasic("wrong", "credentials"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Test\",\"lastName\":\"User\",\"age\":30,\"birthDate\":\"1994-01-15\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void e2e_CreateClientWithMissingFields_ShouldReturnBadRequest() throws Exception {
        // Given - Request with missing required fields
        String incompleteJson = "{\"firstName\":\"Test\"}";

        // When/Then - Should return 400
        mockMvc.perform(post("/api/clients")
                        .with(httpBasic("admin", "password123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(incompleteJson))
                .andExpect(status().isBadRequest());

        // Verify no client was created
        assertThat(clientRepository.findAll()).isEmpty();
    }

    @Test
    void e2e_VerifyDatabasePersistence_AcrossMultipleRequests() throws Exception {
        // Given - Create a client
        CreateClientRequest request = CreateClientRequest.builder()
                .firstName("Persistence")
                .lastName("Test")
                .age(40)
                .birthDate(LocalDate.of(1984, 12, 25))
                .build();

        // When - Create client
        MvcResult createResult = mockMvc.perform(post("/api/clients")
                        .with(httpBasic("admin", "password123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        ClientResponse created = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                ClientResponse.class
        );

        // Then - Verify it persists across multiple GET requests
        for (int i = 0; i < 3; i++) {
            mockMvc.perform(get("/api/clients")
                            .with(httpBasic("admin", "password123")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(created.getId()))
                    .andExpect(jsonPath("$[0].firstName").value("Persistence"));
        }

        // And - Verify database has exactly one record
        assertThat(clientRepository.findAll()).hasSize(1);
    }
}

