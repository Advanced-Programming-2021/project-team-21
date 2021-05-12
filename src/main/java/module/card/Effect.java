package module.card;

public class Effect {
    private int isEffect;
    private int isContinuous;
    private int effectNumber;
    private int continuousNumber;
    private String type;
    private int attack;
    private int defense;
    public Effect(int isEffect, int isContinuous) {
        this.isContinuous = isContinuous;
        this.isEffect = isEffect;
        if (isEffect != 0) effectNumber = isEffect - 1;
        if (isContinuous != 0) continuousNumber = isContinuous - 1;
    }
    // for field and equip cards
    public Effect (int attack , int defense , String type ){
        this.attack = attack;
        this.defense = defense;
        this.type = type;
        this.isEffect = 1;
    }
    public Effect (int isEffect , String isContinuous , String monsterType ){
        this.isEffect = isEffect;
        this.isContinuous = Integer.parseInt(isContinuous);
        this.type = monsterType;
        this.isEffect = 1;
    }
    public int getContinuousNumber() {
        return continuousNumber;
    }

    public int getEffectNumber() {
        return effectNumber;
    }

    public int getIsEffect() {
        return isEffect;
    }

    public int getIsContinuous() {
        return isContinuous;
    }

    public void setIsEffect(int isEffect) {
        this.isEffect = isEffect;
        this.effectNumber = isEffect - 1;
    }

    public void setIsContinuous(int isContinuous) {
        this.isContinuous = isContinuous;
        this.continuousNumber = isContinuous - 1;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public String getType() {
        return type;
    }
    public boolean hasEffect(){
        return this.isEffect != 0;
    }
}
