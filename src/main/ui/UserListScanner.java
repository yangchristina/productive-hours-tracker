package ui;

import java.util.Scanner;

public class UserListScanner extends ScannerOperations {

    // EFFECTS: constructs a scanner with given scanner
    public UserListScanner(Scanner scanner) {
        super(scanner);
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
