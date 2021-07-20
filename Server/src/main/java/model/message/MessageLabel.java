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
    GET("get");



    String name;

    MessageLabel(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }
}
