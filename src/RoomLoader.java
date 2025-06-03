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

                    JsonArray movesArr = pokeObj.getAsJsonArray("moves");
                    List<Moves> movesList = new ArrayList<>();

                    for(int i = 0; i < movesArr.size(); i++){
                        JsonObject moveObj = movesArr.get(i).getAsJsonObject();
                        String moveName = moveObj.get("name").getAsString();
                        String moveType = moveObj.get("type").getAsString();
                        movesList.add(new Moves(moveName, moveType));
                    }

                    // Fallbacks if there are fewer than 2 moves (optional depending on how your game handles it)
                    Moves m1 = movesList.size() > 0 ? movesList.get(0) : new Moves("Tackle", "Normal");
                    Moves m2 = movesList.size() > 1 ? movesList.get(1) : new Moves("Growl", "Normal");


                    pokeList.add(new Pokemon(pokeName, pokeDescription, pokeHealth, pokeAttack, pokeDefense, pokeSpeed, m1, m2));
                    
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
