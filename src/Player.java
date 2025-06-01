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

    public void addItem(Pokemon item) {
        if(pokeparty.size() == 3){
            System.out.println("You are unable to collect any more Pokemon!");
        }
        pokeparty.add(item);
    }

    public void removeItem(Pokemon item) {
        pokeparty.remove(item);
    }

    public List<Pokemon> getInventory() {
        return pokeparty;
    }
}
