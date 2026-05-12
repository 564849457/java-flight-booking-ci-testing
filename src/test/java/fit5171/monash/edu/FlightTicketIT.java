package fit5171.monash.edu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FlightTicketIT {
    private Airplane realAirplane;
    private Timestamp now;
    private Timestamp later;

    @BeforeEach
    void setUp() {
        FlightCollection.flights = new ArrayList<>();
        TicketCollection.tickets = new ArrayList<>();

        realAirplane = new Airplane(101, "Boeing 737", 10, 50, 5);

        now = new Timestamp(System.currentTimeMillis());
        later = new Timestamp(System.currentTimeMillis() + 3600000);
    }

    @Test
    @DisplayName("Ticket calculates correct price based on Passenger age and tax")
    void testTicketPriceIntegrationWithPassenger() {
        Passenger childPassenger = new Passenger("John", "Doe", 10, "Man",
                "john@example.com", "+61412345678", "AB1234567", "1234567812345678", 123);

        Flight flight = new Flight(1, "Sydney", "Melbourne", "QF1", "Qantas", now, later, realAirplane);

        Ticket ticket = new Ticket(100, 1000, flight, false, childPassenger);

        assertEquals(560, ticket.getPrice(), "Ticket price should integrate Passenger age and tax.");
    }

    @Test
    @DisplayName("Ticket calculates correct price for Seniors")
    void testTicketPriceIntegrationWithSeniorPassenger() {
        Passenger seniorPassenger = new Passenger("Jane", "Smith", 65, "Woman",
                "jane@example.com", "+61412345678", "AB1234567", "1234567812345678", 123);

        Flight flight = new Flight(2, "Brisbane", "Perth", "VA1", "Virgin", now, later, realAirplane);
        Ticket ticket = new Ticket(101, 1000, flight, true, seniorPassenger);

        assertEquals(0, ticket.getPrice(), "Senior ticket should be free.");
    }

    @Test
    @DisplayName("Adding tickets fails if Passenger data violates rules")
    void testTicketCreationFailsDueToInvalidPassengerIntegration() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Passenger("Bob", "Jones", 40, "Man",
                        "invalid-email",
                        "+61412345678", "AB1234567", "1234567812345678", 123),
                "Integration should fail immediately when instantiating bad component data");

        assertNotNull(exception);
    }
}