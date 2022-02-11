package ui;

import model.User;
import model.UserList;

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
        String operation;
        String loginMessage = "";
        while (true) {
            User user = null;
            if (!users.isEmpty()) {
                loginMessage = "login, show users, ";
            }
            System.out.println("Please select an option (register, " + loginMessage + "or quit):");
            operation = input.operation();
            if (operation.equals("quit")) {
                break;
            } else if (operation.equals("register")) {
                user = registerUser();
            } else if (operation.equals("login") && !users.isEmpty()) {
                user = loginUser();
            } else if (operation.equals("show users") && !users.isEmpty()) {
                listUsers();
            }
            if (user != null) {
                UserOperations userOperations = new UserOperations(user, scanner);
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
    }
}