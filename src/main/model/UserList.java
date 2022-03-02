package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// the list of all users
public class UserList implements Writable {
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

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("users", userListToJson());
        return json;
    }

    // EFFECTS: returns things in this workroom as a JSON array
    private JSONArray userListToJson() {
        JSONArray jsonArray = new JSONArray();

        for (User u : users) {
            jsonArray.put(u.idToJson());
        }

        return jsonArray;
    }
}