package persistence;

import model.ProductivityEntry;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkEntry(LocalDate date, LocalTime time, int level, ProductivityEntry entry) {
        assertEquals(date, entry.getDate());
        assertEquals(time, entry.getTime());
        assertEquals(level, entry.getLevel());
    }
}
