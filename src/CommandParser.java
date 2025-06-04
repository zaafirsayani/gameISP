import java.util.Arrays;
import java.util.Map;

public class CommandParser {


    private boolean isCombat = false;
    private Fight currentFight;

    public void startCombat(Fight fight){
        this.currentFight = fight;
        fight.startBattle();
        isCombat = true;
    }

    public void endCombat(){
        isCombat = false;
        currentFight = null;
    }

    

    public void parse(String input, Player player, Map<String, Room> rooms) {

        String[] words = input.trim().toLowerCase().split("\\s+");
        if (words.length == 0) {
            System.out.println("Sorry, what was that?");
            return;
        }

        String command = words[0];

        if(isCombat){
            switch (command) {

                case "check":
                    currentFight.repStats();
                    break;
                case "switch":
                    if (words.length < 2) {
                        System.out.println("Switch to who?");
                    } else {
                        currentFight.switchPokemon();
                    }
                break;
                case "attack":
                    if (words.length < 2) {
                        System.out.println("Which move? Try one of: " + currentFight.getActive().getm1() + " or " + currentFight.getActive().getm2());
                    } else {
                        String moveName = String.join(" ", Arrays.copyOfRange(words, 1, words.length));
                        Moves selectedMove = null;
                        if (currentFight.getActive().getm1().equalsIgnoreCase(moveName)) {
                            selectedMove = currentFight.getActive().getMove1();
                        } else if (currentFight.getActive().getm2().equalsIgnoreCase(moveName)) {
                            selectedMove = currentFight.getActive().getMove2();
                        } else {
                            System.out.println("Invalid move name.");
                            return;
                        }
                        currentFight.attack(currentFight.getActive(), currentFight.getChallenger(), selectedMove);
                }
                break;
                case "catch":
                    if(currentFight.attemptCatch()){
                        endCombat();
                }
                break;

                
            }

            if(currentFight != null && currentFight.getChallenger().getHp() <= 0){
                System.out.println(currentFight.getChallengerName() + " fainted! You Win! You can now continue.");
                endCombat();
            } else if(currentFight != null && currentFight.getActive().getHp() <= 0){
                System.out.println("Your Pokemon Fainted!");
                currentFight.getSurvivors().remove(currentFight.getActive());
                if(currentFight.getSurvivors().size() <= 0){
                    System.out.println("All your Pokemon have fainted! You lose!");
                    endCombat();
                    return;
                }
                
                currentFight.switchPokemon();

            }
            return;

            }
            

            
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
            case "north", "south", "east", "west":
                String direction = words[0];
                Room currentRoom = rooms.get(player.getCurrentRoomId());
                String nextRoomId = currentRoom.getExits().get(direction);
                if (nextRoomId != null) {
                    player.setCurrentRoomId(nextRoomId);
                    System.out.println("You move " + direction + ".");
                    currentRoom = rooms.get(player.getCurrentRoomId());
                    System.out.println(currentRoom.getLongDescription());
                } else {
                    System.out.println("You can't go that way.");
                }
                break;
            case "look", "l", "where":
                currentRoom = rooms.get(player.getCurrentRoomId());
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
            case "talk", "speak", "chat", "say", "dialogue":
                currentRoom = rooms.get(player.getCurrentRoomId());
                String dialogue = currentRoom.getDialogue();
                if (dialogue != null && !dialogue.isEmpty()) {
                    System.out.println(dialogue);
                } else {
                    System.out.println("There's no one to talk to here.");
                }
                break;
            case "help", "help me", "commands", "what", "what can i do", "what do i say", "huh":
                System.out.println("Available commands: go [direction], look, take [item], drop [item], inventory, help, talk");
                break;
            case "search", "find":
                currentRoom = rooms.get(player.getCurrentRoomId());
                if (currentRoom == null) {
                    System.out.println("You can't search here, the room does not exist.");
                    return;
                }
                if (currentRoom.getId().equals("pol")) {
                    System.out.println("What are you searching for? Professor Oak has presented the three Pokemon to you! Choose one by typing its name.");
                    return;
                }
                int pokeCount = 0;
                for (Pokemon item : currentRoom.get()) {
                    if (item != null) {
                        pokeCount++;
                    }
                }
                if(pokeCount == 0){
                    System.out.println("There are either no pokemon in this room or you've found all the ones inside it!");
                    return;
                }
                int randomIndex = (int) (Math.random() * (pokeCount + 1));
                if (randomIndex < pokeCount) {
                    Pokemon foundPokemon = currentRoom.get().get(randomIndex);

                    
                
                    currentRoom.removeItem(foundPokemon);
                    currentFight = new Fight(player, foundPokemon);
                    startCombat(currentFight);
                    
                    
                } else {
                    System.out.println("You find nothing of interest... yet. Try searching again!");
                }


                break;
            case "Charmander", "charmander":
                currentRoom = rooms.get(player.getCurrentRoomId());
                if (currentRoom.getId().equals("pol") && currentRoom.get().size() > 0) {
                    if (currentRoom.get().size() < 3) {
                        System.out.println("Prof. Oak: I'm afraid there are no more Pokemon available for you in the lab.");
                        return;
                    }
                    Pokemon foundPokemon = currentRoom.get().get(0);
                    player.addItem(foundPokemon);
                    System.out.println("You received a Charmander!");
                    System.out.println(
                        foundPokemon.getName() + "\n" +
                        foundPokemon.getDescription() + "\n" +
                        "Health: " + foundPokemon.getHp() + "\n" +
                        "Attack: " + foundPokemon.getAtk() + "\n" +
                        "Defense: " + foundPokemon.getDef() + "\n" +
                        "Speed: " + foundPokemon.getSpd()
                    );
                    currentRoom.removeItem(currentRoom.get().get(0)); // Remove Charmander
                    currentRoom.removeItem(currentRoom.get().get(0)); // Remove Squirtle
                    currentRoom.removeItem(currentRoom.get().get(0)); // Remove Bulbasaur
                } else {
                    System.out.println("This isn't the lab, nobody's giving you a Charmander for free!");
                }
                break;
            case "Squirtle", "squirtle":
                currentRoom = rooms.get(player.getCurrentRoomId());
                if (currentRoom.getId().equals("pol")) {
                    if (currentRoom.get().size() < 3) {
                        System.out.println("Prof. Oak: I'm afraid there are no more Pokemon available for you in the lab.");
                        return;
                    }
                    Pokemon foundPokemon = currentRoom.get().get(1);
                    player.addItem(foundPokemon);
                    System.out.println("You received a Squirtle!");
                    System.out.println(
                        foundPokemon.getName() + "\n" +
                        foundPokemon.getDescription() + "\n" +
                        "Health: " + foundPokemon.getHp() + "\n" +
                        "Attack: " + foundPokemon.getAtk() + "\n" +
                        "Defense: " + foundPokemon.getDef() + "\n" +
                        "Speed: " + foundPokemon.getSpd()
                    );
                    currentRoom.removeItem(currentRoom.get().get(0)); // Remove Charmander
                    currentRoom.removeItem(currentRoom.get().get(0)); // Remove Squirtle
                    currentRoom.removeItem(currentRoom.get().get(0)); // Remove Bulbasaur
                } else {
                    System.out.println("This isn't the lab, nobody's giving you a Squirtle for free!");
                }
                break;
            case "Bulbasaur", "bulbasaur":
                currentRoom = rooms.get(player.getCurrentRoomId());
                if (currentRoom.getId().equals("pol")) {
                    if (currentRoom.get().size() < 3) {
                        System.out.println("Prof. Oak: I'm afraid there are no more Pokemon available for you in the lab.");
                        return;
                    }
                    Pokemon foundPokemon = currentRoom.get().get(2);
                    player.addItem(foundPokemon);
                    System.out.println("You received a Bulbasaur!");
                    System.out.println(
                        foundPokemon.getName() + "\n" +
                        foundPokemon.getDescription() + "\n" +
                        "Health: " + foundPokemon.getHp() + "\n" +
                        "Attack: " + foundPokemon.getAtk() + "\n" +
                        "Defense: " + foundPokemon.getDef() + "\n" +
                        "Speed: " + foundPokemon.getSpd()
                    );
                    currentRoom.removeItem(currentRoom.get().get(0)); // Remove Charmander
                    currentRoom.removeItem(currentRoom.get().get(0)); // Remove Squirtle
                    currentRoom.removeItem(currentRoom.get().get(0)); // Remove Bulbasaur
                } else {
                    System.out.println("This isn't the lab, nobody's giving you a Bulbasaur for free!");
                }
                break;
            default:
                System.out.println("Sorry, I don't understand that.");
                break;
        }
    }
}

