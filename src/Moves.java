public class Moves {
    private String moveName;
    private String powerType;

    public Moves(String moveName, String pt){
        this.moveName = moveName;
        this.powerType = pt;
    }

    public String getName(){
        return moveName;    
    }
}
