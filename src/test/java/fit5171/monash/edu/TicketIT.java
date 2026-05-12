package fit5171.monash.edu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketIT {

    private Airplane realAirplane;
    private Flight realFlight;
    private Passenger realPassenger;

    @BeforeEach
    void setUp() {
        realAirplane = new Airplane(1, "B787-9", 10, 50, 10);

        realFlight = new Flight(
                101,
                "Sydney",
                "Melbourne",
                "QF123",
                "Qantas",
                Timestamp.valueOf("2026-05-01 10:00:00"),
                Timestamp.valueOf("2026-05-01 14:00:00"),
                realAirplane
        );

        realPassenger = new Passenger(
                "Jason",
                "Lee",
                29,
                "Man",
                "jason@domain.com",
                "+886987654321",
                "PL1234567",
                "1234567890123456",
                500
        );
    }

    @Test
    @DisplayName("Bottom-up integration: Create Ticket with real Flight and mocked Passenger")
    void testTicketWithRealFlightAndMockPassenger() {
        Passenger mockPassenger = mock(Passenger.class);

        when(mockPassenger.getFirstName()).thenReturn("Amy");
        when(mockPassenger.getAge()).thenReturn(25);

        Ticket ticket = new Ticket();
        ticket.setTicket_id(1);
        ticket.setFlight(realFlight);
        ticket.setPassenger(mockPassenger);
        ticket.setPrice(1000);
        ticket.setTicketStatus(false);

        assertAll(
                () -> assertEquals(1, ticket.getTicket_id()),
                () -> assertEquals("Sydney", ticket.getFlight().getDepartTo()),
                () -> assertEquals("Amy", ticket.getPassenger().getFirstName()),
                () -> assertFalse(ticket.ticketStatus())
        );

        verify(mockPassenger, times(1)).getFirstName();
        verify(mockPassenger, never()).getAge();
    }

    @Test
    @DisplayName("Bottom-up integration: Create Ticket with mocked Flight and real Passenger")
    void testTicketWithMockFlightAndRealPassenger() {
        Flight mockFlight = mock(Flight.class);

        when(mockFlight.getFlightID()).thenReturn(202);
        when(mockFlight.getDepartFrom()).thenReturn("Melbourne");
        when(mockFlight.getDepartTo()).thenReturn("Brisbane");

        Ticket ticket = new Ticket();
        ticket.setTicket_id(2);
        ticket.setFlight(mockFlight);
        ticket.setPassenger(realPassenger);
        ticket.setPrice(800);
        ticket.setTicketStatus(false);

        assertAll(
                () -> assertEquals("Brisbane", ticket.getFlight().getDepartTo()),
                () -> assertEquals(202, ticket.getFlight().getFlightID()),
                () -> assertEquals("Jason", ticket.getPassenger().getFirstName()),
                () -> assertEquals(896, ticket.getPrice())
        );

        verify(mockFlight, times(1)).getDepartTo();
        verify(mockFlight, times(1)).getFlightID();
        verifyNoMoreInteractions(mockFlight);
    }

    @Test
    @DisplayName("Bottom-up integration: Create Ticket with real Flight, Airplane, and Passenger")
    void testTicketWithAllRealObjects() {
        Ticket ticket = new Ticket();
        ticket.setTicket_id(3);
        ticket.setFlight(realFlight);
        ticket.setPassenger(realPassenger);
        ticket.setPrice(1200);
        ticket.setTicketStatus(false);

        assertAll(
                () -> assertEquals(3, ticket.getTicket_id()),
                () -> assertEquals("QF123", ticket.getFlight().getCode()),
                () -> assertEquals("B787-9", ticket.getFlight().getAirplane().getAirplaneModel()),
                () -> assertEquals("Jason", ticket.getPassenger().getFirstName()),
                () -> assertEquals("+886987654321", ticket.getPassenger().getPhoneNumber()),
                () -> assertFalse(ticket.ticketStatus())
        );
    }

}