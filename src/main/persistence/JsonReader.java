package persistence;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// code based on JsonSerializationDemo repository, link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    protected String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads source file as string and returns it
    protected JSONObject readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return new JSONObject(contentBuilder.toString());
    }
}
