package module;

public class User {
    private String username;
    private String password;
    private String nickname;
    private int money;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMoney() {
        return money;
    }

    public String getUsername() {
        return username;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
