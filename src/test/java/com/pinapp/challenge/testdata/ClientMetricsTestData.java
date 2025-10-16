package com.pinapp.challenge.testdata;

import com.pinapp.challenge.domain.model.Client;
import com.pinapp.challenge.domain.model.ClientMetrics;

import java.util.Arrays;
import java.util.List;

/**
 * Test data factory for ClientMetrics domain model
 * Provides predefined test data for various metrics test scenarios
 */
public class ClientMetricsTestData {

    // Basic metrics
    public static final ClientMetrics BASIC_METRICS = new ClientMetrics(30.0, 5.0, 3L);
    public static final ClientMetrics ZERO_METRICS = new ClientMetrics(0.0, 0.0, 0L);
    public static final ClientMetrics NULL_METRICS = new ClientMetrics(null, null, null);
    public static final ClientMetrics NEGATIVE_METRICS = new ClientMetrics(-5.0, -2.0, -1L);

    // Metrics with specific values for testing calculations
    public static final ClientMetrics METRICS_WITH_DECIMAL_AGES = new ClientMetrics(35.5, 12.3, 10L);
    public static final ClientMetrics METRICS_WITH_LARGE_VALUES = new ClientMetrics(999999.99, 888888.88, 999999L);
    public static final ClientMetrics METRICS_WITH_PRECISION = new ClientMetrics(35.123456789, 12.987654321, 10L);

    // Expected metrics for specific client lists
    public static final ClientMetrics EXPECTED_METRICS_FOR_BASIC_CLIENTS = new ClientMetrics(30.0, 5.0, 3L);
    public static final ClientMetrics EXPECTED_METRICS_FOR_SINGLE_CLIENT = new ClientMetrics(30.0, 0.0, 1L);
    public static final ClientMetrics EXPECTED_METRICS_FOR_EMPTY_LIST = new ClientMetrics(0.0, 0.0, 0L);

    // Factory methods for creating metrics with specific properties
    public static ClientMetrics createMetricsWithAverageAge(double averageAge) {
        return new ClientMetrics(averageAge, 0.0, 1L);
    }

    public static ClientMetrics createMetricsWithStandardDeviation(double standardDeviation) {
        return new ClientMetrics(30.0, standardDeviation, 1L);
    }

    public static ClientMetrics createMetricsWithTotalClients(long totalClients) {
        return new ClientMetrics(30.0, 0.0, totalClients);
    }

    // Helper method to calculate expected metrics from a list of clients
    public static ClientMetrics calculateExpectedMetrics(List<Client> clients) {
        if (clients.isEmpty()) {
            return new ClientMetrics(0.0, 0.0, 0L);
        }

        double averageAge = clients.stream()
                .mapToInt(Client::getAge)
                .average()
                .orElse(0.0);

        double standardDeviation = calculateStandardDeviation(clients, averageAge);

        return new ClientMetrics(averageAge, standardDeviation, (long) clients.size());
    }

    private static double calculateStandardDeviation(List<Client> clients, double mean) {
        if (clients.size() <= 1) {
            return 0.0;
        }

        double sumSquaredDifferences = clients.stream()
                .mapToDouble(client -> Math.pow(client.getAge() - mean, 2))
                .sum();

        return Math.sqrt(sumSquaredDifferences / clients.size());
    }

    // Test data for edge cases
    public static final ClientMetrics METRICS_WITH_ZERO_STANDARD_DEVIATION = new ClientMetrics(30.0, 0.0, 1L);
    public static final ClientMetrics METRICS_WITH_HIGH_STANDARD_DEVIATION = new ClientMetrics(30.0, 25.0, 5L);

    // Expected responses for specific scenarios
    public static final ClientMetrics EXPECTED_RESPONSE_FOR_BASIC_CLIENTS = new ClientMetrics(30.0, 5.0, 3L);
    public static final ClientMetrics EXPECTED_RESPONSE_FOR_SINGLE_CLIENT = new ClientMetrics(30.0, 0.0, 1L);
    public static final ClientMetrics EXPECTED_RESPONSE_FOR_EMPTY_LIST = new ClientMetrics(0.0, 0.0, 0L);

    // Expected values for testing
    public static final Double EXPECTED_AVERAGE_AGE = 30.0;
    public static final Double EXPECTED_STANDARD_DEVIATION = 5.0;
    public static final Long EXPECTED_TOTAL_CLIENTS = 3L;
}
