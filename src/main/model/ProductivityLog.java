package model;

import java.time.LocalTime;
import java.util.ArrayList;

// a log of all information related to productivity entries
public class ProductivityLog {
    private ArrayList<ProductivityEntry> energyEntries;
    private ArrayList<ProductivityEntry> motivationEntries;
    private ArrayList<ProductivityEntry> focusEntries;

    private DailyAverageLog energyAverages;
    private DailyAverageLog motivationAverages;
    private DailyAverageLog focusAverages;

    // EFFECTS: constructs a ProductivityLog with an empty list of energy entries, focus entreis, and motivation entries
    //          creates a new DailyAverageLog for each of energy, focus, and motivation
    public ProductivityLog() {
        energyEntries = new ArrayList<>();
        focusEntries = new ArrayList<>();
        motivationEntries = new ArrayList<>();

        energyAverages = new DailyAverageLog();
        motivationAverages = new DailyAverageLog();
        focusAverages = new DailyAverageLog();
    }

    // MODIFIES: this
    // EFFECTS: add given entry to the array it belongs in, and adds it to DailyAverageLog
    public void add(ProductivityEntry entry) { //must add it to correct slot!!!
        String entryType = entry.label();
        switch (entryType) {
            case "energy":
                energyEntries.add(entry);
                energyAverages.add(entry);
                break;
            case "focus":
                focusEntries.add(entry);
                focusAverages.add(entry);
                break;
            case "motivation":
                motivationEntries.add(entry);
                motivationAverages.add(entry);
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: removes entry from list, and removes it to DailyAverageLog
    public boolean remove(ProductivityEntry entry) {
        boolean isRemoved;
        switch (entry.label()) {
            case "energy":
                isRemoved = energyEntries.remove(entry);
                if (isRemoved) {
                    energyAverages.remove(entry);
                }
                return isRemoved;
            case "focus":
                isRemoved = focusEntries.remove(entry);
                if (isRemoved) {
                    focusAverages.remove(entry);
                }
                return isRemoved;
            default:
                isRemoved = motivationEntries.remove(entry);
                if (isRemoved) {
                    motivationAverages.remove(entry);
                }
                return isRemoved;
        }
    }


    // REQUIRES: label is "energy", "focus", or "motivation"
    // EFFECTS: returns the peak hours for the entry list selected
    public ArrayList<LocalTime> getPeakHours(String label) {
        switch (label) {
            case "energy":
                return energyAverages.getPeakHours();
            case "focus":
                return focusAverages.getPeakHours();
            default:
                return motivationAverages.getPeakHours();
        }
    }

    public DailyAverageLog getEnergyAverages() {
        return energyAverages;
    }

    public DailyAverageLog getFocusAverages() {
        return focusAverages;
    }

    public DailyAverageLog getMotivationAverages() {
        return motivationAverages;
    }

    public ArrayList<ProductivityEntry> getEnergyEntries() {
        return energyEntries;
    }

    public ArrayList<ProductivityEntry> getFocusEntries() {
        return focusEntries;
    }

    public ArrayList<ProductivityEntry> getMotivationEntries() {
        return motivationEntries;
    }
}
