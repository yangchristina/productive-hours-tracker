package persistence;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Stream;

// code based on JsonSerializationDemo repository, link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String fileName) {
        this.source = "./data/" + fileName + ".json";
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public User read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseUser(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses user from JSON object and returns it
    private User parseUser(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        UUID id = UUID.fromString(jsonObject.getString("id"));
        JSONObject log = jsonObject.getJSONObject("log");

        ArrayList<ProductivityEntry> energyLog = parseLog(log, "energy");
        ArrayList<ProductivityEntry> focusLog = parseLog(log, "focus");
        ArrayList<ProductivityEntry> motivationLog = parseLog(log, "motivation");

        return new User(name, id, energyLog, focusLog, motivationLog);
    }

    // EFFECTS: parses entry list from JSON array from key of JSON object and returns it
    private ArrayList<ProductivityEntry> parseLog(JSONObject log, String key) {
        JSONArray jsonArray = log.getJSONArray(key);
        ArrayList<ProductivityEntry> list = new ArrayList<>();

        for (Object json : jsonArray) {
            JSONObject entryObject = (JSONObject) json;
            list.add(parseEntry(entryObject, key));
        }
        return list;
    }

    // EFFECTS: parses energy entry from JSON object and adds it to workroom
    private ProductivityEntry parseEntry(JSONObject jsonObject, String key) {
        LocalDate date = LocalDate.parse(jsonObject.getString("date"));
        LocalTime time = LocalTime.parse(jsonObject.getString("time"));
        int level = jsonObject.getInt("level");

        switch (key) {
            case "energy":
                return new EnergyEntry(date, time, level);
            case "focus":
                return new FocusEntry(date, time, level);
            default:
                return new MotivationEntry(date, time, level);
        }
    }
}
