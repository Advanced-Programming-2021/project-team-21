package model.message;

public enum MessageLabel {
    CREATE("create"),
    LOGIN("login"),
    LOGOUT("logout"),
    ALL("all"),
    DELETE("delete"),
    ACTIVATE("activate"),
    ADD("add"),
    BUY("buy"),
    AVAILABLE_CARDS("available-cards"),
    GET("get"),
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
