package model.message;

public enum MessageTag {
    USERNAME("username"),
    PASSWORD("password"),
    NICKNAME("nickname"),
    TOKEN("token"),
    CARD("card"),
    NAME("name"),
    TIME("time"),
    MESSAGE("message");

    String name;
    MessageTag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
