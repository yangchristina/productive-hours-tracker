package model;

import java.time.LocalDate;
import java.time.LocalTime;

// Entries that hold information about motivation levels
public class MotivationEntry extends ProductivityEntry {
    public MotivationEntry(LocalDate localDate, LocalTime localTime, int level) {
        super(localDate, localTime, level);
    }

    @Override
    public Label getLabel() {
        return Label.MOTIVATION;
    }
}
