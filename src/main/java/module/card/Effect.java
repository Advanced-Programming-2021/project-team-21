package module.card;

public class Effect {
    private int isEffect;
    private int isContinuous;
    private int effectNumber;
    private int continuousNumber;

    public Effect(int isEffect, int isContinuous) {
        this.isContinuous = isContinuous;
        this.isEffect = isEffect;
        if (isEffect != 0) effectNumber = isEffect - 1;
        if (isContinuous != 0) continuousNumber = isContinuous - 1;
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
}
