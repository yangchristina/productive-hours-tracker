package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// a log of all information related to productivity entries
public class ProductivityLog implements Writable {
    protected ArrayList<ProductivityEntry> entries;

    // EFFECTS: constructs a ProductivityLog with an empty list of energy entries, focus entreis, and motivation entries
    //          creates a new DailyAverageLog for each of energy, focus, and motivation
    public ProductivityLog() {
        entries = new ArrayList<>();
    }

    // EFFECTS: constructs a ProductivityLog with an empty list of energy entries, focus entreis, and motivation entries
    //          creates a new DailyAverageLog for each of energy, focus, and motivation
    public ProductivityLog(ArrayList<ProductivityEntry> entries) {
        this.entries = entries;
    }

    // EFFECTS: returns an array with a description of each entry in specified list
    public String[] listEntries(ArrayList<ProductivityEntry> entries) {
        String[] list = new String[entries.size()];
        int key = 1;
        for (ProductivityEntry entry : entries) {
            list[key - 1] = entry.toString(key);
            key++;
        }
        return list;
    }

    // MODIFIES: this
    // EFFECTS: add given entry to the array it belongs in, and adds it to DailyAverageLog
    public void add(ProductivityEntry entry) { //must add it to correct slot!!!
        entries.add(entry);
    }

    // MODIFIES: this
    // EFFECTS: removes entry from list, and removes it to DailyAverageLog
    public boolean remove(ProductivityEntry entry) {
        return entries.remove(entry);
    }

    // EFFECTS: returns true if energyEntries, focusEntries, and motivationEntries are all empty
    public boolean isEmpty() {
        return entries.isEmpty();
//        return energyEntries.isEmpty() && focusEntries.isEmpty() && motivationEntries.isEmpty();
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
}
