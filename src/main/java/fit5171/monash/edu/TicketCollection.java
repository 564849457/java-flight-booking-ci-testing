package fit5171.monash.edu;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class TicketCollection {

	private TicketCollection() {
		throw new UnsupportedOperationException("Utility class");
	}

	public static ArrayList<Ticket> tickets;

	public static ArrayList<Ticket> getTickets() {
		return tickets;
	}

	public static void addTickets(ArrayList<Ticket> tickets_db) {
		Objects.requireNonNull(tickets_db, "tickets_db cannot be null");

		Set<Integer> incomingIds = new HashSet<>();
		for (Ticket ticket : tickets_db) {
			ValidationUtil.checkNull(ticket, "ticket");

			int ticketId = ValidationUtil.checkPositiveValue(ticket.getTicket_id(), "ticket_id");
			ValidationUtil.checkNonNegativeValue(ticket.getPrice(), "price");
			ValidationUtil.checkNull(ticket.getFlight(), "flight");
			ValidationUtil.checkNull(ticket.getPassenger(), "passenger");

			if (incomingIds.contains(ticketId)) {
				throw new IllegalArgumentException("Duplicate ticket_id in input: " + ticketId);
			}
			incomingIds.add(ticketId);

			if (getTicketInfo(ticketId) != null) {
				throw new IllegalArgumentException("Duplicate ticket_id in collection: " + ticketId);
			}
		}

		TicketCollection.tickets.addAll(tickets_db);
	}
	
	public static void getAllTickets() {
    	//display all available tickets from the Ticket collection
		for ( Ticket ticket : tickets){
			System.out.println(ticket.toString());
		}
    }
	public static Ticket getTicketInfo(int ticket_id) {
    	//SELECT a ticket where ticket id = ticket_id
		for( Ticket ticket : tickets){
			if (ticket.getTicket_id() == ticket_id){
				return ticket;
			}
		}
    	return null;
    }

	public static ArrayList<Ticket> getTicketsByFlight(int flightId) {
		ArrayList<Ticket> result = new ArrayList<>();
		for (Ticket ticket : tickets) {
			if (ticket.getFlight() != null && ticket.getFlight().getFlightID() == flightId) {
				result.add(ticket);
			}
		}
		return result;
	}

}
