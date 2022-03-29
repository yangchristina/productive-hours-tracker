package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.UUID;

// A user, with a productivityLog, name and id
public class User implements Writable {
    private String name;
    private UUID id;
    private ProductivityLog productivityLog;

    // EFFECTS: constructs a user with given name, a random id, and an empty ProductivityLog
    public User(String name) {
        this.name = name;
        id = UUID.randomUUID();
        productivityLog = new ProductivityLog(this);
    }

    // EFFECTS: constructs a user with given name and id, and a productivityLog with the given entries
    public User(String name, UUID id, ArrayList<ProductivityEntry> entries) {
        this.name = name;
        this.id = id;
        productivityLog = new ProductivityLog(this, entries);
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