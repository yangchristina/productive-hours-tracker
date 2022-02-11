package ui;

import java.util.Scanner;

public class UserListScanner {
    private Scanner scanner;

    // EFFECTS: constructs a scanner with given scanner
    public UserListScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    // EFFECTS: displays and returns operation inputted
    public String operation() {
        String operation = scanner.nextLine();
        System.out.println("Operation: " + operation);
        return operation;
    }

    // EFFECTS: asks for and returns input value for name
    public String name() {
        String name;
        while (true) {
            System.out.println("Enter name");
            name = scanner.nextLine();
            if (!name.isEmpty()) {
                return name;
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }
}
