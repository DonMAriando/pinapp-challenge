package com.pinapp.challenge.infrastructure.adapter.in.rest.dto;

import com.pinapp.challenge.testdata.ClientMetricsResponseTestData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientMetricsResponseTest {

    @Test
    void clientMetricsResponse_WithValidData_ShouldCreateSuccessfully() {
        // Given
        ClientMetricsResponse response = ClientMetricsResponseTestData.VALID_RESPONSE;

        // When & Then
        assertNotNull(response);
        assertEquals(ClientMetricsResponseTestData.EXPECTED_AVERAGE_AGE, response.getAverageAge());
        assertEquals(ClientMetricsResponseTestData.EXPECTED_STANDARD_DEVIATION, response.getStandardDeviation());
        assertEquals(ClientMetricsResponseTestData.EXPECTED_TOTAL_CLIENTS, response.getTotalClients());
    }

    @Test
    void clientMetricsResponse_WithBuilder_ShouldCreateSuccessfully() {
        // Given
        ClientMetricsResponse response = ClientMetricsResponseTestData.RESPONSE_WITH_DECIMAL_AGES;

        // When & Then
        assertNotNull(response);
        assertEquals(35.5, response.getAverageAge());
        assertEquals(12.3, response.getStandardDeviation());
        assertEquals(10L, response.getTotalClients());
    }

    @Test
    void clientMetricsResponse_WithSetters_ShouldUpdateValues() {
        // Given
        ClientMetricsResponse response = new ClientMetricsResponse();

        // When
        response.setAverageAge(42.1);
        response.setStandardDeviation(15.7);
        response.setTotalClients(8L);

        // Then
        assertEquals(42.1, response.getAverageAge());
        assertEquals(15.7, response.getStandardDeviation());
        assertEquals(8L, response.getTotalClients());
    }

    @Test
    void clientMetricsResponse_WithNullValues_ShouldHandleGracefully() {
        // When
        ClientMetricsResponse response = ClientMetricsResponseTestData.RESPONSE_WITH_NULL_VALUES;

        // Then
        assertNull(response.getAverageAge());
        assertNull(response.getStandardDeviation());
        assertNull(response.getTotalClients());
    }

    @Test
    void clientMetricsResponse_WithZeroValues_ShouldHandleCorrectly() {
        // When
        ClientMetricsResponse response = ClientMetricsResponseTestData.RESPONSE_WITH_ZERO_VALUES;

        // Then
        assertEquals(0.0, response.getAverageAge());
        assertEquals(0.0, response.getStandardDeviation());
        assertEquals(0L, response.getTotalClients());
    }

    @Test
    void clientMetricsResponse_ToString_ShouldContainAllFields() {
        // Given
        ClientMetricsResponse response = ClientMetricsResponseTestData.VALID_RESPONSE;

        // When
        String toString = response.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("30.0"));
        assertTrue(toString.contains("5.0"));
        assertTrue(toString.contains("3"));
    }

    @Test
    void clientMetricsResponse_EqualsAndHashCode_ShouldWorkCorrectly() {
        // Given
        ClientMetricsResponse response1 = ClientMetricsResponseTestData.VALID_RESPONSE;
        ClientMetricsResponse response2 = ClientMetricsResponseTestData.VALID_RESPONSE;
        ClientMetricsResponse response3 = ClientMetricsResponseTestData.RESPONSE_WITH_DECIMAL_AGES;

        // When & Then
        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }

    @Test
    void clientMetricsResponse_WithNegativeValues_ShouldHandleCorrectly() {
        // When
        ClientMetricsResponse response = ClientMetricsResponseTestData.RESPONSE_WITH_NEGATIVE_VALUES;

        // Then
        assertEquals(-5.0, response.getAverageAge());
        assertEquals(-2.0, response.getStandardDeviation());
        assertEquals(-1L, response.getTotalClients());
    }
}