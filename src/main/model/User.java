package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.UUID;

// Creates a new user
public class User extends DailyAverageLog implements Writable {
    private String name;
    private UUID id;

    // EFFECTS: constructs a user with given name, a random id, and empty energy, focus and motivation lists
    public User(String name) {
        this.name = name;
        id = UUID.randomUUID();
    }

    // EFFECTS: constructs a user with given name and id, and empty energy, focus and motivation lists
    public User(String name, UUID id, ArrayList<ProductivityEntry> energy,
                ArrayList<ProductivityEntry> focus, ArrayList<ProductivityEntry> motivation) {
        super(energy, focus, motivation);
        this.name = name;
        this.id = id;
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
}