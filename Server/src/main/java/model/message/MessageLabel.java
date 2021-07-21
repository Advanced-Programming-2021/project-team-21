package model.message;

public enum MessageLabel {
    CREATE("create"),
    LOGIN("login"),
    LOGOUT("logout"),
    ALL("all"),
    DELETE("delete"),
    ACTIVATE("activate"),
    ADD("add"),
    AVAILABLE_CARDS("available-cards"),
    BUY("buy"),
    GET("get"),
    START("start"),
    CANCEL("cancel");



    String name;
    MessageLabel(String name) {
        this.name = name;
    }

    public static MessageLabel getInstance() {
        return CREATE;
    }

    public String getName() {
        return name;
    }
}
