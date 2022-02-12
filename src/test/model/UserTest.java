package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User user;
    private EnergyEntry energyEntry;
    private EnergyEntry energyEntry2;
    private FocusEntry focusEntry;
    private MotivationEntry motivationEntry;

    @BeforeEach
    void runBefore() {
        user = new User("Chris");
        energyEntry = new EnergyEntry(LocalDate.now(), LocalTime.now(), 9);
        energyEntry2 = new EnergyEntry(LocalDate.now(), LocalTime.now(), 9);
        focusEntry = new FocusEntry(LocalDate.now(), LocalTime.now(), 8);
        motivationEntry = new MotivationEntry(LocalDate.now(), LocalTime.now(), 10);
    }

    @Test
    void testConstructor() {
        assertEquals("Chris", user.getName());
        assertTrue(user.getEnergyEntries().isEmpty());
        assertTrue(user.getFocusEntries().isEmpty());
        assertTrue(user.getMotivationEntries().isEmpty());
    }

    @Test
    void testAddEntry() {
        user.addEntry(energyEntry);
        assertEquals(1, user.getEnergyEntries().size());
        assertEquals(energyEntry, user.getEnergyEntries().get(0));

        user.addEntry(focusEntry);
        assertEquals(1, user.getFocusEntries().size());
        assertEquals(focusEntry, user.getFocusEntries().get(0));

        user.addEntry(motivationEntry);
        assertEquals(1, user.getMotivationEntries().size());
        assertEquals(motivationEntry, user.getMotivationEntries().get(0));

        user.addEntry(energyEntry);
        assertEquals(2, user.getEnergyEntries().size());
    }

    @Test
    void testRemoveEntry() {
        user.addEntry(energyEntry);
        user.addEntry(energyEntry2);
        user.addEntry(focusEntry);
        user.addEntry(motivationEntry);

        assertTrue(user.removeEntry(energyEntry));
        assertEquals(1, user.getEnergyEntries().size());

        assertTrue(user.removeEntry(focusEntry));
        assertEquals(0, user.getFocusEntries().size());

        assertTrue(user.removeEntry(motivationEntry));
        assertEquals(0, user.getMotivationEntries().size());
    }

    @Test
    void testRemoveEntryNotFound() {
        assertFalse(user.removeEntry(energyEntry));
        assertEquals(0, user.getEnergyEntries().size());

        assertFalse(user.removeEntry(focusEntry));
        assertEquals(0, user.getFocusEntries().size());

        user.addEntry(focusEntry);
        assertFalse(user.removeEntry(motivationEntry));

        assertEquals(0, user.getMotivationEntries().size());
    }

    @Test
    void testCalculateBPT() {
        assertNull(user.calculateBPT());

        user.addEntry(energyEntry);
        user.addEntry(focusEntry);
        user.addEntry(motivationEntry);
        assertEquals(LocalTime.of(17, 0), user.calculateBPT());
    }
}
