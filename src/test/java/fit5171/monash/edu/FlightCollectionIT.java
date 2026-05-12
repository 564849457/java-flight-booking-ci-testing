package fit5171.monash.edu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlightCollectionIT {

    private Airplane realAirplane;
    private Flight realFlight1;
    private Flight realFlight2;

    @BeforeEach
    void setUp() {
        FlightCollection.flights = new ArrayList<>();

        realAirplane = new Airplane(101, "Boeing 737", 10, 50, 5);


        Timestamp time1 = new Timestamp(System.currentTimeMillis() + 86400000);
        Timestamp time2 = new Timestamp(System.currentTimeMillis() + 172800000);

        realFlight1 = new Flight(
                101,             // flight_id
                "London",        // departTo
                "New York",      // departFrom
                "BA123",         // code
                "British Airways", // company
                time1,             // dateFrom
                time2,           // dateTo
                realAirplane        // airplane object
        );

        realFlight2 = new Flight(
                102,
                "Tokyo",
                "Paris",
                "AF456",
                "Air France",
                time1,
                time2,
                realAirplane
        );

    }

    @Test
    @DisplayName("Test adding a fully integrated Flight to the FlightCollection")
    void testAddFlightToCollection() {
        FlightCollection.addFlight(realFlight1);

        assertEquals(1, FlightCollection.flights.size(), "Collection should contain exactly 1 flight");
        assertTrue(FlightCollection.flights.contains(realFlight1), "Collection should contain the specific realFlight1 object");

        Flight retrievedFlight = FlightCollection.flights.getFirst();
        assertNotNull(retrievedFlight.getAirplane(), "The airplane inside the collected flight should not be null");
    }

    @Test
    @DisplayName("Test adding a fully integrated Flights to the FlightCollection")
    void testAddFlightsToCollection() {
        FlightCollection.addFlights(new ArrayList<>(List.of(realFlight1, realFlight2)));

        assertEquals(2, FlightCollection.flights.size(), "Collection should contain exactly 2 flight");
        assertTrue(FlightCollection.flights.contains(realFlight1), "Collection should contain the specific realFlight1 object");
        assertTrue(FlightCollection.flights.contains(realFlight2), "Collection should contain the specific realFlight2 object");

        ArrayList<Flight> retrievedFlights = FlightCollection.getFlights();
        for (Flight flight: retrievedFlights){
            assertNotNull(flight.getAirplane(), "The airplane inside the collected flight should not be null");
        }
    }

    @Test
    @DisplayName("Test getting fully integrated Flight from FlightCollection with route")
    void testGetFlightFromCollectionWithRoute(){
        FlightCollection.addFlight(realFlight1);

        Flight result = FlightCollection.getFlightInfo("New York", "London");
        assertNotNull(result, "Flight should not be null with there's a route");
        assertEquals("New York", result.getDepartFrom());
        assertEquals("London", result.getDepartTo());
        assertEquals(realAirplane, result.getAirplane());
    }

    @Test
    @DisplayName("Test getting a fully integrated Flight from FlightCollection with city")
    void testGetFlightFromCollectionWithCity(){
        FlightCollection.addFlight(realFlight1);

        Flight result = FlightCollection.getFlightInfo("London");
        assertNotNull(result, "Flight should not be null with there's a transfer route");
        assertEquals("London", result.getDepartTo());
        assertEquals(realAirplane, result.getAirplane());
    }
}