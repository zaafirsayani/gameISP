import java.io.FileReader;
import java.util.*;
import com.google.gson.*;

public class RoomLoader {
    public Map<String, Room> loadRooms(String filePath) {
        Map<String, Room> rooms = new HashMap<>();
        try {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(new FileReader(filePath), JsonObject.class);

            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                String roomId = entry.getKey();
                JsonObject roomData = entry.getValue().getAsJsonObject();
                

                String name = roomData.get("name").getAsString();
                String description = roomData.get("description").getAsString();
                JsonElement dialogueElement = roomData.get("dialogue");
                String dialogue = (dialogueElement != null) ? dialogueElement.getAsString() : "";

                Map<String, String> exits = new HashMap<>();
                JsonObject exitsJson = roomData.getAsJsonObject("exits");
                for (Map.Entry<String, JsonElement> exit : exitsJson.entrySet()) {
                    exits.put(exit.getKey(), exit.getValue().getAsString());
                }

                List<Pokemon> pokeList = new ArrayList<>();
                JsonArray pokemonsJson = roomData.getAsJsonArray("pokemons");
                for (JsonElement pokeElement : pokemonsJson) {
                    JsonObject pokeObj = pokeElement.getAsJsonObject();
                    String pokeName = pokeObj.get("name").getAsString();
                    String pokeDescription = pokeObj.get("description").getAsString();
                    int pokeHealth = pokeObj.get("hp").getAsInt();
                    int pokeAttack = pokeObj.get("atk").getAsInt();
                    int pokeDefense = pokeObj.get("def").getAsInt();
                    int pokeSpeed = pokeObj.get("spd").getAsInt();
                    
                    JsonObject move1Obj = pokeObj.getAsJsonObject("move1");
                    JsonObject move2Obj = pokeObj.getAsJsonObject("move2");

                    String m1n = move1Obj.get("name").getAsString();
                    String m2n = move2Obj.get("name").getAsString();

                    String m1t = move1Obj.get("type").getAsString();
                    String m2t = move2Obj.get("type").getAsString();

                    Moves m1 = new Moves(m1n, m1t);
                    Moves m2 = new Moves(m2n, m2t);


                    
                    pokeList.add(new Pokemon(pokeName, pokeDescription, pokeHealth, pokeAttack, pokeDefense, pokeSpeed, m1, m2));
                    // String name, int atk, int def, int hp, int spd
                }

                Room room = new Room(roomId, name, description, exits, pokeList, dialogue);
                rooms.put(roomId, room);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rooms;
    }
}
