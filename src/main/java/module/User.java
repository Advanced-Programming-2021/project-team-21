package module;

import controller.DataController;
import controller.ProgramController;
import module.card.Card;
import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

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
        coins = 100000;
        decks = new ArrayList<>();
        cards = new ArrayList<>();
    }

    public User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        DataController.saveData(this);
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public static User getUserByUsername(String username) {
        return DataController.getUserByUsername(username);
    }

    public static User getUserByNickname(String nickname) {
        return DataController.getUserByNickname(nickname);
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
        DataController.saveData(this);
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
        DataController.saveData(this);
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
        DataController.saveData(this);
    }

    public void setUsername(String username) {
        this.username = username;
        DataController.saveData(this);
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        DataController.saveData(this);
    }

    public void setScore(int score) {
        this.score = score;
        DataController.saveData(this);
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
        DataController.saveData(deck);
    }

    public void setSideDeck(Deck deck) {
        this.decks.add(deck);
        DataController.saveData(deck);
    }

    public ArrayList<Card> getSideDeck() {
        for (Deck deck : decks){
            return deck.getSideDeckCards();
        }
        return null;
    }

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    public void  addDeck(Deck deck) {
        this.decks.add(deck);
        DataController.saveData(this);
    }

    public void removeDeck(Deck deck) {
        this.decks.remove(deck);
        DataController.saveData(this);
    }

    public void addCard(Card card) {
        this.cards.add(card);
        DataController.saveData(this);
    }

    public void removeCard(Card card) {
        this.cards.remove(card);
        DataController.saveData(this);
    }

    public void increaseCoins(int amount) {
        this.coins += amount;
        DataController.saveData(this);
    }

    public void increaseScore(int amount) {
        this.score += amount;
        DataController.saveData(this);
    }

    public boolean doesUserExist(String username) {
        return DataController.getUserByUsername(username) != null;
    }
    public Deck getDeckByName(String name){
        for (Deck deck : decks) {
            if (deck.getName().equals(name)){
                return deck;
            }
        }
        return null;
    }
    public void deactivateDecks(String name){
        for (Deck deck : decks) {
            if (deck.getName().equals(name))continue;
            deck.setActive(false);
        }
        DataController.saveData(this);
    }
    public static ArrayList<User>sort(ArrayList<User> users){
        ArrayList<User>sort = new ArrayList<>(users);
        for (int i = 0; i < sort.size(); i++) {
            for (int j = i + 1; j < sort.size(); j++) {
                if(checkUserScore(sort.get(i) , sort.get(j))){
                    Collections.swap(sort , i , j );
                }
            }
        }
        return sort;
    }

    private static boolean checkUserScore(User user, User user1) {
    if (user.getScore() < user1.getScore())return true;
    else if(user.getScore() == user1.getScore()){
        return user.getUsername().compareTo(user1.getUsername()) > 0;
    }
    return false;
    }
}
