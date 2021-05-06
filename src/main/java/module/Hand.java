package module;

import module.card.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Hand {
    private ArrayList<Card> cardsInHand;
    private User handOwner;
    private Deck deckToDraw;
    private Boolean canDraw;
    private Card selectedCardInHand;

    public Hand (User handOwner) {
        User.getUserByUsername(handOwner.getUsername()).setHand(this);
    }

    public void addCardToHand (Card selectedCard){
        cardsInHand.add(selectedCard);
    }

    public void removeCardFromHand (Card selectedCard) {
        cardsInHand.remove(selectedCard);
    }

    public ArrayList<Card> getCardsInHand () {
        return cardsInHand;
    }

    public Card drawACard () {
        return selectedCardInHand;
    }

    public void shuffleDeck () {
        Collections.shuffle(cardsInHand);
    }

    public void selectACard (int cardAddress) {
        selectedCardInHand =  cardsInHand.get(cardAddress - 1);
    }

    //public Card findACard(Card[] cards) {
    //    return null;
   // }

    public void discardACard (int place) {
        selectedCardInHand = null;
    }

    public Card selectARandomCardFromHand () {
        return cardsInHand.get(new Random().nextInt(cardsInHand.size() - 1));
    }
}
