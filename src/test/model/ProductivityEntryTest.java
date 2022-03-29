package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class ProductivityEntryTest {
    private static final LocalDate DATE = LocalDate.now();
    private static final LocalTime TIME = LocalTime.now();
    private static final User user = new User("test");

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
        ProductivityLog log = new ProductivityLog(user);
        log.add(entry);

        LocalTime time1 = LocalTime.of(4, 0);
        LocalTime time2 = LocalTime.of(0, 0);

        entry.edit(ProductivityEntry.Label.MOTIVATION, time1, 6);
        assertEquals(ProductivityEntry.Label.MOTIVATION, entry.getLabel());
        assertEquals(4, entry.getTime().getHour());
        assertEquals(6, entry.getLevel());

        entry.edit(ProductivityEntry.Label.FOCUS, LocalTime.of(0, 0), 1);
        assertEquals(ProductivityEntry.Label.FOCUS, entry.getLabel());
        assertEquals(0, entry.getTime().getHour());
        assertEquals(1, entry.getLevel());

        entry.edit(ProductivityEntry.Label.ENERGY, LocalTime.of(23, 0), 7);
        assertEquals(ProductivityEntry.Label.ENERGY, entry.getLabel());
        assertEquals(23, entry.getTime().getHour());
        assertEquals(7, entry.getLevel());

        assertEquals(entry.getLevel(), log.getDailyAverageLog().getLog().get(entry.getLabel()).get(entry.getTime()));
        assertNull(log.getDailyAverageLog().getLog().get(ProductivityEntry.Label.MOTIVATION).get(time1));
        assertNull(log.getDailyAverageLog().getLog().get(ProductivityEntry.Label.FOCUS).get(time2));
    }
}