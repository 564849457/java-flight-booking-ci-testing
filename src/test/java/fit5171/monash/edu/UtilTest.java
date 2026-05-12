package fit5171.monash.edu;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {

    Timestamp timeTest = Timestamp.valueOf("2026-04-01 10:00:00");

    @Test
    @DisplayName("")
    void timeToString() {
        assertEquals("10:00:00", Util.timeToString(timeTest));
    }

    @Test
    void dateToString() {
        assertEquals("2026-04-01", Util.dateToString(timeTest));
    }
}