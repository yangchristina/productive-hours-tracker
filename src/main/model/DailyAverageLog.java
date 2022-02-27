package model;

import java.time.LocalTime;
import java.util.*;

//!!!
public class DailyAverageLog {
    private TreeMap<LocalTime, ArrayList<Integer>> log;
    private TreeMap<LocalTime, Double> averagedLog;

    // EFFECTS: constructs AverageLevelsByTime with an empty list of productivity entries
    public DailyAverageLog() {
        log = new TreeMap<>();
        averagedLog = new TreeMap<>();
    }

    // MODIFIES: this
    // EFFECTS: adds an entry log for its time of day
    public void add(ProductivityEntry newEntry) {
        ArrayList<Integer> list;
        if (log.get(newEntry.getTime()) == null) {
            list = new ArrayList<>();
        } else {
            list = log.get(newEntry.getTime());
        }
        list.add(newEntry.getLevel());
        log.put(newEntry.getTime(), list);
    }

    // REQUIRES: log.get(newEntry.getTime()) contains newEntry.getLevel()
    // MODIFIES: this
    // EFFECTS: adds an entry log for its time of day
    public void remove(ProductivityEntry newEntry) {
        ArrayList<Integer> list = log.get(newEntry.getTime());
        list.remove(Integer.valueOf(newEntry.getLevel()));
        log.put(newEntry.getTime(), list);
    }

    // MODIFIES: this
    // EFFECTS: remake averageLog by finding the average for each key in log
    private void updateAveragedLog() {
        TreeMap<LocalTime, Double> averagedLog = new TreeMap<>();

        for (Map.Entry<LocalTime, ArrayList<Integer>> e : log.entrySet()) {
            OptionalDouble average = e.getValue()
                    .stream()
                    .mapToDouble(a -> a)
                    .average();
            if (!average.isPresent()) {
                continue;
            }
            averagedLog.put(e.getKey(), average.getAsDouble());
        }
        this.averagedLog = averagedLog;
    }

    // EFFECTS: returns an arraylist of all the peak values, which are values where the hour before and after have
    //          lower levels
    public ArrayList<LocalTime> getPeakHours() {
        updateAveragedLog();

        if (log.size() < 5) {
            return null;
        }

        ArrayList<LocalTime> peakHours = new ArrayList<>();

        Map.Entry<LocalTime, Double> e1 = null;
        Map.Entry<LocalTime, Double> e2 = null;

        for (Map.Entry<LocalTime, Double> e : averagedLog.entrySet()) {
            if (e1 != null && e2.getValue() >= e1.getValue() && e2.getValue() > e.getValue()) {
                peakHours.add(e2.getKey());
            }
            e1 = e2;
            e2 = e;
        }
        return peakHours;
    }

    public TreeMap<LocalTime, ArrayList<Integer>> getLog() {
        return log;
    }

    public TreeMap<LocalTime, Double> getAveragedLog() {
        return averagedLog;
    }
}
