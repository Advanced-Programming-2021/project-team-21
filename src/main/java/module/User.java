package module;

import controller.ProgramController;

import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private String nickname;
    private int score;
    private int coins;
    private ArrayList<Deck> decks;
    private ArrayList<Card> cards;
    private Board board;

    {
        coins = 0;
        decks = new ArrayList<>();
        cards = new ArrayList<>();
    }

    public User(String username, String password, String nickname) {
        setUsername(username);
        setPassword(password);
        setNickname(nickname);
        ProgramController.saveData(this);
    }

    public static User getUserByUsername(String username) {
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
        ProgramController.saveData(this);
    }

    public String getPassword() {
        return password;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
        ProgramController.saveData(this);
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
        ProgramController.saveData(this);
    }

    public Board setUsername(String username) {
        this.username = username;
        ProgramController.saveData(this);
    }

    public String getUsername() {
        return username;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        ProgramController.saveData(this);
    }

    public String getNickname() {
        return nickname;
    }

    public String setScore(int score) {
        this.score = score;
        ProgramController.saveData(this);
    }

    public int getScore() {
        return score;
    }

    public int setActiveDeck(Deck deck) {
    }

    public void getActiveDeck() {
    }

    public Deck setSideDeck(Deck deck) {
    }

    public void getSideDeck() {
    }

    public Deck getDecks() {
    }

    public Arraylist<Deck> addDeck(deck:Deck) {
    }

    public void removeDeck(deck:Deck) {
    }

    public void addCard(card:Card) {
    }

    public void remvoeCard(card:Card) {
    }

    public void increaseMoney(amount:int) {
    }

    public void increaseScore(amount:int) {
    }

    public boolean doesUserExist(username:String) {
    }
}
