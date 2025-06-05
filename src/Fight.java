import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Fight {
    private String thisRoomId;
    private Player player;
    private Pokemon challenger;
    private Pokemon playerCurrent;
    private boolean isOver = false;
    private List<Pokemon> currentParty;


    public Fight(Player player, Pokemon challenger){
        this.thisRoomId = player.getCurrentRoomId();
        this.player = player;
        this.challenger = challenger;
        this.currentParty = player.getInventory();

        
    }

    public boolean isOver(){
        return isOver;
    }

    public void choosePokemon(){
        System.out.println("Which Pokemon will you send out first? (Type its -number- to pick it!)");
        for(int i = 0; i < currentParty.size(); i++){
            System.out.print((i + 1) + ". " + currentParty.get(i).getName());
            if (currentParty.get(i).getHp() <= 0) {
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
                System.out.println(currentParty.get(choice - 1).getName() + " has already fainted! Choose another one.");
                choice = -1; // Reset choice if the selected Pokémon is fainted
            }
            System.out.println("Please enter a number corresponding to one of your Pokémon!");
        }
        playerCurrent = currentParty.get(choice - 1);
        System.out.println("You sent out " + playerCurrent.getName() + "!");

    }

    public void startBattle(){
        System.out.println("\nA wild " + challenger.getName() + " appears!");
        System.out.println("HP: " + challenger.getHp() + "\n");
    }

    public void switchPokemon(){
        System.out.println("Switch to who? (Type -number-)");
        for (int i = 0; i < currentParty.size(); i++) {
            if (currentParty.get(i) != playerCurrent && currentParty.get(i).getHp() > 0) {
                System.out.println((i + 1) + ". " + currentParty.get(i).getName());
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

    public void attack(Pokemon attacker, Pokemon victim, Moves move){

        
        int enemyDamage = Math.abs(attacker.getAtk() - (victim.getDef() / 2));
       
        victim.receiveDmg(enemyDamage);
        System.out.println(attacker.getName() + " used " + move.getName() + " and dealt " + enemyDamage + " damage!");
 
        if(victim.getHp() > 0){
        System.out.println("REMAINING HP: " + victim.getHp());
        Moves enemyMove = Math.random() < 0.5 ? challenger.getMove1() : challenger.getMove2();
        System.out.println("The wild " + victim.getName() + " attacks!");


        
        int activeDamage = Math.abs(victim.getAtk() - (attacker.getDef() / 2));
        playerCurrent.receiveDmg(activeDamage);
        System.out.println(victim.getName() + " used " + enemyMove.getName() + " and dealt " + activeDamage + " damage!");
        
        }
        
        
    }

    public boolean attemptCatch(){

        if(player.getInventory().size() == 3){
            System.out.println("You already have 3 Pokemon! You can't catch another!");
            return false;
        }
        if(challenger.getHp() > (challenger.getMaxHp() * 0.5)){
            System.out.println(challenger.getName() + "'s health is too high! It must be under 40% of its maximum!");
            return false;
        }

        
        player.addPokemon(challenger);
        challenger.setHp(challenger.getMaxHp());
                    
        System.out.println("You found a " + challenger.getName() + "!");
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

    public void repStats() {
        System.out.println(challenger.getName() + " - HP: " + challenger.getHp() + ", ATK: " + challenger.getAtk() + ", DEF: " + challenger.getDef() + ", SPD: " + challenger.getSpd());
    }

    public Pokemon getActive(){
        return playerCurrent;
    }

    public Pokemon getChallenger(){
        return challenger;
    }

    public String getChallengerName(){
        return challenger.getName();
    }

    public List<Pokemon> getSurvivors(){
        List<Pokemon> survivors = new ArrayList<>();
        for (Pokemon pokemon : currentParty) {
            if (pokemon.getHp() > 0) {
                survivors.add(pokemon);
            }
        }
        return survivors;
    }

    public String getRoomId() {
        return thisRoomId;
    }

}
