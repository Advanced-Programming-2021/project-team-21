package module.card.enums;

public enum SpellTrapIcon {
    NORMAL("Normal"),
    CONTINUOUS("Continuous"),
    QUICK_PLAY("Quick play"),
    COUNTER("Counter"),
    FIELD("Field"),
    EQUIP("Equip"),
    RITUAL("Ritual");
    private final String name;

    SpellTrapIcon(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
