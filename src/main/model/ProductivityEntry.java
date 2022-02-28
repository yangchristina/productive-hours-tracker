package model;

import java.time.LocalDate;
import java.time.LocalTime;

// abstract class for productivity entries, which contain date, time of day, and level
public abstract class ProductivityEntry {
    private LocalDate date;
    private LocalTime time;
    protected int level;

    // EFFECTS: creates a new productivity entry with given values for date, time and level
    public ProductivityEntry(LocalDate localDate, LocalTime localTime, int level) {
        this.date = localDate; // cannot be modified
        this.time = localTime;
        this.level = level;
    }

    // EFFECTS: returns the type of entry as a string
    public abstract String label();

    // EFFECTS: returns a string with a description of the entry with its key
    public String description(int key) {
        return label() + " level of " + level + " at " + time + " on " + date + ". Key: " + key;
    }

    // EFFECTS: returns a string with a description of the entry
    public String description() {
        return label() + " level of " + level + " at " + time + " on " + date + ".";
    }

    // REQUIRES: time.getMinute() == 0
    // MODIFIES: this
    // EFFECTS: sets the time to the given value
    public void editTime(LocalTime time) {
        this.time = time;
    }

    // REQUIRES: 0 <= level <= 10
    // MODIFIES: this
    // EFFECTS: sets the level to the given value
    public void editLevel(int level) {
        this.level = level;
    }

    // EFFECTS: returns date of entry
    public LocalDate getDate() {
        return date;
    }

    // EFFECTS: returns time of day of entry
    public LocalTime getTime() {
        return time;
    }

    // EFFECTS: returns level of the entry
    public int getLevel() {
        return level;
    }
}