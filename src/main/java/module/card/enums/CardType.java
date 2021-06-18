package module.card.enums;

//added name to check which is which
public enum CardType {
    NORMAL("Normal"),
    EFFECT("Effect"),
    RITUAL("Ritual");
    private final String name;

    CardType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
