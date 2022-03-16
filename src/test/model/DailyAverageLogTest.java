package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

public class DailyAverageLogTest {

    private DailyAverageLog log;
    private DailyAverageLog log2;

    private ProductivityEntry energyEntry;
    private ProductivityEntry focusEntry;
    private ProductivityEntry motivationEntry;

    @BeforeEach
    void runBefore() {
        energyEntry = new EnergyEntry(LocalDate.now(),LocalTime.of(1, 0), 1);
        focusEntry = new FocusEntry(LocalDate.now(),LocalTime.of(2, 0), 5);
        motivationEntry = new MotivationEntry(LocalDate.now(),LocalTime.of(3, 0), 6);

        log = new DailyAverageLog();
        log2 = new DailyAverageLog(createEnergyList(), createFocusList(), createMotivationList());
    }

    @Test
    void testConstructorNoParams() {
        for (ProductivityEntry.Label entryType : log.getLog().keySet()) {
            assertEquals(24, log.getLog().get(entryType).size());
            for (ArrayList<Integer> arr : log.getLog().get(entryType).values()) {
                assertTrue(arr.isEmpty());
            }
        }
    }

    @Test
    void testConstructorWithParams() {
        assertEquals(24, log2.getLog().get(ProductivityEntry.Label.ENERGY).size());

        ArrayList<Integer> energy = log2.getLog().get(ProductivityEntry.Label.ENERGY).get(energyEntry.getTime());
        ArrayList<Integer> focus = log2.getLog().get(ProductivityEntry.Label.FOCUS).get(focusEntry.getTime());
        ArrayList<Integer> motivation = log2.getLog().get(ProductivityEntry.Label.MOTIVATION).get(motivationEntry.getTime());

        assertEquals(1, energy.size());
        assertEquals(1, energy.get(0));

        assertEquals(1, focus.size());
        assertEquals(focusEntry.getLevel(), focus.get(0));

        assertEquals(1, motivation.size());
        assertEquals(motivationEntry.getLevel(), motivation.get(0));
    }

    @Test
    void testAdd() {
        log.add(energyEntry);
        log.add(focusEntry);
        log.add(motivationEntry);

        ArrayList<Integer> list1 = log.getLog().get(ProductivityEntry.Label.ENERGY).get(energyEntry.getTime());
        ArrayList<Integer> list2 = log.getLog().get(ProductivityEntry.Label.FOCUS).get(focusEntry.getTime());
        ArrayList<Integer> list3 = log.getLog().get(ProductivityEntry.Label.MOTIVATION).get(motivationEntry.getTime());

        assertEquals(1, list1.size());
        assertEquals(energyEntry.getLevel(), list1.get(0));

        assertEquals(1, list2.size());
        assertEquals(focusEntry.getLevel(), list2.get(0));

        assertEquals(1, list3.size());
        assertEquals(motivationEntry.getLevel(), list3.get(0));
    }

    @Test
    void testRemoveInvalid() {
        assertFalse(log.remove(energyEntry));
    }

    @Test
    void testRemoveValid() {
        assertTrue(log2.remove(energyEntry));
        assertTrue(log2.getLog().get(ProductivityEntry.Label.FOCUS).get(energyEntry.getTime()).isEmpty());

        assertTrue(log2.remove(focusEntry));
        assertTrue(log2.getLog().get(ProductivityEntry.Label.FOCUS).get(focusEntry.getTime()).isEmpty());

        assertTrue(log2.remove(motivationEntry));
        assertTrue(log2.getLog().get(ProductivityEntry.Label.MOTIVATION).get(motivationEntry.getTime()).isEmpty());
    }

    @Test
    void testGetAverageLog() {
        log2.add(new FocusEntry(LocalDate.now(), focusEntry.getTime(), focusEntry.getLevel()+2));

        // just averages all teh values in level
        HashMap<ProductivityEntry.Label, TreeMap<LocalTime, Integer>> averageLog = log2.getAverageLog();
        assertEquals(focusEntry.getLevel()+1, averageLog.get(ProductivityEntry.Label.FOCUS).get(focusEntry.getTime()));
        assertEquals(energyEntry.getLevel(), averageLog.get(ProductivityEntry.Label.ENERGY).get(energyEntry.getTime()));
        assertEquals(motivationEntry.getLevel(), averageLog.get(ProductivityEntry.Label.MOTIVATION).get(motivationEntry.getTime()));
    }

    @Test
    void getPeaksAndTroughs() {
        HashMap<String, ArrayList<LocalTime>> peakTrough = log2.getPeaksAndTroughs().get(ProductivityEntry.Label.ENERGY);
        assertEquals(peakTrough, log2.getPeaksAndTroughs(ProductivityEntry.Label.ENERGY));

        ArrayList<LocalTime> peakHours = peakTrough.get("peak");
        ArrayList<LocalTime> troughHours = peakTrough.get("trough");

        assertEquals(1, peakHours.size());
        assertEquals(1, troughHours.size());

        assertEquals(LocalTime.of(3, 0), peakHours.get(0));
        assertEquals(LocalTime.of(5, 0), troughHours.get(0));
    }

    // EFFECTS: creates and returns a sample energy list
    private ArrayList<ProductivityEntry> createEnergyList() {
        ArrayList<ProductivityEntry> list = new ArrayList<>();

        // level goes up as time goes up
        list.add(energyEntry);
        for (int hour = 2, level = 2; hour < 3; hour++, level++) {
            list.add(new EnergyEntry(LocalDate.now(), LocalTime.of(hour, 0), level));
        }
        // level goes down as time goes up
        for (int hour = 3, level = 3; hour < 5; hour++, level--) {
            list.add(new EnergyEntry(LocalDate.now(), LocalTime.of(hour, 0), level));
        }
        // level goes up as time goes up
        for (int hour = 5, level = 1; hour < 7; hour++, level++) {
            list.add(new EnergyEntry(LocalDate.now(), LocalTime.of(hour, 0), level));
        }
        return list;
    }

    // EFFECTS: creates and returns a sample focus list
    private ArrayList<ProductivityEntry> createFocusList() {
        ArrayList<ProductivityEntry> list = new ArrayList<>();
        list.add(focusEntry);
        return list;
    }

    // EFFECTS: creates a sample motivation list
    private ArrayList<ProductivityEntry> createMotivationList() {
        ArrayList<ProductivityEntry> list = new ArrayList<>();
        list.add(motivationEntry);
        return list;
    }
}