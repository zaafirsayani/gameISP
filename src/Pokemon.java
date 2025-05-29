public class Pokemon {

    private String name;
    
    private int atk;
    private int def;
    private int hp;
    private int spd;

    public Pokemon(String name, int atk, int def, int hp, int spd) {
        this.atk = atk;
        this.def = def;
        this.hp = hp;
        this.spd = spd;
        this.name = name;
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

    public void receiveDmg(int damage){
        hp -= damage;
    }

    public boolean isFainted(){
        return hp <= 0;
    }

    public int attack(Pokemon other){
        int damage = atk;
        other.receiveDmg(damage);
        return damage;

    }


    
}
