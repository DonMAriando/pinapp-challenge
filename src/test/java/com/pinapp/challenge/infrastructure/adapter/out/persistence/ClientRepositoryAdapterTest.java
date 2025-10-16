package com.pinapp.challenge.infrastructure.adapter.out.persistence;

import com.pinapp.challenge.domain.model.Client;
import com.pinapp.challenge.testdata.ClientTestData;
import com.pinapp.challenge.testdata.ClientEntityTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientRepositoryAdapterTest {

    @Mock
    private ClientJpaRepository clientJpaRepository;

    @InjectMocks
    private ClientRepositoryAdapter clientRepositoryAdapter;

    private Client testClient;
    private ClientEntity testEntity;

    @BeforeEach
    void setUp() {
        testClient = ClientTestData.JOHN_DOE;
        testEntity = ClientEntityTestData.VALID_ENTITY;
    }

    @Test
    void save_WithValidClient_ShouldReturnSavedClient() {
        // Given
        when(clientJpaRepository.save(any(ClientEntity.class))).thenReturn(testEntity);

        // When
        Client result = clientRepositoryAdapter.save(testClient);

        // Then
        assertNotNull(result);
        assertEquals(ClientEntityTestData.EXPECTED_ID, result.getId());
        assertEquals(ClientEntityTestData.EXPECTED_FIRST_NAME, result.getFirstName());
        assertEquals(ClientEntityTestData.EXPECTED_LAST_NAME, result.getLastName());
        assertEquals(ClientEntityTestData.EXPECTED_AGE, result.getAge());
        assertEquals(ClientEntityTestData.EXPECTED_BIRTH_DATE, result.getBirthDate());
        
        verify(clientJpaRepository).save(any(ClientEntity.class));
    }

    @Test
    void findById_WithExistingId_ShouldReturnClient() {
        // Given
        when(clientJpaRepository.findById(1L)).thenReturn(Optional.of(testEntity));

        // When
        Optional<Client> result = clientRepositoryAdapter.findById(1L);

        // Then
        assertTrue(result.isPresent());
        Client client = result.get();
        assertEquals(ClientEntityTestData.EXPECTED_ID, client.getId());
        assertEquals(ClientEntityTestData.EXPECTED_FIRST_NAME, client.getFirstName());
        assertEquals(ClientEntityTestData.EXPECTED_LAST_NAME, client.getLastName());
        assertEquals(ClientEntityTestData.EXPECTED_AGE, client.getAge());
        assertEquals(ClientEntityTestData.EXPECTED_BIRTH_DATE, client.getBirthDate());
        
        verify(clientJpaRepository).findById(1L);
    }

    @Test
    void findById_WithNonExistingId_ShouldReturnEmpty() {
        // Given
        when(clientJpaRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Client> result = clientRepositoryAdapter.findById(999L);

        // Then
        assertFalse(result.isPresent());
        verify(clientJpaRepository).findById(999L);
    }

    @Test
    void findAll_WithExistingClients_ShouldReturnAllClients() {
        // Given
        List<ClientEntity> entities = ClientEntityTestData.VALID_ENTITIES_LIST;
        when(clientJpaRepository.findAll()).thenReturn(entities);

        // When
        List<Client> result = clientRepositoryAdapter.findAll();

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        
        Client client1 = result.get(0);
        assertEquals(ClientEntityTestData.EXPECTED_ID, client1.getId());
        assertEquals(ClientEntityTestData.EXPECTED_FIRST_NAME, client1.getFirstName());
        
        Client client2 = result.get(1);
        assertEquals(2L, client2.getId());
        assertEquals("Mary", client2.getFirstName());
        
        verify(clientJpaRepository).findAll();
    }

    @Test
    void findAll_WithNoClients_ShouldReturnEmptyList() {
        // Given
        when(clientJpaRepository.findAll()).thenReturn(ClientEntityTestData.EMPTY_ENTITIES_LIST);

        // When
        List<Client> result = clientRepositoryAdapter.findAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(clientJpaRepository).findAll();
    }

    @Test
    void deleteById_WithValidId_ShouldCallRepository() {
        // When
        clientRepositoryAdapter.deleteById(1L);

        // Then
        verify(clientJpaRepository).deleteById(1L);
    }

    @Test
    void count_ShouldReturnTotalCount() {
        // Given
        when(clientJpaRepository.count()).thenReturn(5L);

        // When
        long result = clientRepositoryAdapter.count();

        // Then
        assertEquals(5L, result);
        verify(clientJpaRepository).count();
    }

    @Test
    void save_WithClientWithoutId_ShouldCreateNewEntity() {
        // Given
        Client clientWithoutId = ClientTestData.JOHN_DOE_WITHOUT_ID;
        ClientEntity entityWithoutId = ClientEntityTestData.ENTITY_WITHOUT_ID;
        when(clientJpaRepository.save(any(ClientEntity.class))).thenReturn(entityWithoutId);

        // When
        Client result = clientRepositoryAdapter.save(clientWithoutId);

        // Then
        assertNotNull(result);
        assertNull(result.getId()); // Should remain null for new clients
        assertEquals(ClientEntityTestData.EXPECTED_FIRST_NAME, result.getFirstName());
        assertEquals(ClientEntityTestData.EXPECTED_LAST_NAME, result.getLastName());
        assertEquals(ClientEntityTestData.EXPECTED_AGE, result.getAge());
        assertEquals(ClientEntityTestData.EXPECTED_BIRTH_DATE, result.getBirthDate());
        
        verify(clientJpaRepository).save(any(ClientEntity.class));
    }
}