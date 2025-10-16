package com.pinapp.challenge.infrastructure.adapter.in.rest;

import com.pinapp.challenge.domain.model.Client;
import com.pinapp.challenge.domain.model.ClientMetrics;
import com.pinapp.challenge.domain.port.in.CreateClientUseCase;
import com.pinapp.challenge.domain.port.in.GetAllClientsUseCase;
import com.pinapp.challenge.domain.port.in.GetClientMetricsUseCase;
import com.pinapp.challenge.infrastructure.adapter.in.rest.dto.ClientMetricsResponse;
import com.pinapp.challenge.infrastructure.adapter.in.rest.dto.ClientResponse;
import com.pinapp.challenge.infrastructure.adapter.in.rest.dto.CreateClientRequest;
import com.pinapp.challenge.testdata.ClientTestData;
import com.pinapp.challenge.testdata.ClientMetricsTestData;
import com.pinapp.challenge.testdata.CreateClientRequestTestData;
import com.pinapp.challenge.testdata.ClientResponseTestData;
import com.pinapp.challenge.testdata.ClientMetricsResponseTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    @Mock
    private CreateClientUseCase createClientUseCase;

    @Mock
    private GetAllClientsUseCase getAllClientsUseCase;

    @Mock
    private GetClientMetricsUseCase getClientMetricsUseCase;

    private ClientController clientController;
    private Client testClient;
    private CreateClientRequest testRequest;

    @BeforeEach
    void setUp() {
        clientController = new ClientController(createClientUseCase, getAllClientsUseCase, getClientMetricsUseCase);
        testClient = ClientTestData.JOHN_DOE;
        testRequest = CreateClientRequestTestData.VALID_REQUEST;
    }

    @Test
    void createClient_WithValidRequest_ShouldReturnCreatedClient() {
        // Given
        when(createClientUseCase.createClient(any(Client.class))).thenReturn(testClient);

        // When
        ResponseEntity<ClientResponse> response = clientController.createClient(testRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        
        ClientResponse clientResponse = response.getBody();
        assertNotNull(clientResponse);
        assertEquals(ClientResponseTestData.EXPECTED_ID, clientResponse.getId());
        assertEquals(ClientResponseTestData.EXPECTED_FIRST_NAME, clientResponse.getFirstName());
        assertEquals(ClientResponseTestData.EXPECTED_LAST_NAME, clientResponse.getLastName());
        assertEquals(ClientResponseTestData.EXPECTED_AGE, clientResponse.getAge());
        assertEquals(ClientResponseTestData.EXPECTED_BIRTH_DATE, clientResponse.getBirthDate());
        assertEquals(ClientResponseTestData.EXPECTED_ESTIMATED_DEATH_DATE, clientResponse.getEstimatedDeathDate());
        
        verify(createClientUseCase).createClient(any(Client.class));
    }

    @Test
    void getAllClients_ShouldReturnAllClients() {
        // Given
        List<Client> clients = ClientTestData.BASIC_CLIENTS_LIST;
        when(getAllClientsUseCase.getAllClients()).thenReturn(clients);

        // When
        ResponseEntity<List<ClientResponse>> response = clientController.getAllClients();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        List<ClientResponse> clientResponses = response.getBody();
        assertNotNull(clientResponses);
        assertEquals(3, clientResponses.size());
        
        ClientResponse firstClient = clientResponses.get(0);
        assertEquals(ClientResponseTestData.EXPECTED_ID, firstClient.getId());
        assertEquals(ClientResponseTestData.EXPECTED_FIRST_NAME, firstClient.getFirstName());
        assertEquals(ClientResponseTestData.EXPECTED_LAST_NAME, firstClient.getLastName());
        assertEquals(ClientResponseTestData.EXPECTED_AGE, firstClient.getAge());
        assertEquals(ClientResponseTestData.EXPECTED_BIRTH_DATE, firstClient.getBirthDate());
        assertEquals(ClientResponseTestData.EXPECTED_ESTIMATED_DEATH_DATE, firstClient.getEstimatedDeathDate());
        
        verify(getAllClientsUseCase).getAllClients();
    }

    @Test
    void getClientMetrics_ShouldReturnMetrics() {
        // Given
        ClientMetrics metrics = ClientMetricsTestData.BASIC_METRICS;
        when(getClientMetricsUseCase.getClientMetrics()).thenReturn(metrics);

        // When
        ResponseEntity<ClientMetricsResponse> response = clientController.getClientMetrics();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        ClientMetricsResponse metricsResponse = response.getBody();
        assertNotNull(metricsResponse);
        assertEquals(ClientMetricsResponseTestData.EXPECTED_AVERAGE_AGE, metricsResponse.getAverageAge());
        assertEquals(ClientMetricsResponseTestData.EXPECTED_STANDARD_DEVIATION, metricsResponse.getStandardDeviation());
        assertEquals(ClientMetricsResponseTestData.EXPECTED_TOTAL_CLIENTS, metricsResponse.getTotalClients());
        
        verify(getClientMetricsUseCase).getClientMetrics();
    }
}