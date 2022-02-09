package model;

import java.util.ArrayList;

public class User {
    private String name;
    private ArrayList<EnergyEntry> energyEntries;
    //can have List<Temperature Entry> as well

    // EFFECTS: constructs a user with given name and empty energy entry list
    public User(String name) {
        this.name = name;
        this.energyEntries = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: add given energy entry to energyEntries
    public void addEnergyEntry(EnergyEntry energyEntry) {
        this.energyEntries.add(energyEntry);
    }

    // MODIFIES: this
    // EFFECTS: removes given energyEntry from energyEntries
    public void removeEnergyEntry(EnergyEntry energyEntry) {
        this.energyEntries.add(energyEntry);
    }

    // EFFECTS: returns name
    public String getName() {
        return name;
    }

    // EFFECTS: returns energyEntries
    public ArrayList<EnergyEntry> getEnergyEntries() {
        return energyEntries;
    }
}