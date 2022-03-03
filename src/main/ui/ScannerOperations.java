package ui;

import java.util.List;
import java.util.Scanner;

public class ScannerOperations {
    protected Scanner scanner;

    // EFFECTS: constructs a scanner with given scanner
    public ScannerOperations(Scanner scanner) {
        this.scanner = scanner;
    }

    // EFFECTS: returns input, and makes sure input is a value in inputOptions
    public String validateInput(List<String> inputOptions) {
        StringBuilder message = buildOperationInstructions(inputOptions);
        while (true) {
            System.out.println(message);
            String input = scanner.nextLine();
            if (inputOptions.contains(input)) {
                return input;
            } else {
                System.out.println("Invalid input. Please try again.");
                System.out.println();
            }
        }
    }

    // EFFECTS: connects the strings in inputOptions to create a message of what inputs to enter and returns message
    private StringBuilder buildOperationInstructions(List<String> inputOptions) {
        StringBuilder message = new StringBuilder("Select ");
        for (int i = 0; i < inputOptions.size() - 1; i++) {
            message.append(inputOptions.get(i)).append(", ");
        }
        message.append("or ").append(inputOptions.get(inputOptions.size() - 1)).append(".");
        return message;
    }
}
