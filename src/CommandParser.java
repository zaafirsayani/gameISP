import java.util.Arrays;
import java.util.Map;

public class CommandParser {

    private boolean isCombat = false; // tracks if player is in combat
    private Fight currentFight; // Current active fight
    Room currentRoom; // The current room the player is in
    private String resumeRoomId; // Room ID to return to after healing

    public void startCombat(Fight fight){
        this.currentFight = fight; // sets current fight
        fight.startBattle(); // begins the battle by triggering stats in the fight class
        isCombat = true; // enable combat
        fight.choosePokemon(); // allows player to pick pokemon
        System.out.println( // display combat options immediately
            "WHAT WILL YOU DO?" + "\n" +
            "1. ATTACK [use + MoveName (e.g. attack slash)]" + "\n" +
            "2. CHECK STATS [check]" + "\n" + 
            "3. SWITCH POKEMON [switch]" + "\n" + 
            "4. CATCH [catch]" 

            );
    }

    public void endCombat(){ // ends combat mode
        if (currentFight.getChallenger().getHp() <= 0) { // if the wild Pokemon has fainted (hp <= 0)
            currentRoom.get().remove(currentFight.getChallenger()); // Remove the defeated Pokemon from the room
        }
        isCombat = false;
        currentFight = null;
        
    }

    public void end(){ // prints congratulations upon game completion
        System.out.println("\n========================");
        System.out.println("CONGRATULATIONS, Ash!");
        System.out.println("Or should I say, the special Trainer behind this screen! Though you played as Ash, it was YOU that defeated all opponents and proved your worth.");
        System.out.println("Thank you for playing!");
        System.out.println("========================\n");

        System.exit(0); // terminate game
    }

    public void parse(String input, Player player, Map<String, Room> rooms) {

        String[] words = input.trim().toLowerCase().split("\\s+");
        if (words.length == 0) {
            System.out.println("Sorry, what was that?");
            return;
        }

        String command = words[0]; // The first word that the player types

        if(isCombat){

            switch (command) {

                case "check": // displays the challenger pokemon's stats and description
                    currentFight.repStats();
                    break;
                case "switch": // Switches the active Pokemon
                    if(player.getInventory().size() <= 1){ // checks if the player has any pokemon they can switch to
                        System.out.println("You have no Pokemon to switch to!");
                        return;
                    }
                    
                    currentFight.switchPokemon(); // switches the pokemon
                    
                break;
                case "use", "attack": // Attacks with the active Pokemon
                    if (words.length < 2) { // if move not specified
                        System.out.println("Which move? Try one of: " + currentFight.getActive().getm1() + " or " + currentFight.getActive().getm2() );
                    } else {
                        String moveName = String.join(" ", Arrays.copyOfRange(words, 1, words.length)); // sets moveName to player imput
                        Moves selectedMove = null; // move to use
                        if (currentFight.getActive().getm1().equalsIgnoreCase(moveName)) { // if the moveName input is one of the moves that the pokemon has, use that move
                            selectedMove = currentFight.getActive().getMove1();
                        } else if (currentFight.getActive().getm2().equalsIgnoreCase(moveName)) {
                            selectedMove = currentFight.getActive().getMove2();
                        } else {
                            System.out.println("Invalid move name."); 
                            return;
                        }
                        currentFight.attack(currentFight.getActive(), currentFight.getChallenger(), selectedMove); // attacks with the move and pokemon
                    }
                break;
                case "catch": // Attempts to catch the opponent's Pokemon
                    if(currentRoom.getId().equals("boss arena")){ // checks if in bossfight to ensure mewtwo cannot be caught
                        System.out.println("You can't catch this Pokemon!");
                        return;
                    }

                    if(currentFight.attemptCatch()){
                        currentRoom.get().remove(currentFight.getChallenger()); // Remove the caught Pokemon from the room
                        endCombat(); 
                    }
                break;
                default: 
                    System.out.println( // prints helper again in case input does not make sense
                        "\nWHAT WILL YOU DO?" + "\n" +
                        "1. ATTACK [use + MoveName (e.g. attack slash)]" + "\n" +
                        "2. CHECK STATS [check]" + "\n" + 
                        "3. SWITCH POKEMON [switch]" + "\n" + 
                        "4. CATCH [catch]"
                    );

                
            }

            if(currentFight != null && currentFight.getChallenger().getHp() <= 0){ // if opposing pokemon has fainted
                System.out.println(currentFight.getChallengerName() + " fainted!");
                if(currentRoom.getId().equals("boss arena")){ // in case this has occurred in the boss arena, end the game
                    endCombat();
                    System.out.println(
                        "\n" +
                        "Red: It can't be... How have I been defeated?" + "\n" + 
                        "Alas, I must accept it. I hereby crown you as the new champion of this stadium!"
                    );
                    end();
                }
                System.out.println("You Win! You can now continue. Try searching again or moving someplace else!"); // otherwise, explain what happened and end combat
                endCombat();
            } else if(currentFight != null && currentFight.getActive().getHp() <= 0){ // If the player's curernt pokemon has died
                System.out.println("Your Pokemon Fainted!");
                currentFight.getSurvivors().remove(currentFight.getActive()); // remove it from the currently alive pokemon
                if(currentFight.getSurvivors().size() <= 0){ // if you have lost all your pokemon, end combat and go to pokestop
                    currentFight.getChallenger().setHp(currentFight.getChallenger().getMaxHp());
                    System.out.println("All your Pokemon have fainted! You lose!");
                    endCombat();

                    resumeRoomId = player.getCurrentRoomId(); // Save the room ID to return to after healing
                    player.setCurrentRoomId("pokecentre"); // transport to pokecentre
                    currentRoom = rooms.get("pokecentre");
                    System.out.println(rooms.get("pokecentre").getLongDescription());
                    for (Pokemon pokemon : player.getInventory()) {
                        pokemon.setHp(pokemon.getMaxHp()); // Heal all Pokemon in the player's inventory
                    }

                    return;
                    

                }

                
                currentFight.switchPokemon(); // if pokemon are still alive, prompt user to switch pokemon

            }
            return;

            }
            

            
        switch (command) {  

            case "go":
                if (player.getCurrentRoomId().equals("pol") && player.getInventory().size() == 0) {
                    // If the player is in Professor Oak's lab and has no Pokemon, they must choose one
                    System.out.println("Prof. Oak: Whoa, you can't go out there without a Pokemon! Choose one first. Charmander, Squirtle or Bulbasaur?");
                    return;
                }
                if (words.length < 2) { // if input too short
                    System.out.println("Go where?");
                } else {
                    String direction = words[1]; // gets direction from player input
                    currentRoom = rooms.get(player.getCurrentRoomId());
                    if(currentRoom == null){
                        System.out.println("next room not found!");
                    }
                    String nextRoomId = currentRoom.getExits().get(direction); // gets room from the other room's exits
                    if (nextRoomId != null) { // if the room exists, then display the direction the user moved and switch rooms
                        player.setCurrentRoomId(nextRoomId);
                        System.out.println("You move " + direction + ".");
                        currentRoom = rooms.get(player.getCurrentRoomId());
                        System.out.println(currentRoom.getLongDescription());

                    } else {
                        System.out.println("You can't go that way.");
                    }
                }
                break;
            case "north", "south", "east", "west": // More convenient direction commands
                if (player.getCurrentRoomId().equals("pol") && player.getInventory().size() == 0) {
                    // If the player is in Professor Oak's lab and has no Pokemon, they must choose one
                    System.out.println("Prof. Oak: Whoa, you can't go out there without a Pokemon! Choose one first. Charmander, Squirtle or Bulbasaur?");
                    return;
                }
                String direction = words[0];
                currentRoom = rooms.get(player.getCurrentRoomId());
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
            case "look", "l", "where": // Look around the current room; get the current room's description
                currentRoom = rooms.get(player.getCurrentRoomId());
                System.out.println(currentRoom.getLongDescription());
                break;
            case "inventory", "i", "inv", "party": // Checks the player's inventory (list of Pokemon)
                if(player.getInventory().size() < 1){ // if your inventory is empty, do not display anything.
                    System.out.println("You are not carrying any Pokemon.");
                    return;
                }
                player.checkInventory();
                break;
            case "drop": // Drops (releases) a Pokemon from the player's inventory
                if(player.getInventory().size() == 1){ // if you only have one pokemon, you cannot drop it (locks player from having no pokemon)
                    System.out.println("You can't get rid of your only Pokemon, silly! Catch some more!");
                    return;
                }
                if(player.getInventory().size() < 1){ // If in the beginning you have no Pokemon, display a different status message
                    System.out.println("You have no Pokemon to drop!");
                    return;
                }
                if (words.length < 2) { // if input too short
                    System.out.println("Drop what?");
                } else {
                    String pokeName = words[1];
                    for(Pokemon p : player.getInventory()){
                    if (p.getName().equalsIgnoreCase(pokeName)) {
                        player.removeItem(p); // removes the Pokemon from the player's inventory
                        return;
                    }
                 } 
                    System.out.println("You don't have that Pokemon.");
                    return;
                    }
                    
                    
                
                break;
            case "talk", "speak", "chat", "say", "dialogue": // Engages dialogue in the current room
                currentRoom = rooms.get(player.getCurrentRoomId());
                String dialogue = currentRoom.getDialogue();
                if (dialogue != null && !dialogue.isEmpty()) {
                    System.out.println(dialogue);
                } else {
                    System.out.println("There's no one to talk to here.");
                }
                break;
            case "help", "help me", "commands", "what", "what can i do", "what do i say", "huh": // Displays a list of available commands
                System.out.println("Feeling lost? No worries! Here are some commands you can say to me.");
                System.out.println("- \"go [direction]\" (e.g. go north, go south, go east, go west): to move in a direction. You can also just type the direction in one word.");
                System.out.println("- \"look\" or \"l\" or \"where\": to look around the current room.");
                System.out.println("- \"inventory\", \"i\" or \"inv\": to check your inventory (list of Pokemon you have).");
                System.out.println("- \"drop [Pokemon name]\": to release a Pokemon from your inventory.");
                System.out.println("- \"talk\", \"speak\", \"chat\", \"say\", or \"dialogue\": to engage in dialogue in the current room.");
                System.out.println("Tip: If an NPC is known to be in a room, you will likely need to talk to it to gather context. There might be dialogue hints in rooms without NPCs too!");
                System.out.println("Some other commands are only available in specific rooms or situations. You'll know when you can use them!");
                System.out.println("Good luck on your journey!");
                break;
            case "return", "back", "go back": // Returns the player to the room where they were defeated
                if (player.getCurrentRoomId().equals("pokecentre")) {
                    player.setCurrentRoomId(resumeRoomId);
                    currentRoom = rooms.get(resumeRoomId);
                    System.out.println("Your Pokemon have been healed and returned to " + currentRoom.getName() + ".");
                    System.out.println(currentRoom.getLongDescription());
                }
                break;
            case "search", "find": // Searches the current room for Pokemon
                
                currentRoom = rooms.get(player.getCurrentRoomId()); 
                if(currentRoom.getId().equals("boss arena")){ // if in boss area, disallow searching
                    System.out.println("You're not allowed to search in here!");
                    return;
                }
                if (currentRoom == null) { // bugfixing
                    System.out.println("You can't search here, the room does not exist.");
                    return;
                }
                if (currentRoom.getId().equals("pol")) {  // disallows searching if in lab so fight is not initiated with lab pokemon
                    System.out.println("What are you searching for? Professor Oak has presented the three Pokemon to you! Choose one by typing its name.");
                    return;
                }
                int pokeCount = 0;
                for (Pokemon item : currentRoom.get()) {
                    if (item != null) { // gets count of pokemon in room
                        pokeCount++;
                    }
                }
                if(pokeCount == 0){ // disallows searching if no pokemon
                    System.out.println("There are either no pokemon in this room or you've found all the ones inside it!");
                    return;
                }
                int randomIndex = (int) (Math.random() * (pokeCount + 1)); // randomizes picked Pokemon from the several in room
                if (randomIndex < pokeCount) {
                    Pokemon foundPokemon = currentRoom.get().get(randomIndex);

                    currentFight = new Fight(player, foundPokemon); // fights whichever pokemon it picks
                    startCombat(currentFight);
                    
                } else {
                    System.out.println("You find nothing of interest... yet. Try searching again!");
                }


                break;
            case "pokestop", "Pokestop", "go pokestop", "rest": // allows player to transport themselves to pokestop if they would like to heal their pokemon before battle
                resumeRoomId = player.getCurrentRoomId(); // Save the room ID to return to after healing
                player.setCurrentRoomId("pokecentre");
                currentRoom = rooms.get("pokecentre");
                System.out.println(rooms.get("pokecentre").getLongDescription());
                for (Pokemon pokemon : player.getInventory()) {
                    pokemon.setHp(pokemon.getMaxHp()); // Heal all Pokemon in the player's inventory
                }
                      
                break;  
            case "challenge", "fight": // challenges player in boss arena
                currentRoom = rooms.get(player.getCurrentRoomId()); 
                if(currentRoom.getId().equals("boss arena")){ // triggers fight if challenged
                    Pokemon foundPokemon = currentRoom.get().get(0);

                    currentFight = new Fight(player, foundPokemon);
                    startCombat(currentFight);                    
                } else {
                    System.out.println("There's no one to challenge here!");
                    return;
                }
                          
            case "Charmander", "charmander": // Gives the player a Charmander if they are in Professor Oak's lab
                currentRoom = rooms.get(player.getCurrentRoomId());
                if (currentRoom.getId().equals("pol")) {
                    if (currentRoom.get().size() < 3) {
                        System.out.println("Prof. Oak: I'm afraid there are no more Pokemon available for you in the lab.");
                        return;
                    }
                    Pokemon foundPokemon = currentRoom.get().get(0); // selects charmander index
                    player.addPokemon(foundPokemon);
                    System.out.println("You received a Charmander!");
                    System.out.println( // prints charmander info
                        foundPokemon.getName() + "\n" +
                        foundPokemon.getDescription() + "\n" +
                        "Health: " + foundPokemon.getHp() + "\n" +
                        "Attack: " + foundPokemon.getAtk() + "\n" +
                        "Defense: " + foundPokemon.getDef() + "\n" +
                        "Speed: " + foundPokemon.getSpd() 
                    );
                    currentRoom.removeItem(currentRoom.get().get(0)); // Remove Charmander so they cannnot be picked anymore
                    currentRoom.removeItem(currentRoom.get().get(0)); // Remove Squirtle
                    currentRoom.removeItem(currentRoom.get().get(0)); // Remove Bulbasaur
                } else {
                    System.out.println("This isn't the lab, nobody's giving you a Charmander for free!");
                }
                break;
            case "Squirtle", "squirtle": // Gives the player a Squirtle if they are in Professor Oak's lab
                currentRoom = rooms.get(player.getCurrentRoomId());
                if (currentRoom.getId().equals("pol")) {
                    if (currentRoom.get().size() < 3) {
                        System.out.println("Prof. Oak: I'm afraid there are no more Pokemon available for you in the lab.");
                        return;
                    }
                    Pokemon foundPokemon = currentRoom.get().get(1); // selects squirtle index
                    player.addPokemon(foundPokemon);
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
            case "Bulbasaur", "bulbasaur": // Gives the player a Bulbasaur if they are in Professor Oak's lab
                currentRoom = rooms.get(player.getCurrentRoomId());
                if (currentRoom.getId().equals("pol")) {
                    if (currentRoom.get().size() < 3) {
                        System.out.println("Prof. Oak: I'm afraid there are no more Pokemon available for you in the lab.");
                        return;
                    }
                    Pokemon foundPokemon = currentRoom.get().get(2); // selects bulbasaur index
                    player.addPokemon(foundPokemon);
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
                    System.out.println("This isn't the lab, nobody's giving you a Bulbasaur for free!"); // disallows receiving these pokemon if command is types outside of Oak's lab
                }
                break;
            default:
                
                System.out.println("Sorry, I don't understand that.");
                break;
        }
    }
}

