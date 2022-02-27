package ui;

import java.time.LocalTime;
import java.util.Scanner;

public class UserScanner extends ScannerOperations {
    public UserScanner(Scanner scanner) {
        super(scanner);
    }

    // EFFECTS: returns the key value of an entry
    public int itemKey() {
        System.out.println("Enter key of item");
        int key = scanner.nextInt();
        scanner.nextLine();
        return key;
    }

    // EFFECTS: returns the key value of an entry
    public String entryType() {
        while (true) {
            System.out.println("Select energy, focus, or motivation");
            String label = scanner.nextLine();
            if (label.equals("energy") || label.equals("focus") || label.equals("motivation")) {
                return label;
            }
            System.out.println("Invalid entry. Please try again");
            System.out.println();
        }
    }

    // EFFECTS: Asks user to input a time of day and returns it
    public LocalTime time() {
        while (true) {
            try {
                System.out.println("Enter -1 to select right now, otherwise enter the hour [0, 23])");
                int hour = scanner.nextInt();
                scanner.nextLine();
                if (hour == -1) {
                    return LocalTime.of(LocalTime.now().getHour(), 0);
                } else if (hour >= 0 && hour < 24) {
                    return LocalTime.of(hour, 0);
                } else {
                    System.out.println("Invalid hour. Please try again.");
                    System.out.println();
                }
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("Invalid hour. Please try again.");
                System.out.println();
            }
        }
    }

    // EFFECTS: Asks user to input value for level and returns it
    public int level(String entryType) {
        while (true) {
//            int level;
            System.out.println("Enter your " + entryType + " level, out of 10");
            try {
                int level = scanner.nextInt();
                scanner.nextLine();
                if (level <= 10 && level >= 0) {
                    return level;
                } else {
                    System.out.println("Invalid " + entryType + " level, please try again.");
                }
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("Invalid " + entryType + " level, please try again.");
            }
        }
    }
}
