package model;

import java.time.LocalTime;
import java.util.ArrayList;

// Creates a new user
public class User {
    private String name;
    private ProductivityLog log;

    // EFFECTS: constructs a user with given name and empty energy, focus and motivation lists
    public User(String name) {
        this.name = name;
        log = new ProductivityLog();
    }

    // MODIFIES: this
    // EFFECTS: add given entry to the array it belongs in
    public void addEntry(ProductivityEntry entry) { //must add it to correct slot!!!
        log.add(entry);
    }

    // MODIFIES: this
    // EFFECTS: removes entry from list
    public boolean removeEntry(ProductivityEntry entry) {
        return log.remove(entry);
    }

    // EFFECTS: returns an array list of the user's peak hours
    public ArrayList<LocalTime> getPeakHours(String label) {
        return log.getPeakHours(label);
    }

    // EFFECTS: returns name
    public String getName() {
        return name;
    }

    // EFFECTS: returns energyEntries
    public ArrayList<ProductivityEntry> getEnergyEntries() {
        return log.getEnergyEntries();
    }

    // EFFECTS: returns focusEntries
    public ArrayList<ProductivityEntry> getFocusEntries() {
        return log.getFocusEntries();
    }

    // EFFECTS: returns motivationEntries
    public ArrayList<ProductivityEntry> getMotivationEntries() {
        return log.getMotivationEntries();
    }
}