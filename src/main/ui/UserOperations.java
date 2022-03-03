package ui;

import model.*;
import persistence.JsonWriter;
import ui.exceptions.InvalidInputException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

// calls user methods through scanner inputs
public class UserOperations {
    private User user;
    private UserScanner input;

    // EFFECTS: constructs a UserOperations with a given user and a UserScanner
    public UserOperations(User user, Scanner scanner) {
        this.user = user;
        input = new UserScanner(scanner);
        System.out.println("Welcome " + user.getName() + "!");
        processOperations();
        System.out.println("End operations");
    }

    // EFFECTS: prints out a message for when the input is invalid
    private void invalidInputMessage() {
        System.out.println("Invalid input. Please try again");
    }

    @SuppressWarnings("methodlength")
    // EFFECTS: calls a method depending on the input value
    private void processOperations() {
        String operation;

        session:
        while (true) {
            System.out.println("Type command or enter help to see commands:");
            operation = input.operation();

            switch (operation) {
                case "logout":
                    promptSave();
                    break session;
                case "add":
                    addEntries();
                    break;
                case "peak":
                    showPeakHours();
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
                case "save":
                    save();
                    break;
                case "help":
                    System.out.println("logout, add, peak, show, edit, delete, save, help");
                    break;
            }
            System.out.println();
        }
    }

    // MODIFIES: this
    // EFFECTS: lets the user enter values to create a new productivity entry, and add it  to the user's log
    private void addEntries() {
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = input.time();

        int energyLevel = input.level("energy");
        EnergyEntry energyEntry = new EnergyEntry(localDate, localTime, energyLevel);

        int focusLevel = input.level("focus");
        FocusEntry focusEntry = new FocusEntry(localDate, localTime, focusLevel);

        int motivationLevel = input.level("motivation");
        MotivationEntry motivationEntry = new MotivationEntry(localDate, localTime, motivationLevel);

        user.add(energyEntry);
        user.add(focusEntry);
        user.add(motivationEntry);

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
    // EFFECTS: removes selected entry from the productivity log
    public void removeEntry() {
        System.out.println("Operation: remove");
        ProductivityEntry entry = selectEntry();
        boolean isRemoved = user.remove(entry);

        if (isRemoved) {
            System.out.println("Removed " + entry.description());
        }
    }

    // EFFECTS: saves user to file
    public void save() {
        try {
            JsonWriter writer = new JsonWriter(user.getId().toString());

            writer.open();
            writer.write(user);
            writer.close();
        } catch (IOException e) {
            // idk what yet, shouldn't every be thrown cuz no illegal file names, all filenames are uuid
        }
    }

    // asks user if they would like to save their session
    // EFFECTS: if user inputs true, save session, if false don't save, else ask again
    private void promptSave() {
        System.out.println("Would you like to save?");
        try {
            boolean ans = input.yesOrNo();
            if (ans) {
                save();
            }
        } catch (InvalidInputException e) {
            promptSave();
        }
    }

    // EFFECTS: shows the user's peak hours for either focus, energy, or motivation, depending on the user's input
    private void showPeakHours() {
        String label = input.entryType();
        ArrayList<LocalTime> peakHours = user.getPeakHours(label);
        if (peakHours == null) {
            System.out.println("Not enough " + label + " entries");
        } else {
            System.out.println("Your peak " + label + " hours are at " + peakHours);
        }
    }

    // EFFECTS: shows the user log indicated by the input options, by calling a method depending on the input
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

    // EFFECTS: prints out the given arraylist for the user to see
    private void showEntries(ArrayList<ProductivityEntry> productivityEntries) {
        if (productivityEntries.isEmpty()) {
            return;
        }
        System.out.println(productivityEntries.get(0).label() + " entries:");
        int key = 1;
        for (ProductivityEntry entry : productivityEntries) {
            System.out.println(entry.description(key));
            key++;
        }
    }

    // EFFECTS: shows details for all entries
    private void showAllEntries() {
        System.out.println("Your entries are: ");
        showAllEnergyEntries();
        showAllFocusEntries();
        showAllMotivationEntries();
        System.out.println();
    }

    // EFFECTS: shows details for all energy logs
    private void showAllEnergyEntries() { // !!!
        showEntries(user.getEnergyEntries());
    }

    // EFFECTS: shows details for all focus logs
    private void showAllFocusEntries() {
        showEntries(user.getFocusEntries());
    }

    // EFFECTS: shows details for all motivation logs
    private void showAllMotivationEntries() {
        showEntries(user.getMotivationEntries());
    }

}
