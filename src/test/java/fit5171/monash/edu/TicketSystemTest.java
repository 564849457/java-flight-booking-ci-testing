package fit5171.monash.edu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TicketSystemTest {

    @BeforeEach
    void setUp() {
        FlightCollection.flights = new ArrayList<>();
        TicketCollection.tickets = new ArrayList<>();
    }

    // ---------- Helper Methods ----------

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

    private Flight mockFlight(int flightId, String departTo, String departFrom, Airplane airplane) {
        Flight flight = mock(Flight.class);
        when(flight.getFlightID()).thenReturn(flightId);
        when(flight.getDepartTo()).thenReturn(departTo);
        when(flight.getDepartFrom()).thenReturn(departFrom);
        when(flight.getAirplane()).thenReturn(airplane);
        return flight;
    }

    private Ticket mockTicket(int ticketId, Flight flight, boolean purchased, boolean vip, String label) {
        Ticket ticket = mock(Ticket.class);
        when(ticket.getTicket_id()).thenReturn(ticketId);
        when(ticket.getFlight()).thenReturn(flight);
        when(ticket.ticketStatus()).thenReturn(purchased);
        when(ticket.getClassVip()).thenReturn(vip);
        when(ticket.simpleDisplay()).thenReturn(label);
        return ticket;
    }

    // ---------- chooseTicket Tests ----------

    @Test
    @DisplayName("BVT: chooseTicket should reject blank departure city boundary")
    void bvtChooseTicketShouldRejectBlankDepartureCityBoundary() {
        TicketSystem system = systemWithInput("");

        assertThrows(IllegalArgumentException.class, () -> system.chooseTicket(" ", "Sydney"));
    }

    @Test
    @DisplayName("ET: chooseTicket should reject city names from invalid format class")
    void etChooseTicketShouldRejectCityNamesFromInvalidFormatClass() {
        TicketSystem system = systemWithInput("");

        assertThrows(IllegalArgumentException.class, () -> system.chooseTicket("Melb0urne", "Sydney"));
    }

    @Test
    @DisplayName("DTT: chooseTicket should show purchased message when booked branch is selected first")
    void dttChooseTicketShouldShowPurchasedMessageWhenBookedBranchSelectedFirst() throws Exception {
        Airplane airplane = mock(Airplane.class);
        Flight direct = mockFlight(10, "Sydney", "Melbourne", airplane);
        FlightCollection.flights.add(direct);

        Ticket booked = mockTicket(1, direct, true, false, "booked-ticket");
        Ticket available = mockTicket(2, direct, false, false, "available-ticket");
        TicketCollection.tickets.add(booked);
        TicketCollection.tickets.add(available);

        String input = String.join("\n",
                "1",
                "2",
                "John",
                "Doe",
                "25",
                "M",
                "john@doe.com",
                "0412345678",
                "EF1234567",
                "0"
        ) + "\n";

        TicketSystem system = systemWithInput(input);
        String printed = captureOutput(() -> {
            try {
                system.chooseTicket("Melbourne", "Sydney");
            } catch (Exception e) {
                fail(e);
            }
        });

        assertTrue(printed.contains("This ticket has been purchased."));
    }

    // ---------- buyTicketDirect Tests ----------

    @Test
    @DisplayName("DTT: buyTicketDirect should reject incomplete ticket branch when flight is missing")
    void dttBuyTicketDirectShouldRejectIncompleteTicketBranchWhenFlightIsMissing() {
        Ticket brokenTicket = mockTicket(50, null, false, false, "broken-ticket");
        TicketCollection.tickets.add(brokenTicket);

        String input = String.join("\n",
                "John",
                "Doe",
                "25",
                "Man",
                "john@doe.com",
                "0412345678",
                "AB1234567",
                "0"
        ) + "\n";

        TicketSystem system = systemWithInput(input);
        String printed = captureOutput(() ->
                assertDoesNotThrow(() -> system.buyTicketDirect(50))
        );

        assertTrue(printed.contains("Ticket information is incomplete."));
    }

    @Test
    @DisplayName("ET: buyTicketDirect should print validation message for invalid passenger input class")
    void etBuyTicketDirectShouldPrintValidationMessageForInvalidPassengerInputClass() {
        Airplane airplane = mock(Airplane.class);
        Flight flight = mockFlight(20, "Sydney", "Melbourne", airplane);
        Ticket ticket = mockTicket(77, flight, false, false, "ticket-77");
        TicketCollection.tickets.add(ticket);

        String input = String.join("\n",
                "John",
                "Doe",
                "25",
                "Man",
                "invalid-email",
                "0412345678",
                "AB1234567",
                "0"
        ) + "\n";

        TicketSystem system = systemWithInput(input);
        String printed = captureOutput(() ->
                assertDoesNotThrow(() -> system.buyTicketDirect(77))
        );

        assertTrue(printed.contains("Invalid email format"));
    }

    @Test
    @DisplayName("ET: buyTicketDirect should print bill and flight summary for successful purchase class")
    void etBuyTicketDirectShouldPrintBillAndFlightSummaryForSuccessfulPurchaseClass() throws Exception {
        Airplane airplane = mock(Airplane.class);
        when(airplane.getEconomySitsNumber()).thenReturn(10);

        Flight flight = mockFlight(30, "Sydney", "Melbourne", airplane);
        Passenger originalPassenger = mock(Passenger.class);
        Ticket ticket = new Ticket(88, 1000, flight, false, originalPassenger);
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
        String printed = captureOutput(() -> {
            try {
                system.buyTicketDirect(88);
            } catch (Exception e) {
                fail(e);
            }
        });

        assertTrue(printed.contains("Your bill: 1120"));
        assertTrue(printed.contains("You have bought a ticket for flight Melbourne - Sydney"));
    }
}