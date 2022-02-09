package model;

import java.util.ArrayList;

public class UserList {
    private ArrayList<User> users;

    // EFFECTS: constructs an empty user list
    public UserList() {
        users = new ArrayList<>();
    }

    // REQUIRES: user is not yet in user list, name is not the empty string
    // MODIFIES: this
    // EFFECTS: adds user to end of user list
    public User registerUser(String name) { //put in UserList
        User user = new User(name);
        users.add(user);
        return user;
    }

    // EFFECTS: returns user with the given name, or null if there are no users with name in user list
    public User loginUser(String name) {
        for (User registeredUser : users) {
            if (registeredUser.getName().equals(name)) {
                return registeredUser;
            }
        }
        System.out.println("User not found");
        return null;
    }

    // EFFECTS: lists all users in list
    public void listUsers() { //put in UserList
        System.out.println("The users are: ");
        for (User user : users) {
            System.out.println(user.getName());
        }
    }

    // EFFECTS: returns true if user list is empty, else false
    public Boolean isEmpty() {
        return users.isEmpty();
    }
}
