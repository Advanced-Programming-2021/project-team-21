package module.card;

public class Monster extends Card {
    Integer level;
    Attributes attribute;
    MonsterTypes monsterType;
    int atk;
    int def;

    public Monster(Object[] parameters) {
        setName((String) parameters[0]);
        setLevel((int) parameters[1]);
        setAttribute(Attributes.valueOf(((String) parameters[2]).toUpperCase()));
        setMonsterType(MonsterTypes.valueOf(((String) parameters[3]).toUpperCase()));
        setCardType(CardType.valueOf(((String) parameters[4]).toUpperCase()));
        setAtk((int) parameters[5]);
        setDef((int) parameters[6]);
        setDescription((String) parameters[7]);
    }

    @Override
    public void destroyWithoutLosingLifePoints() {

    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Attributes getAttribute() {
        return attribute;
    }

    public void setAttribute(Attributes attribute) {
        this.attribute = attribute;
    }

    public MonsterTypes getMonsterType() {
        return monsterType;
    }

    public void setMonsterType(MonsterTypes monsterType) {
        this.monsterType = monsterType;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    @Override
    public String toString() {
        return "Name:" + name + "\nlevel: " + level +
                "\nType: " + monsterType.getName() +
                "\nATK: " + atk +
                "\nDef: " + def +
                "\nDescription: " + getDescription();
    }
}
