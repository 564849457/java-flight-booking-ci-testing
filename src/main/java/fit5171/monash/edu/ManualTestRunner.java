package fit5171.monash.edu;

import java.sql.Timestamp;
import java.util.Scanner;

public class ManualTestRunner {

    private final Scanner scanner;

    public ManualTestRunner() {
        scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        ManualTestRunner runner = new ManualTestRunner();
        runner.run();
    }

    private void run() {
        System.out.println("Manual Testing Runner");
        System.out.println("1. Create Airplane");
        System.out.println("2. Create Flight");
        System.out.println("0. Exit");

        int choice = readInt("Please choose an option:");

        if (choice == 1) {
            createAirplaneFromInput();
        } else if (choice == 2) {
            createFlightFromInput();
        } else {
            System.out.println("Exit.");
        }
    }

    private void createAirplaneFromInput() {
        try {
            int airplaneId = readInt("Enter airplane ID:");
            String model = readString("Enter airplane model:");
            int businessSeats = readInt("Enter business seats:");
            int economySeats = readInt("Enter economy seats:");
            int crewSeats = readInt("Enter crew seats:");

            Airplane airplane = new Airplane(
                    airplaneId,
                    model,
                    businessSeats,
                    economySeats,
                    crewSeats
            );

            System.out.println("Airplane created successfully:");
            System.out.println(airplane);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    private void createFlightFromInput() {
        try {
            int airplaneId = readInt("Enter airplane ID:");
            String model = readString("Enter airplane model:");
            int businessSeats = readInt("Enter business seats:");
            int economySeats = readInt("Enter economy seats:");
            int crewSeats = readInt("Enter crew seats:");

            Airplane airplane = new Airplane(
                    airplaneId,
                    model,
                    businessSeats,
                    economySeats,
                    crewSeats
            );

            int flightId = readInt("Enter flight ID:");
            String departTo = readString("Enter destination city:");
            String departFrom = readString("Enter departure city:");
            String code = readString("Enter flight code:");
            String company = readString("Enter company:");
            String dateFromText = readString("Enter departure time, e.g. 2026-05-20 10:00:00:");
            String dateToText = readString("Enter arrival time, e.g. 2026-05-20 12:00:00:");

            Timestamp dateFrom = Timestamp.valueOf(dateFromText);
            Timestamp dateTo = Timestamp.valueOf(dateToText);

            Flight flight = new Flight(
                    flightId,
                    departTo,
                    departFrom,
                    code,
                    company,
                    dateFrom,
                    dateTo,
                    airplane
            );

            System.out.println("Flight created successfully:");
            System.out.println(flight);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    private String readString(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine().trim();
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.println(prompt);

            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            }

            System.out.println("Invalid input. Please enter an integer.");
            scanner.nextLine();
        }
    }
}