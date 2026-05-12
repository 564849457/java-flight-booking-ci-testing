package fit5171.monash.edu;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PassengerTest {

    static Passenger passenger;

    @BeforeAll
    static void setUp() {
        passenger = new Passenger();
    }

    // ---------- Constructor Tests ----------

    @Test
    @DisplayName("Default constructor should create Passenger object")
    void defaultConstructorShouldCreatePassengerObject() {
        Passenger localPassenger = new Passenger();

        assertNotNull(localPassenger);
        assertNull(localPassenger.getEmail());
        assertNull(localPassenger.getPhoneNumber());
        assertNull(localPassenger.getPassport());
        assertNull(localPassenger.getCardNumber());
        assertEquals(0, localPassenger.getSecurityCode());
    }

    @Test
    @DisplayName("Parameterized constructor should create Passenger object with provided values")
    void parameterizedConstructorShouldCreatePassengerObjectWithProvidedValues() {
        Passenger localPassenger = new Passenger(
                "Kanin",
                "Prakaikowit",
                24,
                "Man",
                "kanin@pkkw.com",
                "0411111111",
                "PL1111111",
                "1111111111111111",
                111
        );

        assertNotNull(localPassenger);
        assertEquals("kanin@pkkw.com", localPassenger.getEmail());
        assertEquals("+61411111111", localPassenger.getPhoneNumber());
        assertEquals("PL1111111", localPassenger.getPassport());
        assertEquals("1111111111111111", localPassenger.getCardNumber());
        assertEquals(111, localPassenger.getSecurityCode());
    }

    @Test
    @DisplayName("non-default constructor should throw error with blank input")
    void nonDefaultConstructorShouldThrowErrorWithBlankInput() {
        assertThrows(IllegalArgumentException.class, () ->  new Passenger(
                "Jason",
                "Lee",
                29,
                "Man",
                "jasonlee2222@domain.com",
                "",
                "123456789",
                "1234567890123456",
                500
        ));
    }

    @Test
    @DisplayName("non-default constructor should throw error with invalid input")
    void nonDefaultConstructorShouldThrowErrorWithInvalidInput() {
        assertThrows(IllegalArgumentException.class, () ->  new Passenger(
                "Jason",
                "Lee",
                29,
                "HELLOWORLD",
                "jasonlee2222@domain.com",
                "+886123456789",
                "123456789",
                "1234567890123456",
                500
        ));
    }

    // ---------- Email Tests ----------

    @Test
    @DisplayName("ET: setEmail should store a normal email address")
    void setEmailShouldStoreNormalEmail() {
        passenger.setEmail("kanin@pkkw.com");
        assertEquals("kanin@pkkw.com", passenger.getEmail());
    }

    @Test
    @DisplayName("ET: setEmail should throw error for null")
    void setEmailShouldThrowErrorForNull() {
        assertThrows(IllegalArgumentException.class, () -> passenger.setEmail(null));
    }

    @Test
    @DisplayName("ET: setEmail should throw error for empty string")
    void setEmailShouldThrowErrorForEmptyString() {
        assertThrows(IllegalArgumentException.class, () -> passenger.setEmail(""));
    }

    @Test
    @DisplayName("ET: setEmail should throw error for malformed email")
    void setEmailShouldThrowErrorForMalformedEmail() {
        assertThrows(IllegalArgumentException.class, () -> passenger.setEmail("invalid-email"));
    }

    // ---------- Card Number Tests ----------

    @Test
    @DisplayName("ET: setCardNumber should store a normal card number")
    void setCardNumberShouldStoreNormalCardNumber() {
        passenger.setCardNumber("1111111111111111");
        assertEquals("1111111111111111", passenger.getCardNumber());
    }

    @Test
    @DisplayName("ET: setCardNumber should throw error for null")
    void setCardNumberShouldThrowErrorForNull() {
        assertThrows(IllegalArgumentException.class, () -> passenger.setCardNumber(null));
    }

    @Test
    @DisplayName("ET: setCardNumber should throw error for empty string")
    void setCardNumberShouldThrowErrorForEmptyString() {
        assertThrows(IllegalArgumentException.class, () -> passenger.setCardNumber(""));
    }

    @Test
    @DisplayName("ET: setCardNumber should throw error for non numeric text")
    void setCardNumberShouldThrowErrorForNonNumericText() {
        assertThrows(IllegalArgumentException.class, () -> passenger.setCardNumber("abcd-1234-xyz"));
    }

    @Test
    @DisplayName("ET: setCardNumber should throw error for short card number")
    void setCardNumberShouldThrowErrorTooShort() {
        assertThrows(IllegalArgumentException.class, () -> passenger.setCardNumber("1234"));
    }

    @Test
    @DisplayName("ET: setCardNumber should throw error for card number longer than 16 digits")
    void setCardNumberShouldThrowErrorTooLong() {
        assertThrows(IllegalArgumentException.class, () -> passenger.setCardNumber("12345678901234567890"));
    }

    @Test
    @DisplayName("ET: setCardNumber accept card number with spaces")
    void setCardNumberShouldAcceptCorrectFormatWithSpace() {
        passenger.setCardNumber("4111 1111 1111 1111");
        assertEquals("4111111111111111", passenger.getCardNumber());
    }

    // ---------- Security Code Tests ----------

    @Test
    @DisplayName("BVT: Validate security code (min-1, min, nominal, max, max+1)")
    void setSecurityCodeShouldStoreNormalCode() {
//      min -1
        assertThrows(IllegalArgumentException.class, () -> passenger.setSecurityCode(0));
//      min
        passenger.setSecurityCode(1);
        assertEquals(1, passenger.getSecurityCode());
//      nominal
        passenger.setSecurityCode(111);
        assertEquals(111, passenger.getSecurityCode());
//      MAX
        passenger.setSecurityCode(999999999);
        assertEquals(999999999, passenger.getSecurityCode());
//        MAX + 1
        assertThrows(IllegalArgumentException.class, () -> passenger.setSecurityCode(1000000000));
    }

    // ---------- Phone Number Tests ----------

    @Test
    @DisplayName("ET: setPhoneNumber should store normal AU number")
    void setPhoneNumberShouldStoreNormalPhoneNumber() {
        passenger.setPhoneNumber("0411111111");
        assertEquals("+61411111111", passenger.getPhoneNumber());
    }

    @Test
    @DisplayName("ET: setPhoneNumber should store normal Taiwan number")
    void setPhoneNumberShouldStoreNormalTWNumber() {
        passenger.setPhoneNumber("+886999444333");
        assertEquals("+886999444333", passenger.getPhoneNumber());
    }

    @Test
    @DisplayName("ET: setPhoneNumber should store normal Thailand number")
    void setPhoneNumberShouldStoreNormalTHNumber() {
        passenger.setPhoneNumber("+66789456123");
        assertEquals("+66789456123", passenger.getPhoneNumber());
    }

    @Test
    @DisplayName("ET: setPhoneNumber should throw error for null")
    void setPhoneNumberShouldThrowErrorForNull() {
        assertThrows(IllegalArgumentException.class, () -> passenger.setPhoneNumber(null));
    }

    @Test
    @DisplayName("ET: setPhoneNumber should throw error for empty string")
    void setPhoneNumberShouldThrowErrorForEmptyString() {
        assertThrows(IllegalArgumentException.class, () -> passenger.setPhoneNumber(""));
    }

    @Test
    @DisplayName("ET: setPhoneNumber should store formatted phone number with space")
    void setPhoneNumberShouldStoreFormattedPhoneNumber() {
        passenger.setPhoneNumber("+61 400 123 456");
        assertEquals("+61400123456", passenger.getPhoneNumber());
    }

    @Test
    @DisplayName("ET: setPhoneNumber should throw error for short phone number")
    void setPhoneNumberShouldThrowErrorTooShort() {
        assertThrows(IllegalArgumentException.class, () -> passenger.setPhoneNumber("123"));
    }

    @Test
    @DisplayName("ET: setPhoneNumber should throw error for phone number longer than typical length")
    void setPhoneNumberShouldThrowErrorTooLong() {
        assertThrows(IllegalArgumentException.class, () -> passenger.setPhoneNumber("123456789012345"));
    }

    @Test
    @DisplayName("ET: setPhoneNumber should throw error for phone number with letters")
    void setPhoneNumberShouldThrowErrorWithLetters() {
        assertThrows(IllegalArgumentException.class, () -> passenger.setPhoneNumber("0400ABC123"));
    }

    // ---------- Passport Tests ----------

    @Test
    @DisplayName("ET: setPassport should store Australia or Thailand format passport number")
    void setPassportShouldStoreAUTHPassportNumber() {
        passenger.setPassport("PI1111111");
        assertEquals("PI1111111", passenger.getPassport());
    }

    @Test
    @DisplayName("ET: setPassport should store Taiwan format passport number")
    void setPassportShouldStoreTWPassportNumber() {
        passenger.setPassport("123456789");
        assertEquals("123456789", passenger.getPassport());
    }

    @Test
    @DisplayName("ET: setPassport should throw error for null")
    void setPassportShouldThrowErrorForNull() {
        assertThrows(IllegalArgumentException.class, () -> passenger.setPassport(null));
    }

    @Test
    @DisplayName("ET: setPassport should throw error for empty string")
    void setPassportShouldThrowErrorForEmptyString() {
        assertThrows(IllegalArgumentException.class, () -> passenger.setPassport(""));
    }

    @Test
    @DisplayName("ET: setPassport should throw error for short passport number")
    void setPassportShouldThrowErrorTooShort() {
        assertThrows(IllegalArgumentException.class, () -> passenger.setPassport("P1"));
    }

    @Test
    @DisplayName("ET: setPassport should throw error for long passport number")
    void setPassportShouldThrowErrorTooLong() {
        assertThrows(IllegalArgumentException.class, () -> passenger.setPassport("P123456789012"));
    }

    @Test
    @DisplayName("ET: setPassport should throw error for passport with special characters")
    void setPassportShouldThrowErrorWithSpecialCharacters() {
        assertThrows(IllegalArgumentException.class, () -> passenger.setPassport("P123-456"));
    }
}