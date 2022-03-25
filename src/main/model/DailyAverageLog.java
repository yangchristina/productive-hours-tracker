package model;

import java.time.LocalTime;
import java.util.*;

// includes a log of the average level for each time of day, for each entry type
public class DailyAverageLog {

    private HashMap<ProductivityEntry.Label, TreeMap<LocalTime, Double>> averageLog;

    private HashMap<ProductivityEntry.Label, int[]> counts;

    // EFFECTS: constructs DailyAverageLog with an empty counts and an empty averageLog
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

    // EFFECTS: returns a hashMap with a key for all productivity entries and empty int[] as values
    private HashMap<ProductivityEntry.Label, int[]> createEmptyCounts() {
        HashMap<ProductivityEntry.Label, int[]> counts = new HashMap<>();
        for (ProductivityEntry.Label label : ProductivityEntry.Label.values()) {
            counts.put(label, new int[24]);
        }
        return counts;
    }

    // EFFECTS: returns a hashMap with a key for all productivity entries and empty TreeMap as values
    private HashMap<ProductivityEntry.Label, TreeMap<LocalTime, Double>> createEmptyLog() {
        HashMap<ProductivityEntry.Label, TreeMap<LocalTime, Double>> emptyLog = new HashMap<>();
        for (ProductivityEntry.Label label : ProductivityEntry.Label.values()) {
            emptyLog.put(label, new TreeMap<>());
        }
        return emptyLog;
    }

    // EFFECTS: calls add method to add all entries to the average in averageLog
    private void initAverageLog(ArrayList<ProductivityEntry> entries) {
        for (ProductivityEntry entry : entries) {
            add(entry);
        }
    }

    // MODIFIES: this
    // EFFECTS: adds the level of entry to the average for the time of the entry
    public double add(ProductivityEntry entry) {
        LocalTime time = entry.getTime();
        int level = entry.getLevel();
        ProductivityEntry.Label label = entry.getLabel();

        Double oldAverage = averageLog.get(label).get(time);
        double newAverage;

        int newCount = ++counts.get(label)[time.getHour()];
        if (oldAverage != null) {
            newAverage = oldAverage + ((level - oldAverage) / newCount);
        } else {
            newAverage = level;
        }
        averageLog.get(label).put(time, newAverage);
        return newAverage;
    }

    // REQUIRES: averageLog.get(entry.getLabel()).get(entry.getTime()) != null
    // MODIFIES: this
    // EFFECTS: updates the log for the removal of this entry, by calculating the new average after removing this entry
    public Double remove(ProductivityEntry entry) {
        LocalTime time = entry.getTime();
        int level = entry.getLevel();
        ProductivityEntry.Label label = entry.getLabel();

        Double oldAverage = averageLog.get(label).get(time);
        int newCount = --counts.get(label)[time.getHour()];

        double newAverage;
        if (newCount == 0) {
            averageLog.get(label).remove(time);
            return null;
        }

        newAverage = oldAverage + (oldAverage - level) / newCount;
        averageLog.get(label).put(time, newAverage);
        return newAverage;
    }

    public HashMap<ProductivityEntry.Label, TreeMap<LocalTime, Double>> getLog() {
        return averageLog;
    }
}