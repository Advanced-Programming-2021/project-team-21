package model;

import controller.DataController;
import controller.ProgramController;
import model.card.Card;
import model.card.Monster;

import java.io.Serializable;
import java.util.ArrayList;

public class Deck implements Serializable {
    private ArrayList<Card> mainDeckCards;
    private ArrayList<Card> sideDeckCards;
    private String name;
    private boolean isActive;

    {
        isActive = false;
        mainDeckCards = new ArrayList<>();
        sideDeckCards = new ArrayList<>();
    }

    public Deck(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public ArrayList<Card> getSideDeckCards() {
        return sideDeckCards;
    }

    public void setSideDeckCards(ArrayList<Card> sideDeckCards) {
        this.sideDeckCards = sideDeckCards;
    }

    public ArrayList<Card> getMainDeckCards() {
        return mainDeckCards;
    }

    public void setMainDeckCards(ArrayList<Card> mainDeckCards) {
        this.mainDeckCards = mainDeckCards;
    }

    public void addCardToMainDeck(Card newCard) {
        mainDeckCards.add(newCard);
    }

    public void removeCardFromMainDeck(Card cardToRemove) {
        mainDeckCards.remove(cardToRemove);
    }

    public void addCardToSideDeck(Card newCard) {
        sideDeckCards.add(newCard);
    }

    public void removeCardFromSideDeck(Card cardToRemove) {
        sideDeckCards.remove(cardToRemove);
    }

    public int getNumberOfMainDeckCards() {
        return mainDeckCards.size();
    }

    public int getNumberOfSideDeckCards() {
        return sideDeckCards.size();
    }

    public String toString(String type) {
        StringBuilder monstersToString = new StringBuilder(), spellAndTrapToString = new StringBuilder();
        ArrayList<Card> cards;
        if (type.equals("side")) cards = new ArrayList<>(this.getSideDeckCards());
        else cards = new ArrayList<>(this.getMainDeckCards());
        Card.sort(cards);
        for (Card card : cards) {
            if (card instanceof Monster) {
                monstersToString.append(card.getName()).append(": ").append(card.getDescription()).append("\n");
            } else {
                spellAndTrapToString.append(card.getName()).append(": ").append(card.getDescription()).append("\n");
            }
        }
        return "Deck:" +
                name +
                "\n" + type + " deck:\nMonsters:\n" +
                monstersToString +
                "Spell and Traps:\n" +
                spellAndTrapToString;
    }
    public boolean isValid() {
        return this.getNumberOfMainDeckCards() <= 39 ||
                this.getNumberOfMainDeckCards() >= 61 ||
                this.getNumberOfSideDeckCards() >= 16;
    }
}
