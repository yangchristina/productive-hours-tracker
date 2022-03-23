package model;

import java.time.LocalTime;
import java.util.*;

// includes a log of all levels sorted by entry type and time of day
public class DailyAverageLog {

    private HashMap<ProductivityEntry.Label, TreeMap<LocalTime, Integer>> averageLog;

    private HashMap<ProductivityEntry.Label, int[]> counts;

    // EFFECTS: constructs DailyAverageLog with an empty list of productivity entries and empty log
    public DailyAverageLog() {
        counts = createEmptyCounts();
        averageLog = createEmptyLog();
    }

    // EFFECTS: constructs DailyAverageLog with a given list of productivity entries,
    //          and a log of levels by time of day, which is calculated from the productivity entries
    public DailyAverageLog(ArrayList<ProductivityEntry> entries) {
        counts = createEmptyCounts();
        averageLog = createEmptyLog();
        initAverageLog(entries);
    }

    private HashMap<ProductivityEntry.Label, TreeMap<LocalTime, Integer>> createEmptyLog() {
        HashMap<ProductivityEntry.Label, TreeMap<LocalTime, Integer>> emptyLog = new HashMap<>();
        for (ProductivityEntry.Label label : ProductivityEntry.Label.values()) {
            emptyLog.put(label, new TreeMap<>());
        }
        return emptyLog;
    }

    private HashMap<ProductivityEntry.Label, int[]> createEmptyCounts() {
        HashMap<ProductivityEntry.Label, int[]> counts = new HashMap<>();
        for (ProductivityEntry.Label label : ProductivityEntry.Label.values()) {
            counts.put(label, new int[24]);
        }
        return counts;
    }

    // EFFECTS: returns a map of the average level for each time and entry type
    private void initAverageLog(ArrayList<ProductivityEntry> entries) {
        for (ProductivityEntry entry : entries) {
            add(entry);
        }
    }

    // MODIFIES: log
    // EFFECTS:
    public int add(ProductivityEntry entry) {
        LocalTime time = entry.getTime();
        int level = entry.getLevel();
        ProductivityEntry.Label label = entry.getLabel();

        Integer oldAverage = averageLog.get(label).get(time);
        int newAverage;

        int newCount = ++counts.get(label)[time.getHour()];
        if (oldAverage != null) {
            newAverage = oldAverage + ((level - oldAverage) / newCount);
        } else {
            newAverage = level;
        }
        averageLog.get(label).put(time, newAverage);
        return newAverage;
    }

    // REQUIRES: entry is in log
    // MODIFIES: log, counts
    // EFFECTS: updates the log for the removal of this entry
    public Integer remove(ProductivityEntry entry) {
        LocalTime time = entry.getTime();
        int level = entry.getLevel();
        ProductivityEntry.Label label = entry.getLabel();

        Integer oldAverage = averageLog.get(label).get(time);
        int newCount = --counts.get(label)[time.getHour()];

        int newAverage;
        if (newCount == 0) {
            averageLog.get(label).remove(time);
            return null;
        }

        newAverage = oldAverage + (oldAverage - level) / newCount;
        averageLog.get(label).put(time, newAverage);
        return newAverage;
    }

    public HashMap<ProductivityEntry.Label, TreeMap<LocalTime, Integer>> getLog() {
        return averageLog;
    }
}