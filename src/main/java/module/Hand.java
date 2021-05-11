package module;

import module.card.Card;

import java.util.ArrayList;
import java.util.Collections;

public class Hand {
    private Card[] cardsInHand;
    private User handOwner;
    private Deck deckToDraw;
    private Boolean canDraw;

    {
        cardsInHand = new Card[6];
    }

    public Hand(User handOwner) {
        handOwner.setHand(this);
        deckToDraw = handOwner.getActiveDeck();
    }

    public void addCardToHand(Card cardToAdd) {
        for (int i = cardsInHand.length - 1; i >= 0; i--) {
            if (cardsInHand[i] == null) {
                cardsInHand[i] = cardToAdd;
                break;
            }
        }
    }

    public void removeCardFromHand(int cardAddress) {
        cardsInHand[cardAddress - 1] = null;
    }

    public Card[] getCardsInHand() {
        return cardsInHand;
    }

    public Card drawACard() {
        if (deckToDraw.getMainDeckCards().size() == 0)
            // ending game
            for (int i = getCardsInHand().length - 1; i >= 0; i--) {
                if (cardsInHand[i] == null) {
                    cardsInHand[i] = deckToDraw.getMainDeckCards().get(0);
                    break;
                }
            }
        deckToDraw.getMainDeckCards().remove(0);
        return deckToDraw.getMainDeckCards().get(0);
    }

    public void shuffleDeck() {
        Collections.shuffle(deckToDraw.getMainDeckCards());
    }

    public Card selectACard(int cardAddress) {
        return cardsInHand[cardAddress - 1];
    }

    public void discardACard(int place) {
        handOwner.getBoard().getGraveyard().add(cardsInHand[place - 1]);
        cardsInHand[place - 1] = null;
    }

    public Card selectARandomCardFromHand() {
        ArrayList<Integer> randomizeCards = new ArrayList<>();
        for (int i = 0; i < cardsInHand.length; i++)
            if (cardsInHand[i] != null)
                randomizeCards.add(i);
        Collections.shuffle(randomizeCards);
        return cardsInHand[randomizeCards.get(0)];
    }

    //checks if the given card is in hand
    public boolean isCardInHand(Card card){
        return true;
    }
}
