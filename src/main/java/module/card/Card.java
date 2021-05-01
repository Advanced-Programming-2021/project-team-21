package module.card;

// a problem is when there are multiple cards in print
import controller.ProgramController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public abstract class Card {
    protected String name;
    CardType cardType;
    private String description;
    private boolean isFaceUp;
    private int price;

    public static Card getCardByName(String name) {
        HashMap <String , Card>allCards = ProgramController.allCards;
        for (String cardName : allCards.keySet()){
            if (cardName.equals(name))return allCards.get(cardName);
        }
        return null;
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected CardType getCardType() {
        return cardType;
    }

    protected void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    protected boolean isFaceUp() {
        return isFaceUp;
    }

    protected void setFaceUp(boolean faceUp) {
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

    public abstract void destroyWithoutLosingLifePoints();

    public static ArrayList<Card> sort(ArrayList<Card> cards){
        ArrayList<Card>sort = new ArrayList<>(cards);
        for (int i = 0; i < sort.size(); i++) {
            for (int j = i + 1; j < sort.size(); j++) {
                if (sort.get(i).getName().compareTo(sort.get(j).getName()) > 0)
                    Collections.swap(sort , i , j);
            }
        }
        return sort;
    }
}
