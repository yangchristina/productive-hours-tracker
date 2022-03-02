package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.time.LocalTime;
import java.util.ArrayList;

// a log of all information related to productivity entries
public class ProductivityLog implements Writable {
    protected ArrayList<ProductivityEntry> energyEntries;
    protected ArrayList<ProductivityEntry> motivationEntries;
    protected ArrayList<ProductivityEntry> focusEntries;

    protected DailyAverageLog energyAverages;
    protected DailyAverageLog motivationAverages;
    protected DailyAverageLog focusAverages;

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

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("energy", logToJson(energyEntries));
        json.put("focus", logToJson(focusEntries));
        json.put("motivation", logToJson(motivationEntries));
        return json;
    }

    // EFFECTS: returns items in this log as a JSON array
    private JSONArray logToJson(ArrayList<ProductivityEntry> log) {
        JSONArray jsonArray = new JSONArray();

        for (ProductivityEntry entry : log) {
            jsonArray.put(entry.toJson());
        }

        return jsonArray;
    }
}
