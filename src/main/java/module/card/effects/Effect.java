package module.card.effects;

public class Effect {
    private int isEffect;
    private int isEffectHolder;
    private int isContinuous;
    private int isContinuousHolder;
    private int effectNumber;
    private int continuousNumber;
    private String type;
    private int attack;
    private int defense;
    private boolean needsToBeReset;

    public Effect(int isEffect, int isContinuous) {
        this.isContinuous = isContinuous;
        this.isEffect = isEffect;
        this.isEffectHolder = isEffect;
        this.isContinuousHolder = isContinuous;
        if (isEffect != 0) effectNumber = isEffect - 1;
        if (isContinuous != 0) continuousNumber = 2 * isContinuous - 1;
        needsToBeReset = false;
    }

    // for field and equip cards
    public Effect(int attack, int defense, String type) {
        this.attack = attack;
        this.defense = defense;
        this.type = type;
        this.isEffect = 1;
    }

    public Effect(int isEffect, String isContinuous, String monsterType) {
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

    public void setIsEffect(int isEffect) {
        this.isEffect = isEffect;
        this.effectNumber = isEffect - 1;
    }

    public int getIsContinuous() {
        return isContinuous;
    }

    public void setIsContinuous(int isContinuous) {
        this.isContinuous = isContinuous;
        this.continuousNumber = isContinuous - 1;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public String getType() {
        return type;
    }

    public boolean hasEffect() {
        return this.isEffect != 0;
    }

    public int getIsContinuousHolder() {
        return isContinuousHolder;
    }

    public int getIsEffectHolder() {
        return isEffectHolder;
    }

    public void finishEffect() {
        this.isEffect = 0;
        this.isContinuous = 0;
    }

    public boolean isNeedsToBeReset() {
        return needsToBeReset;
    }

    public void setNeedsToBeReset(boolean needsToBeReset) {
        this.needsToBeReset = needsToBeReset;
    }

    public void resetEffect() {
        this.isEffect = isEffectHolder;
        this.isContinuous = isContinuousHolder;
    }
}
