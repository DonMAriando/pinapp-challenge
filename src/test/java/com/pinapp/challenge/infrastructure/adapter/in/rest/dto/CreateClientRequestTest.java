package com.pinapp.challenge.infrastructure.adapter.in.rest.dto;

import com.pinapp.challenge.testdata.CreateClientRequestTestData;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CreateClientRequestTest {

    @Test
    void createClientRequest_WithValidData_ShouldCreateSuccessfully() {
        // Given
        CreateClientRequest request = CreateClientRequestTestData.VALID_REQUEST;

        // When & Then
        assertNotNull(request);
        assertEquals(CreateClientRequestTestData.EXPECTED_FIRST_NAME, request.getFirstName());
        assertEquals(CreateClientRequestTestData.EXPECTED_LAST_NAME, request.getLastName());
        assertEquals(CreateClientRequestTestData.EXPECTED_AGE, request.getAge());
        assertEquals(CreateClientRequestTestData.EXPECTED_BIRTH_DATE, request.getBirthDate());
    }

    @Test
    void createClientRequest_WithBuilder_ShouldCreateSuccessfully() {
        // Given
        CreateClientRequest request = CreateClientRequestTestData.VALID_REQUEST_MARY;

        // When & Then
        assertNotNull(request);
        assertEquals("Mary", request.getFirstName());
        assertEquals("Garcia", request.getLastName());
        assertEquals(25, request.getAge());
        assertEquals(LocalDate.of(1999, 5, 20), request.getBirthDate());
    }

    @Test
    void createClientRequest_WithSetters_ShouldUpdateValues() {
        // Given
        CreateClientRequest request = new CreateClientRequest();

        // When
        request.setFirstName("Carlos");
        request.setLastName("Lopez");
        request.setAge(35);
        request.setBirthDate(LocalDate.of(1989, 3, 10));

        // Then
        assertEquals("Carlos", request.getFirstName());
        assertEquals("Lopez", request.getLastName());
        assertEquals(35, request.getAge());
        assertEquals(LocalDate.of(1989, 3, 10), request.getBirthDate());
    }

    @Test
    void createClientRequest_WithNullValues_ShouldHandleGracefully() {
        // When
        CreateClientRequest request = CreateClientRequestTestData.REQUEST_WITH_ALL_NULLS;

        // Then
        assertNull(request.getFirstName());
        assertNull(request.getLastName());
        assertNull(request.getAge());
        assertNull(request.getBirthDate());
    }

    @Test
    void createClientRequest_ToString_ShouldContainAllFields() {
        // Given
        CreateClientRequest request = CreateClientRequestTestData.VALID_REQUEST;

        // When
        String toString = request.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("John"));
        assertTrue(toString.contains("Doe"));
        assertTrue(toString.contains("30"));
        assertTrue(toString.contains("1994-01-15"));
    }

    @Test
    void createClientRequest_EqualsAndHashCode_ShouldWorkCorrectly() {
        // Given
        CreateClientRequest request1 = CreateClientRequestTestData.VALID_REQUEST;
        CreateClientRequest request2 = CreateClientRequestTestData.VALID_REQUEST;
        CreateClientRequest request3 = CreateClientRequestTestData.VALID_REQUEST_MARY;

        // When & Then
        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }
}