package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnergyEntryTest {
    private static final LocalDate DATE = LocalDate.now();
    private static final LocalTime TIME = LocalTime.now();

    private EnergyEntry entry;

    @BeforeEach
    void runBefore() {
        entry = new EnergyEntry(DATE, TIME, 5);
    }

    @Test
    void testConstructor() {
        assertEquals(5, entry.getLevel());
        assertEquals(DATE, entry.getDate());
        assertEquals(TIME, entry.getTime());
    }

    @Test
    void testLabel() {
        assertEquals("energy", entry.getLabel());
    }

    @Test
    void testDescriptionWithoutKey() {
        assertEquals("energy level of 5 at " + TIME + " on " + DATE
                + ".", entry.description());
    }

    @Test
    void testDescriptionWithKey() {
        assertEquals("energy level of 5 at " + TIME + " on " + DATE
                + ". Key: 0", entry.description(0));
    }

    @Test
    void testEditTime() {
        entry.editTime(LocalTime.of(4, 0));
        assertEquals(4, entry.getTime().getHour());
    }

    @Test
    void testEditLevel() {
        entry.editLevel(2);
        assertEquals(2, entry.getLevel());
    }
}