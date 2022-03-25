package model;

import org.json.JSONObject;
import persistence.Writable;

import java.time.LocalDate;
import java.time.LocalTime;

// class for productivity entries, which contain label, date, time of day, and level
public class ProductivityEntry implements Writable {
    private final LocalDate date;
    private LocalTime time;
    protected int level;
    private Label label;

    public enum Label {
        ENERGY,
        FOCUS,
        MOTIVATION
    }

    // EFFECTS: creates a new productivity entry with given values for label, date, time and level
    public ProductivityEntry(Label label, LocalDate localDate, LocalTime localTime, int level) {
        this.label = label;
        this.date = localDate; // cannot be modified
        this.time = localTime;
        this.level = level;
    }

    // EFFECTS: returns a string with a description of the entry
    public String toString() {
        return getLabel() + " level of " + level + " at " + time + " on " + date + ".";
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public Label getLabel() {
        return label;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("label", getLabel());
        json.put("date", date);
        json.put("time", time);
        json.put("level", level);
        return json;
    }
}