package model.message;

public enum MessageInstruction {
    USER("user"),
    DECK("deck");
String name;
    MessageInstruction(String name) {
    this.name = name;
    }

    public String getName() {
        return name;
    }
}