package module.card;

public enum SpellTrapIcon {
    NORMAL("Normal"),
    CONTINUOUS("Continuous"),
    QUICK_PLAY("Quick play"),
    COUNTER("Counter"),
    FIELD("Field"),
    EQUIP("Equip"),
    RITUAL("Ritual");
    private String name;

    SpellTrapIcon(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
