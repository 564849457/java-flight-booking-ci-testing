package fit5171.monash.edu;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
public class Main {

    private static final String DEFAULT_AIRLINE = "DemoAir";

    public static void main(String[] args) throws Exception {
        initializeCollections();
        loadSampleData();

        Scanner menuIn = new Scanner(System.in);
        TicketSystem ticketSystem = new TicketSystem();

        while (true) {
            printMainMenu();
            int choice = readInt(menuIn, "Choose an option: ");
            menuIn.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.println("Enter origin city (e.g. New York): ");
                    String origin = menuIn.nextLine().trim();
                    System.out.println("Enter destination city (e.g. London): ");
                    String destination = menuIn.nextLine().trim();
                    if (origin.isEmpty() || destination.isEmpty()) {
                        System.out.println("Origin and destination cannot be empty.");
                    } else {
                        ticketSystem.chooseTicket(origin, destination);
                    }
                }
                case 2 -> listScheduledFlights();
                case 0 -> {
                    System.out.println("Goodbye.");
                    return;
                }
                default -> System.out.println("Unknown option. Try again.");
            }
        }
    }

    private static void printMainMenu() {
        System.out.println();
        System.out.println("========== Flight ticket system ==========");
        System.out.println("  1  Book a ticket (search route & purchase)");
        System.out.println("  2  View scheduled flights");
        System.out.println("  0  Exit");
        System.out.println("==========================================");
    }

    private static int readInt(Scanner scanner, String prompt) {
        System.out.println(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Enter a whole number.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static void initializeCollections() {
        FlightCollection.flights = new ArrayList<>();
        TicketCollection.tickets = new ArrayList<>();
    }

    /**
     * Loads demo flights and tickets. Flight order matters for transfer routes into the same city:
     * flights that should act as the “into hub” leg for a connection should appear before other
     * flights with the same {@code departTo}.
     */
    private static void loadSampleData() {
        Calendar cal = Calendar.getInstance();

        Airplane planeNyLon = new Airplane(1, "B737-800", 8, 120, 6);
        Airplane planeMelDxb = new Airplane(2, "A350", 10, 200, 8);
        Airplane planeDxbLon = new Airplane(3, "B787", 10, 180, 8);

        Timestamp t0 = new Timestamp(cal.getTimeInMillis());
        cal.add(Calendar.HOUR, 14);
        Timestamp t1 = new Timestamp(cal.getTimeInMillis());

        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Timestamp tMelStart = new Timestamp(cal.getTimeInMillis());
        cal.add(Calendar.HOUR, 13);
        Timestamp tMelEnd = new Timestamp(cal.getTimeInMillis());

        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 2);
        Timestamp tDxbStart = new Timestamp(cal.getTimeInMillis());
        cal.add(Calendar.HOUR, 8);
        Timestamp tDxbEnd = new Timestamp(cal.getTimeInMillis());

        // Transfer example: Melbourne -> London via Dubai (must be listed before NY-London so
        // getFlightInfo("London") picks the Dubai leg as the incoming segment for connections).
        Flight melToDubai = new Flight(
                201,
                "Dubai",
                "Melbourne",
                "ME-DX001",
                DEFAULT_AIRLINE,
                tMelStart,
                tMelEnd,
                planeMelDxb
        );
        Flight dubaiToLondon = new Flight(
                202,
                "London",
                "Dubai",
                "DX-LN001",
                DEFAULT_AIRLINE,
                tDxbStart,
                tDxbEnd,
                planeDxbLon
        );
        Flight nyToLondon = new Flight(
                101,
                "London",
                "New York",
                "NY-LN001",
                DEFAULT_AIRLINE,
                t0,
                t1,
                planeNyLon
        );

        ArrayList<Flight> flights = new ArrayList<>();
        flights.add(melToDubai);
        flights.add(dubaiToLondon);
        flights.add(nyToLondon);
        FlightCollection.addFlights(flights);

        Passenger placeholder = null;

        ArrayList<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket(1001, 450_000, nyToLondon, false, placeholder));
        tickets.add(new Ticket(1002, 980_000, nyToLondon, true, placeholder));
        tickets.add(new Ticket(2001, 520_000, melToDubai, false, placeholder));
        tickets.add(new Ticket(2002, 1_100_000, melToDubai, true, placeholder));
        tickets.add(new Ticket(3001, 380_000, dubaiToLondon, false, placeholder));
        tickets.add(new Ticket(3002, 920_000, dubaiToLondon, true, placeholder));
        TicketCollection.addTickets(tickets);
    }

    private static void listScheduledFlights() {
        System.out.println();
        System.out.println("--- Scheduled flights ---");
        if (FlightCollection.flights == null || FlightCollection.flights.isEmpty()) {
            System.out.println("(none)");
            return;
        }
        for (Flight f : FlightCollection.flights) {
            System.out.println(
                    "Flight " + f.getFlightID()
                            + "  " + f.getDepartFrom()
                            + " -> " + f.getDepartTo()
                            + "  |  " + f.getCode()
                            + "  |  " + f.getCompany()
            );
        }
    }
}
