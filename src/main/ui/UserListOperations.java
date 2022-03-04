package ui;

import model.User;
import model.UserList;
import model.exceptions.InvalidUserException;
import persistence.JsonReadUserList;
import persistence.JsonWriter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class UserListOperations {
    private static final List<String> OPS = Arrays.asList("register", "login", "show users", "quit");

    private UserList users;
    private UserListScanner input;
    private Scanner scanner;

    // EFFECTS: constructs an empty user list and initializes scanner and UserListScanner
    public UserListOperations() {
        scanner = new Scanner(System.in);

        JsonReadUserList reader = new JsonReadUserList();
        try {
            users = new UserList(reader.read()); //!!! read data here
        } catch (IOException e) {
            //maybe write ome?
            //when does this happen???, shouldn't ever happen cuz data/users.json (should) always exist
        }

        input = new UserListScanner(scanner);
        processOperations();
    }

    // MODIFIES: this
    // EFFECTS: processes operations input by users
    private void processOperations() {
        session:
        while (true) {
            String operation = input.validateInput(OPS);
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
            startSessionIfUserValid(user);
        }
    }

    // EFFECTS: if user is not null, starts the session for the user. After session, if user was saved, save user list
    private void startSessionIfUserValid(User user) {
        if (user != null) {
            UserOperations operationRecord = new UserOperations(user, scanner);
            if (operationRecord.wasSaved()) {
                save();
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