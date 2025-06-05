import java.util.ArrayList;
import java.util.List;

public class Player {
    private String currentRoomId; // current room id
    private List<Pokemon> pokeparty; // player's party of pokemon

    public Player(String startingRoomId) {
        this.currentRoomId = startingRoomId;
        this.pokeparty = new ArrayList<>(); // begins as empty array list
    }

    public String getCurrentRoomId() { //returns the current room id
        return currentRoomId;
    }

    public void setCurrentRoomId(String roomId) {//replaces the current room id
        this.currentRoomId = roomId;
    }

    public void addPokemon(Pokemon item) { // adds a pokemon to the party unless the party is full
        if(pokeparty.size() == 3){
            System.out.println("You are unable to collect any more Pokemon!");
        }
        pokeparty.add(item);
    }

    public void removeItem(Pokemon item) { // removes a pokemon from the party :(
        
        System.out.println("You sent " + item.getName() + " away with a heartfelt goodbye.");
        pokeparty.remove(item);
    }

    public List<Pokemon> getInventory() { // returns party
        return pokeparty;
    }

    public void checkInventory(){ // displays pokemon in inventory as well as health
        for(Pokemon p : pokeparty){
            System.out.println(
                "\n" + p.getName() + ". HP: " + p.getHp() + "\n"
            );
        }
    }
}
