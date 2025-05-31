import java.util.Map;

public class CommandParser {
    public void parse(String input, Player player, Map<String, Room> rooms) {
        String[] words = input.trim().toLowerCase().split("\\s+");
        if (words.length == 0) {
            System.out.println("Sorry, what was that?");
            return;
        }

        String command = words[0];

        switch (command) {
            case "go":
                if (words.length < 2) {
                    System.out.println("Go where?");
                } else {
                    String direction = words[1];
                    Room currentRoom = rooms.get(player.getCurrentRoomId());
                    if(currentRoom == null){
                        System.out.println("next room not found!");
                    }
                    String nextRoomId = currentRoom.getExits().get(direction);
                    if (nextRoomId != null) {
                        player.setCurrentRoomId(nextRoomId);
                        System.out.println("You move " + direction + ".");
                        currentRoom = rooms.get(player.getCurrentRoomId());
                        System.out.println(currentRoom.getLongDescription());

                    } else {
                        System.out.println("You can't go that way.");
                    }
                }
                break;
            case "look", "l", "where":
                Room currentRoom = rooms.get(player.getCurrentRoomId());
                System.out.println(currentRoom.getLongDescription());
                break;
            case "inventory", "i", "inv":
                if (player.getInventory().isEmpty()) {
                    System.out.println("Your inventory is empty.");
                } else {
                    System.out.println("You are carrying:");
                    for (Pokemon item : player.getInventory()) {
                        System.out.println("- " + item.getName());
                    }
                }
                break;
            case "drop":
                if (words.length < 2) {
                    System.out.println("Drop what?");
                } else {
                    String pokeName = words[1];
                   
                }
                break;
            case "talk", "speak":
                currentRoom = rooms.get(player.getCurrentRoomId());
                String dialogue = currentRoom.getDialogue();
                if (dialogue != null && !dialogue.isEmpty()) {
                    System.out.println(dialogue);
                } else {
                    System.out.println("There's no one to talk to here.");
                }
                break;
            case "help", "help me", "commands", "what can i do", "what do i say", "huh":
                System.out.println("Available commands: go [direction], look, take [item], drop [item], inventory, help, talk");
                break;
            default:
                System.out.println("Sorry, I don't understand that.");
                break;
        }
    }
}
