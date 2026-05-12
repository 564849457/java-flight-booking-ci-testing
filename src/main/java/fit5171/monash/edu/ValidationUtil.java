package fit5171.monash.edu;

import java.sql.Timestamp;

public class ValidationUtil {
    private ValidationUtil() {}

    public static String checkBlankString(String input, String fieldName){
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank.");
        }
        return input.trim();
    }

    public static int checkPositiveValue(int value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be greater than 0.");
        }
        return value;
    }

    public static int checkNonNegativeValue(int value, String fieldName) {
        if (value < 0) {
            throw new IllegalArgumentException(fieldName + " must be greater than 0.");
        }
        return value;
    }

    public static <T> T checkNull(T value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null.");
        }
        return value;
    }

    public static void checkDateOrder(Timestamp from, Timestamp to) {
        if (from != null && to != null && from.after(to)) {
            throw new IllegalArgumentException("DateFrom cannot be after DateTo.");
        }
    }

    public static String validateCityName(String city, String fieldName) {
        city = checkBlankString(city, fieldName);
        if (!city.matches("[A-Za-z ]+")) {
            throw new IllegalArgumentException(fieldName + " must contain only letters.");
        }
        return city;
    }
}