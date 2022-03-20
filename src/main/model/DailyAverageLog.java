package model;

import java.time.LocalTime;
import java.util.*;

// includes a log of all levels sorted by entry type and time of day
public class DailyAverageLog {

    private HashMap<ProductivityEntry.Label, HashMap<LocalTime, Integer>> averageLog;

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

    private HashMap<ProductivityEntry.Label, HashMap<LocalTime, Integer>> createEmptyLog() {
        HashMap<ProductivityEntry.Label, HashMap<LocalTime, Integer>> emptyLog = new HashMap<>();
        for (ProductivityEntry.Label label : ProductivityEntry.Label.values()) {
            emptyLog.put(label, new HashMap<>());
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
    public void initAverageLog(ArrayList<ProductivityEntry> entries) {
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

    public LocalTime[] getPeakAndTrough() {
//        for () !!!
        return new LocalTime[2];
    }

    public HashMap<ProductivityEntry.Label, HashMap<LocalTime, Integer>> getLog() {
        return averageLog;
    }

    //    // MODIFIES: this
//    // EFFECTS: adds an entry to log
//    private void addToLog(ProductivityEntry entry) {
//        ArrayList<Integer> levels = getLevels(entry);
//        levels.add(entry.getLevel());
//    }

//    @Override
//    // MODIFIES: this
//    // EFFECTS: adds an entry log for its time of day
//    public boolean remove(ProductivityEntry entry) {
//        boolean isRemoved = super.remove(entry);
//        if (isRemoved) {
//            ArrayList<Integer> levels = getLevels(entry);
//            levels.remove(Integer.valueOf(entry.getLevel()));
//        }
//        return isRemoved;
//    }

//    // EFFECTS: calls and returns the getLevels method with two parameters
//    private ArrayList<Integer> getLevels(ProductivityEntry entry) {
//        return getLevels(entry.getLabel(), entry.getTime());
//    }
//
//    // EFFECTS: gets the array list of the given entryType and time from log
//    private ArrayList<Integer> getLevels(ProductivityEntry.Label entryType, LocalTime time) {
//        return log.get(entryType).get(time);
//    }
//
//    // EFFECTS: returns map of tree where each value in array list is averaged
//    private TreeMap<LocalTime, Integer> getDailyAverages(ProductivityEntry.Label entryType, TreeMap<LocalTime,
//            ArrayList<Integer>> tree) {
//        TreeMap<LocalTime, Integer> averagedTree = new TreeMap<>();
//
//        Set<LocalTime> keys = tree.keySet();
//        for (LocalTime time: keys) {
//            ArrayList<Integer> levels = getLevels(entryType, time);
//            if (!levels.isEmpty()) {
//                averagedTree.put(time, averageLevel(levels));
//            }
//        }
//        return averagedTree;
//    }
//
//    // EFFECTS: returns the average of all the values in list
//    private int averageLevel(ArrayList<Integer> levels) {
//
//        int sum = 0;
//        for (Integer i : levels) {
//            sum += i;
//        }
//        return sum / levels.size();
//    }
//
//    // EFFECTS: gets peaks and troughs for all entry types
//    public HashMap<ProductivityEntry.Label, HashMap<String, ArrayList<LocalTime>>> getPeaksAndTroughs() {
//        HashMap<ProductivityEntry.Label, HashMap<String, ArrayList<LocalTime>>> peakTroughLog = new HashMap<>();
//        for (ProductivityEntry.Label entryType : getAverageLog().keySet()) {
//            peakTroughLog.put(entryType, getPeaksAndTroughs(entryType));
//        }
//        return peakTroughLog;
//    }
//
//    // EFFECTS: gets peaks and troughs of a certain entry type
//    public HashMap<String, ArrayList<LocalTime>> getPeaksAndTroughs(ProductivityEntry.Label entryType) {
//        HashMap<String, ArrayList<LocalTime>> peaksAndTroughs = new HashMap<>();
//
//        ArrayList<LocalTime> peakHours = new ArrayList<>();
//        ArrayList<LocalTime> troughHours = new ArrayList<>();
//
//        Map.Entry<LocalTime, Integer> left = null;
//        Map.Entry<LocalTime, Integer> curr = null;
//
//        for (Map.Entry<LocalTime, Integer> right : getAverageLog().get(entryType).entrySet()) {
//            if (left != null) {
//                if (curr.getValue() < left.getValue() && curr.getValue() < right.getValue()) {
//                    troughHours.add(curr.getKey());
//                } else if (curr.getValue() > left.getValue() && curr.getValue() > right.getValue()) {
//                    peakHours.add(curr.getKey());
//                } // ignoring equals case /--\ and \__/ and edge cases, ex. \____
//            }
//            left = curr;
//            curr = right;
//        }
//
//        peaksAndTroughs.put("peak", peakHours);
//        peaksAndTroughs.put("trough", troughHours);
//        return peaksAndTroughs;
//    }
//
//    public HashMap<ProductivityEntry.Label, TreeMap<LocalTime, ArrayList<Integer>>> getLog() {
//        return log;
//    }
}
