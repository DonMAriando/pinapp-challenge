package com.pinapp.challenge.testdata;

import com.pinapp.challenge.infrastructure.adapter.in.rest.dto.ClientResponse;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Test data factory for ClientResponse DTO
 * Provides predefined test data for various response test scenarios
 */
public class ClientResponseTestData {

    // Valid responses
    public static final ClientResponse VALID_RESPONSE = ClientResponse.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .age(30)
            .birthDate(LocalDate.of(1994, 1, 15))
            .estimatedDeathDate(LocalDate.of(2074, 1, 15))
            .build();

    public static final ClientResponse VALID_RESPONSE_MARY = ClientResponse.builder()
            .id(2L)
            .firstName("Mary")
            .lastName("Garcia")
            .age(25)
            .birthDate(LocalDate.of(1999, 5, 20))
            .estimatedDeathDate(LocalDate.of(2079, 5, 20))
            .build();

    public static final ClientResponse VALID_RESPONSE_CARLOS = ClientResponse.builder()
            .id(3L)
            .firstName("Carlos")
            .lastName("Lopez")
            .age(35)
            .birthDate(LocalDate.of(1989, 3, 10))
            .estimatedDeathDate(LocalDate.of(2069, 3, 10))
            .build();

    // Responses with null values (for edge case tests)
    public static final ClientResponse RESPONSE_WITH_NULL_ID = ClientResponse.builder()
            .id(null)
            .firstName("John")
            .lastName("Doe")
            .age(30)
            .birthDate(LocalDate.of(1994, 1, 15))
            .estimatedDeathDate(LocalDate.of(2074, 1, 15))
            .build();

    public static final ClientResponse RESPONSE_WITH_NULL_FIRST_NAME = ClientResponse.builder()
            .id(1L)
            .firstName(null)
            .lastName("Doe")
            .age(30)
            .birthDate(LocalDate.of(1994, 1, 15))
            .estimatedDeathDate(LocalDate.of(2074, 1, 15))
            .build();

    public static final ClientResponse RESPONSE_WITH_NULL_LAST_NAME = ClientResponse.builder()
            .id(1L)
            .firstName("John")
            .lastName(null)
            .age(30)
            .birthDate(LocalDate.of(1994, 1, 15))
            .estimatedDeathDate(LocalDate.of(2074, 1, 15))
            .build();

    public static final ClientResponse RESPONSE_WITH_NULL_AGE = ClientResponse.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .age(null)
            .birthDate(LocalDate.of(1994, 1, 15))
            .estimatedDeathDate(LocalDate.of(2074, 1, 15))
            .build();

    public static final ClientResponse RESPONSE_WITH_NULL_BIRTH_DATE = ClientResponse.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .age(30)
            .birthDate(null)
            .estimatedDeathDate(null)
            .build();

    public static final ClientResponse RESPONSE_WITH_NULL_ESTIMATED_DEATH_DATE = ClientResponse.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .age(30)
            .birthDate(LocalDate.of(1994, 1, 15))
            .estimatedDeathDate(null)
            .build();

    public static final ClientResponse RESPONSE_WITH_ALL_NULLS = ClientResponse.builder()
            .id(null)
            .firstName(null)
            .lastName(null)
            .age(null)
            .birthDate(null)
            .estimatedDeathDate(null)
            .build();

    // Responses with edge cases
    public static final ClientResponse RESPONSE_WITH_YOUNG_CLIENT = ClientResponse.builder()
            .id(4L)
            .firstName("Young")
            .lastName("Person")
            .age(18)
            .birthDate(LocalDate.of(2006, 1, 1))
            .estimatedDeathDate(LocalDate.of(2086, 1, 1))
            .build();

    public static final ClientResponse RESPONSE_WITH_OLD_CLIENT = ClientResponse.builder()
            .id(5L)
            .firstName("Old")
            .lastName("Person")
            .age(80)
            .birthDate(LocalDate.of(1944, 1, 1))
            .estimatedDeathDate(LocalDate.of(2024, 1, 1))
            .build();

    public static final ClientResponse RESPONSE_WITH_FUTURE_BIRTH = ClientResponse.builder()
            .id(6L)
            .firstName("Future")
            .lastName("Person")
            .age(0)
            .birthDate(LocalDate.now().plusYears(5))
            .estimatedDeathDate(LocalDate.now().plusYears(85))
            .build();

    // Collections of responses
    public static final List<ClientResponse> VALID_RESPONSES_LIST = Arrays.asList(
            VALID_RESPONSE, VALID_RESPONSE_MARY, VALID_RESPONSE_CARLOS
    );

    public static final List<ClientResponse> EMPTY_RESPONSES_LIST = Arrays.asList();

    public static final List<ClientResponse> SINGLE_RESPONSE_LIST = Arrays.asList(VALID_RESPONSE);

    public static final List<ClientResponse> LARGE_RESPONSES_LIST = Arrays.asList(
            VALID_RESPONSE, VALID_RESPONSE_MARY, VALID_RESPONSE_CARLOS,
            RESPONSE_WITH_YOUNG_CLIENT, RESPONSE_WITH_OLD_CLIENT, RESPONSE_WITH_FUTURE_BIRTH
    );

    // Factory methods for creating responses with specific properties
    public static ClientResponse createResponseWithAge(int age) {
        LocalDate birthDate = LocalDate.now().minusYears(age);
        LocalDate estimatedDeathDate = birthDate.plusYears(80);
        return ClientResponse.builder()
                .id(1L)
                .firstName("Test")
                .lastName("User")
                .age(age)
                .birthDate(birthDate)
                .estimatedDeathDate(estimatedDeathDate)
                .build();
    }

    public static ClientResponse createResponseWithBirthDate(LocalDate birthDate) {
        int age = java.time.Period.between(birthDate, LocalDate.now()).getYears();
        LocalDate estimatedDeathDate = birthDate.plusYears(80);
        return ClientResponse.builder()
                .id(1L)
                .firstName("Test")
                .lastName("User")
                .age(age)
                .birthDate(birthDate)
                .estimatedDeathDate(estimatedDeathDate)
                .build();
    }

    public static ClientResponse createResponseWithName(String firstName, String lastName) {
        return ClientResponse.builder()
                .id(1L)
                .firstName(firstName)
                .lastName(lastName)
                .age(30)
                .birthDate(LocalDate.of(1994, 1, 15))
                .estimatedDeathDate(LocalDate.of(2074, 1, 15))
                .build();
    }

    public static List<ClientResponse> createResponsesWithAges(int... ages) {
        return Arrays.stream(ages)
                .mapToObj(ClientResponseTestData::createResponseWithAge)
                .toList();
    }

    // Expected values for testing
    public static final Long EXPECTED_ID = 1L;
    public static final String EXPECTED_FIRST_NAME = "John";
    public static final String EXPECTED_LAST_NAME = "Doe";
    public static final Integer EXPECTED_AGE = 30;
    public static final LocalDate EXPECTED_BIRTH_DATE = LocalDate.of(1994, 1, 15);
    public static final LocalDate EXPECTED_ESTIMATED_DEATH_DATE = LocalDate.of(2074, 1, 15);
}
