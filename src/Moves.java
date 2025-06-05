public class Moves {
    private String moveName; // name of move
    private String powerType; // power-type of move

    public Moves(String moveName, String pt){
        this.moveName = moveName;
        this.powerType = pt;
    }

    public String getName(){ // returns name
        return moveName;    
    }

    public String getPower(){ //returns power-type name
        return powerType;
    }
}
