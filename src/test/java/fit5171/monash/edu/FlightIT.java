package fit5171.monash.edu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class FlightIT {

    private Airplane realAirplane;
    private Flight flight;
    private Timestamp departTime;
    private Timestamp arriveTime;

    @BeforeEach
    void setUp() {
        realAirplane = new Airplane(101, "Boeing 737", 10, 50, 5);

        departTime = new Timestamp(System.currentTimeMillis() + 86400000);
        arriveTime = new Timestamp(System.currentTimeMillis() + 172800000);

        flight = new Flight(1, "Sydney", "Melbourne", "QF400", "Quantas", departTime, arriveTime, realAirplane);

    }

    @Test
    @DisplayName("Test Flight successfully integrates with a real Airplane object")
    void testFlightAirplaneIntegration() {
        assertNotNull(flight.getAirplane(), "Flight should contain an instantiated Airplane");

        assertEquals("Boeing 737", flight.getAirplane().getAirplaneModel(), "Flight should return the correct Airplane model");
        assertEquals(101, flight.getAirplane().getAirplaneID(), "Flight should return the correct Airplane ID");
    }

}