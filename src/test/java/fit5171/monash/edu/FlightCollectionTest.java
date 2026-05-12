package fit5171.monash.edu;

import org.junit.jupiter.api.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

//Mockito imports
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class FlightCollectionTest {

    Timestamp now;
    Timestamp later;
    Airplane mockPlane;
    Flight distinctFlight;
    Flight duplicatedFlight;
    ArrayList<Flight> distinctFlights;
    ArrayList<Flight> duplicatedFlights;

    @BeforeEach
    void setUp(){

        mockPlane = mock(Airplane.class);

        now = new Timestamp(System.currentTimeMillis());
        later = new Timestamp(System.currentTimeMillis() + 3600000);

        distinctFlight = new Flight(104,
                "Melbourne",
                "Hobart",
                "ADF321",
                "Australian Domestic Airline",
                now,
                later,
                mockPlane);

        duplicatedFlight = new Flight(102,
                "Melbourne",
                "Hobart",
                "ADF321",
                "Australian Domestic Airline",
                now,
                later,
                mockPlane);

        distinctFlights = new ArrayList<>(List.of(
                new Flight(104,
                        "Melbourne",
                        "Hobart",
                        "ADF321",
                        "Australian Domestic Airline",
                        now,
                        later,
                        mockPlane),
                new Flight(105,
                        "Melbourne",
                        "Launceston",
                        "ADF222",
                        "Australian Domestic Airline",
                        now,
                        later,
                        mockPlane)
        ));

        duplicatedFlights = new ArrayList<>(List.of(
                new Flight(102,
                        "Melbourne",
                        "Hobart",
                        "ADF321",
                        "Australian Domestic Airline",
                        now,
                        later,
                        mockPlane),
                new Flight(103,
                        "Melbourne",
                        "Hobart",
                        "ADF321",
                        "Australian Domestic Airline",
                        now,
                        later,
                        mockPlane)
        ));

        FlightCollection.flights = new ArrayList<>(List.of(
                new Flight(
                        101,             // flight_id
                        "London",        // departTo
                        "New York",      // departFrom
                        "BA123",         // code
                        "British Airways", // company
                        now,             // dateFrom
                        later,           // dateTo
                        mockPlane        // airplane object
                ),
                new Flight(
                        102,
                        "Tokyo",
                        "Paris",
                        "AF456",
                        "Air France",
                        now,
                        later,
                        mockPlane
                ),
                new Flight(
                        103,
                        "Berlin",
                        "Rome",
                        "LH789",
                        "Lufthansa",
                        now,
                        later,
                        mockPlane
                )
        ));
    }

    @Test
    @DisplayName("Test: getFlights()")
    void testGetFlights() {
        ArrayList<Flight> result = FlightCollection.getFlights();
        assertNotNull(result);
        assertEquals(3, result.size(), "Initial database should have 3 flights");
    }

    @Test
    @DisplayName("Test: Should Throw when flights hasn't initialized")
    void testUninitializedDatabase() {
        FlightCollection.flights = null;

        assertThrows(IllegalStateException.class, () -> FlightCollection.getFlightInfo("London", "New York"));
        assertThrows(IllegalStateException.class, () -> FlightCollection.getFlightInfo("London"));
        assertThrows(IllegalStateException.class, () -> FlightCollection.getFlightInfo(101));
        assertThrows(IllegalStateException.class, () -> FlightCollection.addFlights(distinctFlights));
        assertThrows(IllegalStateException.class, () -> FlightCollection.addFlight(distinctFlight));
    }

    @Nested
    @DisplayName("ET: getFlightInfo(String city1, String city2")
    class GetFlightInfoWithRouteTests{

        @Test
        @DisplayName("Return Flight object when direct route exists")
        void testGetFlightInfo_DirectFlightExists() {
            Flight result = FlightCollection.getFlightInfo("New York", "London");
            assertNotNull(result, "Flight should not be null with there's a direct route");
            assertEquals("New York", result.getDepartFrom());
            assertEquals("London", result.getDepartTo());
        }

        @Test
        @DisplayName("Return Null when direct route doesn't exist")
        void testGetFlightInfo_NoDirectFlight(){
            Flight result = FlightCollection.getFlightInfo("Non exist1", "Non exist2");
            assertNull(result, "Flight should return null");
        }

        @Test
        @DisplayName("Should return null if origin and destination is the same")
        void testGetFlightInfo_SameCity() {
            assertThrows(IllegalArgumentException.class, () -> FlightCollection.getFlightInfo("London", "London"));
        }

        @Test
        @DisplayName("Test invalid input of city strings")
        void testGetFlightInfo_InvalidInput() {
            assertThrows(IllegalArgumentException.class, () -> FlightCollection.getFlightInfo("", null));
            assertThrows(IllegalArgumentException.class, () -> FlightCollection.getFlightInfo(null, ""));
        }
    }

    @Nested
    @DisplayName("ET: getFlightInfo(String city)")
    class GetFlightInfoWithCityTests{

        @Test
        @DisplayName("Test invalid input of city strings")
        void testCity_Invalid(){
            assertThrows(IllegalArgumentException.class, () -> FlightCollection.getFlightInfo(null));
            assertThrows(IllegalArgumentException.class, () -> FlightCollection.getFlightInfo(""));
            assertThrows(IllegalArgumentException.class, () -> FlightCollection.getFlightInfo("   "));
        }

        @Test
        @DisplayName("Test valid but Non-existent input of city strings")
        void testCity_ValidNonExistent(){
            assertNull(FlightCollection.getFlightInfo("Non Exist"));
        }

        @Test
        @DisplayName("Test valid of city strings")
        void testCity_Valid(){
            Flight result = FlightCollection.getFlightInfo("London");
            assertNotNull(result, "Flight should not be null with there's a transfer route");
            assertEquals("London", result.getDepartTo());
        }
    }

    @Nested
    @DisplayName("BVT: getFlightInfo(int id)")
    class GetFlightInfoWithIDTest{

        @Test
        @DisplayName("BVT: ID boundaries [-1, 0, 1]")
        void testIdBoundaries() {
            assertThrows(IllegalArgumentException.class, () -> FlightCollection.getFlightInfo(-1));
            assertNull(FlightCollection.getFlightInfo(0));
            assertNull(FlightCollection.getFlightInfo(1));
        }

        @Test
        @DisplayName("BVT: ID boundaries [Existing, Non-Existent]")
        void testIdExistenceBoundaries() {
            assertNotNull(FlightCollection.getFlightInfo(102));
            assertNull(FlightCollection.getFlightInfo(999));
        }
    }

    //-------------Test AddFlight/AddFlights------------------

    @Nested
    @DisplayName("ET: addFlights(ArrayList)")
    class AddFlightsEquivalenceTests {

        @Test
        @DisplayName("Invalid: Null or Empty list")
        void testAdd_InvalidLists() {
            ArrayList<Flight> emptyFlights = new ArrayList<>();

            assertThrows(IllegalStateException.class, () -> FlightCollection.addFlights(null));
            assertThrows(IllegalStateException.class, () -> FlightCollection.addFlights(emptyFlights));
        }

        @Test
        @DisplayName("Invalid: Duplicate IDs Input/List")
        void testAdd_Duplicates() {
            ArrayList<Flight> internalDup = new ArrayList<>(List.of(distinctFlight, distinctFlight));
            assertThrows(IllegalStateException.class, () -> FlightCollection.addFlights(internalDup));
            assertThrows(IllegalStateException.class, () -> FlightCollection.addFlights(duplicatedFlights));
        }

        @Test
        @DisplayName("Valid: Add Flights to list.")
        void testAddFlights_distinctFlights(){
            FlightCollection.addFlights(distinctFlights);
            assertTrue(FlightCollection.flights.containsAll(distinctFlights));
        }
    }

    @Nested
    @DisplayName("ET: addFlight(Flight)")
    class AddFlightEquivalenceTests {

        @Test
        @DisplayName("Invalid: Null")
        void testAdd_InvalidFlight() {
            assertThrows(IllegalStateException.class, () -> FlightCollection.addFlight(null));
        }

        @Test
        @DisplayName("Invalid: Duplicate ID")
        void testAdd_DuplicateFlight() {
            assertThrows(IllegalStateException.class, () -> FlightCollection.addFlight(duplicatedFlight));
        }

        @Test
        @DisplayName("Valid: Add Flight to list.")
        void testAddFlights_distinctFlight(){
            FlightCollection.addFlight(distinctFlight);
            assertTrue(FlightCollection.flights.contains(distinctFlight));
        }
    }
}