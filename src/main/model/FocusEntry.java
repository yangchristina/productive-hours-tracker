package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class FocusEntry extends ProductivityEntry {
    public FocusEntry(LocalDate localDate, LocalTime localTime, int level) {
        super(localDate, localTime, level);
    }

    @Override
    public String label() {
        return "focus";
    }
}
