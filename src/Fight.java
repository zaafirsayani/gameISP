import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Fight {
    private String thisRoomId; // currentroomid
    private Player player; // the player
    private Pokemon challenger; // wild pokemon
    private Pokemon playerCurrent; // current active pokemon
    private List<Pokemon> currentParty; // current inventory


    public Fight(Player player, Pokemon challenger){ // constructor
        this.thisRoomId = player.getCurrentRoomId();
        this.player = player;
        this.challenger = challenger;
        this.currentParty = player.getInventory();

        
    }

    

    public void choosePokemon(){ // chooses first pokemon to send out when fight begins
        System.out.println("Which Pokemon will you send out first? (Type its -number- to pick it!)");
        for(int i = 0; i < currentParty.size(); i++){
            System.out.print((i + 1) + ". " + currentParty.get(i).getName()); // displays pokemon to pick
            if (currentParty.get(i).getHp() <= 0) { // checks to see if pokemon is fainted
                System.out.println(" (fainted!)");
            } else {
                System.out.println();
            }
        }
        
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        while (choice < 1 || choice > currentParty.size()) {
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                choice = -1; // Reset choice if input is invalid
            }
            if (choice > 0 && choice <= currentParty.size() && currentParty.get(choice - 1).getHp() <= 0) {
                System.out.println(currentParty.get(choice - 1).getName() + " has already fainted! Choose another one."); // ensures picked pokemon is not fainted
                choice = -1; // Reset choice if the selected Pokémon is fainted
            }
            System.out.println("Please enter a number corresponding to one of your Pokémon!");
        }
        playerCurrent = currentParty.get(choice - 1);
        System.out.println("You sent out " + playerCurrent.getName() + "!");

    }

    public void startBattle(){ // displays battle beginning text
        System.out.println("\nA wild " + challenger.getName() + " appears!");
        System.out.println("HP: " + challenger.getHp() + "\n");
    }

    public void switchPokemon(){ // switches active pokemon based on player input
        System.out.println("Switch to who? (Type -number-)");
        for (int i = 0; i < currentParty.size(); i++) {
            if (currentParty.get(i) != playerCurrent && currentParty.get(i).getHp() > 0) {
                System.out.println((i + 1) + ". " + currentParty.get(i).getName()); // displays switchable pokemon
            }
        }
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        while (choice < 1 || choice > currentParty.size()) {
            System.out.print("> ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice < 1 || choice > currentParty.size() || currentParty.get(choice - 1).getHp() <= 0) {
                    System.out.println("Please enter a number corresponding to one of your Pokémon. Make sure it's still able to fight!");
                    choice = -1; // Reset choice if the input is invalid
                }
            } catch (Exception e) {
                System.out.println("Please enter a number corresponding to one of your Pokémon!");
            }
        }
        Pokemon chosen = currentParty.get(choice - 1);
        playerCurrent = chosen;
        System.out.println("You switched to " + playerCurrent.getName() + "!");
    }

    public void attack(Pokemon attacker, Pokemon victim, Moves move){ // attacks the pokemon

        
        int enemyDamage = Math.abs(attacker.getAtk() - (victim.getDef() / 3)); //calculates damage against enemy
       
        victim.receiveDmg(enemyDamage);
        System.out.println(attacker.getName() + " used the " + move.getPower() + "-type " + move.getName() + " and dealt " + enemyDamage + " damage!"); // displays damage
 
        if(victim.getHp() > 0){ // if pokemonis still alive, display health and let them attack back
        System.out.println("REMAINING HP: " + victim.getHp()); 
        Moves enemyMove = Math.random() < 0.5 ? challenger.getMove1() : challenger.getMove2(); // randomize enemy move between 1 and 2
        System.out.println("The wild " + victim.getName() + " attacks!");


        
        int activeDamage = Math.abs(victim.getAtk() - (attacker.getDef() / 2)); // calculates damage against player pokemon
        playerCurrent.receiveDmg(activeDamage);
        System.out.println(victim.getName() + " used the " + enemyMove.getPower() + "-type " + enemyMove.getName() + " and dealt " + activeDamage + " damage!"); // displays damage
        
        }
        
        
    }

    public boolean attemptCatch(){ // attempts catch

        if(player.getInventory().size() == 3){ // prevents catching if player has more than 3 pokemon in their party
            System.out.println("You already have 3 Pokemon! You can't catch another!");
            return false;
        }
        if(challenger.getHp() > (challenger.getMaxHp() * 0.5)){ // prevents catching if pokemon is under 50% health
            System.out.println(challenger.getName() + "'s health is too high! It must be under 50% of its maximum!");
            return false;
        }

        
        player.addPokemon(challenger); // if successful, add them to party
        challenger.setHp(challenger.getMaxHp()); // reset their hp
                    
        System.out.println("You found a " + challenger.getName() + "!"); // display stats
        System.out.println(
            challenger.getName() + "\n" + 
            challenger.getDescription() + "\n" +
            "Health: " + challenger.getHp() + "\n" +
            "Attack: " + challenger.getAtk() + "\n" +
            "Defense: " + challenger.getDef() + "\n" +
            "Speed: " + challenger.getSpd() + "\n" +
            "Move 1: " + challenger.getm1() + "\n" + 
            "Move 2: " + challenger.getm2()
        );

        return true;

    }

    public void repStats() { // displays Pokemon statistics and description
        System.out.println(challenger.getDescription());
        System.out.println(challenger.getName() + " - HP: " + challenger.getHp() + ", ATK: " + challenger.getAtk() + ", DEF: " + challenger.getDef() + ", SPD: " + challenger.getSpd());
    }

    public Pokemon getActive(){ // returns active pokemon
        return playerCurrent;
    }

    public Pokemon getChallenger(){ //returns wild pokemon
        return challenger;
    }

    public String getChallengerName(){
        return challenger.getName();
    }

    public List<Pokemon> getSurvivors(){ // returns list of currently surviving player's pokemon
        List<Pokemon> survivors = new ArrayList<>();
        for (Pokemon pokemon : currentParty) {
            if (pokemon.getHp() > 0) {
                survivors.add(pokemon);
            }
        }
        return survivors;
    }

    public String getRoomId() { // returns roomId
        return thisRoomId;
    }

}
