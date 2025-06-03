import java.util.List;
import java.util.Map;

public class Room {
    private String id;
    private String name;
    private String description;
    private String dialogue;
    private Map<String, String> exits; // direction → roomId
    private List<Pokemon> pokemons;

    public Room(String id, String name, String description, Map<String, String> exits, List<Pokemon> pokemons, String dialogue) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.exits = exits;
        this.pokemons = pokemons;
        this.dialogue = dialogue;
    }

    public String getDialogue() {
        return dialogue;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getShortDescription() {
        return description;
    }

    public Map<String, String> getExits() {
        return exits;
    }

    public List<Pokemon> get() {
        return pokemons;
    }

    public void removeItem(Pokemon item) {
        pokemons.remove(item);
    }

    public void addItem(Pokemon item) {
        pokemons.add(item);
    }

    

    public String getLongDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        sb.append("\n");
        sb.append(description).append("\n");
        sb.append("\n");

        if (!pokemons.isEmpty()) {
         
            // Remove trailing comma and space
            sb.setLength(sb.length() - 2);
            sb.append(".\n");
        }

        if (!exits.isEmpty()) {
            sb.append("Exits: ");
            for (String direction : exits.keySet()) {
                sb.append(direction).append(", ");
            }
            sb.setLength(sb.length() - 2);
            sb.append(".\n");
        }

        return sb.toString();
    }
}
