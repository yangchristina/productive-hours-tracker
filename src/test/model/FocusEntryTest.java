package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FocusEntryTest {
    private FocusEntry entry;
    @BeforeEach
    void runBefore() {
        entry = new FocusEntry(LocalDate.now(), LocalTime.now(), 5);
    }

    @Test
    void testLabel() {
        assertEquals("focus", entry.label());
    }
}
