package fit5171.monash.edu;

import org.junit.jupiter.api.*;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

//Mockito imports
import static org.mockito.Mockito.*;

class FlightTest {

    private Flight flight;
    private Airplane mockPlane;

    @BeforeEach
    void setUp() {
        mockPlane = mock(Airplane.class);
        Timestamp dateFrom = Timestamp.valueOf("2026-04-01 10:00:00");
        Timestamp dateTo = Timestamp.valueOf("2026-04-01 14:00:00");

        flight = new Flight(1, "Sydney", "Melbourne", "QF123", "Qantas", dateFrom, dateTo, mockPlane);
    }

    @Test
    @DisplayName("Constructor should map all valid inputs to flight state")
    void constructorShouldMapAllValidInputsToFlightState() {
        Timestamp dateFrom = Timestamp.valueOf("2026-05-01 08:00:00");
        Timestamp dateTo = Timestamp.valueOf("2026-05-01 12:00:00");

        Flight constructedFlight = new Flight(
                99,
                "Perth",
                "Adelaide",
                "QF999",
                "Qantas",
                dateFrom,
                dateTo,
                mockPlane
        );

        assertEquals(99, constructedFlight.getFlightID());
        assertEquals("Perth", constructedFlight.getDepartTo());
        assertEquals("Adelaide", constructedFlight.getDepartFrom());
        assertEquals("QF999", constructedFlight.getCode());
        assertEquals("Qantas", constructedFlight.getCompany());
        assertEquals(dateFrom, constructedFlight.getDateFrom());
        assertEquals(dateTo, constructedFlight.getDateTo());
        assertEquals(mockPlane, constructedFlight.getAirplane());
    }

    @Test
    @DisplayName("Constructor should reject null dateFrom")
    void constructorShouldRejectNullDateFrom() {
        Timestamp dateTo = Timestamp.valueOf("2026-05-01 12:00:00");

        assertThrows(IllegalArgumentException.class,
                () -> new Flight(10, "Perth", "Adelaide", "QF999", "Qantas", null, dateTo, mockPlane));
    }

    @Test
    @DisplayName("Constructor should reject null dateTo")
    void constructorShouldRejectNullDateTo() {
        Timestamp dateFrom = Timestamp.valueOf("2026-05-01 08:00:00");

        assertThrows(IllegalArgumentException.class,
                () -> new Flight(10, "Perth", "Adelaide", "QF999", "Qantas", dateFrom, null, mockPlane));
    }

    @Test
    @DisplayName("Constructor should reject null airplane")
    void constructorShouldRejectNullAirplane() {
        Timestamp dateFrom = Timestamp.valueOf("2026-05-01 08:00:00");
        Timestamp dateTo = Timestamp.valueOf("2026-05-01 12:00:00");

        assertThrows(IllegalArgumentException.class,
                () -> new Flight(10, "Perth", "Adelaide", "QF999", "Qantas", dateFrom, dateTo, null));
    }

    @Test
    @DisplayName("setDateFrom should accept date before existing dateTo")
    void setDateFromShouldAcceptDateBeforeExistingDateTo() {
        Timestamp newDateFrom = Timestamp.valueOf("2026-04-01 09:00:00");

        assertDoesNotThrow(() -> flight.setDateFrom(newDateFrom));
        assertEquals(newDateFrom, flight.getDateFrom());
    }

    @Test
    @DisplayName("setDateTo should reject date before dateFrom")
    void setDateToShouldRejectDateBeforeDateFrom() {
        Timestamp invalidDateTo = Timestamp.valueOf("2026-04-01 09:59:59");

        assertThrows(IllegalArgumentException.class, () -> flight.setDateTo(invalidDateTo));
    }

    @Nested
    @DisplayName("ET+BVT: setFlightID()")
    class FlightIDTest {

        @Test
        @DisplayName("ET: Nominal valid positive flight ID")
        void testValidFlightID() {
            flight.setFlightID(35);
            assertEquals(35, flight.getFlightID());
        }

        @Test
        @DisplayName("BVT: Lower bound of valid ID (1)")
        void testBoundaryValidFlightID() {
            flight.setFlightID(1);
            assertEquals(1, flight.getFlightID());
        }

        @Test
        @DisplayName("BVT: Upper bound of invalid ID (0)")
        void testBoundaryInvalidFlightID() {
            assertThrows(IllegalArgumentException.class, () -> flight.setFlightID(0));
        }

        @Test
        @DisplayName("ET: Nominal invalid negative flight ID")
        void testInvalidFlightIDNegative() {
            assertThrows(IllegalArgumentException.class, () -> flight.setFlightID(-100));
        }
    }

    @Nested
    @DisplayName("ET: setDepartTo()")
    class SetDepartToTest {

        @Test
        @DisplayName("Valid String Input")
        void testSetDepartToValid() {
            flight.setDepartTo("Brisbane");
            assertEquals("Brisbane", flight.getDepartTo());
        }

        @Test
        @DisplayName("Empty String")
        void testSetDepartToEmpty() {
            assertThrows(IllegalArgumentException.class, () -> flight.setDepartTo(""));
        }

        @Test
        @DisplayName("Null String")
        void testSetDepartToNull() {
            assertThrows(IllegalArgumentException.class, () -> flight.setDepartTo(null));
        }
    }

    @Nested
    @DisplayName("ET: setDepartFrom()")
    class SetDepartFromTest {

        @Test
        @DisplayName("Valid String Input")
        void testSetDepartFromValid() {
            flight.setDepartFrom("Brisbane");
            assertEquals("Brisbane", flight.getDepartFrom());
        }

        @Test
        @DisplayName("Empty String")
        void testSetDepartFromEmpty() {
            assertThrows(IllegalArgumentException.class, () -> flight.setDepartFrom(""));
        }

        @Test
        @DisplayName("Null String")
        void testSetDepartFromNull() {
            assertThrows(IllegalArgumentException.class, () -> flight.setDepartFrom(null));
        }
    }

    @Nested
    @DisplayName("ET: setCode()")
    class SetCodeTest {

        @Test
        @DisplayName("Valid String Input")
        void testSetCodeValid() {
            flight.setCode("VA999");
            assertEquals("VA999", flight.getCode());
        }

        @Test
        @DisplayName("Empty String")
        void testSetCodeEmpty() {
            assertThrows(IllegalArgumentException.class, () -> flight.setCode(""));
        }

        @Test
        @DisplayName("Null String")
        void testSetCodeNull() {
            assertThrows(IllegalArgumentException.class, () -> flight.setCode(null));
        }
    }

    @Nested
    @DisplayName("ET: setCompany()")
    class SetCompanyTest {

        @Test
        @DisplayName("Valid String Input")
        void testSetCompanyValid() {
            flight.setCompany("Virgin");
            assertEquals("Virgin", flight.getCompany());
        }

        @Test
        @DisplayName("Empty String")
        void testSetCompanyEmpty() {
            assertThrows(IllegalArgumentException.class, () -> flight.setCompany(""));
        }

        @Test
        @DisplayName("Null String")
        void testSetCompanyNull() {
            assertThrows(IllegalArgumentException.class, () -> flight.setCompany(null));
        }
    }

    @Nested
    @DisplayName("BVT+ET: Date Logic Tests")
    class DateLogicTests {

        @Test
        @DisplayName("Valid: DateTo is significantly later than DateFrom")
        void testValidDateOrder() {
            Timestamp newDateTo = Timestamp.valueOf("2026-04-02 11:00:00");
            assertDoesNotThrow(() -> flight.setDateTo(newDateTo));
        }

        @Test
        @DisplayName("Invalid: DateTo is exactly equal to DateFrom")
        void testBoundaryDatesEqual() {
            Timestamp exactSameTime = Timestamp.valueOf("2026-04-01 10:00:00");
            assertDoesNotThrow(() -> flight.setDateTo(exactSameTime));
        }

        @Test
        @DisplayName("Invalid: DateFrom is 1 second after DateTo")
        void testBoundaryDateFromJustAfterDateTo() {
            Timestamp invalidDateFrom = Timestamp.valueOf("2026-04-01 14:00:01");
            assertThrows(IllegalArgumentException.class, () -> flight.setDateFrom(invalidDateFrom));
        }

        @Test
        @DisplayName("Invalid: Null Dates")
        void testNullDates() {
            assertThrows(IllegalArgumentException.class, () -> flight.setDateFrom(null));
            assertThrows(IllegalArgumentException.class, () -> flight.setDateTo(null));
        }
    }

    @Nested
    @DisplayName("ET: setAirplane()")
    class SetAirplaneTests {

        @Test
        @DisplayName("Valid Airplane Object")
        void testSetAirplaneValid() {
            flight.setAirplane(mockPlane);
            assertEquals(mockPlane, flight.getAirplane());
        }

        @Test
        @DisplayName("Null Airplane Object")
        void testSetAirplaneNull() {
            assertThrows(IllegalArgumentException.class, () -> flight.setAirplane(null));
        }
    }

    @Test
    @DisplayName("Constructor should reject invalid flight ID")
    void constructorShouldRejectInvalidFlightId() {
        Timestamp dateFrom = Timestamp.valueOf("2026-05-01 08:00:00");
        Timestamp dateTo = Timestamp.valueOf("2026-05-01 12:00:00");

        assertThrows(IllegalArgumentException.class,
                () -> new Flight(0, "Perth", "Adelaide", "QF999", "Qantas", dateFrom, dateTo, mockPlane));
    }
}