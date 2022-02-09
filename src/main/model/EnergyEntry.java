package model;

import java.util.Date;

public class EnergyEntry {
    private Date date;
    private int energyLevel;

    // REQUIRES: date earlier than current time, energy level [1,10]
    // EFFECTS: constructs a user's energy level entry with the date for the entry and the energy level recorded
    public EnergyEntry(Date date, int energyLevel) { //either Date date or String date
        this.date = date; // or this.date = new Date(date); where date is type long
        this.energyLevel = energyLevel;
    }

    // MODIFIES: this
    // EFFECTS: changes value of date
    public void editDate(Date date) {
        this.date = date;
    }

    // REQUIRES: energyLevel is [1, 10]
    // MODIFIES: this
    // EFFECTS: changes value of energy level
    public void editEnergyLevel(int energyLevel) {
        this.energyLevel = energyLevel;
    }

    // EFFECTS: returns date of energy entry
    public Date getDate() {
        return date;
    }

    // EFFECTS: returns energy level
    public int getEnergyLevel() {
        return energyLevel;
    }
}