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

    private EnergyEntry energyEntry;
    private EnergyEntry energyEntry2;
    private FocusEntry focusEntry;
    private MotivationEntry motivationEntry;

    @BeforeEach
    void runBefore() {
        log = new ProductivityLog();

        energyEntry = new EnergyEntry(LocalDate.now(), LocalTime.of(5, 0), 9);
        energyEntry2 = new EnergyEntry(LocalDate.now(), LocalTime.of(5, 0), 9);
        focusEntry = new FocusEntry(LocalDate.now(), LocalTime.of(7, 0), 8);
        motivationEntry = new MotivationEntry(LocalDate.now(), LocalTime.of(8, 0), 10);

        ArrayList<ProductivityEntry> energy = createEnergyList();
        ArrayList<ProductivityEntry> focus = createFocusList();
        ArrayList<ProductivityEntry> motivation = createMotivationList();

        log2 = new ProductivityLog(energy, focus, motivation);
    }

    @Test
    void testConstructorNoParams() {
        assertTrue(log.isEmpty());
    }

    @Test
    void testConstructorTwoParams() {
        assertFalse(log2.isEmpty());
        assertEquals(createEnergyList(), log2.getEnergyEntries());
        assertEquals(createFocusList(), log2.getFocusEntries());
        assertEquals(createMotivationList() , log2.getMotivationEntries());
    }

    @Test
    void testAdd() {
        log.add(energyEntry);
        log.add(energyEntry2);
        assertEquals(2, log.getEnergyEntries().size());
        assertEquals(energyEntry, log.getEnergyEntries().get(0));
        assertEquals(energyEntry2, log.getEnergyEntries().get(1));

        log.add(focusEntry);
        assertEquals(1, log.getFocusEntries().size());
        assertEquals(focusEntry, log.getFocusEntries().get(0));

        log.add(motivationEntry);
        assertEquals(1, log.getMotivationEntries().size());
        assertEquals(motivationEntry, log.getMotivationEntries().get(0));
    }

    @Test
    void testRemoveInvalid() {
        assertFalse(log.remove(energyEntry));
        assertTrue(log.getEnergyEntries().isEmpty());
    }

    @Test
    void testRemoveGeneral() {
        log.add(energyEntry);
        log.add(energyEntry2);
        log.add(focusEntry);
        log.add(motivationEntry);

        assertTrue(log.remove(energyEntry));
        assertEquals(1, log.getEnergyEntries().size());

        assertTrue(log.remove(focusEntry));
        assertEquals(0, log.getFocusEntries().size());

        assertTrue(log.remove(motivationEntry));
        assertEquals(0, log.getMotivationEntries().size());
    }

    @Test
    void testGetAllEntries() {
        assertEquals(log2.getEnergyEntries().size() + log2.getFocusEntries().size()
                + log2.getMotivationEntries().size(), log2.getAllEntries().size());
    }

    @Test
    void testIsEmpty() {
        assertTrue(log.isEmpty());
    }

    // EFFECTS: creates and returns a sample energy list
    private ArrayList<ProductivityEntry> createEnergyList() {
        ArrayList<ProductivityEntry> list = new ArrayList<>();
        list.add(energyEntry);
        list.add(energyEntry2);
        return list;
    }

    // EFFECTS: creates and returns a sample focus list
    private ArrayList<ProductivityEntry> createFocusList() {
        ArrayList<ProductivityEntry> list = new ArrayList<>();
        list.add(motivationEntry);
        return list;
    }

    // EFFECTS: creates a sample motivation list
    private ArrayList<ProductivityEntry> createMotivationList() {
        ArrayList<ProductivityEntry> list = new ArrayList<>();
        list.add(focusEntry);
        return list;
    }
}
