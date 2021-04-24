package module;

import controller.ProgramController;
import module.card.Card;

import java.io.File;
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
        return null;
    }

    public static User getUserByNickname(String nickname) {
        return null;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
        ProgramController.saveData(this);
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

    public void setUsername(String username) {
        this.username = username;
        ProgramController.saveData(this);
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        ProgramController.saveData(this);
    }

    public void setScore(int score) {
        this.score = score;
        ProgramController.saveData(this);
    }

    public int getScore() {
        return score;
    }

    public Deck getActiveDeck() {
        for (Deck deck : decks) {
            if (deck.isActive())
                return deck;
        }
        return null;
    }

    public void setActiveDeck(Deck deck) {
        deck.setActive(true);
        ProgramController.saveData(deck);
    }

    public void setSideDeck(Deck deck) {

    }

    public Deck getSideDeck() {
        return null;
    }

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    public void  addDeck(Deck deck) {
        this.decks.add(deck);
        ProgramController.saveData(this);
    }

    public void removeDeck(Deck deck) {
        this.decks.remove(deck);
        ProgramController.saveData(this);
    }

    public void addCard(Card card) {
        this.cards.add(card);
        ProgramController.saveData(this);
    }

    public void removeCard(Card card) {
        this.cards.remove(card);
        ProgramController.saveData(this);
    }

    public void increaseCoins(int amount) {
        this.coins += amount;
        ProgramController.saveData(this);
    }

    public void increaseScore(int amount) {
        this.score += amount;
        ProgramController.saveData(this);
    }

    public boolean doesUserExist(String username) {
        File file = new File("directoy????");
        String[] pathNames = file.list();
        if (pathNames != null) {
            for (String pathName : pathNames){
                if (pathName.equals(username))
                    return true;
            }
        }
        return false;
    }
}
