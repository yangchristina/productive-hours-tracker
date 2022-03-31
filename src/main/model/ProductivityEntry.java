package model;

import org.json.JSONObject;
import persistence.Writable;

import java.time.LocalDate;
import java.time.LocalTime;

// class for productivity entries, which contain label, date, time of day, and level
public class ProductivityEntry extends Subject implements Writable  {
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

    @Override
    // EFFECTS: returns a string with a description of the entry
    public String toString() {
        return getLabel() + " level of " + level + " at " + time + " on " + date + ".";
    }

    // EFFECTS: calls setter methods to edit productivity entry
    public void edit(Label label, LocalTime time, int level) {
        ProductivityEntry old = new ProductivityEntry(this.label, this.date, this.time, this.level);

        this.label = label;
        this.time = time;
        this.level = level;

        super.notifyObservers(this, old);
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