package model;

import java.util.ArrayList;
import java.util.Scanner;

// contains functions for adding, editing, deleting entries
public class User {
    private String name;
    private ArrayList<ProductivityEntry> energyEntries;
    private ArrayList<ProductivityEntry> motivationEntries;
    private ArrayList<ProductivityEntry> focusEntries;
    private Scanner scanner;

    // EFFECTS: constructs a user with given name and empty energy, focus and motivation lists
    public User(String name) {
        this.name = name;
        this.energyEntries = new ArrayList<>();
        this.focusEntries = new ArrayList<>();
        this.motivationEntries = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    // EFFECTS: calculates and shows your biological prime time
    public void showBPT() {
        System.out.println("Your biological prime time is from ___ to ___");
        // ...
    }

// Show entries
    public void showEntries(ArrayList<ProductivityEntry> productivityEntries) {
        System.out.println(productivityEntries.get(0).label() + " entries:");
        int key = 1;
        for (ProductivityEntry entry : productivityEntries) {
            entry.showDetails(key);
            key++;
        }
    }

    // EFFECTS: shows details for all energy logs !!! try to make these sorted by date??
    public void showAllEnergyEntries() { // !!!
        showEntries(energyEntries);
    }

    // EFFECTS: shows details for all focus logs !!! try to make these sorted by date??
    public void showAllFocusEntries() {
        showEntries(focusEntries);
    }

    // EFFECTS: shows details for all motivation logs !!! try to make these sorted by date??
    public void showAllMotivationEntries() {
        showEntries(motivationEntries);
    }

    // EFFECTS: shows details for all entries
    public void showAllEntries() {
        showAllEnergyEntries();
        showAllFocusEntries();
        showAllMotivationEntries();
    }

// Add entry
    // MODIFIES: this
    // EFFECTS: add given energy entry to energyEntries
    public void addEnergyEntry() {
        EnergyEntry energyEntry = new EnergyEntry();
        this.energyEntries.add(energyEntry);
    }

    // MODIFIES: this
    // EFFECTS: add given energy entry to energyEntries
    public void addFocusEntry() {
        FocusEntry focusEntry = new FocusEntry();
        this.focusEntries.add(focusEntry);
    }

    // MODIFIES: this
    // EFFECTS: add given energy entry to energyEntries
    public void addMotivationEntry() {
        MotivationEntry motivationEntry = new MotivationEntry();
        this.motivationEntries.add(motivationEntry);
    }

    // EFFECTS: selects an entry based on category and key input by user
    private ProductivityEntry selectEntry() {
        while (true) {
            System.out.println("Select energy, focus, or motivation, or cancel");
            String category = scanner.nextLine();
            System.out.println("Enter key of item");
            int key = scanner.nextInt();

            if (category.equals("energy") && key <= energyEntries.size() && key > 0) {
                return energyEntries.get(key - 1);
            } else if (category.equals("focus") && key <= focusEntries.size() && key > 0) {
                return focusEntries.get(key - 1);
            } else if (category.equals("motivation") && key <= motivationEntries.size() && key > 0) {
                return motivationEntries.get(key - 1);
            } else if (!category.equals("cancel")) {
                System.out.println("Invalid input. Please try again");
                continue;
            }
            break;
        }
        return null;
    }

    // MODIFIES: this
    // EFFECTS: removes given entry from its array list
    private void removeEntry() {
        System.out.println("Operation: remove");
        ProductivityEntry entry = selectEntry();
        boolean isRemoved = Boolean.FALSE;

        switch (entry.label()) {
            case "energy":
                isRemoved = energyEntries.remove(entry);
                break;
            case "focus":
                isRemoved = focusEntries.remove(entry);
                break;
            case "motivation":
                isRemoved = motivationEntries.remove(entry);
                break;
        }

        if (isRemoved) {
            System.out.println("Removed " + entry.showDetails());
        }
    }

    // MODIFIES: this
    // EFFECTS: edits selected entry according to user inputs
    private void editEntry() {
        System.out.println("Operation: edit");
        ProductivityEntry entry = selectEntry();

        edit:
        while (true) {
            System.out.println("Select level, time, or quit");
            String operation = scanner.nextLine();
            switch (operation) {
                case "quit":
                    break edit;
                case "level":
                    entry.editLevel();
                    break;
                case "time":
                    entry.editTime();
                    break;
            }
        }
        System.out.println("Entry is now: " + entry.showDetails());
    }

    // EFFECTS: returns name
    public String getName() {
        return name;
    }

    // EFFECTS: returns energyEntries
    public ArrayList<ProductivityEntry> getEnergyEntries() {
        return energyEntries;
    }

    // EFFECTS: returns focusEntries
    public ArrayList<ProductivityEntry> getFocusEntries() {
        return focusEntries;
    }

    // EFFECTS: returns motivationEntries
    public ArrayList<ProductivityEntry> getMotivationEntries() {
        return motivationEntries;
    }
}