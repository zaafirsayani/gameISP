import java.util.ArrayList;
import java.util.List;

public class Player {
    private String currentRoomId;
    private List<Pokemon> pokeparty;

    public Player(String startingRoomId) {
        this.currentRoomId = startingRoomId;
        this.pokeparty = new ArrayList<>();
    }

    public String getCurrentRoomId() {
        return currentRoomId;
    }

    public void setCurrentRoomId(String roomId) {
        this.currentRoomId = roomId;
    }

    public void addPokemon(Pokemon item) {
        if(pokeparty.size() == 3){
            System.out.println("You are unable to collect any more Pokemon!");
        }
        pokeparty.add(item);
    }

    public void removeItem(Pokemon item) {
        
        System.out.println("You sent " + item.getName() + " away with a heartfelt goodbye.");
        pokeparty.remove(item);
    }

    public List<Pokemon> getInventory() {
        return pokeparty;
    }

    public void checkInventory(){
        for(Pokemon p : pokeparty){
            System.out.println(
                "\n" + p.getName() + ". HP: " + p.getHp() + "\n"
            );
        }
    }
}
