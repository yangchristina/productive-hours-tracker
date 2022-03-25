package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class DailyAverageLogTest {

    private DailyAverageLog log;
    private DailyAverageLog log2;

    private ProductivityEntry energyEntry;
    private ProductivityEntry focusEntry;
    private ProductivityEntry motivationEntry;

    @BeforeEach
    void runBefore() {
        energyEntry = new ProductivityEntry(ProductivityEntry.Label.ENERGY, LocalDate.now(),LocalTime.of(21, 0), 1);
        focusEntry = new ProductivityEntry(ProductivityEntry.Label.FOCUS, LocalDate.now(),LocalTime.of(22, 0), 5);
        motivationEntry = new ProductivityEntry(ProductivityEntry.Label.MOTIVATION, LocalDate.now(),LocalTime.of(23, 0), 6);

        log = new DailyAverageLog();
        log2 = new DailyAverageLog(createEntriesList());
    }

    @Test
    void testConstructorNoParams() {
        for (ProductivityEntry.Label label : ProductivityEntry.Label.values()) {
            assertTrue(log.getLog().get(label).isEmpty());
        }
    }

    @Test
    void testConstructorWithParams() {
        for (ProductivityEntry.Label label : ProductivityEntry.Label.values()) {
            assertEquals(1, log2.getLog().get(label).size());
        }
        assertEquals(energyEntry.getLevel(), log2.getLog().get(ProductivityEntry.Label.ENERGY).get(energyEntry.getTime()));
        assertEquals(focusEntry.getLevel(), log2.getLog().get(ProductivityEntry.Label.FOCUS).get(focusEntry.getTime()));
        assertEquals(motivationEntry.getLevel(), log2.getLog().get(ProductivityEntry.Label.MOTIVATION).get(motivationEntry.getTime()));
    }

    @Test
    void testAdd() {
        log.add(energyEntry);
        log.add(focusEntry);
        log.add(motivationEntry);

        for (ProductivityEntry.Label label : ProductivityEntry.Label.values()) {
            assertEquals(1, log.getLog().get(label).size());
        }
        assertEquals(energyEntry.getLevel(), log.getLog().get(ProductivityEntry.Label.ENERGY).get(energyEntry.getTime()));
        assertEquals(focusEntry.getLevel(), log.getLog().get(ProductivityEntry.Label.FOCUS).get(focusEntry.getTime()));
        assertEquals(motivationEntry.getLevel(), log.getLog().get(ProductivityEntry.Label.MOTIVATION).get(motivationEntry.getTime()));

        LocalTime time = LocalTime.of(0, 0);
        ProductivityEntry.Label label = ProductivityEntry.Label.ENERGY;
        log.add(new ProductivityEntry(label, LocalDate.now(), time, 0));
        assertEquals(0, log.getLog().get(label).get(time));
        log.add(new ProductivityEntry(label, LocalDate.now(), time, 2));
        assertEquals(1, log.getLog().get(label).get(time));

        time = LocalTime.of(1, 0);
        label = ProductivityEntry.Label.FOCUS;
        log.add(new ProductivityEntry(label, LocalDate.now(), time, 1));
        assertEquals(1, log.getLog().get(label).get(time));
        log.add(new ProductivityEntry(label, LocalDate.now(), time, 3));
        assertEquals(2, log.getLog().get(label).get(time));
        log.add(new ProductivityEntry(label, LocalDate.now(), time, 5));
        assertEquals(3, log.getLog().get(label).get(time));

        time = LocalTime.of(2, 0);
        label = ProductivityEntry.Label.MOTIVATION;
        log.add(new ProductivityEntry(label, LocalDate.now(), time, 0));
        assertEquals(0, log.getLog().get(label).get(time));
        log.add(new ProductivityEntry(label, LocalDate.now(), time, 1));
        assertEquals(0.5, log.getLog().get(label).get(time));
    }

    @Test
    void testRemove() {
        LocalTime time = LocalTime.of(0, 0);
        ProductivityEntry.Label label = ProductivityEntry.Label.ENERGY;

        ProductivityEntry entry1 = new ProductivityEntry(label, LocalDate.now(), time, 0);
        ProductivityEntry entry2 = new ProductivityEntry(label, LocalDate.now(), time, 2);

        log.add(entry1);
        log.add(entry2);
        assertEquals(2, log.remove(entry1));
        assertNull(log.remove(entry2));

        assertNull(log2.remove(energyEntry));
        assertNull(log2.remove(focusEntry));
        assertNull(log2.remove(motivationEntry));
    }

    // EFFECTS: constructs and returns a sample energy list
    private ArrayList<ProductivityEntry> createEntriesList() {
        ArrayList<ProductivityEntry> list = new ArrayList<>();

        list.add(energyEntry);
        list.add(focusEntry);
        list.add(motivationEntry);

        return list;
    }
}