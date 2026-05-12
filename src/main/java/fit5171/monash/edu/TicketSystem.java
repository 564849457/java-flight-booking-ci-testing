package fit5171.monash.edu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TicketSystem {

    private final Scanner in;

    public TicketSystem() {
        in = new Scanner(System.in);
    }

    public void chooseTicket(String city1, String city2) {
        String departureCity = ValidationUtil.validateCityName(city1, "Departure city");
        String destinationCity = ValidationUtil.validateCityName(city2, "Destination city");

        Flight directFlight = FlightCollection.getFlightInfo(departureCity, destinationCity);

        if (directFlight != null) {
            chooseDirectTicket(directFlight);
            return;
        }

        chooseTransferTicket(departureCity, destinationCity);
    }

    private void chooseDirectTicket(Flight directFlight) {
        ArrayList<Ticket> matchingTickets = TicketCollection.getTicketsByFlight(directFlight.getFlightID());

        if (matchingTickets.isEmpty()) {
            System.out.println("No tickets available for this flight.");
            return;
        }

        printTickets("Available tickets for this flight:", matchingTickets);

        Ticket selectedTicket = selectTicketForFlight(directFlight, "Enter ID of ticket you want to choose:");
        buyTicketDirect(selectedTicket.getTicket_id());
    }

    private void chooseTransferTicket(String departureCity, String destinationCity) {
        Flight secondLeg = FlightCollection.getFlightInfo(destinationCity);

        if (secondLeg == null) {
            System.out.println("There is no possible route.");
            return;
        }

        String connectCity = secondLeg.getDepartFrom();
        Flight firstLeg = FlightCollection.getFlightInfo(departureCity, connectCity);

        if (firstLeg == null) {
            System.out.println("There is no possible route.");
            return;
        }

        ArrayList<Ticket> firstLegTickets = TicketCollection.getTicketsByFlight(firstLeg.getFlightID());
        ArrayList<Ticket> secondLegTickets = TicketCollection.getTicketsByFlight(secondLeg.getFlightID());

        if (firstLegTickets.isEmpty() || secondLegTickets.isEmpty()) {
            System.out.println("No available tickets for the transfer route.");
            return;
        }

        printTransferRoute(departureCity, connectCity, destinationCity);

        printTickets("Available tickets for first leg (" + departureCity + " -> " + connectCity + "):",
                firstLegTickets);
        Ticket firstSelectedTicket = selectTicketForFlight(firstLeg, "Enter ID of ticket for first leg:");

        printTickets("Available tickets for second leg (" + connectCity + " -> " + destinationCity + "):",
                secondLegTickets);
        Ticket secondSelectedTicket = selectTicketForFlight(secondLeg, "Enter ID of ticket for second leg:");

        buyTicketTransfer(firstSelectedTicket.getTicket_id(), secondSelectedTicket.getTicket_id());
    }

    private void printTickets(String heading, List<Ticket> tickets) {
        System.out.println(heading);
        for (Ticket ticket : tickets) {
            System.out.println(ticket.simpleDisplay());
        }
    }

    private void printTransferRoute(String departureCity, String connectCity, String destinationCity) {
        System.out.println("No direct flight found. But there is a transfer route available.");
        System.out.println("First leg: " + departureCity + " -> " + connectCity);
        System.out.println("Second leg: " + connectCity + " -> " + destinationCity);
        System.out.println("Searching for tickets...");
    }

    private Ticket selectTicketForFlight(Flight flight, String prompt) {
        while (true) {
            int ticketId = getIntInput(prompt);
            Ticket selectedTicket = TicketCollection.getTicketInfo(ticketId);

            if (isSelectedTicketValidForFlight(selectedTicket, flight)) {
                return selectedTicket;
            }
        }
    }

    private boolean isSelectedTicketValidForFlight(Ticket selectedTicket, Flight flight) {
        if (selectedTicket == null) {
            System.out.println("This ticket does not exist.");
            return false;
        }

        if (selectedTicket.ticketStatus()) {
            System.out.println("This ticket has been purchased.");
            return false;
        }

        if (selectedTicket.getFlight() == null || selectedTicket.getFlight().getFlightID() != flight.getFlightID()) {
            System.out.println("This ticket does not belong to the selected flight.");
            return false;
        }

        return true;
    }

    public void showTicket(Ticket ticket) {
        if (ticket == null || ticket.getFlight() == null) {
            System.out.println("Ticket information is incomplete.");
            return;
        }

        System.out.println("You have bought a ticket for flight "
                + ticket.flight.getDepartFrom() + " - "
                + ticket.flight.getDepartTo() + "\n\nDetails:");
        System.out.println(ticket.toString());
    }

    public void buyTicketDirect(int ticketId) {
        Ticket validTicket = TicketCollection.getTicketInfo(ticketId);
        if (!isTicketReadyForPurchase(validTicket)) {
            return;
        }

        Flight flight = validTicket.getFlight();

        Passenger passenger;
        try {
            passenger = collectPassengerInfo();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        int purchaseChoice = getIntInput("Do you want to purchase?\n 1-YES 0-NO");
        if (purchaseChoice == 0) {
            return;
        }

        try {
            setCardInfo(passenger);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        Airplane airplane = flight.getAirplane();

        validTicket.setPassenger(passenger);

        if (!reserveSeat(validTicket, airplane)) {
            System.out.println("Purchase failed because no seat is available.");
            return;
        }

        validTicket.setTicketStatus(true);
        System.out.println("Your bill: " + validTicket.getPrice() + "\n");

        showTicket(validTicket);
    }

    public void buyTicketTransfer(int firstTicketId, int secondTicketId) {
        Ticket validTicketFirst = TicketCollection.getTicketInfo(firstTicketId);
        Ticket validTicketSecond = TicketCollection.getTicketInfo(secondTicketId);

        if (!isTicketReadyForPurchase(validTicketFirst) || !isTicketReadyForPurchase(validTicketSecond)) {
            return;
        }

        Flight firstFlight = validTicketFirst.getFlight();
        Flight secondFlight = validTicketSecond.getFlight();

        Passenger passenger;
        try {
            passenger = collectPassengerInfo();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        int purchaseChoice = getIntInput("Do you want to purchase?\n 1-YES 0-NO");
        if (purchaseChoice == 0) {
            return;
        }

        try {
            setCardInfo(passenger);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        Airplane planeFirst = firstFlight.getAirplane();
        Airplane planeSecond = secondFlight.getAirplane();

        if (!checkSeatAmount(validTicketFirst, planeFirst)) {
            System.out.println("No available seat for the first leg.");
            return;
        }

        if (!checkSeatAmount(validTicketSecond, planeSecond)) {
            System.out.println("No available seat for the second leg.");
            return;
        }

        reserveSeat(validTicketFirst, planeFirst);
        reserveSeat(validTicketSecond, planeSecond);

        validTicketFirst.setPassenger(passenger);
        validTicketFirst.setTicketStatus(true);

        validTicketSecond.setPassenger(passenger);
        validTicketSecond.setTicketStatus(true);

        int totalPrice = validTicketFirst.getPrice() + validTicketSecond.getPrice();

        System.out.println("First leg price: " + validTicketFirst.getPrice());
        System.out.println("Second leg price: " + validTicketSecond.getPrice());
        System.out.println("=================================");
        System.out.println("Total bill: " + totalPrice + "\n");
        showTicket(validTicketFirst);
        showTicket(validTicketSecond);
    }

    private boolean checkSeatAmount(Ticket ticket, Airplane airplane) {
        if (ticket == null || airplane == null) {
            return false;
        }

        if (ticket.getClassVip()) {
            return airplane.getBusinessSitsNumber() > 0;
        }

        return airplane.getEconomySitsNumber() > 0;
    }

    private boolean reserveSeat(Ticket ticket, Airplane airplane) {
        if (ticket == null || airplane == null) {
            return false;
        }

        if (ticket.getClassVip()) {
            int businessSeats = airplane.getBusinessSitsNumber();

            if (businessSeats <= 0) {
                System.out.println("No business class seats available.");
                return false;
            }

            airplane.setBusinessSitsNumber(businessSeats - 1);
            return true;
        }

        int economySeats = airplane.getEconomySitsNumber();

        if (economySeats <= 0) {
            System.out.println("No economy class seats available.");
            return false;
        }

        airplane.setEconomySitsNumber(economySeats - 1);
        return true;
    }

    private Passenger collectPassengerInfo() {
        Passenger passenger = new Passenger();

        System.out.println("Enter your First Name: ");
        passenger.setFirstName(in.nextLine().trim());

        System.out.println("Enter your Second Name: ");
        passenger.setSecondName(in.nextLine().trim());

        passenger.setAge(getIntInput("Enter your age: "));

        System.out.println("Enter your gender: ");
        passenger.setGender(in.nextLine().trim());

        System.out.println("Enter your e-mail address: ");
        passenger.setEmail(in.nextLine().trim());

        System.out.println("Enter your phone number (+7): ");
        passenger.setPhoneNumber(in.nextLine().trim());

        System.out.println("Enter your passport number: ");
        passenger.setPassport(in.nextLine().trim());

        return passenger;
    }

    private void setCardInfo(Passenger passenger) {
        System.out.println("Enter your card number:");
        passenger.setCardNumber(in.nextLine().trim());

        passenger.setSecurityCode(getIntInput("Enter your security code:"));
    }

    private int getIntInput(String prompt) {
        System.out.println(prompt);

        while (!in.hasNextInt()) {
            System.out.println("Invalid input. Please only enter numbers.");
            in.nextLine();
        }

        int value = in.nextInt();
        in.nextLine();
        return value;
    }

    private boolean isTicketReadyForPurchase(Ticket ticket) {
        if (ticket == null) {
            System.out.println("This ticket does not exist.");
            return false;
        }

        if (ticket.getFlight() == null) {
            System.out.println("Ticket information is incomplete.");
            return false;
        }

        if (ticket.ticketStatus()) {
            System.out.println("This ticket has been purchased.");
            return false;
        }

        return true;
    }
}