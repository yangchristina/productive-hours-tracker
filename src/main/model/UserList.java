package model;

import model.exceptions.InvalidUserException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.JsonReadUser;
import persistence.Writable;

import java.io.IOException;
import java.util.*;

// the list of all users
public class UserList implements Writable {
    public static final String JSON_USER_LIST = "./data/users.json";

    private HashMap<String, UUID> users;

    // EFFECTS: constructs an empty user list
    public UserList(HashMap<String, UUID> userList) {
        users = userList;
    }

    // MODIFIES: this
    // EFFECTS: adds user's name and id to end of users
    public void add(User user) {
        users.put(user.getName(), user.getId());
    }

    public User loadUser(String name) throws InvalidUserException {
        UUID userID = getUserId(name); // throws InvalidUserException if name is invalid

        JsonReadUser reader = new JsonReadUser(userID.toString());
        try {
            return reader.read();
        } catch (IOException e) {
            // this exception shouldn't ever be caught cuz all valid users have a file
            return null; // !!! might want to add an exception here
        }
    }

    // EFFECTS: returns user id from users with given name, null otherwise
    public UUID getUserId(String name) throws InvalidUserException {
        UUID id = users.get(name);
        if (id == null) {
            throw new InvalidUserException();
        }
        return id;
    }

    // EFFECTS: removes user and returns true if user is in list, else returns false
    public boolean remove(User user) {
        return remove(user.getName());
    }

    // EFFECTS: if name is in list, remove it and return true, else return false
    public boolean remove(String name) {
        UUID removed = users.remove(name);
        return removed != null;
    }

    // EFFECTS: returns number of users
    public int size() {
        return users.size();
    }

    // EFFECTS: returns true if user list is empty, else false
    public Boolean isEmpty() {
        return users.isEmpty();
    }

    // EFFECTS: returns names of all users in user list
    public Set<String> getNames() {
        return users.keySet();
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("users", userListToJson());
        return json;
    }

    // EFFECTS: returns users as a JSON array
    private JSONArray userListToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Map.Entry<String, UUID> user : users.entrySet()) {
            jsonArray.put(userInfoToJson(user));
        }

        return jsonArray;
    }

    // EFFECTS: returns user as a json object
    private JSONObject userInfoToJson(Map.Entry<String, UUID> user) {
        JSONObject json = new JSONObject();

        json.put("name", user.getKey());
        json.put("id", user.getValue().toString());

        return json;
    }
}