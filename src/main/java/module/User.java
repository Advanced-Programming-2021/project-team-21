package module;

import controller.DataController;
import module.card.Card;

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
    private Hand hand;
    private ArrayList<Card> graveyard;
    private int lifePoints;

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

    public static User getUserByUsername(String username) {
        return DataController.getUserByUsername(username);
    }

    public static User getUserByNickname(String nickname) {
        return DataController.getUserByNickname(nickname);
    }

    public ArrayList<Card> getCards() {
        return cards;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        DataController.saveData(this);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        DataController.saveData(this);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
        DataController.saveData(this);
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

    public ArrayList<Card> getSideDeck() {
        for (Deck deck : decks) {
            return deck.getSideDeckCards();
        }
        return null;
    }

    public void setSideDeck(Deck deck) {
        this.decks.add(deck);
        DataController.saveData(deck);
    }

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    public void addDeck(Deck deck) {
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

    public Deck getDeckByName(String name) {
        for (Deck deck : decks) {
            if (deck.getName().equals(name)) {
                return deck;
            }
        }
        return null;
    }

    public void deactivateDecks(String name) {
        for (Deck deck : decks) {
            if (deck.getName().equals(name)) continue;
            deck.setActive(false);
        }
        DataController.saveData(this);
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public ArrayList<Card> getGraveyard() {
        return graveyard;
    }

    public void setGraveyard(ArrayList<Card> graveyard) {
        this.graveyard = graveyard;
    }

    public int getLifePoints() {
        return lifePoints;
    }

    public void setLifePoints(int lifePoints) {
        this.lifePoints = lifePoints;
    }
}
