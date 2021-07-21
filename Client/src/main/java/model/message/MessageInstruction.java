package model.message;

public enum MessageInstruction {
    USER("user"),
    DECK("deck"),
    SHOP("shop"),
    CARD("card");
String name;
    MessageInstruction(String name) {
    this.name = name;
    }

    public String getName() {
        return name;
    }
}
