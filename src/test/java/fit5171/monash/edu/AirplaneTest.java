package fit5171.monash.edu;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;


import static org.junit.jupiter.api.Assertions.*;

class AirplaneTest {

    Airplane airplane;

    @BeforeEach
    void setUp(){
        airplane = new Airplane();
    }

    //    ------------test constructor------------
    @Test
    @DisplayName("Default constructor should create Airplane object")
    void defaultAirplaneConstructor() {
        Airplane defaultAirplane = new Airplane();

        assertEquals(0, defaultAirplane.getAirplaneID());
        assertNull(defaultAirplane.getAirplaneModel());
        assertEquals(0, defaultAirplane.getBusinessSitsNumber());
        assertEquals(0, defaultAirplane.getEconomySitsNumber());
        assertEquals(0, defaultAirplane.getCrewSitsNumber());
    }

    @Test
    @DisplayName("Non-default constructor should instantiate Airplane object with valid input")
    void nondefaultConstructorShouldInstantiateValidObject() {
        Airplane nondefaultAirplane = new Airplane(321, "A348-789", 15, 50, 5);

        assertEquals(321, nondefaultAirplane.getAirplaneID());
        assertEquals("A348-789", nondefaultAirplane.getAirplaneModel());
        assertEquals(15, nondefaultAirplane.getBusinessSitsNumber());
        assertEquals(50, nondefaultAirplane.getEconomySitsNumber());
        assertEquals(5, nondefaultAirplane.getCrewSitsNumber());
    }

    @Test
    @DisplayName("Non-default constructor should throw error with invalid input")
    void nondefaultConstructorShouldThrowErrorWithInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> new Airplane(321, "HelloThisIsAirBus1122334455", 15, 50, 5));
    }

//    ------------test airplane ID------------
    @Test
    @DisplayName("BVT: Validate airplane ID (min-1, min, nominal, max, max+1)")
    void setAirplaneID() {
//        min - 1
        assertThrows(IllegalArgumentException.class, () -> airplane.setAirplaneID(0));

//        min & nominal & MAX
        airplane.setAirplaneID(1);
        assertEquals(1, airplane.getAirplaneID());
        airplane.setAirplaneID(561);
        assertEquals(561, airplane.getAirplaneID());
        airplane.setAirplaneID(10000);
        assertEquals(10000, airplane.getAirplaneID());

//        MAX + 1
        assertThrows(IllegalArgumentException.class, () -> airplane.setAirplaneID(10001));
    }

    //    ------------test airplane model------------
    @Test
    @DisplayName("Equivalence Testing: Validate airplane model (valid formats, length limits, invalid characters, null/blank)")
    void setAirplaneModel() {
//        legal input
        airplane.setAirplaneModel("B787-9");
        assertEquals("B787-9", airplane.getAirplaneModel());
        airplane.setAirplaneModel("Airbus A330");
        assertEquals("Airbus A330", airplane.getAirplaneModel());

//        illegal input
//        too short
        assertThrows(IllegalArgumentException.class, () -> airplane.setAirplaneModel("A33"));
//        too long
        assertThrows(IllegalArgumentException.class, () -> airplane.setAirplaneModel("A330-123456789123456"));
//        invalid structure
        assertThrows(IllegalArgumentException.class, () -> airplane.setAirplaneModel("@@@%%B787"));

//        null & blank
        assertThrows(IllegalArgumentException.class, () -> airplane.setAirplaneModel(null));
        assertThrows(IllegalArgumentException.class, () -> airplane.setAirplaneModel(""));
    }

    //    ------------test business seat------------
    //    ------------use decision table method------------
    @Test
    @DisplayName("BVT: Validate business seats (min-1, min, nominal, max, max+1)")
    void setBusinessSitsNumber() {
//        min - 1
        assertThrows(IllegalArgumentException.class, () -> airplane.setBusinessSitsNumber(-1));

//        min & nominal & MAX
        airplane.setBusinessSitsNumber(0);
        assertEquals(0, airplane.getBusinessSitsNumber());
        airplane.setBusinessSitsNumber(40);
        assertEquals(40, airplane.getBusinessSitsNumber());
        airplane.setBusinessSitsNumber(70);
        assertEquals(70, airplane.getBusinessSitsNumber());

//        MAX + 1
        assertThrows(IllegalArgumentException.class, () -> airplane.setBusinessSitsNumber(71));
    }

    //    ------------test economy seat------------
    @Test
    @DisplayName("BVT: Validate economy seats (min-1, min, nominal, max, max+1)")
    void setEconomySitsNumber() {
//        min - 1
        assertThrows(IllegalArgumentException.class, () -> airplane.setEconomySitsNumber(-1));

//        min & nominal & MAX
        airplane.setEconomySitsNumber(0);
        assertEquals(0, airplane.getEconomySitsNumber());
        airplane.setEconomySitsNumber(37);
        assertEquals(37, airplane.getEconomySitsNumber());
        airplane.setEconomySitsNumber(70);
        assertEquals(70, airplane.getEconomySitsNumber());

//        MAX + 1
        assertThrows(IllegalArgumentException.class, () -> airplane.setEconomySitsNumber(71));
    }

    //    ------------test crew seat------------
    @Test
    @DisplayName("BVT: Validate crew seats (min-1, min, nominal, max, max+1)")
    void setCrewSitsNumber() {
//        min - 1
        assertThrows(IllegalArgumentException.class, () -> airplane.setCrewSitsNumber(-1));

//        min & nominal & MAX
        airplane.setCrewSitsNumber(0);
        assertEquals(0, airplane.getCrewSitsNumber());
        airplane.setCrewSitsNumber(49);
        assertEquals(49, airplane.getCrewSitsNumber());
        airplane.setCrewSitsNumber(70);
        assertEquals(70, airplane.getCrewSitsNumber());

//        MAX + 1
        assertThrows(IllegalArgumentException.class, () -> airplane.setCrewSitsNumber(71));

    }

    // ------------Decision Table: Seat Validation ------------

    @Test
    @DisplayName("Decision table - R1: Accept seat settings when all values are valid and total is below 70")
    void testSeatRule1() {
        airplane.setBusinessSitsNumber(10);
        airplane.setEconomySitsNumber(40);
        airplane.setCrewSitsNumber(5);

        assertEquals(10, airplane.getBusinessSitsNumber());
        assertEquals(40, airplane.getEconomySitsNumber());
        assertEquals(5, airplane.getCrewSitsNumber());
    }

    @Test
    @DisplayName("Decision table - R2: Accept seat settings when total is exactly 70")
    void testSeatRule2() {
        airplane.setBusinessSitsNumber(20);
        airplane.setEconomySitsNumber(40);
        airplane.setCrewSitsNumber(10);

        assertEquals(20, airplane.getBusinessSitsNumber());
        assertEquals(40, airplane.getEconomySitsNumber());
        assertEquals(10, airplane.getCrewSitsNumber());
    }

    @Test
    @DisplayName("Decision table - R3: Throw exception when total seats exceed 70")
    void testSeatRule3() {
        airplane.setBusinessSitsNumber(20);
        airplane.setEconomySitsNumber(30);

        assertThrows(IllegalArgumentException.class, () -> airplane.setCrewSitsNumber(25)); // total = 75
    }

    @Test
    @DisplayName("BVT: Airplane model should accept length boundary values")
    void airplaneModelShouldAcceptLengthBoundaries() {
        airplane.setAirplaneModel("A123");
        assertEquals("A123", airplane.getAirplaneModel());

        airplane.setAirplaneModel("Airbus A330-900");
        assertEquals("Airbus A330-900", airplane.getAirplaneModel());
    }

    @Test
    @DisplayName("Invalid airplane ID should not overwrite previous valid value")
    void invalidAirplaneIdShouldNotOverwritePreviousValidValue() {
        airplane.setAirplaneID(100);

        assertThrows(IllegalArgumentException.class, () -> airplane.setAirplaneID(10001));

        assertEquals(100, airplane.getAirplaneID());
    }

    @Test
    @DisplayName("Invalid business seat update should not overwrite previous valid value")
    void invalidBusinessSeatsShouldNotOverwritePreviousValidValue() {
        airplane.setBusinessSitsNumber(10);

        assertThrows(IllegalArgumentException.class, () -> airplane.setBusinessSitsNumber(71));

        assertEquals(10, airplane.getBusinessSitsNumber());
    }

    @Test
    @DisplayName("Invalid economy seat update should not overwrite previous valid value")
    void invalidEconomySeatsShouldNotOverwritePreviousValidValue() {
        airplane.setEconomySitsNumber(20);

        assertThrows(IllegalArgumentException.class, () -> airplane.setEconomySitsNumber(71));

        assertEquals(20, airplane.getEconomySitsNumber());
    }

    @Test
    @DisplayName("Invalid crew seat update should not overwrite previous valid value")
    void invalidCrewSeatsShouldNotOverwritePreviousValidValue() {
        airplane.setCrewSitsNumber(5);

        assertThrows(IllegalArgumentException.class, () -> airplane.setCrewSitsNumber(71));

        assertEquals(5, airplane.getCrewSitsNumber());
    }

    @Test
    @DisplayName("toString should include model and seat information")
    void toStringShouldIncludeModelAndSeatInformation() {
        Airplane validAirplane = new Airplane(321, "B787-9", 10, 50, 5);

        String result = validAirplane.toString();

        assertTrue(result.contains("B787-9"));
        assertTrue(result.contains("10"));
        assertTrue(result.contains("50"));
        assertTrue(result.contains("5"));
    }
}