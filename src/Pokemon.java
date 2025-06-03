public class Pokemon {

    private String name;
    private String description;
    
    private int atk;
    private int def;
    private int hp;
    private int spd;
    private Moves m1;
    private Moves m2;

    public Pokemon(String name, String description, int atk, int def, int hp, int spd, Moves move1, Moves move2) {
        this.atk = atk;
        this.def = def;
        this.hp = hp;
        int maxhp = hp;
        this.spd = spd;
        this.name = name;
        this.description = description;
        m1 = move1;
        m2 = move2;
    }

    public String getName(){
        return name;
    }

    public int getAtk() {
        return atk;
    }

    public int getDef() {
        return def;
    }

    public int getHp() {
        return hp;
    }

    public int getSpd() {
        return spd;
    }

    public String getm1(){
        return m1.getName();
    }

    public String getm2(){
        return m2.getName();
    }

    public Moves getMove1(){
        return m1;
    }

    public Moves getMove2(){
        return m2;
    }

    public String getDescription() {
        return description;
    }

    public void receiveDmg(int damage){
        hp -= damage;
    }

    public boolean isFainted(){
        return hp <= 0;
    }



    
}
