package fit5171.monash.edu;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;


public class Util {

    private Util() {
        throw new UnsupportedOperationException("Utility class");
    }

    //TODO: add implementations

    public static String timeToString(Timestamp timestamp){
        return timestamp.toLocalDateTime()
                .format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public static String dateToString(Timestamp timestamp){
        return timestamp.toLocalDateTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
