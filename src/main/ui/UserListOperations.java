package ui;

import model.User;
import model.UserList;

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
        users = new UserList();
        input = new UserListScanner(scanner);
        processOperations();
    }

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
        User user = users.getUserByName(name);
        if (user == null) {
            System.out.println("User not found");
        }
        return user;
    }

    // EFFECTS: lists all users in list
    public void listUsers() { //put in UserList
        System.out.println("The users are: ");
        for (User user : users.getUsers()) {
            System.out.println(user.getName());
        }
        System.out.println();
    }
}