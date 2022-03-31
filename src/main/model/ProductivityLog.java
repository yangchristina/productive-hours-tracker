package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// a log of all information related to productivity entries
public class ProductivityLog implements Writable, Observer {
    private ArrayList<ProductivityEntry> entries;
    private final DailyAverageLog dailyAverageLog;
    private User user;

    // EFFECTS: constructs a ProductivityLog with an empty list of entries and an empty dailyAverageLog
    public ProductivityLog(User user) {
        entries = new ArrayList<>();
        dailyAverageLog = new DailyAverageLog();
        this.user = user;
    }

    // EFFECTS: constructs a ProductivityLog with an given list of entries and a dailyAverageLog with these entries
    public ProductivityLog(User user, ArrayList<ProductivityEntry> entries) {
        this.entries = entries;
        dailyAverageLog = new DailyAverageLog(entries);
        this.user = user;
    }

    // MODIFIES: this
    // EFFECTS: add given entry to the array it belongs in, and adds it to DailyAverageLog
    public Double add(ProductivityEntry entry) {
        entries.add(entry);
        EventLog.getInstance().logEvent(new Event(user.getName() + " added entry: " + entry.toString()));
        entry.addObserver(this);
        return dailyAverageLog.add(entry);
    }

    // MODIFIES: this
    // EFFECTS: removes entry from list, and removes it to DailyAverageLog
    public Double remove(ProductivityEntry entry) {
        entries.remove(entry);
        EventLog.getInstance().logEvent(new Event(user.getName() + " removed entry: " + entry.toString()));
        entry.removeObserver(this);
        return dailyAverageLog.remove(entry);
    }

    @Override
    public JSONObject toJson() {
        JSONArray jsonArray = new JSONArray();
        for (ProductivityEntry entry : entries) {
            jsonArray.put(entry.toJson());
        }

        JSONObject json = new JSONObject();
        json.put("entries", jsonArray);
        return json;
    }

    public ArrayList<ProductivityEntry> getEntries() {
        return entries;
    }

    public DailyAverageLog getDailyAverageLog() {
        return dailyAverageLog;
    }

    public User getUser() {
        return user;
    }

    @Override
    public void update(ProductivityEntry curr, ProductivityEntry prev) {
        dailyAverageLog.remove(prev);
        dailyAverageLog.add(curr);
        EventLog.getInstance().logEvent(new Event(user.getName() + " edited entry: \n" + prev + " –––>\n" + curr));
    }
}
