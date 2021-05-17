package module.card;

// a problem is when there are multiple cards in print

import controller.ProgramController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Card {
    protected String name;
    CardType cardType;
    private String description;
    private boolean isFaceUp;
    private int price;
    private boolean hasEffect;
    private boolean isATK;

    public static Card getCardByName(String name) {
        HashMap<String, Card> allCards = ProgramController.allCards;
        if (allCards == null)
            return null;
        for (String cardName : allCards.keySet()) {
            if (cardName.equals(name)) return allCards.get(cardName);
        }
        return null;
    }

    public static ArrayList<Card> sort(ArrayList<Card> cards) {
        ArrayList<Card> sort = new ArrayList<>(cards);
        for (int i = 0; i < sort.size(); i++) {
            for (int j = i + 1; j < sort.size(); j++) {
                if (sort.get(i).getName().compareTo(sort.get(j).getName()) > 0)
                    Collections.swap(sort, i, j);
            }
        }
        return sort;
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public CardType getCardType() {
        return cardType;
    }

    protected void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public boolean isFaceUp() {
        return isFaceUp;
    }

    public void setFaceUp(boolean faceUp) {
        isFaceUp = faceUp;
    }

    public int getPrice() {
        return price;
    }

    protected void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void destroyWithoutLosingLifePoints() {
    }

    public void setHasEffect(boolean hasEffect) {
        this.hasEffect = hasEffect;
    }

    public boolean HasEffect() {
        return hasEffect;
    }

    public boolean isATK() {
        return isATK;
    }

    public void setATK(boolean ATK) {
        isATK = ATK;
    }

    //for searching a card on board or deck or graveYard
    public Card getACardFromArrayByName(ArrayList<Card> cards, String name) {
        for (Card card : cards) {
            if (card.getName().equals(name)) return card;
        }
        return null;
    }
}
