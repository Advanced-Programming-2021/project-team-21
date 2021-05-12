package module;

import module.card.Card;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import view.PrintResponses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Hand{
    private Card[] cardsInHand;
    private User handOwner;
    private Deck deckToDraw;
    private Boolean canDraw;
    private ArrayList<Integer> orderInHand;

    {
        cardsInHand = new Card[6];
        this.orderInHand = new ArrayList<>();
        orderInHand.add(5);
        orderInHand.add(3);
        orderInHand.add(1);
        orderInHand.add(2);
        orderInHand.add(4);
    }

    public Hand(User handOwner) {
        handOwner.setHand(this);
        deckToDraw = new Deck(handOwner.getActiveDeck());
    }


    public void addCardToHand(Card cardToAdd) {
        for (int i = orderInHand.size() - 1; i >= 0; i--) {
            if (cardsInHand[orderInHand.get(i)] == null) {
                cardsInHand[orderInHand.get(i)] = cardToAdd;
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
            for (int i = orderInHand.size() - 1; i >= 0; i--) {
                if (cardsInHand[orderInHand.get(i)] == null) {
                    cardsInHand[orderInHand.get(i)] = deckToDraw.getMainDeckCards().get(0);
                    break;
                }
            }
        System.out.println("new card added to the hand : " + deckToDraw.getMainDeckCards().get(0).getName());
        deckToDraw.getMainDeckCards().remove(0);
        return deckToDraw.getMainDeckCards().get(0);
    }

    public void shuffleDeck() {
        Collections.shuffle(deckToDraw.getMainDeckCards());
    }

    public Card selectACard(int cardAddress) {
        return cardsInHand [cardAddress - 1];
    }

    public void deselectACard(Card selectedCard) {
            selectedCard = null;
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
        for (int i = 0; i < cardsInHand.length; i++)
            if (cardsInHand[i] == card)
                return true;
        return false;
    }

    // the cards have been compared with reference reminder to change
    public Boolean isCardOnHand (Card card) {
        for (int i = 0; i < cardsInHand.length; i++){
            if (cardsInHand[i] == card)
                return true;
        }
        return false;
    }
    // to find cards with specific cards from deck (1) and hand(2) and graveYard (4)
    public   ArrayList<Card>  getCardsWithType(int identifier , String type){
        ArrayList<Card> found = new ArrayList<>();
        ArrayList <Card> lookingCards = new ArrayList<>();
        if (identifier >= 4) lookingCards.addAll(handOwner.getBoard().getGraveyard());
         if (identifier % 4 >= 2 ) lookingCards.addAll(Arrays.asList(this.cardsInHand));
        else if (identifier % 2 ==  1)lookingCards.addAll(this.deckToDraw.getMainDeckCards());
        for (Card card : lookingCards) {
            if (card.getCardType().getName().equals(type))found.add(card);
        }
        return found;
    }
}
