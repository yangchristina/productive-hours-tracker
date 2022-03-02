package model;

import org.json.JSONObject;
import persistence.Writable;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;

// Creates a new user
public class User extends ProductivityLog implements Writable {
    private String name;
    private UUID id;

    // EFFECTS: constructs a user with given name and empty energy, focus and motivation lists
    public User(String name) {
        this.name = name;
        id = UUID.randomUUID();
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("id", id);
        json.put("log", super.toJson());
        return json;
    }

    public JSONObject idToJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("id", id);
        return json;
    }
}