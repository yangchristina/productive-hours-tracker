package ui;

import model.User;
import model.UserList;
import model.exceptions.InvalidUserException;
import persistence.JsonWriter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class UserListOperations {
    private UserList users;
    private UserListScanner input;
    private Scanner scanner;

    // EFFECTS: constructs an empty user list and initializes scanner and UserListScanner
    public UserListOperations() {
        scanner = new Scanner(System.in);
        users = new UserList(); //!!! read data here
        input = new UserListScanner(scanner);
        processOperations();
        save();
    }

    // MODIFIES: this
    // EFFECTS: processes commands input by users
    private void processOperations() {
        String[] inputOptions = new String[]{"register", "login", "show users", "quit"};
        List<String> options = Arrays.asList(inputOptions);

        session:
        while (true) {
            String operation = input.validateInput(options);
            User user = null;
            switch (operation) {
                case "quit":  //end process
                    break session;
                case "register":
                    user = registerUser(); // ask again
                    break;
                case "login":
                    user = loginUser();
                    break;
                case "show users":
                    listUsers();
                    continue;
            }
            if (user != null) {
                new UserOperations(user, scanner);
            }
        }
    }

    // REQUIRES: user is not yet in user list, name is not the empty string
    // MODIFIES: this
    // EFFECTS: adds user to end of user list
    public User registerUser() { //put in UserList
        String name = input.name();
        User user = new User(name);
        users.add(user);
        return user;
    }

    // EFFECTS: returns user with the given name, or null if there are no users with name in user list
    public User loginUser() {
        String name = input.name();

        try {
            return users.loadUser(name);
        } catch (InvalidUserException e) { // create this exception
            System.out.println("User not found");
            return null;
        }
    }

    // EFFECTS: lists all users in list
    public void listUsers() { //put in UserList
        System.out.println("\nThe users are: ");

        for (String name : users.getNames()) {
            System.out.println("\t - " + name);
        }

        System.out.println();
    }

    // EFFECTS: saves user list to file
    public void save() {
        try {
            JsonWriter writer = new JsonWriter("users");

            writer.open();
            writer.write(users);
            writer.close();
        } catch (IOException e) {
            // !!!
        }
    }
}