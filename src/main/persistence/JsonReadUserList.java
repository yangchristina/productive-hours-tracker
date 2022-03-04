package persistence;

import model.UserList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

// code based on JsonSerializationDemo repository, link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
// Reads data for user list from user.json
public class JsonReadUserList extends JsonReader {

    public JsonReadUserList() {
        super(UserList.JSON_USER_LIST);
    }

    // EFFECTS: reads user list from file, parses it, and returns it;
    // throws IOException if an error occurs reading data from file
    public HashMap<String, UUID> read() throws IOException {
        JSONArray jsonArray = readFile(source).getJSONArray("users");
        HashMap<String, UUID> map = new HashMap<>();
        for (Object json : jsonArray) {
            JSONObject entryObject = (JSONObject) json;
            map.put(entryObject.getString("name"), UUID.fromString(entryObject.getString("id")));
        }
        return map;
    }
}