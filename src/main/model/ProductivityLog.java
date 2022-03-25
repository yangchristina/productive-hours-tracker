package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.time.LocalTime;
import java.util.ArrayList;

// a log of all information related to productivity entries
public class ProductivityLog implements Writable {
    protected ArrayList<ProductivityEntry> entries;
    private DailyAverageLog dailyAverageLog;

    // EFFECTS: constructs a ProductivityLog with an empty list of energy entries, focus entreis, and motivation entries
    //          creates a new DailyAverageLog for each of energy, focus, and motivation
    public ProductivityLog() {
        entries = new ArrayList<>();
        dailyAverageLog = new DailyAverageLog();
    }

    // EFFECTS: constructs a ProductivityLog with an empty list of energy entries, focus entreis, and motivation entries
    //          creates a new DailyAverageLog for each of energy, focus, and motivation
    public ProductivityLog(ArrayList<ProductivityEntry> entries) {
        this.entries = entries;
        dailyAverageLog = new DailyAverageLog(entries);
    }

    // MODIFIES: this
    // EFFECTS: add given entry to the array it belongs in, and adds it to DailyAverageLog
    public Double add(ProductivityEntry entry) { //must add it to correct slot!!!
        entries.add(entry);
        return dailyAverageLog.add(entry);
    }

    // MODIFIES: this
    // EFFECTS: removes entry from list, and removes it to DailyAverageLog
    public Double remove(ProductivityEntry entry) {
        entries.remove(entry);
        return dailyAverageLog.remove(entry);
    }

//    // !!! where to call dailyAverageLog for edit
//    public void edit(ProductivityEntry entry, ProductivityEntry.Label label, int level, LocalTime time) {
//        dailyAverageLog.remove(entry);
//        entry.editLabel(label);
//        entry.editLevel(level);
//        entry.editTime(time);
//        dailyAverageLog.add(entry);
//    }

    // EFFECTS: returns true if energyEntries, focusEntries, and motivationEntries are all empty
    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public ArrayList<ProductivityEntry> getEntries() {
        return entries;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("entries", logToJson(entries));
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

    public DailyAverageLog getDailyAverageLog() {
        return dailyAverageLog;
    }
}
