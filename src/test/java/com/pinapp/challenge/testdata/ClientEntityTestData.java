package com.pinapp.challenge.testdata;

import com.pinapp.challenge.infrastructure.adapter.out.persistence.ClientEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Test data factory for ClientEntity JPA entity
 * Provides predefined test data for various entity test scenarios
 */
public class ClientEntityTestData {

    // Valid entities
    public static final ClientEntity VALID_ENTITY = ClientEntity.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .age(30)
            .birthDate(LocalDate.of(1994, 1, 15))
            .build();

    public static final ClientEntity VALID_ENTITY_MARY = ClientEntity.builder()
            .id(2L)
            .firstName("Mary")
            .lastName("Garcia")
            .age(25)
            .birthDate(LocalDate.of(1999, 5, 20))
            .build();

    public static final ClientEntity VALID_ENTITY_CARLOS = ClientEntity.builder()
            .id(3L)
            .firstName("Carlos")
            .lastName("Lopez")
            .age(35)
            .birthDate(LocalDate.of(1989, 3, 10))
            .build();

    // Entities without ID (for creation tests)
    public static final ClientEntity ENTITY_WITHOUT_ID = ClientEntity.builder()
            .firstName("John")
            .lastName("Doe")
            .age(30)
            .birthDate(LocalDate.of(1994, 1, 15))
            .build();

    public static final ClientEntity ENTITY_MARY_WITHOUT_ID = ClientEntity.builder()
            .firstName("Mary")
            .lastName("Garcia")
            .age(25)
            .birthDate(LocalDate.of(1999, 5, 20))
            .build();

    public static final ClientEntity ENTITY_CARLOS_WITHOUT_ID = ClientEntity.builder()
            .firstName("Carlos")
            .lastName("Lopez")
            .age(35)
            .birthDate(LocalDate.of(1989, 3, 10))
            .build();

    // Entities with null values (for edge case tests)
    public static final ClientEntity ENTITY_WITH_NULL_ID = ClientEntity.builder()
            .id(null)
            .firstName("John")
            .lastName("Doe")
            .age(30)
            .birthDate(LocalDate.of(1994, 1, 15))
            .build();

    public static final ClientEntity ENTITY_WITH_NULL_FIRST_NAME = ClientEntity.builder()
            .id(1L)
            .firstName(null)
            .lastName("Doe")
            .age(30)
            .birthDate(LocalDate.of(1994, 1, 15))
            .build();

    public static final ClientEntity ENTITY_WITH_NULL_LAST_NAME = ClientEntity.builder()
            .id(1L)
            .firstName("John")
            .lastName(null)
            .age(30)
            .birthDate(LocalDate.of(1994, 1, 15))
            .build();

    public static final ClientEntity ENTITY_WITH_NULL_AGE = ClientEntity.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .age(null)
            .birthDate(LocalDate.of(1994, 1, 15))
            .build();

    public static final ClientEntity ENTITY_WITH_NULL_BIRTH_DATE = ClientEntity.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .age(30)
            .birthDate(null)
            .build();

    public static final ClientEntity ENTITY_WITH_ALL_NULLS = ClientEntity.builder()
            .id(null)
            .firstName(null)
            .lastName(null)
            .age(null)
            .birthDate(null)
            .build();

    // Entities with edge cases
    public static final ClientEntity ENTITY_WITH_YOUNG_CLIENT = ClientEntity.builder()
            .id(4L)
            .firstName("Young")
            .lastName("Person")
            .age(18)
            .birthDate(LocalDate.of(2006, 1, 1))
            .build();

    public static final ClientEntity ENTITY_WITH_OLD_CLIENT = ClientEntity.builder()
            .id(5L)
            .firstName("Old")
            .lastName("Person")
            .age(80)
            .birthDate(LocalDate.of(1944, 1, 1))
            .build();

    public static final ClientEntity ENTITY_WITH_FUTURE_BIRTH = ClientEntity.builder()
            .id(6L)
            .firstName("Future")
            .lastName("Person")
            .age(0)
            .birthDate(LocalDate.now().plusYears(5))
            .build();

    // Collections of entities
    public static final List<ClientEntity> VALID_ENTITIES_LIST = Arrays.asList(
            VALID_ENTITY, VALID_ENTITY_MARY, VALID_ENTITY_CARLOS
    );

    public static final List<ClientEntity> EMPTY_ENTITIES_LIST = Arrays.asList();

    public static final List<ClientEntity> SINGLE_ENTITY_LIST = Arrays.asList(VALID_ENTITY);

    public static final List<ClientEntity> LARGE_ENTITIES_LIST = Arrays.asList(
            VALID_ENTITY, VALID_ENTITY_MARY, VALID_ENTITY_CARLOS,
            ENTITY_WITH_YOUNG_CLIENT, ENTITY_WITH_OLD_CLIENT, ENTITY_WITH_FUTURE_BIRTH
    );

    // Factory methods for creating entities with specific properties
    public static ClientEntity createEntityWithAge(int age) {
        return ClientEntity.builder()
                .id(1L)
                .firstName("Test")
                .lastName("User")
                .age(age)
                .birthDate(LocalDate.now().minusYears(age))
                .build();
    }

    public static ClientEntity createEntityWithBirthDate(LocalDate birthDate) {
        int age = java.time.Period.between(birthDate, LocalDate.now()).getYears();
        return ClientEntity.builder()
                .id(1L)
                .firstName("Test")
                .lastName("User")
                .age(age)
                .birthDate(birthDate)
                .build();
    }

    public static ClientEntity createEntityWithName(String firstName, String lastName) {
        return ClientEntity.builder()
                .id(1L)
                .firstName(firstName)
                .lastName(lastName)
                .age(30)
                .birthDate(LocalDate.of(1994, 1, 15))
                .build();
    }

    public static List<ClientEntity> createEntitiesWithAges(int... ages) {
        return Arrays.stream(ages)
                .mapToObj(ClientEntityTestData::createEntityWithAge)
                .toList();
    }

    // Expected values for testing
    public static final Long EXPECTED_ID = 1L;
    public static final String EXPECTED_FIRST_NAME = "John";
    public static final String EXPECTED_LAST_NAME = "Doe";
    public static final Integer EXPECTED_AGE = 30;
    public static final LocalDate EXPECTED_BIRTH_DATE = LocalDate.of(1994, 1, 15);

    // Test data for specific scenarios
    public static final ClientEntity ENTITY_FOR_SAVE_TEST = ClientEntity.builder()
            .firstName("New")
            .lastName("Client")
            .age(28)
            .birthDate(LocalDate.of(1996, 6, 15))
            .build();

    public static final ClientEntity ENTITY_FOR_UPDATE_TEST = ClientEntity.builder()
            .id(1L)
            .firstName("Updated")
            .lastName("Client")
            .age(31)
            .birthDate(LocalDate.of(1993, 1, 15))
            .build();
}
