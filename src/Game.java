import java.util.Map;
import java.util.Scanner;

public class Game {
    private Map<String, Room> rooms;
    private Player player;
    private CommandParser commandParser;

    public Game() {
        RoomLoader roomLoader = new RoomLoader();
        rooms = roomLoader.loadRooms("rooms.json");
        player = new Player("start");
        commandParser = new CommandParser();
    }

    public void start() { // provides helper tool and scanner/parsing
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the world of Pokemon!");
        System.out.println("Here are some commands you can give me to get started.");
        System.out.println("- \"go [direction]\" (e.g. go north, go south, go east, go west): move in a direction. You can also just type the direction in one word.");
        System.out.println("- \"look\" or \"l\" or \"where\": look around the current room.");
        System.out.println("- \"inventory\", \"i\" or \"inv\": check your inventory (list of Pokemon you have).");
        System.out.println("- \"drop [Pokemon name]\": release a Pokemon from your inventory.");
        System.out.println("- \"talk\", \"speak\", \"chat\", \"say\", or \"dialogue\": engage in dialogue in the current room.");
        System.out.println("\"help\": show this list of commands again.");
        System.out.println("Tip: If an NPC is known to be in a room, you will likely need to talk to it to gather context. There might be dialogue hints in rooms without NPCs too!");
        System.out.println("Some other commands are only available in specific rooms or situations. You'll know when you can use them!");
        System.out.println("Good luck on your journey!");
        Room currentRoom = rooms.get(player.getCurrentRoomId());
        System.out.println(currentRoom.getLongDescription());

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            commandParser.parse(input, player, rooms);
        }

    }

   
}
