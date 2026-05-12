package fit5171.monash.edu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketTest {

    Ticket ticket;
    Flight flight;
    Passenger passenger;

    @BeforeEach
    void setUp() {
        flight = mock(Flight.class);
        passenger = mock(Passenger.class);
        ticket = new Ticket();
    }

    // ---------- Constructor Tests ----------

    @Test
    @DisplayName("ET: default constructor should create ticket with default state")
    void etDefaultConstructorShouldCreateTicketWithDefaultState() {
        when(passenger.getAge()).thenReturn(24);

        Ticket localTicket = new Ticket();

        assertNotNull(localTicket);
        // default constructor currently leaves the id unset; business validation should happen when assigning a real ticket id
        assertEquals(0, localTicket.getTicket_id());
        assertEquals(0, localTicket.getPrice());
        assertNull(localTicket.getFlight());
        assertFalse(localTicket.getClassVip());
        assertFalse(localTicket.ticketStatus());
        assertNull(localTicket.getPassenger());
    }

    @Test
    @DisplayName("ET: parameterized constructor should map valid inputs to ticket state")
    void etParameterizedConstructorShouldMapValidInputsToTicketState() {
        when(passenger.getAge()).thenReturn(24);

        Ticket localTicket = new Ticket(101, 500, flight, true, passenger);

        assertNotNull(localTicket);
        assertEquals(101, localTicket.getTicket_id());
        assertEquals(560, localTicket.getPrice());
        assertEquals(flight, localTicket.getFlight());
        assertTrue(localTicket.getClassVip());
        assertFalse(localTicket.ticketStatus());
        assertEquals(passenger, localTicket.getPassenger());
    }

    // ---------- Ticket ID Tests ----------

    @Test
    @DisplayName("ET: setTicket_id should accept representative valid id")
    void etSetTicketIdShouldAcceptRepresentativeValidId() {
        ticket.setTicket_id(2001);
        assertEquals(2001, ticket.getTicket_id());
    }

    @Test
    @DisplayName("BVT: setTicket_id should accept lower boundary value 1")
    void bvtSetTicketIdShouldAcceptLowerBoundaryValueOne() {
        ticket.setTicket_id(1);
        assertEquals(1, ticket.getTicket_id());
    }

    @Test
    @DisplayName("ET: setTicket_id should accept large valid id")
    void etSetTicketIdShouldAcceptLargeValidId() {
        ticket.setTicket_id(999999);
        assertEquals(999999, ticket.getTicket_id());
    }

    @Test
    @DisplayName("BVT: setTicket_id should reject boundary value 0")
    void bvtSetTicketIdShouldRejectBoundaryValueZero() {
        assertThrows(IllegalArgumentException.class, () -> ticket.setTicket_id(0));
    }

    @Test
    @DisplayName("ET: setTicket_id should reject invalid negative class")
    void etSetTicketIdShouldRejectInvalidNegativeClass() {
        assertThrows(IllegalArgumentException.class, () -> ticket.setTicket_id(-1));
    }

    // ---------- Price Tests ----------

    @ParameterizedTest
    @CsvSource({
            "24, 112",
            "10, 56",
            "14, 56",
            "15, 112",
            "59, 112",
            "60, 0"
    })
    @DisplayName("ET/BVT: setPrice should calculate ticket price based on passenger age")
    void setPriceShouldCalculateTicketPriceBasedOnPassengerAge(int age, int expectedPrice) {
        when(passenger.getAge()).thenReturn(age);
        ticket.setPassenger(passenger);

        ticket.setPrice(100);

        assertEquals(expectedPrice, ticket.getPrice());
    }

    @Test
    @DisplayName("ET: getPrice should return base price when passenger is null")
    void etGetPriceShouldReturnBasePriceWhenPassengerIsNull() {
        ticket.setPassenger(null);
        ticket.setPrice(100);

        assertEquals(100, ticket.getPrice());
    }

    @Test
    @DisplayName("BVT: setPrice should accept boundary value 0")
    void bvtSetPriceShouldAcceptBoundaryValueZero() {
        when(passenger.getAge()).thenReturn(24);
        ticket.setPassenger(passenger);

        ticket.setPrice(0);

        assertTrue(ticket.getPrice() >= 0);
    }

    @Test
    @DisplayName("ET: setPrice should reject invalid negative class")
    void etSetPriceShouldRejectInvalidNegativeClass() {
        when(passenger.getAge()).thenReturn(24);
        ticket.setPassenger(passenger);

        assertThrows(IllegalArgumentException.class, () -> ticket.setPrice(-100));
    }

    // ---------- Flight Tests ----------

    @Test
    @DisplayName("ET: setFlight should accept valid flight reference")
    void etSetFlightShouldAcceptValidFlightReference() {
        ticket.setFlight(flight);
        assertEquals(flight, ticket.getFlight());
    }

    @Test
    @DisplayName("ET: setFlight should accept null flight class")
    void etSetFlightShouldAcceptNullFlightClass() {
        ticket.setFlight(null);
        assertNull(ticket.getFlight());
    }

    // ---------- VIP Class Tests ----------

    @Test
    @DisplayName("DTT: setClassVip should store true branch")
    void dttSetClassVipShouldStoreTrueBranch() {
        ticket.setClassVip(true);
        assertTrue(ticket.getClassVip());
    }

    @Test
    @DisplayName("DTT: setClassVip should store false branch")
    void dttSetClassVipShouldStoreFalseBranch() {
        ticket.setClassVip(false);
        assertFalse(ticket.getClassVip());
    }

    // ---------- Ticket Status Tests ----------

    @Test
    @DisplayName("ET: ticket status should default to available")
    void etTicketStatusShouldDefaultToAvailable() {
        Ticket localTicket = new Ticket(100, 300, flight, false, passenger);
        assertFalse(localTicket.ticketStatus());
    }

    @Test
    @DisplayName("DTT: setTicketStatus should store booked branch")
    void dttSetTicketStatusShouldStoreBookedBranch() {
        ticket.setTicketStatus(true);
        assertTrue(ticket.ticketStatus());
    }

    @Test
    @DisplayName("DTT: setTicketStatus should store available branch")
    void dttSetTicketStatusShouldStoreAvailableBranch() {
        ticket.setTicketStatus(false);
        assertFalse(ticket.ticketStatus());
    }

    // ---------- Passenger Tests ----------

    @Test
    @DisplayName("ET: setPassenger should accept valid passenger reference")
    void etSetPassengerShouldAcceptValidPassengerReference() {
        ticket.setPassenger(passenger);
        assertEquals(passenger, ticket.getPassenger());
    }

    @Test
    @DisplayName("ET: setPassenger should accept null passenger class")
    void etSetPassengerShouldAcceptNullPassengerClass() {
        ticket.setPassenger(null);
        assertNull(ticket.getPassenger());
    }
}