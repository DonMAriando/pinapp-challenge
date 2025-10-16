package com.pinapp.challenge.infrastructure.adapter.in.rest.dto;

import com.pinapp.challenge.testdata.ClientResponseTestData;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ClientResponseTest {

    @Test
    void clientResponse_WithValidData_ShouldCreateSuccessfully() {
        // Given
        ClientResponse response = ClientResponseTestData.VALID_RESPONSE;

        // When & Then
        assertNotNull(response);
        assertEquals(ClientResponseTestData.EXPECTED_ID, response.getId());
        assertEquals(ClientResponseTestData.EXPECTED_FIRST_NAME, response.getFirstName());
        assertEquals(ClientResponseTestData.EXPECTED_LAST_NAME, response.getLastName());
        assertEquals(ClientResponseTestData.EXPECTED_AGE, response.getAge());
        assertEquals(ClientResponseTestData.EXPECTED_BIRTH_DATE, response.getBirthDate());
        assertEquals(ClientResponseTestData.EXPECTED_ESTIMATED_DEATH_DATE, response.getEstimatedDeathDate());
    }

    @Test
    void clientResponse_WithBuilder_ShouldCreateSuccessfully() {
        // Given
        ClientResponse response = ClientResponseTestData.VALID_RESPONSE_MARY;

        // When & Then
        assertNotNull(response);
        assertEquals(2L, response.getId());
        assertEquals("Mary", response.getFirstName());
        assertEquals("Garcia", response.getLastName());
        assertEquals(25, response.getAge());
        assertEquals(LocalDate.of(1999, 5, 20), response.getBirthDate());
        assertEquals(LocalDate.of(2079, 5, 20), response.getEstimatedDeathDate());
    }

    @Test
    void clientResponse_WithSetters_ShouldUpdateValues() {
        // Given
        ClientResponse response = new ClientResponse();

        // When
        response.setId(3L);
        response.setFirstName("Carlos");
        response.setLastName("Lopez");
        response.setAge(35);
        response.setBirthDate(LocalDate.of(1989, 3, 10));
        response.setEstimatedDeathDate(LocalDate.of(2069, 3, 10));

        // Then
        assertEquals(3L, response.getId());
        assertEquals("Carlos", response.getFirstName());
        assertEquals("Lopez", response.getLastName());
        assertEquals(35, response.getAge());
        assertEquals(LocalDate.of(1989, 3, 10), response.getBirthDate());
        assertEquals(LocalDate.of(2069, 3, 10), response.getEstimatedDeathDate());
    }

    @Test
    void clientResponse_WithNullValues_ShouldHandleGracefully() {
        // When
        ClientResponse response = ClientResponseTestData.RESPONSE_WITH_ALL_NULLS;

        // Then
        assertNull(response.getId());
        assertNull(response.getFirstName());
        assertNull(response.getLastName());
        assertNull(response.getAge());
        assertNull(response.getBirthDate());
        assertNull(response.getEstimatedDeathDate());
    }

    @Test
    void clientResponse_ToString_ShouldContainAllFields() {
        // Given
        ClientResponse response = ClientResponseTestData.VALID_RESPONSE;

        // When
        String toString = response.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("John"));
        assertTrue(toString.contains("Doe"));
        assertTrue(toString.contains("30"));
        assertTrue(toString.contains("1994-01-15"));
        assertTrue(toString.contains("2074-01-15"));
    }

    @Test
    void clientResponse_EqualsAndHashCode_ShouldWorkCorrectly() {
        // Given
        ClientResponse response1 = ClientResponseTestData.VALID_RESPONSE;
        ClientResponse response2 = ClientResponseTestData.VALID_RESPONSE;
        ClientResponse response3 = ClientResponseTestData.VALID_RESPONSE_MARY;

        // When & Then
        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }
}