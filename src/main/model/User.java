package model;

import ui.UserListScanner;

import java.util.ArrayList;

// contains functions for adding, editing, deleting entries
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


// Add entry
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
        try {
            switch (entry.label()) {
                case "energy":
                    return energyEntries.remove(entry);
                case "focus":
                    return focusEntries.remove(entry);
                case "motivation":
                    return motivationEntries.remove(entry);
            }
        } catch (Exception e) {
            return false;
        }
        return false;
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