package module;

import controller.DataController;
import module.card.Card;
import module.card.Monster;

import java.util.ArrayList;

public class Deck {
    private ArrayList<Card> mainDeckCards;
    private ArrayList<Card> sideDeckCards;
    private String name;
    private boolean isActive;
    private User userWhoOwns;

    {
        isActive = false;
    }

    public Deck(User userWhoOwns, String name) {
        setName(name);
        setUserWhoOwns(userWhoOwns);
        DataController.saveData(this);
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

    public ArrayList<Card> getMainDeckCards() {
        return mainDeckCards;
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


    public User getUserWhoOwns() {
        return userWhoOwns;
    }

    public void setUserWhoOwns(User userWhoOwns) {
        this.userWhoOwns = userWhoOwns;
    }

    @Override
    public String toString() {
        StringBuilder monstersToString = new StringBuilder(), spellAndTrapToString = new StringBuilder();
        ArrayList<Card> cards = new ArrayList<>(mainDeckCards);
        cards.addAll(sideDeckCards);
        for (Card card : cards) {
            if (card instanceof Monster) {
                monstersToString.append(card.getName()).append(": ").append(card.getDescription()).append("\n");
            } else {
                spellAndTrapToString.append(card.getName()).append(": ").append(card.getDescription()).append("\n");
            }
        }
        return "Deck:" +
                name +
                "\nSide/Main deck:\nMonsters:\n" +
                monstersToString +
                "Spell and Traps:\n" +
                spellAndTrapToString;
    }
}
