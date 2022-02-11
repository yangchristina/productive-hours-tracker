package ui;

import model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class UserOperations {
    private User user;
    private UserScanner input;

    public UserOperations(User user, Scanner scanner) {
        this.user = user;
        input = new UserScanner(scanner);
        System.out.println("Welcome " + user.getName() + "!");
        processOperations();
        System.out.println("End operations");
    }

    private void invalidInputMessage() {
        System.out.println("Invalid input. Please try again");
    }

    @SuppressWarnings("methodlength")
    private void processOperations() {
        String operation;

        session:
        while (true) {
            System.out.println("Type command or enter help to see commands:");
            operation = input.operation();

            switch (operation) {
                case "logout":
                    break session;
                case "add":
                    addEntries();
                    break;
                case "peak":
                    showBPT();
                    break;
                case "trough":
                    // ...
                    break;
                case "show":
                    processShowEntries();
                    break;
                case "edit":
                    editEntry();
                    break;
                case "delete":
                    removeEntry();
                    break;
                case "help":
                    System.out.println("logout, add, peak, trough, show, edit, delete, help");
                    break;
            }
            System.out.println();
        }
    }

    // EFFECTS:
    private void addEntries() {
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = input.time();

        int energyLevel = input.level("energy");
        EnergyEntry energyEntry = new EnergyEntry(localDate, localTime, energyLevel);

        int focusLevel = input.level("focus");
        FocusEntry focusEntry = new FocusEntry(localDate, localTime, focusLevel);

        int motivationLevel = input.level("motivation");
        MotivationEntry motivationEntry = new MotivationEntry(localDate, localTime, motivationLevel);

        user.addEntry(energyEntry);
        user.addEntry(focusEntry);
        user.addEntry(motivationEntry);

        System.out.println("You have added " + energyEntry.description());
        System.out.println("You have added " + focusEntry.description());
        System.out.println("You have added " + motivationEntry.description());
    }

    // EFFECTS: selects an entry based on category and key input by user
    private ProductivityEntry selectEntry() {
        String[] inputOptions = new String[]{"energy", "focus", "motivation", "cancel"};
        List<String> options = Arrays.asList(inputOptions);

        while (true) {
            String category = input.validateInput(options);
            System.out.println(category);
            int key = input.itemKey();

            try {
                switch (category) {
                    case "energy":
                        return user.getEnergyEntries().get(key - 1);
                    case "focus":
                        return user.getFocusEntries().get(key - 1);
                    case "motivation":
                        return user.getMotivationEntries().get(key - 1);
                    default:
                        return null;
                }
            } catch (Exception e) {
                System.out.println("exception");
                invalidInputMessage();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: edits selected entry according to user inputs
    private void editEntry() {
        ProductivityEntry entry = selectEntry();

        edit:
        while (true) {
            String[] inputOptions = new String[]{"level", "time", "quit"};
            List<String> options = Arrays.asList(inputOptions);

            String entryValue = input.validateInput(options);
            switch (entryValue) {
                case "quit":
                    break edit;
                case "level":
                    String label = entry.label();
                    int level = input.level(label);
                    entry.editLevel(level);
                    break;
                case "time":
                    LocalTime time = input.time();
                    entry.editTime(time);
                    break;
            }
        }
        System.out.println("Entry is now: " + entry.description());
    }

    // MODIFIES: this
    // EFFECTS: removes selected entry from its array list
    public void removeEntry() {
        System.out.println("Operation: remove");
        ProductivityEntry entry = selectEntry();
        boolean isRemoved = user.removeEntry(entry);

        if (isRemoved) {
            System.out.println("Removed " + entry.description());
        }
    }

    // EFFECTS: calculates and shows your biological prime time
    private void showBPT() {
        System.out.println("Your biological prime time is from ___ to ___");
        // ...
    }

// Show entries
    private void processShowEntries() {
        String[] inputOptions = new String[]{"all", "energy", "focus", "motivation"};
        List<String> options = Arrays.asList(inputOptions);

        String entryType = input.validateInput(options);
        switch (entryType) {
            case "all":
                showAllEntries();
                break;
            case "energy":
                showAllEnergyEntries();
                break;
            case "focus":
                showAllFocusEntries();
                break;
            case "motivation":
                showAllMotivationEntries();
                break;
        }
    }

    private void showEntries(ArrayList<ProductivityEntry> productivityEntries) {
        System.out.println(productivityEntries.get(0).label() + " entries:");
        int key = 1;
        for (ProductivityEntry entry : productivityEntries) {
            System.out.println(entry.description(key));
            key++;
        }
    }

    // EFFECTS: shows details for all entries
    private void showAllEntries() {
        showAllEnergyEntries();
        showAllFocusEntries();
        showAllMotivationEntries();
    }

    // EFFECTS: shows details for all energy logs !!! try to make these sorted by date??
    private void showAllEnergyEntries() { // !!!
        showEntries(user.getEnergyEntries());
    }

    // EFFECTS: shows details for all focus logs !!! try to make these sorted by date??
    private void showAllFocusEntries() {
        showEntries(user.getFocusEntries());
    }

    // EFFECTS: shows details for all motivation logs !!! try to make these sorted by date??
    private void showAllMotivationEntries() {
        showEntries(user.getMotivationEntries());
    }



}
