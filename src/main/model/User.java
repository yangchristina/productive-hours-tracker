package model;

import ui.UserListScanner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ValueRange;
import java.util.ArrayList;

public class User {
    private String name;
    private ArrayList<ProductivityEntry> energyEntries;
    private ArrayList<ProductivityEntry> motivationEntries;
    private ArrayList<ProductivityEntry> focusEntries;

    // EFFECTS: constructs a user with given name and empty energy, focus and motivation lists
    public User(String name) {
        this.name = name;
        this.energyEntries = new ArrayList<>();
        this.focusEntries = new ArrayList<>();
        this.motivationEntries = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: add given entry to the array it belongs in
    public ProductivityEntry addEntry(ProductivityEntry entry) {
        String entryType = entry.label();
        switch (entryType) {
            case "energy":
                energyEntries.add(entry);
                break;
            case "focus":
                focusEntries.add(entry);
                break;
            case "motivation":
                motivationEntries.add(entry);
                break;
        }
        return entry;
    }

    // MODIFIES: this
    // EFFECTS: removes entry from list
    public boolean removeEntry(ProductivityEntry entry) {
        switch (entry.label()) {
            case "energy":
                return energyEntries.remove(entry);
            case "focus":
                return focusEntries.remove(entry);
            default:
                return motivationEntries.remove(entry);
        }
    }

    // EFFECTS: calculates and returns your biological prime time (calculation method needs work)
    public LocalTime calculateBPT() {
        int averageHour = (peakHour(energyEntries).getHour() + peakHour(focusEntries).getHour()
                + peakHour(motivationEntries).getHour()) / 3;

        return LocalTime.of(averageHour, 0);
    }

    // EFFECTS: returns the first value for time with the max value for level
    private LocalTime peakHour(ArrayList<ProductivityEntry> entries) {
        ProductivityEntry maxEntry = entries.get(0);
        for (ProductivityEntry entry : entries) {
            if (entry.getLevel() > maxEntry.getLevel()) {
                maxEntry = entry;
            }
        }
        return maxEntry.getTime();
    }

    // EFFECTS: returns name
    public String getName() {
        return name;
    }

    // EFFECTS: returns energyEntries
    public ArrayList<ProductivityEntry> getEnergyEntries() {
        return energyEntries;
    }

    // EFFECTS: returns focusEntries
    public ArrayList<ProductivityEntry> getFocusEntries() {
        return focusEntries;
    }

    // EFFECTS: returns motivationEntries
    public ArrayList<ProductivityEntry> getMotivationEntries() {
        return motivationEntries;
    }
}