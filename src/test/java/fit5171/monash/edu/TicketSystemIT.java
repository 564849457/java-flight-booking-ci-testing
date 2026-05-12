package fit5171.monash.edu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TicketSystemIT {

    @BeforeEach
    void setUp() {
        FlightCollection.flights = new ArrayList<>();
        TicketCollection.tickets = new ArrayList<>();
    }

    private TicketSystem systemWithInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        return new TicketSystem();
    }

    private String captureOutput(Runnable action) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));
        try {
            action.run();
        } finally {
            System.setOut(originalOut);
        }
        return out.toString();
    }

    private Airplane buildAirplane(int id, int businessSeats, int economySeats, int crewSeats) {
        return new Airplane(id, "B737-800", businessSeats, economySeats, crewSeats);
    }

    private Flight buildFlight(
            int flightId,
            String departFrom,
            String departTo,
            Airplane airplane
    ) {
        Timestamp from = Timestamp.valueOf("2026-05-01 08:00:00");
        Timestamp to = Timestamp.valueOf("2026-05-01 10:00:00");
        return new Flight(flightId, departTo, departFrom, "QF" + flightId, "Qantas", from, to, airplane);
    }

    private Passenger buildSeedPassenger() {
        return new Passenger("Seed", "User", 25, "Man", "seed@user.com", "0412345678", "AB1234567", "1234123412341234", 111);
    }

    @Test
    @DisplayName("IT: buyTicketDirect should update ticket and seat state with real objects")
    void itBuyTicketDirectShouldUpdateTicketAndSeatStateWithRealObjects() {
        Airplane plane = buildAirplane(1, 5, 2, 3);
        Flight flight = buildFlight(101, "Melbourne", "Sydney", plane);
        Ticket ticket = new Ticket(9001, 1000, flight, false, buildSeedPassenger());
        TicketCollection.tickets.add(ticket);

        String input = String.join("\n",
                "John",
                "Doe",
                "30",
                "Man",
                "john@doe.com",
                "0412345678",
                "AB1234567",
                "1",
                "1234567812345678",
                "123"
        ) + "\n";

        TicketSystem system = systemWithInput(input);
        String printed = captureOutput(() -> assertDoesNotThrow(() -> system.buyTicketDirect(9001)));

        assertTrue(ticket.ticketStatus());
        assertEquals(1, plane.getEconomySitsNumber());
        assertEquals("John", ticket.getPassenger().getFirstName());
        assertTrue(printed.contains("Your bill: 1120"));
    }

    @Test
    @DisplayName("IT: chooseTicket should complete transfer purchase with real collections")
    void itChooseTicketShouldCompleteTransferPurchaseWithRealCollections() {
        Airplane firstPlane = buildAirplane(2, 4, 2, 3);
        Airplane secondPlane = buildAirplane(3, 4, 3, 3);

        Flight firstLeg = buildFlight(201, "Melbourne", "Adelaide", firstPlane);
        Flight secondLeg = buildFlight(202, "Adelaide", "Sydney", secondPlane);
        FlightCollection.flights.add(firstLeg);
        FlightCollection.flights.add(secondLeg);

        Ticket firstLegTicket = new Ticket(3001, 500, firstLeg, false, buildSeedPassenger());
        Ticket secondLegTicket = new Ticket(3002, 700, secondLeg, false, buildSeedPassenger());
        TicketCollection.tickets.add(firstLegTicket);
        TicketCollection.tickets.add(secondLegTicket);

        String input = String.join("\n",
                "3001",
                "3002",
                "Alice",
                "Ng",
                "28",
                "Woman",
                "alice@ng.com",
                "0412345678",
                "AB1234567",
                "1",
                "1234567812345678",
                "456"
        ) + "\n";

        TicketSystem system = systemWithInput(input);
        String printed = captureOutput(() -> assertDoesNotThrow(() -> system.chooseTicket("Melbourne", "Sydney")));

        assertTrue(firstLegTicket.ticketStatus());
        assertTrue(secondLegTicket.ticketStatus());
        assertEquals(1, firstPlane.getEconomySitsNumber());
        assertEquals(2, secondPlane.getEconomySitsNumber());
        assertSame(firstLegTicket.getPassenger(), secondLegTicket.getPassenger());
        assertTrue(printed.contains("No direct flight found. But there is a transfer route available."));
    }

    @Test
    @DisplayName("IT: buyTicketDirect should keep ticket unpurchased when no seat available")
    void itBuyTicketDirectShouldKeepTicketUnpurchasedWhenNoSeatAvailable() {
        Airplane plane = buildAirplane(4, 2, 0, 2);
        Flight flight = buildFlight(301, "Melbourne", "Sydney", plane);
        Ticket ticket = new Ticket(4001, 800, flight, false, buildSeedPassenger());
        TicketCollection.tickets.add(ticket);

        String input = String.join("\n",
                "Tom",
                "Lee",
                "33",
                "Man",
                "tom@lee.com",
                "0412345678",
                "AB1234567",
                "1",
                "1234567812345678",
                "789"
        ) + "\n";

        TicketSystem system = systemWithInput(input);
        String printed = captureOutput(() -> assertDoesNotThrow(() -> system.buyTicketDirect(4001)));

        assertFalse(ticket.ticketStatus());
        assertEquals(0, plane.getEconomySitsNumber());
        assertTrue(printed.contains("Purchase failed because no seat is available."));
    }
}
