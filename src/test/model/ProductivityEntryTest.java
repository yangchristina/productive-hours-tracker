package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductivityEntryTest {
    private static final LocalDate DATE = LocalDate.now();
    private static final LocalTime TIME = LocalTime.now();

    private ProductivityEntry entry;

    @BeforeEach
    void runBefore() {
        entry = new ProductivityEntry(ProductivityEntry.Label.ENERGY, DATE, TIME, 5);
    }

    @Test
    void testConstructor() {
        assertEquals(5, entry.getLevel());
        assertEquals(DATE, entry.getDate());
        assertEquals(TIME, entry.getTime());
    }

    @Test
    void testLabel() {
        assertEquals(ProductivityEntry.Label.ENERGY, entry.getLabel());
    }

    @Test
    void testDescriptionWithoutKey() {
        assertEquals("ENERGY level of 5 at " + TIME + " on " + DATE
                + ".", entry.toString());
    }

    @Test
    void testDescriptionWithKey() {
        assertEquals("ENERGY level of 5 at " + TIME + " on " + DATE
                + ".", entry.toString());
    }

    @Test
    void testSetTime() {
        entry.setTime(LocalTime.of(4, 0));
        assertEquals(4, entry.getTime().getHour());
    }

    @Test
    void testSetLevel() {
        entry.setLevel(2);
        assertEquals(2, entry.getLevel());
    }

    @Test
    void testSetLabel() {
        entry.setLabel(ProductivityEntry.Label.MOTIVATION);
        assertEquals(ProductivityEntry.Label.MOTIVATION, entry.getLabel());

        entry.setLabel(ProductivityEntry.Label.FOCUS);
        assertEquals(ProductivityEntry.Label.FOCUS, entry.getLabel());

        entry.setLabel(ProductivityEntry.Label.ENERGY);
        assertEquals(ProductivityEntry.Label.ENERGY, entry.getLabel());
    }
}