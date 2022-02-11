package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class MotivationEntry extends ProductivityEntry {
    public MotivationEntry(LocalDate localDate, LocalTime localTime, int level) {
        super(localDate, localTime, level);
    }

    @Override
    public String label() {
        return "motivation";
    }
}
