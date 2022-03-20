package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.UUID;

// Creates a new user
public class User implements Writable {
    private String name;
    private UUID id;
    private ProductivityLog productivityLog;

    // EFFECTS: constructs a user with given name, a random id, and empty energy, focus and motivation lists
    public User(String name) {
        this.name = name;
        id = UUID.randomUUID();
        productivityLog = new ProductivityLog();
    }

    // EFFECTS: constructs a user with given name, id, energy, focus and motivation lists, and
    //          a daily average log based on energy, focus, and motivation values
    public User(String name, UUID id, ArrayList<ProductivityEntry> entries) {
        this.name = name;
        this.id = id;
        productivityLog = new ProductivityLog(entries);
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public ProductivityLog getProductivityLog() {
        return productivityLog;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = productivityLog.toJson();
        json.put("name", name);
        json.put("id", id);
        return json;
    }
}