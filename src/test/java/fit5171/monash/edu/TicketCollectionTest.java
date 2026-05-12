package fit5171.monash.edu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TicketCollectionTest {

    @BeforeEach
    void setUp() {
        TicketCollection.tickets = new ArrayList<>();
    }

    // ---------- Helper Methods ----------

    private Ticket mockTicketWithRequiredFields(int ticketId) {
        Ticket ticket = mock(Ticket.class);
        Flight flight = mock(Flight.class);
        Passenger passenger = mock(Passenger.class);
        when(ticket.getTicket_id()).thenReturn(ticketId);
        when(ticket.getPrice()).thenReturn(100);
        when(ticket.getFlight()).thenReturn(flight);
        when(ticket.getPassenger()).thenReturn(passenger);
        return ticket;
    }

    private Ticket mockTicketWithFlight(int ticketId, Flight flight) {
        Ticket ticket = mock(Ticket.class);
        Passenger passenger = mock(Passenger.class);
        when(ticket.getTicket_id()).thenReturn(ticketId);
        when(ticket.getPrice()).thenReturn(100);
        when(ticket.getFlight()).thenReturn(flight);
        when(ticket.getPassenger()).thenReturn(passenger);
        return ticket;
    }

    // ---------- getTickets Tests ----------

    @Test
    @DisplayName("ET: getTickets should return current backing collection reference")
    void etGetTicketsShouldReturnCurrentBackingCollectionReference() {
        ArrayList<Ticket> snapshot = TicketCollection.getTickets();
        assertNotNull(snapshot);
        assertSame(TicketCollection.tickets, snapshot);
        assertTrue(snapshot.isEmpty());
    }

    // ---------- addTickets Tests ----------

    @Test
    @DisplayName("DTT: addTickets should reject list containing null ticket branch")
    void dttAddTicketsShouldRejectListContainingNullTicketBranch() {
        Ticket valid = mockTicketWithRequiredFields(1);

        ArrayList<Ticket> incoming = new ArrayList<>();
        incoming.add(valid);
        incoming.add(null);

        assertThrows(IllegalArgumentException.class, () -> TicketCollection.addTickets(incoming));
    }

    @Test
    @DisplayName("DTT: addTickets should reject duplicate id against existing collection branch")
    void dttAddTicketsShouldRejectDuplicateIdAgainstExistingCollectionBranch() {
        Ticket existing = mock(Ticket.class);
        when(existing.getTicket_id()).thenReturn(100);
        Ticket duplicate = mockTicketWithRequiredFields(100);

        TicketCollection.tickets.add(existing);

        ArrayList<Ticket> incoming = new ArrayList<>();
        incoming.add(duplicate);

        assertThrows(IllegalArgumentException.class, () -> TicketCollection.addTickets(incoming));
    }

    @Test
    @DisplayName("DTT: addTickets should reject duplicate id inside incoming list branch")
    void dttAddTicketsShouldRejectDuplicateIdInsideIncomingListBranch() {
        Ticket t1 = mockTicketWithRequiredFields(200);
        Ticket t2 = mockTicketWithRequiredFields(200);

        ArrayList<Ticket> incoming = new ArrayList<>();
        incoming.add(t1);
        incoming.add(t2);

        assertThrows(IllegalArgumentException.class, () -> TicketCollection.addTickets(incoming));
    }

    @Test
    @DisplayName("DTT: addTickets should reject null flight branch")
    void dttAddTicketsShouldRejectNullFlightBranch() {
        Ticket invalid = mock(Ticket.class);
        Passenger passenger = mock(Passenger.class);
        when(invalid.getTicket_id()).thenReturn(1);
        when(invalid.getPrice()).thenReturn(100);
        when(invalid.getFlight()).thenReturn(null);
        when(invalid.getPassenger()).thenReturn(passenger);

        ArrayList<Ticket> incoming = new ArrayList<>();
        incoming.add(invalid);

        assertThrows(IllegalArgumentException.class, () -> TicketCollection.addTickets(incoming));
    }

    @Test
    @DisplayName("DTT: addTickets should reject null passenger branch")
    void dttAddTicketsShouldRejectNullPassengerBranch() {
        Ticket invalid = mock(Ticket.class);
        Flight flight = mock(Flight.class);
        when(invalid.getTicket_id()).thenReturn(1);
        when(invalid.getPrice()).thenReturn(100);
        when(invalid.getFlight()).thenReturn(flight);
        when(invalid.getPassenger()).thenReturn(null);

        ArrayList<Ticket> incoming = new ArrayList<>();
        incoming.add(invalid);

        assertThrows(IllegalArgumentException.class, () -> TicketCollection.addTickets(incoming));
    }

    @Test
    @DisplayName("DTT: addTickets should keep collection unchanged when validation branch fails")
    void dttAddTicketsShouldKeepCollectionUnchangedWhenValidationBranchFails() {
        Ticket existing = mock(Ticket.class);
        when(existing.getTicket_id()).thenReturn(10);
        TicketCollection.tickets.add(existing);

        Ticket invalid = mockTicketWithRequiredFields(10); // duplicate

        ArrayList<Ticket> incoming = new ArrayList<>();
        incoming.add(invalid);

        assertThrows(IllegalArgumentException.class, () -> TicketCollection.addTickets(incoming));
        assertEquals(1, TicketCollection.tickets.size());
        assertSame(existing, TicketCollection.tickets.get(0));
    }

    @Test
    @DisplayName("ET: addTickets should append all valid incoming tickets")
    void etAddTicketsShouldAppendAllValidIncomingTickets() {
        Ticket t1 = mockTicketWithRequiredFields(1);
        Ticket t2 = mockTicketWithRequiredFields(2);
        Ticket t3 = mockTicketWithRequiredFields(3);
        TicketCollection.tickets.add(t1);

        ArrayList<Ticket> incoming = new ArrayList<>();
        incoming.add(t2);
        incoming.add(t3);

        TicketCollection.addTickets(incoming);

        assertEquals(3, TicketCollection.tickets.size());
        assertSame(t1, TicketCollection.tickets.get(0));
        assertSame(t2, TicketCollection.tickets.get(1));
        assertSame(t3, TicketCollection.tickets.get(2));
    }

    @Test
    @DisplayName("BVT: addTickets should keep collection unchanged for empty input boundary")
    void bvtAddTicketsShouldKeepCollectionUnchangedForEmptyInputBoundary() {
        Ticket existing = mock(Ticket.class);
        when(existing.getTicket_id()).thenReturn(10);
        TicketCollection.tickets.add(existing);

        TicketCollection.addTickets(new ArrayList<>());

        assertEquals(1, TicketCollection.tickets.size());
        assertSame(existing, TicketCollection.tickets.get(0));
    }

    @Test
    @DisplayName("DTT: addTickets should reject null input list branch")
    void dttAddTicketsShouldRejectNullInputListBranch() {
        assertThrows(NullPointerException.class, () -> TicketCollection.addTickets(null));
    }

    // ---------- getTicketInfo Tests ----------

    @Test
    @DisplayName("ET: getTicketInfo should return exact matching ticket instance")
    void etGetTicketInfoShouldReturnExactMatchingTicketInstance() {
        Ticket nonMatching = mock(Ticket.class);
        Ticket matching = mock(Ticket.class);
        when(nonMatching.getTicket_id()).thenReturn(100);
        when(matching.getTicket_id()).thenReturn(200);
        TicketCollection.tickets.add(nonMatching);
        TicketCollection.tickets.add(matching);

        Ticket found = TicketCollection.getTicketInfo(200);

        assertSame(matching, found);
    }

    @Test
    @DisplayName("ET: getTicketInfo should return null for non-existing id class")
    void etGetTicketInfoShouldReturnNullForNonExistingIdClass() {
        Ticket only = mock(Ticket.class);
        when(only.getTicket_id()).thenReturn(1);
        TicketCollection.tickets.add(only);

        Ticket found = TicketCollection.getTicketInfo(999);

        assertNull(found);
    }

    @Test
    @DisplayName("BVT: getTicketInfo should return null for empty collection boundary")
    void bvtGetTicketInfoShouldReturnNullForEmptyCollectionBoundary() {
        Ticket found = TicketCollection.getTicketInfo(123);
        assertNull(found);
    }

    // ---------- getTicketsByFlight Tests ----------

    @Test
    @DisplayName("DTT: getTicketsByFlight should include only matching flight id branch")
    void dttGetTicketsByFlightShouldIncludeOnlyMatchingFlightIdBranch() {
        Flight flight7 = mock(Flight.class);
        Flight flight8 = mock(Flight.class);
        when(flight7.getFlightID()).thenReturn(7);
        when(flight8.getFlightID()).thenReturn(8);

        Ticket match1 = mockTicketWithFlight(1, flight7);
        Ticket match2 = mockTicketWithFlight(2, flight7);
        Ticket nonMatch = mockTicketWithFlight(3, flight8);
        TicketCollection.tickets.add(match1);
        TicketCollection.tickets.add(nonMatch);
        TicketCollection.tickets.add(match2);

        ArrayList<Ticket> result = TicketCollection.getTicketsByFlight(7);

        assertEquals(2, result.size());
        assertSame(match1, result.get(0));
        assertSame(match2, result.get(1));
    }

    @Test
    @DisplayName("DTT: getTicketsByFlight should ignore null flight branch")
    void dttGetTicketsByFlightShouldIgnoreNullFlightBranch() {
        Flight flight10 = mock(Flight.class);
        when(flight10.getFlightID()).thenReturn(10);

        Ticket noFlight = mockTicketWithFlight(1, null);
        Ticket withFlight = mockTicketWithFlight(2, flight10);
        TicketCollection.tickets.add(noFlight);
        TicketCollection.tickets.add(withFlight);

        ArrayList<Ticket> result = TicketCollection.getTicketsByFlight(10);

        assertEquals(1, result.size());
        assertSame(withFlight, result.get(0));
    }

    @Test
    @DisplayName("ET: getTicketsByFlight should return empty list for no-match class")
    void etGetTicketsByFlightShouldReturnEmptyListForNoMatchClass() {
        Flight flight = mock(Flight.class);
        when(flight.getFlightID()).thenReturn(2);
        Ticket t = mockTicketWithFlight(1, flight);
        TicketCollection.tickets.add(t);

        ArrayList<Ticket> result = TicketCollection.getTicketsByFlight(999);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("ET: getAllTickets should print each ticket representation once")
    void etGetAllTicketsShouldPrintEachTicketRepresentationOnce() {
        Ticket t1 = mock(Ticket.class);
        Ticket t2 = mock(Ticket.class);
        when(t1.toString()).thenReturn("ticket-111");
        when(t2.toString()).thenReturn("ticket-222");
        TicketCollection.tickets.add(t1);
        TicketCollection.tickets.add(t2);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));
        try {
            TicketCollection.getAllTickets();
        } finally {
            System.setOut(original);
        }

        String printed = out.toString();
        assertTrue(printed.contains("ticket-111"));
        assertTrue(printed.contains("ticket-222"));
    }
}