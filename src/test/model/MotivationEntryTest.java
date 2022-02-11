package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MotivationEntryTest {
    private MotivationEntry entry;
    @BeforeEach
    void runBefore() {
        entry = new MotivationEntry(LocalDate.now(), LocalTime.now(), 5);
    }

    @Test
    void testLabel() {
        assertEquals("motivation", entry.label());
    }
}
