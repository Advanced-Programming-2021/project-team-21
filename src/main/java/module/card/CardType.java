package module.card;

//added name to check which is which
public enum CardType {
    NORMAL("Normal"),
    EFFECT("Effect"),
    RITUAL("Ritual");
    private String name;

    CardType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
