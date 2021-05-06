package module;

import module.card.Card;
import view.PrintResponses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Hand {
    private Card[] cardsInHand;
    private User handOwner;
    private Deck deckToDraw;
    private Boolean canDraw;
    private Card selectedCardInHand;

    {
        selectedCardInHand = null;
        cardsInHand = new Card[6];
    }

    public Hand(User handOwner) {
        User.getUserByUsername(handOwner.getUsername()).setHand(this);
        deckToDraw = handOwner.getActiveDeck();
    }

    public void addCardToHand(Card cardToAdd) {

    }

    public void removeCardFromHand(int cardAddress) {
        cardsInHand[cardAddress - 1] = null;
    }

    public Card[] getCardsInHand() {
        return cardsInHand;
    }

    public void drawACard() {
        if (deckToDraw.getMainDeckCards().size() == 0)
            // ending game
            for (int i = getCardsInHand().length - 1; i >= 0; i++) {
                if (cardsInHand[i] == null)
                    cardsInHand[i] = deckToDraw.getMainDeckCards().get(0);
            }
        System.out.println("new card added to the hand : " + deckToDraw.getMainDeckCards().get(0).getName());
        deckToDraw.getMainDeckCards().remove(0);
    }

    public void shuffleDeck() {
        Collections.shuffle(deckToDraw.getMainDeckCards());
    }

    public void selectACard(int cardAddress) {
        if (cardAddress > 6)
            PrintResponses.printInvalidSelection();
        else if (cardsInHand[cardAddress - 1] == null)
            PrintResponses.printNoCardInPosition();
        else {
            selectedCardInHand = cardsInHand[cardAddress - 1];
            PrintResponses.printSuccessfulCardSelection();
        }
    }

    public void deselectACard() {
        if (selectedCardInHand == null)
            PrintResponses.printNoCardSelected();
        else {
            selectedCardInHand = null;
            PrintResponses.printSuccessfulCardDeselection();
        }
    }

    public void discardACard(int place) {

    }

    public Card selectARandomCardFromHand() {
        ArrayList<Integer> randomizeCards = new ArrayList<>();
        for (int i = 0; i < cardsInHand.length; i++)
            if (cardsInHand[i] != null)
                randomizeCards.add(i);
        Collections.shuffle(randomizeCards);
        return cardsInHand[randomizeCards.get(0)];
    }
}
