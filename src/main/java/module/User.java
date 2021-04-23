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
    }

    public void getPassword() {
    }

    public String getMoney() {
    }

    public void setMoney(int money) {
    }

    public Board getBoard() {
    }

    public void setBoard(Board board) {
    }

    public Board setUsername(String username) {
    }

    public void getUsername() {
    }

    public String setNickname(String nickname) {
    }

    public void getNichname() {
    }

    public String setScore(int score) {
    }

    public void getScore() {
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
