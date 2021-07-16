package model;

public enum WhereToChain {
    ATTACK("attack"),
    EFFECT_ACTIVATE("effect"),
    SUMMON("summon");
    private final String place;

    WhereToChain(String place) {
        this.place = place;
    }

    public String getPlace() {
        return place;
    }
}
