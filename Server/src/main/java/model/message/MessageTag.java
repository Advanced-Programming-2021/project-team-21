package model.message;

public enum MessageTag {
    USERNAME("username"),
    PASSWORD("password"),
    NICKNAME("nickname"),
    TOKEN("token"),
    NAME("name");

    String name;
    MessageTag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
