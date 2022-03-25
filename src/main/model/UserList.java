package model;

import model.exceptions.InvalidUserException;
import model.exceptions.UserAlreadyExistsException;
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

    // EFFECTS: constructs a user list with a given users
    public UserList(HashMap<String, UUID> users) {
        this.users = users;
    }

    // EFFECTS: if user is not in users, then call add method to add it to users, else throw UserAlreadyExistsException
    public void register(User user) throws UserAlreadyExistsException {
        if (users.get(user.getName()) != null) {
            throw new UserAlreadyExistsException();
        }
        add(user);
    }

    // MODIFIES: this
    // EFFECTS: adds an entry to users where the user's name is the key, and the user's id is the value
    public void add(User user) {
        users.put(user.getName(), user.getId());
    }

    // EFFECTS: loads user from file by name and returns it
    public User loadUser(String name) throws InvalidUserException {
        UUID userID = getUserId(name); // throws InvalidUserException if name is invalid

        JsonReadUser reader = new JsonReadUser(userID.toString());
        try {
            return reader.read();
        } catch (IOException e) {
            return null; // this exception shouldn't ever be caught cuz all valid users have a file
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