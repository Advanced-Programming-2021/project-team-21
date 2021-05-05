package module;

import module.card.Card;

import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> cardsInHand;
    private User handOwner;
    private Deck deckToDraw;
    private Boolean canDraw;

    public Hand () {

    }

    public void addCardToHand (){

    }

    public void removeCardFromHand () {

    }

    public ArrayList<Card> getCardsInHand () {
        return cardsInHand;
    }

    public void drawACard () {

    }

    public void shuffleDeck () {

    }

    public Card selectACard (int cardAddress) {
        return null;
    }

    public Card findACard(Card[] cards) {
        return null;

    }

    public void discardACard (int place) {

    }

    public Card selectARandomCardFromHand () {
        return null;

    }
}
