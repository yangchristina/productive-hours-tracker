package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// a log of all information related to productivity entries
public class ProductivityLog implements Writable {
    protected ArrayList<ProductivityEntry> entries;
    private final DailyAverageLog dailyAverageLog;

    // EFFECTS: constructs a ProductivityLog with an empty list of entries and an empty dailyAverageLog
    public ProductivityLog() {
        entries = new ArrayList<>();
        dailyAverageLog = new DailyAverageLog();
    }

    // EFFECTS: constructs a ProductivityLog with an given list of entries and a dailyAverageLog with these entries
    public ProductivityLog(ArrayList<ProductivityEntry> entries) {
        this.entries = entries;
        dailyAverageLog = new DailyAverageLog(entries);
    }

    // MODIFIES: this
    // EFFECTS: add given entry to the array it belongs in, and adds it to DailyAverageLog
    public Double add(ProductivityEntry entry) {
        entries.add(entry);
        EventLog.getInstance().logEvent(new Event("Added entry: " + entry.toString()));
        return dailyAverageLog.add(entry);
    }

    // MODIFIES: this
    // EFFECTS: removes entry from list, and removes it to DailyAverageLog
    public Double remove(ProductivityEntry entry) {
        entries.remove(entry);
        EventLog.getInstance().logEvent(new Event("Removed entry: " + entry.toString()));
        return dailyAverageLog.remove(entry);
    }

    // EFFECTS: returns true if entries is empty
    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public ArrayList<ProductivityEntry> getEntries() {
        return entries;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("entries", logToJson());
        return json;
    }

    // EFFECTS: returns entries as a JSON array
    private JSONArray logToJson() {
        JSONArray jsonArray = new JSONArray();

        for (ProductivityEntry entry : entries) {
            jsonArray.put(entry.toJson());
        }

        return jsonArray;
    }

    public DailyAverageLog getDailyAverageLog() {
        return dailyAverageLog;
    }
}
