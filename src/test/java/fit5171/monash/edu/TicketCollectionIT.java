package fit5171.monash.edu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TicketCollectionIT {

    @BeforeEach
    void setUp() {
        TicketCollection.tickets = new ArrayList<>();
    }

    private Ticket createValidTicket(int ticketId, int flightId) {
        Airplane airplane = new Airplane(
                1,
                "B787-9",
                10,
                50,
                10
        );

        Flight flight = new Flight(
                flightId,
                "Sydney",
                "Melbourne",
                "QF" + flightId,
                "Qantas",
                Timestamp.valueOf("2026-05-01 10:00:00"),
                Timestamp.valueOf("2026-05-01 14:00:00"),
                airplane
        );

        Passenger passenger = new Passenger(
                "Jason",
                "Lee",
                29,
                "Man",
                "jason@domain.com",
                "0411111111",
                "PL1111111",
                "1234567890123456",
                500
        );

        Ticket ticket = new Ticket();
        ticket.setTicket_id(ticketId);
        ticket.setFlight(flight);
        ticket.setPassenger(passenger);
        ticket.setPrice(1000);
        ticket.setTicketStatus(false);

        return ticket;
    }

    @Test
    @DisplayName("Bottom-up integration: add real Ticket objects into TicketCollection")
    void testAddTicketsWithRealObjects() {
        Ticket ticket1 = createValidTicket(1, 101);
        Ticket ticket2 = createValidTicket(2, 102);

        ArrayList<Ticket> ticketsToAdd = new ArrayList<>();
        ticketsToAdd.add(ticket1);
        ticketsToAdd.add(ticket2);

        TicketCollection.addTickets(ticketsToAdd);

        assertEquals(2, TicketCollection.getTickets().size());
        assertTrue(TicketCollection.getTickets().contains(ticket1));
        assertTrue(TicketCollection.getTickets().contains(ticket2));
    }

    @Test
    @DisplayName("Bottom-up integration: getTicketInfo should return correct real Ticket by ID")
    void testGetTicketInfoReturnsCorrectTicket() {
        Ticket ticket1 = createValidTicket(1, 101);
        Ticket ticket2 = createValidTicket(2, 102);

        ArrayList<Ticket> ticketsToAdd = new ArrayList<>();
        ticketsToAdd.add(ticket1);
        ticketsToAdd.add(ticket2);

        TicketCollection.addTickets(ticketsToAdd);

        Ticket result = TicketCollection.getTicketInfo(2);

        assertNotNull(result);
        assertEquals(2, result.getTicket_id());
        assertEquals(102, result.getFlight().getFlightID());
        assertEquals("Lee", result.getPassenger().getSecondName());
        assertEquals("1234567890123456", result.getPassenger().getCardNumber());
    }

    @Test
    @DisplayName("Bottom-up integration: getTicketInfo should return null when ticket ID does not exist")
    void testGetTicketInfoReturnsNullForMissingTicket() {
        Ticket ticket = createValidTicket(1, 101);

        ArrayList<Ticket> ticketsToAdd = new ArrayList<>();
        ticketsToAdd.add(ticket);

        TicketCollection.addTickets(ticketsToAdd);

        Ticket result = TicketCollection.getTicketInfo(999);

        assertNull(result);
    }

    @Test
    @DisplayName("Bottom-up integration: getTicketsByFlight should return tickets for selected real Flight")
    void testGetTicketsByFlightReturnsMatchingTickets() {
        Ticket ticket1 = createValidTicket(1, 101);
        Ticket ticket2 = createValidTicket(2, 101);
        Ticket ticket3 = createValidTicket(3, 202);

        ArrayList<Ticket> ticketsToAdd = new ArrayList<>();
        ticketsToAdd.add(ticket1);
        ticketsToAdd.add(ticket2);
        ticketsToAdd.add(ticket3);

        TicketCollection.addTickets(ticketsToAdd);

        ArrayList<Ticket> result = TicketCollection.getTicketsByFlight(101);

        assertEquals(2, result.size());
        assertTrue(result.contains(ticket1));
        assertTrue(result.contains(ticket2));
        assertFalse(result.contains(ticket3));
    }

    @Test
    @DisplayName("Bottom-up integration: addTickets should reject duplicate ticket IDs in input list")
    void testAddTicketsRejectsDuplicateIdsInInput() {
        Ticket ticket1 = createValidTicket(1, 101);
        Ticket ticket2 = createValidTicket(1, 102);

        ArrayList<Ticket> ticketsToAdd = new ArrayList<>();
        ticketsToAdd.add(ticket1);
        ticketsToAdd.add(ticket2);

        assertThrows(IllegalArgumentException.class, () -> TicketCollection.addTickets(ticketsToAdd));
    }

    @Test
    @DisplayName("Bottom-up integration: addTickets should reject duplicate ticket ID already in collection")
    void testAddTicketsRejectsDuplicateIdsAlreadyInCollection() {
        Ticket existingTicket = createValidTicket(1, 101);
        ArrayList<Ticket> existingList = new ArrayList<>();
        existingList.add(existingTicket);
        TicketCollection.addTickets(existingList);

        Ticket duplicateTicket = createValidTicket(1, 202);
        ArrayList<Ticket> newList = new ArrayList<>();
        newList.add(duplicateTicket);

        assertThrows(IllegalArgumentException.class, () -> TicketCollection.addTickets(newList));
    }

    @Test
    @DisplayName("Bottom-up integration: addTickets should reject real Ticket without Flight")
    void testAddTicketsRejectsTicketWithoutFlight() {
        Ticket ticket = createValidTicket(1, 101);
        ticket.setFlight(null);

        ArrayList<Ticket> ticketsToAdd = new ArrayList<>();
        ticketsToAdd.add(ticket);

        assertThrows(IllegalArgumentException.class, () -> TicketCollection.addTickets(ticketsToAdd));
    }

    @Test
    @DisplayName("Bottom-up integration: addTickets should reject real Ticket without Passenger")
    void testAddTicketsRejectsTicketWithoutPassenger() {
        Ticket ticket = createValidTicket(1, 101);
        ticket.setPassenger(null);

        ArrayList<Ticket> ticketsToAdd = new ArrayList<>();
        ticketsToAdd.add(ticket);

        assertThrows(IllegalArgumentException.class, () -> TicketCollection.addTickets(ticketsToAdd));
    }
}