package com.pinapp.challenge.testdata;

import com.pinapp.challenge.domain.model.Client;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Test data factory for Client domain model
 * Provides predefined test data for various test scenarios
 */
public class ClientTestData {

    // Basic test clients
    public static final Client JOHN_DOE = new Client(1L, "John", "Doe", 30, LocalDate.of(1994, 1, 15));
    public static final Client MARY_GARCIA = new Client(2L, "Mary", "Garcia", 25, LocalDate.of(1999, 5, 20));
    public static final Client CARLOS_LOPEZ = new Client(3L, "Carlos", "Lopez", 35, LocalDate.of(1989, 3, 10));
    public static final Client ANNA_SMITH = new Client(4L, "Anna", "Smith", 28, LocalDate.of(1996, 8, 12));
    public static final Client DAVID_BROWN = new Client(5L, "David", "Brown", 42, LocalDate.of(1982, 11, 25));

    // Clients without ID (for creation tests)
    public static final Client JOHN_DOE_WITHOUT_ID = new Client("John", "Doe", 30, LocalDate.of(1994, 1, 15));
    public static final Client MARY_GARCIA_WITHOUT_ID = new Client("Mary", "Garcia", 25, LocalDate.of(1999, 5, 20));
    public static final Client CARLOS_LOPEZ_WITHOUT_ID = new Client("Carlos", "Lopez", 35, LocalDate.of(1989, 3, 10));

    // Edge case clients
    public static final Client YOUNG_CLIENT = new Client(6L, "Young", "Person", 18, LocalDate.of(2006, 1, 1));
    public static final Client OLD_CLIENT = new Client(7L, "Old", "Person", 80, LocalDate.of(1944, 1, 1));
    public static final Client FUTURE_BIRTH_CLIENT = new Client(8L, "Future", "Person", 0, LocalDate.now().plusYears(5));

    // Clients with null values (for validation tests)
    public static final Client CLIENT_WITH_NULL_FIRST_NAME = new Client(null, "Doe", 30, LocalDate.of(1994, 1, 15));
    public static final Client CLIENT_WITH_NULL_LAST_NAME = new Client("John", null, 30, LocalDate.of(1994, 1, 15));
    public static final Client CLIENT_WITH_NULL_AGE = new Client("John", "Doe", null, LocalDate.of(1994, 1, 15));
    public static final Client CLIENT_WITH_NULL_BIRTH_DATE = new Client("John", "Doe", 30, null);
    public static final Client CLIENT_WITH_ALL_NULLS = new Client(null, null, null, null);

    // Clients with invalid data (for validation tests)
    public static final Client CLIENT_WITH_EMPTY_FIRST_NAME = new Client("", "Doe", 30, LocalDate.of(1994, 1, 15));
    public static final Client CLIENT_WITH_EMPTY_LAST_NAME = new Client("John", "", 30, LocalDate.of(1994, 1, 15));
    public static final Client CLIENT_WITH_NEGATIVE_AGE = new Client("John", "Doe", -5, LocalDate.of(1994, 1, 15));
    public static final Client CLIENT_WITH_ZERO_AGE = new Client("John", "Doe", 0, LocalDate.of(1994, 1, 15));

    // Collections of clients for different test scenarios
    public static final List<Client> BASIC_CLIENTS_LIST = Arrays.asList(JOHN_DOE, MARY_GARCIA, CARLOS_LOPEZ);
    public static final List<Client> CLIENTS_FOR_METRICS = Arrays.asList(JOHN_DOE, MARY_GARCIA, CARLOS_LOPEZ);
    public static final List<Client> EMPTY_CLIENTS_LIST = Arrays.asList();
    public static final List<Client> SINGLE_CLIENT_LIST = Arrays.asList(JOHN_DOE);
    public static final List<Client> LARGE_CLIENTS_LIST = Arrays.asList(
        JOHN_DOE, MARY_GARCIA, CARLOS_LOPEZ, ANNA_SMITH, DAVID_BROWN, YOUNG_CLIENT, OLD_CLIENT
    );

    // Factory methods for creating clients with specific properties
    public static Client createClientWithAge(int age) {
        return new Client("Test", "User", age, LocalDate.now().minusYears(age));
    }

    public static Client createClientWithBirthDate(LocalDate birthDate) {
        int age = java.time.Period.between(birthDate, LocalDate.now()).getYears();
        return new Client("Test", "User", age, birthDate);
    }

    public static Client createClientWithName(String firstName, String lastName) {
        return new Client(firstName, lastName, 30, LocalDate.of(1994, 1, 15));
    }

    public static List<Client> createClientsWithAges(int... ages) {
        return Arrays.stream(ages)
                .mapToObj(ClientTestData::createClientWithAge)
                .toList();
    }

    // Expected calculated values for testing
    public static final LocalDate JOHN_DOE_EXPECTED_DEATH_DATE = LocalDate.of(2074, 1, 15);
    public static final LocalDate MARY_GARCIA_EXPECTED_DEATH_DATE = LocalDate.of(2079, 5, 20);
    public static final LocalDate CARLOS_LOPEZ_EXPECTED_DEATH_DATE = LocalDate.of(2069, 3, 10);

    // Expected current ages (calculated from birth dates)
    public static final int JOHN_DOE_CURRENT_AGE = 30; // Assuming test runs in 2024
    public static final int MARY_GARCIA_CURRENT_AGE = 25;
    public static final int CARLOS_LOPEZ_CURRENT_AGE = 35;

    // Expected values for testing
    public static final Long EXPECTED_ID = 1L;
    public static final String EXPECTED_FIRST_NAME = "John";
    public static final String EXPECTED_LAST_NAME = "Doe";
    public static final Integer EXPECTED_AGE = 30;
    public static final LocalDate EXPECTED_BIRTH_DATE = LocalDate.of(1994, 1, 15);
}
