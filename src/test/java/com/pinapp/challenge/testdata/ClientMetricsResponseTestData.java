package com.pinapp.challenge.testdata;

import com.pinapp.challenge.infrastructure.adapter.in.rest.dto.ClientMetricsResponse;

import java.util.Arrays;
import java.util.List;

/**
 * Test data factory for ClientMetricsResponse DTO
 * Provides predefined test data for various metrics response test scenarios
 */
public class ClientMetricsResponseTestData {

    // Valid responses
    public static final ClientMetricsResponse VALID_RESPONSE = ClientMetricsResponse.builder()
            .averageAge(30.0)
            .standardDeviation(5.0)
            .totalClients(3L)
            .build();

    public static final ClientMetricsResponse RESPONSE_WITH_DECIMAL_AGES = ClientMetricsResponse.builder()
            .averageAge(35.5)
            .standardDeviation(12.3)
            .totalClients(10L)
            .build();

    public static final ClientMetricsResponse RESPONSE_WITH_ZERO_VALUES = ClientMetricsResponse.builder()
            .averageAge(0.0)
            .standardDeviation(0.0)
            .totalClients(0L)
            .build();

    public static final ClientMetricsResponse RESPONSE_WITH_NULL_VALUES = ClientMetricsResponse.builder()
            .averageAge(null)
            .standardDeviation(null)
            .totalClients(null)
            .build();

    public static final ClientMetricsResponse RESPONSE_WITH_NEGATIVE_VALUES = ClientMetricsResponse.builder()
            .averageAge(-5.0)
            .standardDeviation(-2.0)
            .totalClients(-1L)
            .build();

    // Responses with specific values for testing calculations
    public static final ClientMetricsResponse RESPONSE_WITH_LARGE_VALUES = ClientMetricsResponse.builder()
            .averageAge(999999.99)
            .standardDeviation(888888.88)
            .totalClients(999999L)
            .build();

    public static final ClientMetricsResponse RESPONSE_WITH_PRECISION = ClientMetricsResponse.builder()
            .averageAge(35.123456789)
            .standardDeviation(12.987654321)
            .totalClients(10L)
            .build();

    public static final ClientMetricsResponse RESPONSE_WITH_HIGH_STANDARD_DEVIATION = ClientMetricsResponse.builder()
            .averageAge(30.0)
            .standardDeviation(25.0)
            .totalClients(5L)
            .build();

    public static final ClientMetricsResponse RESPONSE_WITH_ZERO_STANDARD_DEVIATION = ClientMetricsResponse.builder()
            .averageAge(30.0)
            .standardDeviation(0.0)
            .totalClients(1L)
            .build();

    // Expected responses for specific scenarios
    public static final ClientMetricsResponse EXPECTED_RESPONSE_FOR_BASIC_CLIENTS = ClientMetricsResponse.builder()
            .averageAge(30.0)
            .standardDeviation(5.0)
            .totalClients(3L)
            .build();

    public static final ClientMetricsResponse EXPECTED_RESPONSE_FOR_SINGLE_CLIENT = ClientMetricsResponse.builder()
            .averageAge(30.0)
            .standardDeviation(0.0)
            .totalClients(1L)
            .build();

    public static final ClientMetricsResponse EXPECTED_RESPONSE_FOR_EMPTY_LIST = ClientMetricsResponse.builder()
            .averageAge(0.0)
            .standardDeviation(0.0)
            .totalClients(0L)
            .build();

    // Collections of responses
    public static final List<ClientMetricsResponse> VALID_RESPONSES_LIST = Arrays.asList(
            VALID_RESPONSE, RESPONSE_WITH_DECIMAL_AGES, RESPONSE_WITH_ZERO_VALUES
    );

    public static final List<ClientMetricsResponse> EDGE_CASE_RESPONSES_LIST = Arrays.asList(
            RESPONSE_WITH_NULL_VALUES, RESPONSE_WITH_NEGATIVE_VALUES, RESPONSE_WITH_LARGE_VALUES
    );

    // Factory methods for creating responses with specific properties
    public static ClientMetricsResponse createResponseWithAverageAge(double averageAge) {
        return ClientMetricsResponse.builder()
                .averageAge(averageAge)
                .standardDeviation(0.0)
                .totalClients(1L)
                .build();
    }

    public static ClientMetricsResponse createResponseWithStandardDeviation(double standardDeviation) {
        return ClientMetricsResponse.builder()
                .averageAge(30.0)
                .standardDeviation(standardDeviation)
                .totalClients(1L)
                .build();
    }

    public static ClientMetricsResponse createResponseWithTotalClients(long totalClients) {
        return ClientMetricsResponse.builder()
                .averageAge(30.0)
                .standardDeviation(0.0)
                .totalClients(totalClients)
                .build();
    }

    // Expected values for testing
    public static final Double EXPECTED_AVERAGE_AGE = 30.0;
    public static final Double EXPECTED_STANDARD_DEVIATION = 5.0;
    public static final Long EXPECTED_TOTAL_CLIENTS = 3L;

    // Test data for specific calculation scenarios
    public static final ClientMetricsResponse CALCULATION_TEST_RESPONSE_1 = ClientMetricsResponse.builder()
            .averageAge(25.0)
            .standardDeviation(0.0)
            .totalClients(1L)
            .build();

    public static final ClientMetricsResponse CALCULATION_TEST_RESPONSE_2 = ClientMetricsResponse.builder()
            .averageAge(30.0)
            .standardDeviation(7.07)
            .totalClients(2L)
            .build();

    public static final ClientMetricsResponse CALCULATION_TEST_RESPONSE_3 = ClientMetricsResponse.builder()
            .averageAge(30.0)
            .standardDeviation(5.0)
            .totalClients(3L)
            .build();
}
