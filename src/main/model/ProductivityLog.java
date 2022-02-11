package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public abstract class ProductivityLog {
    private LocalDate date;
    private LocalTime time;
    protected int level;
    protected Scanner scanner;

    // EFFECTS: creates a new productivity log with current date and uses scanner to take in user entered values
    // for time and level
    public ProductivityLog() {
        scanner = new Scanner(System.in);
        this.date = LocalDate.now(); // cannot be modified
        this.time = enterTime();
        this.level = enterLevel();
    }

    // Effect: returns the label for the log
    public abstract String label();

    // EFFECTS: print out details for the entry
    public void showDetails() {
        System.out.println(label() + " level of " + level + " at " + time.toString() + " on " + date.toString());
    }

    // MODIFIES: this
    // EFFECTS: changes value hour of energy log
    public void editTime() {
        this.time = enterTime();
    }

    // REQUIRES: energyLevel is [1, 10]
    // MODIFIES: this
    // EFFECTS: changes value of energy level
    public void editLevel() {
        this.level = enterLevel();
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

// Scanner operations
    // EFFECTS: Asks user to input a time of day and returns it
    private LocalTime enterTime() {
        while (true) {
            System.out.println("Enter -1 to select right now, otherwise enter the hour [0, 23] you would like to log)");
            int hour = scanner.nextInt();
            if (hour == -1) {
                return LocalTime.of(LocalTime.now().getHour(), 0);
            } else if (hour >= 0 && hour < 24) {
                return LocalTime.of(hour, 0);
            } else {
                System.out.println("Invalid hour. Please try again.");
            }
        }
    }

    // EFFECTS: Asks user to input value for level and returns it
    private int enterLevel() {
        while (true) {
            System.out.println("Enter your " + label() + " level, out of 10");
            level = scanner.nextInt();
            if (level <= 10 && level >= 0) {
                return level;
            } else {
                System.out.println("Invalid " + label() + " level, please try again.");
            }
            scanner.nextLine();
        }
    }
}