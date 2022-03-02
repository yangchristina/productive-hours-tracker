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
    }

    @Test
    void testAddEntry() {
        user.add(energyEntry);
        assertEquals(1, user.getEnergyEntries().size());
        assertEquals(energyEntry, user.getEnergyEntries().get(0));

        user.add(focusEntry);
        assertEquals(1, user.getFocusEntries().size());
        assertEquals(focusEntry, user.getFocusEntries().get(0));

        user.add(motivationEntry);
        assertEquals(1, user.getMotivationEntries().size());
        assertEquals(motivationEntry, user.getMotivationEntries().get(0));

        user.add(energyEntry);
        assertEquals(2, user.getEnergyEntries().size());
    }

    @Test
    void testRemoveEntry() {
        user.add(energyEntry);
        user.add(energyEntry2);
        user.add(focusEntry);
        user.add(motivationEntry);

        assertTrue(user.remove(energyEntry));
        assertEquals(1, user.getEnergyEntries().size());

        assertTrue(user.remove(focusEntry));
        assertEquals(0, user.getFocusEntries().size());

        assertTrue(user.remove(motivationEntry));
        assertEquals(0, user.getMotivationEntries().size());
    }

    @Test
    void testRemoveEntryNotFound() {
        assertFalse(user.remove(energyEntry));
        assertEquals(0, user.getEnergyEntries().size());

        assertFalse(user.remove(focusEntry));
        assertEquals(0, user.getFocusEntries().size());

        user.add(focusEntry);
        assertFalse(user.remove(motivationEntry));

        assertEquals(0, user.getMotivationEntries().size());
    }

    @Test
    void testGetPeakHours() {
        assertNull(user.getPeakHours("energy"));
        assertNull(user.getPeakHours("focus"));
        assertNull(user.getPeakHours("motivation"));
    }
}
