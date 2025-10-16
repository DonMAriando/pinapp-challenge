package com.pinapp.challenge.testdata;

import com.pinapp.challenge.infrastructure.adapter.in.rest.dto.CreateClientRequest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Test data factory for CreateClientRequest DTO
 * Provides predefined test data for various request test scenarios
 */
public class CreateClientRequestTestData {

    // Valid requests
    public static final CreateClientRequest VALID_REQUEST = CreateClientRequest.builder()
            .firstName("John")
            .lastName("Doe")
            .age(30)
            .birthDate(LocalDate.of(1994, 1, 15))
            .build();

    public static final CreateClientRequest VALID_REQUEST_MARY = CreateClientRequest.builder()
            .firstName("Mary")
            .lastName("Garcia")
            .age(25)
            .birthDate(LocalDate.of(1999, 5, 20))
            .build();

    public static final CreateClientRequest VALID_REQUEST_CARLOS = CreateClientRequest.builder()
            .firstName("Carlos")
            .lastName("Lopez")
            .age(35)
            .birthDate(LocalDate.of(1989, 3, 10))
            .build();

    // Requests with null values (for validation tests)
    public static final CreateClientRequest REQUEST_WITH_NULL_FIRST_NAME = CreateClientRequest.builder()
            .firstName(null)
            .lastName("Doe")
            .age(30)
            .birthDate(LocalDate.of(1994, 1, 15))
            .build();

    public static final CreateClientRequest REQUEST_WITH_NULL_LAST_NAME = CreateClientRequest.builder()
            .firstName("John")
            .lastName(null)
            .age(30)
            .birthDate(LocalDate.of(1994, 1, 15))
            .build();

    public static final CreateClientRequest REQUEST_WITH_NULL_AGE = CreateClientRequest.builder()
            .firstName("John")
            .lastName("Doe")
            .age(null)
            .birthDate(LocalDate.of(1994, 1, 15))
            .build();

    public static final CreateClientRequest REQUEST_WITH_NULL_BIRTH_DATE = CreateClientRequest.builder()
            .firstName("John")
            .lastName("Doe")
            .age(30)
            .birthDate(null)
            .build();

    public static final CreateClientRequest REQUEST_WITH_ALL_NULLS = CreateClientRequest.builder()
            .firstName(null)
            .lastName(null)
            .age(null)
            .birthDate(null)
            .build();

    // Requests with invalid data (for validation tests)
    public static final CreateClientRequest REQUEST_WITH_EMPTY_FIRST_NAME = CreateClientRequest.builder()
            .firstName("")
            .lastName("Doe")
            .age(30)
            .birthDate(LocalDate.of(1994, 1, 15))
            .build();

    public static final CreateClientRequest REQUEST_WITH_EMPTY_LAST_NAME = CreateClientRequest.builder()
            .firstName("John")
            .lastName("")
            .age(30)
            .birthDate(LocalDate.of(1994, 1, 15))
            .build();

    public static final CreateClientRequest REQUEST_WITH_NEGATIVE_AGE = CreateClientRequest.builder()
            .firstName("John")
            .lastName("Doe")
            .age(-5)
            .birthDate(LocalDate.of(1994, 1, 15))
            .build();

    public static final CreateClientRequest REQUEST_WITH_ZERO_AGE = CreateClientRequest.builder()
            .firstName("John")
            .lastName("Doe")
            .age(0)
            .birthDate(LocalDate.of(1994, 1, 15))
            .build();

    // Requests with edge cases
    public static final CreateClientRequest REQUEST_WITH_WHITESPACE_FIRST_NAME = CreateClientRequest.builder()
            .firstName("   ")
            .lastName("Doe")
            .age(30)
            .birthDate(LocalDate.of(1994, 1, 15))
            .build();

    public static final CreateClientRequest REQUEST_WITH_WHITESPACE_LAST_NAME = CreateClientRequest.builder()
            .firstName("John")
            .lastName("   ")
            .age(30)
            .birthDate(LocalDate.of(1994, 1, 15))
            .build();

    public static final CreateClientRequest REQUEST_WITH_FUTURE_BIRTH_DATE = CreateClientRequest.builder()
            .firstName("Future")
            .lastName("Person")
            .age(0)
            .birthDate(LocalDate.now().plusYears(5))
            .build();

    // Collections of requests
    public static final List<CreateClientRequest> VALID_REQUESTS_LIST = Arrays.asList(
            VALID_REQUEST, VALID_REQUEST_MARY, VALID_REQUEST_CARLOS
    );

    public static final List<CreateClientRequest> INVALID_REQUESTS_LIST = Arrays.asList(
            REQUEST_WITH_NULL_FIRST_NAME,
            REQUEST_WITH_NULL_LAST_NAME,
            REQUEST_WITH_NULL_AGE,
            REQUEST_WITH_NULL_BIRTH_DATE,
            REQUEST_WITH_EMPTY_FIRST_NAME,
            REQUEST_WITH_EMPTY_LAST_NAME,
            REQUEST_WITH_NEGATIVE_AGE,
            REQUEST_WITH_ZERO_AGE
    );

    // Factory methods for creating requests with specific properties
    public static CreateClientRequest createRequestWithAge(int age) {
        return CreateClientRequest.builder()
                .firstName("Test")
                .lastName("User")
                .age(age)
                .birthDate(LocalDate.now().minusYears(age))
                .build();
    }

    public static CreateClientRequest createRequestWithBirthDate(LocalDate birthDate) {
        int age = java.time.Period.between(birthDate, LocalDate.now()).getYears();
        return CreateClientRequest.builder()
                .firstName("Test")
                .lastName("User")
                .age(age)
                .birthDate(birthDate)
                .build();
    }

    public static CreateClientRequest createRequestWithName(String firstName, String lastName) {
        return CreateClientRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .age(30)
                .birthDate(LocalDate.of(1994, 1, 15))
                .build();
    }

    // Expected values for testing
    public static final String EXPECTED_FIRST_NAME = "John";
    public static final String EXPECTED_LAST_NAME = "Doe";
    public static final Integer EXPECTED_AGE = 30;
    public static final LocalDate EXPECTED_BIRTH_DATE = LocalDate.of(1994, 1, 15);
}
