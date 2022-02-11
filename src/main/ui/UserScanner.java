package ui;

import java.time.LocalTime;
import java.util.Scanner;

public class UserScanner {
    private Scanner scanner;

    public UserScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public String operation() {
        String operation = "";
        while (operation.isEmpty()) {
            operation = scanner.nextLine();
        }
        System.out.println("Operation: " + operation);
        return operation;
    }

    public String entryValueToEdit() {
        while (true) {
            System.out.println("Select level, time, or quit");
            String entryValue = scanner.nextLine();
            if (entryValue.equals("level") || entryValue.equals("time") || entryValue.equals("quit")) {
                return entryValue;
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }

    public String entryType() {
        String entryType = "";
        while (!entryType.equals("all") && !entryType.equals("energy") && !entryType.equals("focus")
                && !entryType.equals("motivation") && !entryType.equals("cancel")) {
            System.out.println("Select all, energy, focus, or motivation, or cancel");
            entryType = scanner.nextLine();
        }
        return entryType;
    }

    public int itemKey() {
        System.out.println("Enter key of item");
        return scanner.nextInt();
    }


    // EFFECTS: Asks user to input a time of day and returns it
    public LocalTime time() {
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
    public int level(String entryType) {
        int level;
        while (true) {
            System.out.println("Enter your " + entryType + " level, out of 10");
            try {
                level = scanner.nextInt();
                if (level <= 10 && level >= 0) {
                    return level;
                }

            } catch (Exception e) {
                System.out.println("Invalid " + entryType + " level, please try again.");
                continue;
            }
            System.out.println("Invalid " + entryType + " level, please try again.");
        }
    }
}
