package fit5171.monash.edu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {
    private static Person person;

    @BeforeEach
    void setUp() {
        person = new Person() {};
    }

    // --------- Constructor Tests ----------
    @Test
    @DisplayName("non-default constructor should instantiate object with valid input")
    void nonDefaultConstructorShouldInstantiateValidObject() {
        Person person1 = new Person("Jason", "Lee", 28, "Man");

        assertNotNull(person1);
        assertEquals("Jason", person1.getFirstName());
        assertEquals("Lee", person1.getSecondName());
        assertEquals(28, person1.getAge());
        assertEquals("Man", person1.getGender());
    }

    @Test
    @DisplayName("non-default constructor should throw error with invalid input")
    void nonDefaultConstructorShouldThrowErrorWithInvalidInput() {
        assertThrows(IllegalArgumentException.class, () ->  new Person("", "2Lee", 156, "HELLO"));
    }

    // ---------- Age Tests ----------

    @Test
    @DisplayName("BVT: Validate age (min-1, min, nominal, max, max+1)")
    void setAgeWithBVT() {
//        min - 1
        assertThrows(IllegalArgumentException.class, () -> person.setAge(-1));
//        min
        person.setAge(0);
        assertEquals(0, person.getAge());
//        nominal
        person.setAge(24);
        assertEquals(24, person.getAge());
//        MAX
        person.setAge(122);
        assertEquals(122, person.getAge());
//        MAX + 1
        assertThrows(IllegalArgumentException.class, () -> person.setAge(123));
    }

    // ---------- Gender Tests ----------

    @Test
    @DisplayName("ET: setGender should store gender value")
    void setGenderShouldStoreGenderValue() {
        person.setGender("Non-Binary");
        assertEquals("Non-Binary", person.getGender());
    }

    @Test
    @DisplayName("ET: setGender should throw error for invalid value")
    void setGenderShouldThrowErrorForInvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> person.setGender("BISEXUAL"));
    }

    @Test
    @DisplayName("ET: setGender should throw error for null")
    void setGenderShouldThrowErrorForNull() {
        assertThrows(IllegalArgumentException.class, () -> person.setGender(null));
    }

    @Test
    @DisplayName("ET: setGender should throw error for null")
    void setGenderShouldThrowErrorForBlank() {
        assertThrows(IllegalArgumentException.class, () -> person.setGender(""));
    }

    // ---------- First Name Tests ----------

    @Test
    @DisplayName("ET: setFirstName should store a valid first name")
    void setFirstNameShouldStoreValidName() {
        person.setFirstName("Kanin");
        assertEquals("Kanin", person.getFirstName());
    }

    @Test
    @DisplayName("ET: setFirstName should throw error for invalid name - starts with number")
    void setFirstNameShouldThrowErrorForInvalidName1() {
        assertThrows(IllegalArgumentException.class, () -> person.setSecondName("321Jason"));
    }

    @Test
    @DisplayName("ET: setFirstName should throw error for invalid name - starts with symbol")
    void setFirstNameShouldThrowErrorForInvalidName2() {
        assertThrows(IllegalArgumentException.class, () -> person.setSecondName("@@#Jason"));
    }

    @Test
    @DisplayName("ET: setFirstName should throw error for null")
    void setFirstNameShouldThrowErrorForNull() {
        assertThrows(IllegalArgumentException.class, () -> person.setFirstName(null));
    }

    @Test
    @DisplayName("ET: setFirstName should throw error for empty string")
    void setFirstNameShouldThrowErrorForEmptyString() {
        assertThrows(IllegalArgumentException.class, () -> person.setFirstName(""));
    }

    // ---------- Second Name Tests ----------

    @Test
    @DisplayName("ET: setSecondName should store a valid second name")
    void setSecondNameShouldStoreValidName() {
        person.setSecondName("Prakaikowit");
        assertEquals("Prakaikowit", person.getSecondName());
    }

    @Test
    @DisplayName("ET: setSecondName should throw error for invalid name - starts with number")
    void setSecondNameShouldThrowErrorForInvalidName1() {
        assertThrows(IllegalArgumentException.class, () -> person.setSecondName("7Lee"));
    }

    @Test
    @DisplayName("ET: setSecondName should throw error for invalid name - starts with symbol")
    void setSecondNameShouldThrowErrorForInvalidName2() {
        assertThrows(IllegalArgumentException.class, () -> person.setSecondName("!?Lee"));
    }

    @Test
    @DisplayName("ET: setSecondName should throw error for null")
    void setSecondNameShouldThrowErrorForNull() {
        assertThrows(IllegalArgumentException.class, () -> person.setSecondName(null));
    }

    @Test
    @DisplayName("ET: setSecondName should throw error for empty string")
    void setSecondNameShouldThrowErrorForEmptyString() {
        assertThrows(IllegalArgumentException.class, () -> person.setSecondName(""));
    }
}