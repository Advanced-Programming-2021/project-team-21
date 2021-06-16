package module.card;

public enum WhereToChain {
    ATTACK("attack"),
    EFFECT_ACTIVATE("effect"),
    SUMMON("summon");
    String place;

    WhereToChain(String place) {
        this.place = place;
    }

    public String getPlace() {
        return place;
    }
}
