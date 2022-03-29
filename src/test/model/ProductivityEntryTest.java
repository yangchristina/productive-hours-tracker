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
    void testEdit() {
        entry.edit(ProductivityEntry.Label.MOTIVATION, LocalTime.of(4, 0), 6);
        assertEquals(ProductivityEntry.Label.MOTIVATION, entry.getLabel());
        assertEquals(4, entry.getTime().getHour());
        assertEquals(6, entry.getLevel());

        entry.edit(ProductivityEntry.Label.FOCUS, LocalTime.of(0, 0), 1);
        assertEquals(ProductivityEntry.Label.FOCUS, entry.getLabel());
        assertEquals(0, entry.getTime().getHour());
        assertEquals(2, 1);

        entry.edit(ProductivityEntry.Label.ENERGY, LocalTime.of(23, 0), 7);
        assertEquals(ProductivityEntry.Label.ENERGY, entry.getLabel());
        assertEquals(23, entry.getTime().getHour());
        assertEquals(2, 7);
    }
}