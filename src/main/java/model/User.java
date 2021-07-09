package model;

import controller.DataController;
import model.card.Card;

import java.util.ArrayList;
import java.util.Collections;

public class User {
    private final ArrayList<Deck> decks;
    private final ArrayList<Card> cards;
    private String username;
    private String password;
    private String nickname;
    private String avatar;
    private int score;
    private int coins;
    private Board board;
    private Hand hand;
    private ArrayList<Card> graveyard;
    private int lifePoints;
    private int maxLifePoint = 0;
    private boolean canSummonMonster = true, canSummonSpell = true, canSummonTrap = true, canAttack = true;
    private int winsInAMatch;
    private int CanNotDrawRounds;
    private int increaseATK, increaseDEF;
    private boolean hasSummonedAlteringATK;
    private int alteringATKPlace = -1;

    {
        coins = 100000;
        decks = new ArrayList<>();
        cards = new ArrayList<>();
    }

    public User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.avatar = setRandomAvatar();
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

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getCoins() {
        System.out.println("coin");
        return coins;
    }

    public String getAvatar() {
        return avatar;
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


    public Deck getDeckByName(String name) {
        for (Deck deck : decks) {
            if (deck.getName().equals(name)) {
                return deck;
            }
        }
        return null;
    }

    public String setRandomAvatar() {
        ArrayList<String> images = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            String address = "/images/avatars/avatar" + i + ".jfif";
            images.add(address);
        }
        Collections.shuffle(images);
        return images.get(0);
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
        System.out.println("LP");
        return lifePoints;
    }

    public void setLifePoints(int lifePoints) {
        this.lifePoints = lifePoints;
    }

    public boolean isCanSummonMonster() {
        return canSummonMonster;
    }

    public void setCanSummonMonster(boolean canSummonMonster) {
        this.canSummonMonster = canSummonMonster;
    }

    public boolean isCanNotSetSpell() {
        return !canSummonSpell;
    }

    public void setCanSummonSpell(boolean canSummonSpell) {
        this.canSummonSpell = canSummonSpell;
    }

    public boolean isCanSummonTrap() {
        return canSummonTrap;
    }

    public void setCanSummonTrap(boolean canSummonTrap) {
        this.canSummonTrap = canSummonTrap;
    }

    public int getWinsInAMatch() {
        return winsInAMatch;
    }

    public void setWinsInAMatch(int winsInAMatch) {
        this.winsInAMatch = winsInAMatch;
    }

    public int getMaxLifePoint() {
        return maxLifePoint;
    }

    public void setMaxLifePoint(int maxLifePoint) {
        this.maxLifePoint = Math.max(maxLifePoint, this.maxLifePoint);
    }

    public int getIncreaseATK() {
        return increaseATK;
    }

    public void setIncreaseATK(int increaseATK) {
        this.increaseATK = increaseATK;
    }

    public int getIncreaseDEF() {
        return increaseDEF;
    }

    public void setIncreaseDEF(int increaseDEF) {
        this.increaseDEF = increaseDEF;
    }

    public boolean isHasSummonedAlteringATK() {
        return hasSummonedAlteringATK;
    }

    public void setHasSummonedAlteringATK(boolean hasSummonedAlteringATK) {
        this.hasSummonedAlteringATK = hasSummonedAlteringATK;
    }

    public int getAlteringATKPlace() {
        return alteringATKPlace;
    }

    public void setAlteringATKPlace(int alteringATKPlace) {
        this.alteringATKPlace = alteringATKPlace;
    }

    public int getCanNotDrawRounds() {
        return CanNotDrawRounds;
    }

    public void setCanNotDrawRounds(int canNotDrawRounds) {
        CanNotDrawRounds = canNotDrawRounds;
    }

    public boolean isCanAttack() {
        return canAttack;
    }

    public void setCanAttack(boolean canAttack) {
        this.canAttack = canAttack;
    }
}
