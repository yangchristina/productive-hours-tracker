package model;

import ui.UserListScanner;

import java.util.ArrayList;

public class UserList {
    private ArrayList<User> users;

    // EFFECTS: constructs an empty user list
    public UserList() {
        users = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds user to end of users
    public void add(User user) {
        users.add(user);
    }

    // EFFECTS: returns user from users with given name, null otherwise
    public User getUserByName(String name) {
        for (User user : users) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    // EFFECTS: returns number of users
    public int size() {
        return users.size();
    }

    // EFFECTS: returns true if user list is empty, else false
    public Boolean isEmpty() {
        return users.isEmpty();
    }

    // EFFECTS: returns users
    public ArrayList<User> getUsers() {
        return users;
    }
}