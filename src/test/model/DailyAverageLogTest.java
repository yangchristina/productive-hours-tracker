package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class DailyAverageLogTest {

    private DailyAverageLog log;
    private ProductivityEntry entry1;
    private ProductivityEntry entry2;
    private ProductivityEntry entry3;
    private ProductivityEntry entry4;
    private ProductivityEntry entry5;
    private ProductivityEntry entry6;

    @BeforeEach
    void runBefore() {
        log = new DailyAverageLog();
        entry1 = new EnergyEntry(LocalDate.now(),LocalTime.of(5, 0), 3);
        entry2 = new FocusEntry(LocalDate.now(),LocalTime.of(10, 0), 3);
        entry3 = new MotivationEntry(LocalDate.now(),LocalTime.of(10, 0), 8);
        entry4 = new EnergyEntry(LocalDate.now(),LocalTime.of(12, 0), 5);
        entry5 = new EnergyEntry(LocalDate.now(),LocalTime.of(14, 0), 6);
        entry6 = new EnergyEntry(LocalDate.now(),LocalTime.of(16, 0), 4);
    }

    @Test
    void testConstructor() {
        assertTrue(log.getLog().isEmpty());
        assertTrue(log.getAveragedLog().isEmpty());
    }

    @Test
    void testAdd() {
        log.add(entry1);
        assertEquals(1, log.getLog().size());
        assertEquals(1, log.getLog().get(entry1.getTime()).size());
        assertEquals(entry1.getLevel(), log.getLog().get(entry1.getTime()).get(0));

        log.add(entry2);
        log.add(entry3);
        assertEquals(2, log.getLog().size());
        assertEquals(2, log.getLog().get(entry2.getTime()).size());
        assertEquals(entry2.getLevel(), log.getLog().get(entry2.getTime()).get(0));
        assertEquals(entry3.getLevel(), log.getLog().get(entry2.getTime()).get(1));
    }

    @Test
    void testRemove() {
        log.add(entry1);
        log.add(entry2);
        log.add(entry3);

        log.remove(entry1);
        assertTrue(log.getLog().get(entry1.getTime()).isEmpty());

        log.remove(entry2);
        assertEquals(1, log.getLog().get(entry2.getTime()).size());
        assertFalse(log.getLog().get(entry2.getTime()).contains(entry2.getLevel()));

        log.remove(entry3);
        assertTrue(log.getLog().get(entry3.getTime()).isEmpty());
    }

    @Test
    void testGetPeakHours() {
        assertNull(log.getPeakHours());

        log.add(entry1);
        log.add(entry2);
        log.add(entry3);
        log.add(entry4);
        log.add(entry5);
        log.add(entry6);

        ArrayList<LocalTime> peakHours = log.getPeakHours();
        assertEquals(2, peakHours.size());
        assertEquals(LocalTime.of(10, 0), peakHours.get(0));
        assertEquals(LocalTime.of(14, 0), peakHours.get(1));
    }
}