import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Fight {
    private Player player;
    private Pokemon challenger;
    private Pokemon playerCurrent;
    private boolean isOver = false;
    private List<Pokemon> currentParty;
    private List<Pokemon> survivors;


    public Fight(Player player, Pokemon challenger){
        this.player = player;
        this.challenger = challenger;
        this.playerCurrent = playerCurrent;
        this.currentParty = player.getInventory();
        this.survivors = currentParty;

        
    }

    public boolean isOver(){
        return isOver;
    }

    public void choosePokemon(){
        System.out.println("Which Pokemon will you send out first? (Type its -number- to pick it!)");
        for(int i = 0; i < currentParty.size(); i++){
            System.out.println((i + 1) + ". " + currentParty.get(i).getName());
        }
        
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        while (choice < 1 || choice > currentParty.size()) {
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                choice = -1;
            }
            if (choice < 1 || choice > currentParty.size()) {
                System.out.println("Invalid choice, try again.");
            }
        }
        playerCurrent = currentParty.get(choice - 1);
        System.out.println("You sent out " + playerCurrent.getName() + "!");

    }

    public void startBattle(){
        System.out.println("A wild " + challenger.getName() + " appears!");
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
            } catch (Exception e) {
                choice = -1;
            }
        }
        Pokemon chosen = currentParty.get(choice - 1);
        if (chosen.getHp() <= 0 || chosen == playerCurrent) {
            System.out.println("You can't switch to that Pokémon.");
        } else {
            playerCurrent = chosen;
            System.out.println("You switched to " + playerCurrent.getName() + "!");
        }
    }

    public void attack(Pokemon attacker, Pokemon victim, Moves move){

        

        victim.receiveDmg(attacker.getAtk());
        System.out.println(attacker.getName() + " used " + move.getName() + " and dealt " + attacker.getAtk() + " damage!");

        if(victim.getHp() > 0){
        Moves enemyMove = Math.random() < 0.5 ? challenger.getMove1() : challenger.getMove2();
        System.out.println("The wild " + victim.getName() + " attacks!");
        
        playerCurrent.receiveDmg(victim.getAtk());
        System.out.println(victim.getName() + " used " + enemyMove.getName() + " and dealt " + victim.getAtk() + " damage!");
        }
        
        
    }

    public boolean attemptCatch(){

        if(player.getInventory().size() == 3){
            System.out.println("You already have 3 Pokemon! You can't catch another!");
            return false;
        }

        
        player.addItem(challenger);
                    
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
        return survivors;
    }

    
}
