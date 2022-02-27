package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductivityLogTest {
    private ProductivityLog log;

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
    }

    @Test
    void testConstructor() {
        assertTrue(log.getEnergyEntries().isEmpty());
        assertTrue(log.getFocusEntries().isEmpty());
        assertTrue(log.getMotivationEntries().isEmpty());
    }

    @Test
    void testAdd() {
        log.add(energyEntry);
        assertEquals(1, log.getEnergyEntries().size());
        assertEquals(energyEntry, log.getEnergyEntries().get(0));
        assertEquals(1, log.getEnergyAverages().getLog().size());

        log.add(focusEntry);
        assertEquals(1, log.getFocusEntries().size());
        assertEquals(focusEntry, log.getFocusEntries().get(0));
        assertEquals(1, log.getFocusAverages().getLog().size());

        log.add(motivationEntry);
        assertEquals(1, log.getMotivationEntries().size());
        assertEquals(motivationEntry, log.getMotivationEntries().get(0));
        assertEquals(1, log.getMotivationAverages().getLog().size());

        log.add(energyEntry);
        assertEquals(2, log.getEnergyEntries().size());
        assertEquals(1, log.getEnergyAverages().getLog().size());
        assertEquals(2, log.getEnergyAverages().getLog().get(energyEntry.getTime()).size());
    }

    @Test
    void testRemove() {
        log.add(energyEntry);
        log.add(energyEntry2);
        log.add(focusEntry);
        log.add(motivationEntry);

        assertTrue(log.remove(energyEntry));
        assertEquals(1, log.getEnergyEntries().size());
        assertEquals(1, log.getEnergyAverages().getLog().get(energyEntry.getTime()).size());

        assertTrue(log.remove(focusEntry));
        assertEquals(0, log.getFocusEntries().size());
        assertTrue(log.getFocusAverages().getLog().get(focusEntry.getTime()).isEmpty());

        assertTrue(log.remove(motivationEntry));
        assertEquals(0, log.getMotivationEntries().size());
        assertTrue(log.getMotivationAverages().getLog().get(motivationEntry.getTime()).isEmpty());
    }

}
