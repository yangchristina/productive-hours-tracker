package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ProductivityLogTest {
    private static final User user = new User("test");

    private ProductivityLog log;
    private ProductivityLog log2;

    private ProductivityEntry energyEntry;
    private ProductivityEntry energyEntry2;
    private ProductivityEntry focusEntry;
    private ProductivityEntry motivationEntry;

    @BeforeEach
    void runBefore() {
        log = new ProductivityLog(user);

        energyEntry = new ProductivityEntry(ProductivityEntry.Label.ENERGY, LocalDate.now(), LocalTime.of(5, 0), 8);
        energyEntry2 = new ProductivityEntry(ProductivityEntry.Label.ENERGY, LocalDate.now(), LocalTime.of(5, 0), 4);
        focusEntry = new ProductivityEntry(ProductivityEntry.Label.FOCUS, LocalDate.now(), LocalTime.of(7, 0), 8);
        motivationEntry = new ProductivityEntry(ProductivityEntry.Label.MOTIVATION, LocalDate.now(), LocalTime.of(8, 0), 10);

        log2 = new ProductivityLog(user, createEntriesList());
    }

    @Test
    void testConstructorNoParams() {
        assertTrue(log.isEmpty());
        assertNotNull(log.getDailyAverageLog().getLog());
        assertEquals(user, log.getUser());
    }

    @Test
    void testConstructorTwoParams() {
        assertFalse(log2.isEmpty());
        assertEquals(createEntriesList(), log2.getEntries());
        assertNotNull(log.getDailyAverageLog());
        assertEquals(user, log.getUser());
    }

    @Test
    void testAdd() {
        log.add(energyEntry);
        log.add(energyEntry2);
        assertEquals(2, log.getEntries().size());
        assertEquals(energyEntry, log.getEntries().get(0));
        assertEquals(energyEntry2, log.getEntries().get(1));

        log.add(focusEntry);
        assertEquals(3, log.getEntries().size());
        assertEquals(focusEntry, log.getEntries().get(2));

        log.add(motivationEntry);
        assertEquals(4, log.getEntries().size());
        assertEquals(motivationEntry, log.getEntries().get(3));
    }

    @Test
    void testRemove() {
        // initial size of log 2 is 4
        assertNotNull(log2.remove(energyEntry));
        assertEquals(3, log2.getEntries().size());

        assertNull(log2.remove(energyEntry2));
        assertEquals(2, log2.getEntries().size());

        assertNull(log2.remove(focusEntry));
        assertEquals(1, log2.getEntries().size());

        assertNull(log2.remove(motivationEntry));
        assertTrue(log2.isEmpty());
    }

    @Test
    void testIsEmpty() {
        assertTrue(log.isEmpty());
        log.add(focusEntry);
        assertFalse(log.isEmpty());
    }

    @Test
    void testUpdate() {
        log.add(energyEntry);
        log.update(focusEntry, energyEntry);
        Double oldLevel = log.getDailyAverageLog().getLog().get(ProductivityEntry.Label.FOCUS).get(energyEntry2.getTime());
        Double newLevel = log.getDailyAverageLog().getLog().get(ProductivityEntry.Label.FOCUS).get(focusEntry.getTime());
        assertNull(oldLevel);
        assertEquals(focusEntry.getLevel(), newLevel);

        boolean isContained = false;
        for (Event e : EventLog.getInstance()) {
            if (e.getDescription().equals(user.getName() + " edited entry: \n" + energyEntry + " –––>\n" + focusEntry)) {
                isContained = true;
                break;
            }
        }
        assertTrue(isContained);
    }

    // EFFECTS: creates and returns a sample energy list
    private ArrayList<ProductivityEntry> createEntriesList() {
        ArrayList<ProductivityEntry> list = new ArrayList<>();
        list.add(energyEntry);
        list.add(energyEntry2);

        list.add(motivationEntry);
        list.add(focusEntry);
        return list;
    }
}
