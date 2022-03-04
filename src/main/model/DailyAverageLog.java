package model;

import java.time.LocalTime;
import java.util.*;

// includes a log of all levels sorted by entry type and time of day
public class DailyAverageLog extends ProductivityLog {
    private HashMap<String, TreeMap<LocalTime, ArrayList<Integer>>> log;
    
    // EFFECTS: constructs DailyAverageLog with an empty list of productivity entries and empty log
    public DailyAverageLog() {
        log = createEmptyLog();
    }

    // EFFECTS: constructs DailyAverageLog with a given list of productivity entries,
    //          and a log of levels by time of day, which is calculated from the productivity entries
    public DailyAverageLog(ArrayList<ProductivityEntry> energy,
                           ArrayList<ProductivityEntry> focus, 
                           ArrayList<ProductivityEntry> motivation) {
        super(energy, focus, motivation);
        log = createEmptyLog();
        initiateLog(getAllEntries());
    }

    // EFFECTS: constructs an empty log, with energyType as the key
    //          and a map with the time as the key and a list of levels as the values
    private HashMap<String, TreeMap<LocalTime, ArrayList<Integer>>> createEmptyLog() {
        TreeMap<LocalTime, ArrayList<Integer>> energy = new TreeMap<>();
        TreeMap<LocalTime, ArrayList<Integer>> focus = new TreeMap<>();
        TreeMap<LocalTime, ArrayList<Integer>> motivation = new TreeMap<>();

        for (int i = 0; i < 24; i++) {
            energy.put(LocalTime.of(i, 0), new ArrayList<>());
            focus.put(LocalTime.of(i, 0), new ArrayList<>());
            motivation.put(LocalTime.of(i, 0), new ArrayList<>());
        }

        HashMap<String, TreeMap<LocalTime, ArrayList<Integer>>> log = new HashMap<>();
        log.put("energy", energy);
        log.put("focus", focus);
        log.put("motivation", motivation);
        return log;
    }

    // MODIFIES: this
    // EFFECTS: constructs and returns a new array sorted by entry type, and containing all levels for each hour of day
    private HashMap<String, TreeMap<LocalTime, ArrayList<Integer>>> initiateLog(ArrayList<ProductivityEntry> entries) {
        log = createEmptyLog();

        for (ProductivityEntry entry : entries) {
            addToLog(entry);
        }
        return log;
    }

    @Override
    // EFFECTS: calls two add methods, one for adding to lists in ProductivityLog and another for adding to log
    public void add(ProductivityEntry entry) {
        super.add(entry);
        addToLog(entry);
    }

    // MODIFIES: this
    // EFFECTS: adds an entry to log
    private void addToLog(ProductivityEntry entry) {
        ArrayList<Integer> levels = getLevels(entry);
        levels.add(entry.getLevel());
    }

    @Override
    // MODIFIES: this
    // EFFECTS: adds an entry log for its time of day
    public boolean remove(ProductivityEntry entry) {
        boolean isRemoved = super.remove(entry);
        if (isRemoved) {
            ArrayList<Integer> levels = getLevels(entry);
            levels.remove(Integer.valueOf(entry.getLevel()));
        }
        return isRemoved;
    }

    // EFFECTS: calls and returns the getLevels method with two parameters
    private ArrayList<Integer> getLevels(ProductivityEntry entry) {
        return getLevels(entry.label(), entry.getTime());
    }

    // EFFECTS: gets the array list of the given entryType and time from log
    private ArrayList<Integer> getLevels(String entryType, LocalTime time) {
        return log.get(entryType).get(time);
    }

    // EFFECTS: returns a map of the average level for each time and entry type
    public HashMap<String, TreeMap<LocalTime, Integer>> getAverageLog() {
        HashMap<String, TreeMap<LocalTime, Integer>> averageLog = new HashMap<>();

        for (Map.Entry<String, TreeMap<LocalTime, ArrayList<Integer>>> entry : log.entrySet()) {
            TreeMap<LocalTime, Integer> dailyAveragesOfEntryType = getDailyAverages(entry.getKey(), entry.getValue());
            averageLog.put(entry.getKey(), dailyAveragesOfEntryType);
        }
        return averageLog;
    }

    // EFFECTS: returns map of tree where each value in array list is averaged
    private TreeMap<LocalTime, Integer> getDailyAverages(String entryType, TreeMap<LocalTime,
            ArrayList<Integer>> tree) {
        TreeMap<LocalTime, Integer> averagedTree = new TreeMap<>();

        Set<LocalTime> keys = tree.keySet();
        for (LocalTime time: keys) {
            ArrayList<Integer> levels = getLevels(entryType, time);
            if (!levels.isEmpty()) {
                averagedTree.put(time, averageLevel(levels));
            }
        }
        return averagedTree;
    }

    // EFFECTS: returns the average of all the values in list
    private int averageLevel(ArrayList<Integer> levels) {

        int sum = 0;
        for (Integer i : levels) {
            sum += i;
        }
        return sum / levels.size();
    }

    // EFFECTS: gets peaks and troughs for all entry types
    public HashMap<String, HashMap<String, ArrayList<LocalTime>>> getPeaksAndTroughs() {
        HashMap<String, HashMap<String, ArrayList<LocalTime>>> peakTroughLog = new HashMap<>();
        for (String entryType : getAverageLog().keySet()) {
            peakTroughLog.put(entryType, getPeaksAndTroughs(entryType));
        }
        return peakTroughLog;
    }

    // EFFECTS: gets peaks and troughs of a certain entry type
    public HashMap<String, ArrayList<LocalTime>> getPeaksAndTroughs(String entryType) {
        HashMap<String, ArrayList<LocalTime>> peaksAndTroughs = new HashMap<>();
        
        ArrayList<LocalTime> peakHours = new ArrayList<>();
        ArrayList<LocalTime> troughHours = new ArrayList<>();

        Map.Entry<LocalTime, Integer> left = null;
        Map.Entry<LocalTime, Integer> curr = null;

        for (Map.Entry<LocalTime, Integer> right : getAverageLog().get(entryType).entrySet()) {
            if (left != null) {
                if (curr.getValue() < left.getValue() && curr.getValue() < right.getValue()) {
                    troughHours.add(curr.getKey());
                } else if (curr.getValue() > left.getValue() && curr.getValue() > right.getValue()) {
                    peakHours.add(curr.getKey());
                } // ignoring equals case /--\ and \__/ and edge cases, ex. \____
            }
            left = curr;
            curr = right;
        }
        
        peaksAndTroughs.put("peak", peakHours);
        peaksAndTroughs.put("trough", troughHours);
        return peaksAndTroughs;
    }

    public HashMap<String, TreeMap<LocalTime, ArrayList<Integer>>> getLog() {
        return log;
    }
}
