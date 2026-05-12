package fit5171.monash.edu;

import java.util.*;

public class FlightCollection {

    private FlightCollection() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static ArrayList<Flight> flights;

    public static ArrayList<Flight> getFlights() {
        return flights;
    }

    public static void addFlight(Flight flight) {
        if (flights == null) throw new IllegalStateException("Flight database has not been initialized.");
        if (flight == null) throw new IllegalStateException("flights input should not be null");

        for (Flight existingFlight : FlightCollection.flights){
            if (flight.getFlightID() == existingFlight.getFlightID()) throw new IllegalStateException("only distinct flight is allowed");
        }
            flights.add(flight);
    }

    public static void addFlights(ArrayList<Flight> flights) {
        if (FlightCollection.flights == null) throw new IllegalStateException("Flight database has not been initialized.");
        if (flights == null) throw new IllegalStateException("flights input should not be null");
        if (flights.isEmpty()) throw new IllegalStateException("flights input should not be empty");

        Set<Integer> incomingIds = new HashSet<>();
        for (Flight newFlight : flights) {
            if (!incomingIds.add(newFlight.getFlightID())) {
                throw new IllegalStateException("Incoming list contains duplicate flight IDs.");
            }
        }

        for (Flight newFlight : flights) {
            for (Flight existingFlight : FlightCollection.flights) {
                if (newFlight.getFlightID() == existingFlight.getFlightID())
                    throw new IllegalStateException("Only distinct flight is allowed.");
            }
        }
        FlightCollection.flights.addAll(flights);
    }

    public static Flight getFlightInfo(String city1, String city2) {
        //display the flights where there is a direct flight from city 1 to city2
        if (flights == null) {
            throw new IllegalStateException("Flight database has not been initialized.");
        }
        if (city1 == null || city2 == null || city1.isBlank() || city2.isBlank()){
            throw new IllegalArgumentException("Input cannot be blank or null");
        }
        if (city1.equals(city2)) throw new IllegalArgumentException("Cannot input two same cities");

        for (Flight flight : flights) {
            if (Objects.equals(flight.getDepartFrom(), city1) && Objects.equals(flight.getDepartTo(), city2)) {
                return flight;
            }
        }
        return null;
    }

    public static Flight getFlightInfo(String city) {
        //SELECT a flight where depart_to = city
        if (flights == null) {
            throw new IllegalStateException("Flight database has not been initialized.");
        }
        if (city == null || city.isBlank()){
            throw new IllegalArgumentException("Input cannot be blank or null");
        }

        for (Flight flight : flights) {
            if (Objects.equals(flight.getDepartTo(), city)) {
                return flight;
            }
        }
        return null;

    }

    public static Flight getFlightInfo(int flight_id) {
        //SELECT a flight with a particular flight id
        if (flights == null) {
            throw new IllegalStateException("Flight database has not been initialized.");
        }
        if (flight_id < 0) throw new IllegalArgumentException("flight_id cannot be negative");

        for (Flight flight : flights) {
            if (flight.getFlightID() == flight_id) {
                return flight;
            }
        }
        return null;
    }


}
