package module;

import module.card.Card;

import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> cardsInHand;
    private User handOwner;
    private Deck deckToDraw;
    private Boolean canDraw;

    private Hand (User handOwner) {
        this.handOwner = handOwner;
    }

    private void addCardToHand (){

    }

    private void removeCardFromHand () {

    }

    private  ArrayList<Card> getCardsInHand () {
        return cardsInHand;
    }

    private void drawACard () {

    }

    private void shuffleDeck () {

    }

    private Card selectACard (int cardAddress) {

    }

    private Card findACard(Card[] cards) {

    }

    private void discardACard (int place) {

    }

    private Card selectARandomCardFromHand () {

    }
}
