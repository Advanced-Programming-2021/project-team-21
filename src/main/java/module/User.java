package module;

public class User {
    private String username;
    private String password;
    private String nickname;
    private int money;

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public int getMoney() {
        return money;
    }
}
