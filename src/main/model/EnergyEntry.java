package model;

import java.time.LocalDate;
import java.time.LocalTime;

// Entries that hold information about energy levels
public class EnergyEntry extends ProductivityEntry {
    public EnergyEntry(LocalDate localDate, LocalTime localTime, int level) {
        super(localDate, localTime, level);
    }

    @Override
    public String label() {
        return "energy";
    }
}