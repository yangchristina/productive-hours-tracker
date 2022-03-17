package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ProductivityLogTest {
    private ProductivityLog log;
    private ProductivityLog log2;

    private ProductivityEntry energyEntry;
    private ProductivityEntry energyEntry2;
    private ProductivityEntry focusEntry;
    private ProductivityEntry motivationEntry;

    @BeforeEach
    void runBefore() {
        log = new ProductivityLog();

        energyEntry = new ProductivityEntry(ProductivityEntry.Label.ENERGY, LocalDate.now(), LocalTime.of(5, 0), 9);
        energyEntry2 = new ProductivityEntry(ProductivityEntry.Label.ENERGY, LocalDate.now(), LocalTime.of(5, 0), 9);
        focusEntry = new ProductivityEntry(ProductivityEntry.Label.FOCUS, LocalDate.now(), LocalTime.of(7, 0), 8);
        motivationEntry = new ProductivityEntry(ProductivityEntry.Label.MOTIVATION, LocalDate.now(), LocalTime.of(8, 0), 10);

        log2 = new ProductivityLog(createEntriesList());
    }

    @Test
    void testConstructorNoParams() {
        assertTrue(log.isEmpty());
    }

    @Test
    void testConstructorTwoParams() {
        assertFalse(log2.isEmpty());
        assertEquals(createEntriesList(), log2.getEntries());
//        assertEquals(createFocusList(), log2.getFocusEntries());
//        assertEquals(createMotivationList() , log2.getMotivationEntries());
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
    void testRemoveInvalid() {
        assertFalse(log.remove(energyEntry));
        assertTrue(log.isEmpty());
    }

    @Test
    void testRemoveGeneral() {
        log.add(energyEntry);
        log.add(focusEntry);
        log.add(motivationEntry);

        assertTrue(log.remove(energyEntry));
        assertEquals(2, log.getEntries().size());

        assertTrue(log.remove(focusEntry));
        assertEquals(1, log.getEntries().size());

        assertTrue(log.remove(motivationEntry));
        assertTrue(log.isEmpty());
    }

//    @Test
//    void testGetAllEntries() {
//        assertEquals(log2.getEnergyEntries().size() + log2.getFocusEntries().size()
//                + log2.getMotivationEntries().size(), log2.getAllEntries().size());
//    }

    @Test
    void testIsEmpty() {
        assertTrue(log.isEmpty());
        log.add(focusEntry);
        assertFalse(log.isEmpty());
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

//    // EFFECTS: creates and returns a sample focus list
//    private ArrayList<ProductivityEntry> createFocusList() {
//        ArrayList<ProductivityEntry> list = new ArrayList<>();
//        list.add(motivationEntry);
//        return list;
//    }
//
//    // EFFECTS: creates a sample motivation list
//    private ArrayList<ProductivityEntry> createMotivationList() {
//        ArrayList<ProductivityEntry> list = new ArrayList<>();
//        list.add(focusEntry);
//        return list;
//    }
}
