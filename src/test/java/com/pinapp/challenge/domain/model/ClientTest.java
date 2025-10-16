package com.pinapp.challenge.domain.model;

import com.pinapp.challenge.testdata.ClientTestData;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void client_WithValidData_ShouldCreateSuccessfully() {
        // Given
        Client client = ClientTestData.JOHN_DOE_WITHOUT_ID;

        // When & Then
        assertNotNull(client);
        assertNull(client.getId()); // ID should be null for new clients
        assertEquals(ClientTestData.EXPECTED_FIRST_NAME, client.getFirstName());
        assertEquals(ClientTestData.EXPECTED_LAST_NAME, client.getLastName());
        assertEquals(ClientTestData.EXPECTED_AGE, client.getAge());
        assertEquals(ClientTestData.EXPECTED_BIRTH_DATE, client.getBirthDate());
    }

    @Test
    void client_WithId_ShouldCreateSuccessfully() {
        // Given
        Client client = ClientTestData.JOHN_DOE;

        // When & Then
        assertNotNull(client);
        assertEquals(ClientTestData.EXPECTED_ID, client.getId());
        assertEquals(ClientTestData.EXPECTED_FIRST_NAME, client.getFirstName());
        assertEquals(ClientTestData.EXPECTED_LAST_NAME, client.getLastName());
        assertEquals(ClientTestData.EXPECTED_AGE, client.getAge());
        assertEquals(ClientTestData.EXPECTED_BIRTH_DATE, client.getBirthDate());
    }

    @Test
    void client_WithSetters_ShouldUpdateValues() {
        // Given
        Client client = new Client();

        // When
        client.setId(2L);
        client.setFirstName("Carlos");
        client.setLastName("Lopez");
        client.setAge(35);
        client.setBirthDate(LocalDate.of(1989, 3, 10));

        // Then
        assertEquals(2L, client.getId());
        assertEquals("Carlos", client.getFirstName());
        assertEquals("Lopez", client.getLastName());
        assertEquals(35, client.getAge());
        assertEquals(LocalDate.of(1989, 3, 10), client.getBirthDate());
    }

    @Test
    void calculateLifeExpectancy_WithValidBirthDate_ShouldReturnCorrectDate() {
        // Given
        Client client = ClientTestData.JOHN_DOE;

        // When
        LocalDate lifeExpectancy = client.calculateLifeExpectancy();

        // Then
        assertNotNull(lifeExpectancy);
        assertEquals(ClientTestData.JOHN_DOE_EXPECTED_DEATH_DATE, lifeExpectancy);
    }

    @Test
    void calculateLifeExpectancy_WithNullBirthDate_ShouldReturnNull() {
        // Given
        Client client = ClientTestData.CLIENT_WITH_NULL_BIRTH_DATE;

        // When
        LocalDate lifeExpectancy = client.calculateLifeExpectancy();

        // Then
        assertNull(lifeExpectancy);
    }

    @Test
    void calculateCurrentAge_WithValidBirthDate_ShouldReturnCorrectAge() {
        // Given
        Client client = ClientTestData.JOHN_DOE;

        // When
        Integer currentAge = client.calculateCurrentAge();

        // Then
        assertNotNull(currentAge);
        assertTrue(currentAge >= ClientTestData.JOHN_DOE_CURRENT_AGE); // Age should be current or higher
    }

    @Test
    void calculateCurrentAge_WithNullBirthDate_ShouldReturnNull() {
        // Given
        Client client = ClientTestData.CLIENT_WITH_NULL_BIRTH_DATE;

        // When
        Integer currentAge = client.calculateCurrentAge();

        // Then
        assertNull(currentAge);
    }

    @Test
    void calculateCurrentAge_WithFutureBirthDate_ShouldReturnNegativeAge() {
        // Given
        Client client = ClientTestData.FUTURE_BIRTH_CLIENT;

        // When
        Integer currentAge = client.calculateCurrentAge();

        // Then
        assertNotNull(currentAge);
        assertTrue(currentAge < 0); // Should be negative for future dates
    }

    @Test
    void toString_ShouldContainAllFields() {
        // Given
        Client client = ClientTestData.JOHN_DOE;

        // When
        String toString = client.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("John"));
        assertTrue(toString.contains("Doe"));
        assertTrue(toString.contains("30"));
        assertTrue(toString.contains("1994-01-15"));
    }

    @Test
    void equalsAndHashCode_ShouldWorkCorrectly() {
        // Given
        Client client1 = ClientTestData.JOHN_DOE;
        Client client2 = ClientTestData.JOHN_DOE;
        Client client3 = ClientTestData.MARY_GARCIA;

        // When & Then
        assertEquals(client1, client2);
        assertNotEquals(client1, client3);
        assertEquals(client1.hashCode(), client2.hashCode());
        assertNotEquals(client1.hashCode(), client3.hashCode());
    }

    @Test
    void client_WithNullValues_ShouldHandleGracefully() {
        // When
        Client client = ClientTestData.CLIENT_WITH_ALL_NULLS;

        // Then
        assertNull(client.getId());
        assertNull(client.getFirstName());
        assertNull(client.getLastName());
        assertNull(client.getAge());
        assertNull(client.getBirthDate());
    }

    @Test
    void equals_WithSameObject_ShouldReturnTrue() {
        // Given
        Client client = ClientTestData.JOHN_DOE;

        // When & Then
        assertEquals(client, client);
    }

    @Test
    void equals_WithNull_ShouldReturnFalse() {
        // Given
        Client client = ClientTestData.JOHN_DOE;

        // When & Then
        assertNotEquals(null, client);
    }

    @Test
    void equals_WithDifferentClass_ShouldReturnFalse() {
        // Given
        Client client = ClientTestData.JOHN_DOE;
        String notAClient = "Not a client";

        // When & Then
        assertNotEquals(client, notAClient);
    }

    @Test
    void equals_WithDifferentId_ShouldReturnFalse() {
        // Given
        Client client1 = new Client(1L, "John", "Doe", 30, LocalDate.of(1994, 1, 15));
        Client client2 = new Client(2L, "John", "Doe", 30, LocalDate.of(1994, 1, 15));

        // When & Then
        assertNotEquals(client1, client2);
    }

    @Test
    void equals_WithDifferentFirstName_ShouldReturnFalse() {
        // Given
        Client client1 = new Client(1L, "John", "Doe", 30, LocalDate.of(1994, 1, 15));
        Client client2 = new Client(1L, "Jane", "Doe", 30, LocalDate.of(1994, 1, 15));

        // When & Then
        assertNotEquals(client1, client2);
    }

    @Test
    void equals_WithDifferentLastName_ShouldReturnFalse() {
        // Given
        Client client1 = new Client(1L, "John", "Doe", 30, LocalDate.of(1994, 1, 15));
        Client client2 = new Client(1L, "John", "Smith", 30, LocalDate.of(1994, 1, 15));

        // When & Then
        assertNotEquals(client1, client2);
    }

    @Test
    void equals_WithDifferentAge_ShouldReturnFalse() {
        // Given
        Client client1 = new Client(1L, "John", "Doe", 30, LocalDate.of(1994, 1, 15));
        Client client2 = new Client(1L, "John", "Doe", 31, LocalDate.of(1994, 1, 15));

        // When & Then
        assertNotEquals(client1, client2);
    }

    @Test
    void equals_WithDifferentBirthDate_ShouldReturnFalse() {
        // Given
        Client client1 = new Client(1L, "John", "Doe", 30, LocalDate.of(1994, 1, 15));
        Client client2 = new Client(1L, "John", "Doe", 30, LocalDate.of(1994, 1, 16));

        // When & Then
        assertNotEquals(client1, client2);
    }
}