package ui;

import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class UserScanner {
    private Scanner scanner;

    // EFFECTS: constructs a scanner with given scanner
    public UserScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    // EFFECTS: displays and returns operation inputted
    public String operation() {
        String operation = "";
        while (operation.isEmpty()) {
            operation = scanner.nextLine();
        }
        System.out.println("Operation: " + operation);
        return operation;
    }

    public String validateInput(List<String> inputOptions) {
        StringBuilder message = new StringBuilder("Select ");
        for (int i = 0; i < inputOptions.size() - 1; i++) {
            message.append(inputOptions.get(i)).append(", ");
        }
        message.append("or ").append(inputOptions.get(inputOptions.size() - 1)).append(".");

        while (true) {
            System.out.println(message);
            String input = scanner.nextLine();
            if (inputOptions.contains(input)) {
                return input;
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }

    // EFFECTS: returns the key value of an entry
    public int itemKey() {
        System.out.println("Enter key of item");
        int key = scanner.nextInt();
        scanner.nextLine();
        return key;
    }

    // EFFECTS: Asks user to input a time of day and returns it
    public LocalTime time() {
        while (true) {
            System.out.println("Enter -1 to select right now, otherwise enter the hour [0, 23] you would like to log)");
            int hour = scanner.nextInt();
            scanner.nextLine();
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
