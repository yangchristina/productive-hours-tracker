package model;

import java.time.LocalDate;
import java.time.LocalTime;

public abstract class ProductivityEntry {
    private LocalDate date;
    private LocalTime time;
    protected int level;

    // EFFECTS: creates a new productivity log given values for date, time and level
    public ProductivityEntry(LocalDate localDate, LocalTime localTime, int level) {
        this.date = localDate; // cannot be modified
        this.time = localTime;
        this.level = level;
    }

    // EFFECTS: returns the type of entry as a string
    public abstract String label();

    // EFFECTS: print out details for the entry with key
    public String description(int key) {
        return label() + " level of " + level + " at " + time.toString() + " on " + date.toString() + ". Key: " + key;
    }

    // EFFECTS: returns details for the entry without key
    public String description() {
        return label() + " level of " + level + " at " + time.toString() + " on " + date.toString() + ".";
    }

    // REQUIRES: time.getMinute() == 0
    // MODIFIES: this
    // EFFECTS: sets the time to the given value
    public void editTime(LocalTime time) {
        this.time = time;
    }

    // REQUIRES: energyLevel is [1, 10]
    // MODIFIES: this
    // EFFECTS: sets the level to the given value
    public void editLevel(int level) {
        this.level = level;
    }

// GETTERS
    // EFFECTS: returns date of energy entry
    public LocalDate getDate() {
        return date;
    }

    // EFFECTS: returns time of day of energy entry
    public LocalTime getTime() {
        return time;
    }

    // EFFECTS: returns energy level
    public int getLevel() {
        return level;
    }
}