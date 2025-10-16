package com.pinapp.challenge.domain.model;

import com.pinapp.challenge.testdata.ClientMetricsTestData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientMetricsTest {

    @Test
    void clientMetrics_WithValidData_ShouldCreateSuccessfully() {
        // Given
        ClientMetrics metrics = ClientMetricsTestData.BASIC_METRICS;

        // When & Then
        assertNotNull(metrics);
        assertEquals(ClientMetricsTestData.EXPECTED_AVERAGE_AGE, metrics.getAverageAge());
        assertEquals(ClientMetricsTestData.EXPECTED_STANDARD_DEVIATION, metrics.getStandardDeviation());
        assertEquals(ClientMetricsTestData.EXPECTED_TOTAL_CLIENTS, metrics.getTotalClients());
    }

    @Test
    void clientMetrics_WithSetters_ShouldUpdateValues() {
        // Given
        ClientMetrics metrics = new ClientMetrics();

        // When
        metrics.setAverageAge(28.7);
        metrics.setStandardDeviation(8.9);
        metrics.setTotalClients(5L);

        // Then
        assertEquals(28.7, metrics.getAverageAge());
        assertEquals(8.9, metrics.getStandardDeviation());
        assertEquals(5L, metrics.getTotalClients());
    }

    @Test
    void clientMetrics_WithNullValues_ShouldHandleGracefully() {
        // When
        ClientMetrics metrics = ClientMetricsTestData.NULL_METRICS;

        // Then
        assertNull(metrics.getAverageAge());
        assertNull(metrics.getStandardDeviation());
        assertNull(metrics.getTotalClients());
    }

    @Test
    void clientMetrics_WithZeroValues_ShouldHandleCorrectly() {
        // When
        ClientMetrics metrics = ClientMetricsTestData.ZERO_METRICS;

        // Then
        assertEquals(0.0, metrics.getAverageAge());
        assertEquals(0.0, metrics.getStandardDeviation());
        assertEquals(0L, metrics.getTotalClients());
    }

    @Test
    void clientMetrics_WithNegativeValues_ShouldHandleCorrectly() {
        // When
        ClientMetrics metrics = ClientMetricsTestData.NEGATIVE_METRICS;

        // Then
        assertEquals(-5.0, metrics.getAverageAge());
        assertEquals(-2.0, metrics.getStandardDeviation());
        assertEquals(-1L, metrics.getTotalClients());
    }

    @Test
    void toString_ShouldContainAllFields() {
        // Given
        ClientMetrics metrics = ClientMetricsTestData.BASIC_METRICS;

        // When
        String toString = metrics.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("30.0"));
        assertTrue(toString.contains("5.0"));
        assertTrue(toString.contains("3"));
    }

    @Test
    void equalsAndHashCode_ShouldWorkCorrectly() {
        // Given
        ClientMetrics metrics1 = ClientMetricsTestData.BASIC_METRICS;
        ClientMetrics metrics2 = ClientMetricsTestData.BASIC_METRICS;
        ClientMetrics metrics3 = ClientMetricsTestData.METRICS_WITH_DECIMAL_AGES;

        // When & Then
        assertEquals(metrics1, metrics2);
        assertNotEquals(metrics1, metrics3);
        assertEquals(metrics1.hashCode(), metrics2.hashCode());
        assertNotEquals(metrics1.hashCode(), metrics3.hashCode());
    }

    @Test
    void clientMetrics_WithLargeValues_ShouldHandleCorrectly() {
        // When
        ClientMetrics metrics = ClientMetricsTestData.METRICS_WITH_LARGE_VALUES;

        // Then
        assertEquals(999999.99, metrics.getAverageAge());
        assertEquals(888888.88, metrics.getStandardDeviation());
        assertEquals(999999L, metrics.getTotalClients());
    }

    @Test
    void clientMetrics_WithDecimalPrecision_ShouldHandleCorrectly() {
        // When
        ClientMetrics metrics = ClientMetricsTestData.METRICS_WITH_PRECISION;

        // Then
        assertEquals(35.123456789, metrics.getAverageAge());
        assertEquals(12.987654321, metrics.getStandardDeviation());
        assertEquals(10L, metrics.getTotalClients());
    }

    @Test
    void equals_WithSameObject_ShouldReturnTrue() {
        // Given
        ClientMetrics metrics = ClientMetricsTestData.BASIC_METRICS;

        // When & Then
        assertEquals(metrics, metrics);
    }

    @Test
    void equals_WithNull_ShouldReturnFalse() {
        // Given
        ClientMetrics metrics = ClientMetricsTestData.BASIC_METRICS;

        // When & Then
        assertNotEquals(null, metrics);
    }

    @Test
    void equals_WithDifferentClass_ShouldReturnFalse() {
        // Given
        ClientMetrics metrics = ClientMetricsTestData.BASIC_METRICS;
        String notAMetrics = "Not metrics";

        // When & Then
        assertNotEquals(metrics, notAMetrics);
    }

    @Test
    void equals_WithDifferentAverageAge_ShouldReturnFalse() {
        // Given
        ClientMetrics metrics1 = new ClientMetrics(30.0, 5.0, 3L);
        ClientMetrics metrics2 = new ClientMetrics(31.0, 5.0, 3L);

        // When & Then
        assertNotEquals(metrics1, metrics2);
    }

    @Test
    void equals_WithDifferentStandardDeviation_ShouldReturnFalse() {
        // Given
        ClientMetrics metrics1 = new ClientMetrics(30.0, 5.0, 3L);
        ClientMetrics metrics2 = new ClientMetrics(30.0, 6.0, 3L);

        // When & Then
        assertNotEquals(metrics1, metrics2);
    }

    @Test
    void equals_WithDifferentTotalClients_ShouldReturnFalse() {
        // Given
        ClientMetrics metrics1 = new ClientMetrics(30.0, 5.0, 3L);
        ClientMetrics metrics2 = new ClientMetrics(30.0, 5.0, 4L);

        // When & Then
        assertNotEquals(metrics1, metrics2);
    }
}