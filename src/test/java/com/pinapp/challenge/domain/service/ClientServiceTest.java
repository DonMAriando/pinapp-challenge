package com.pinapp.challenge.domain.service;

import com.pinapp.challenge.domain.model.Client;
import com.pinapp.challenge.domain.model.ClientMetrics;
import com.pinapp.challenge.domain.port.out.ClientRepositoryPort;
import com.pinapp.challenge.testdata.ClientTestData;
import com.pinapp.challenge.testdata.ClientMetricsTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepositoryPort clientRepositoryPort;

    private ClientService clientService;

    @BeforeEach
    void setUp() {
        clientService = new ClientService(clientRepositoryPort);
    }

    @Test
    void createClient_WithValidData_ShouldReturnCreatedClient() {
        // Given
        Client client = ClientTestData.JOHN_DOE_WITHOUT_ID;
        Client savedClient = ClientTestData.JOHN_DOE;
        
        when(clientRepositoryPort.save(client)).thenReturn(savedClient);

        // When
        Client result = clientService.createClient(client);

        // Then
        assertNotNull(result);
        assertEquals(ClientTestData.EXPECTED_ID, result.getId());
        assertEquals(ClientTestData.EXPECTED_FIRST_NAME, result.getFirstName());
        assertEquals(ClientTestData.EXPECTED_LAST_NAME, result.getLastName());
        assertEquals(ClientTestData.EXPECTED_AGE, result.getAge());
        assertEquals(ClientTestData.EXPECTED_BIRTH_DATE, result.getBirthDate());
        
        verify(clientRepositoryPort).save(client);
    }

    @Test
    void createClient_WithInvalidFirstName_ShouldThrowException() {
        // Given
        Client client = ClientTestData.CLIENT_WITH_EMPTY_FIRST_NAME;

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> clientService.createClient(client));
        
        assertEquals("First name is required", exception.getMessage());
        verify(clientRepositoryPort, never()).save(any());
    }

    @Test
    void createClient_WithInvalidAge_ShouldThrowException() {
        // Given
        Client client = ClientTestData.CLIENT_WITH_NEGATIVE_AGE;

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> clientService.createClient(client));
        
        assertEquals("Age must be greater than 0", exception.getMessage());
        verify(clientRepositoryPort, never()).save(any());
    }

    @Test
    void getAllClients_ShouldReturnAllClients() {
        // Given
        List<Client> clients = ClientTestData.BASIC_CLIENTS_LIST;
        
        when(clientRepositoryPort.findAll()).thenReturn(clients);

        // When
        List<Client> result = clientService.getAllClients();

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(ClientTestData.EXPECTED_FIRST_NAME, result.get(0).getFirstName());
        assertEquals("Mary", result.get(1).getFirstName());
        
        verify(clientRepositoryPort).findAll();
    }

    @Test
    void getClientMetrics_WithEmptyList_ShouldReturnZeroMetrics() {
        // Given
        when(clientRepositoryPort.findAll()).thenReturn(ClientTestData.EMPTY_CLIENTS_LIST);

        // When
        ClientMetrics result = clientService.getClientMetrics();

        // Then
        assertNotNull(result);
        assertEquals(ClientMetricsTestData.EXPECTED_RESPONSE_FOR_EMPTY_LIST.getAverageAge(), result.getAverageAge());
        assertEquals(ClientMetricsTestData.EXPECTED_RESPONSE_FOR_EMPTY_LIST.getStandardDeviation(), result.getStandardDeviation());
        assertEquals(ClientMetricsTestData.EXPECTED_RESPONSE_FOR_EMPTY_LIST.getTotalClients(), result.getTotalClients());
        
        verify(clientRepositoryPort).findAll();
    }

    @Test
    void getClientMetrics_WithClients_ShouldCalculateCorrectMetrics() {
        // Given
        List<Client> clients = ClientTestData.CLIENTS_FOR_METRICS;
        
        when(clientRepositoryPort.findAll()).thenReturn(clients);

        // When
        ClientMetrics result = clientService.getClientMetrics();

        // Then
        assertNotNull(result);
        assertEquals(ClientMetricsTestData.EXPECTED_RESPONSE_FOR_BASIC_CLIENTS.getAverageAge(), result.getAverageAge());
        assertEquals(ClientMetricsTestData.EXPECTED_RESPONSE_FOR_BASIC_CLIENTS.getStandardDeviation(), result.getStandardDeviation(), 0.1);
        assertEquals(ClientMetricsTestData.EXPECTED_RESPONSE_FOR_BASIC_CLIENTS.getTotalClients(), result.getTotalClients());
        
        verify(clientRepositoryPort).findAll();
    }
}