package module;

import com.rits.cloning.Cloner;
import module.card.Card;
import module.card.Monster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Hand {
    private final Card[] cardsInHand;
    private final Deck deckToDraw;
    private User handOwner;
    private Boolean canDraw;

    {
        cardsInHand = new Card[6];
    }

    public Hand(User handOwner) {
        Cloner cloner = new Cloner();
        handOwner.setHand(this);
        deckToDraw = cloner.deepClone(handOwner.getActiveDeck());
    }


    public void addCardToHand(Card cardToAdd) {
        for (int i = 0; i < cardsInHand.length; i++) {
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
        if (deckToDraw.getMainDeckCards().size() == 0) {
            // ending game
            return null;
        }
        for (int i = 0; i < cardsInHand.length; i++) {
            if (cardsInHand[i] == null) {
                cardsInHand[i] = deckToDraw.getMainDeckCards().get(0);
                break;
            }
        }
        deckToDraw.getMainDeckCards().remove(0);
        return deckToDraw.getMainDeckCards().get(0);
    }

    public void removeFromDeck(String name) {
        deckToDraw.getMainDeckCards().removeIf(mainDeckCard -> mainDeckCard.getName().equals(name));
    }

    public void shuffleDeck() {
        Collections.shuffle(deckToDraw.getMainDeckCards());
    }

    public Card selectACard(int cardAddress) {
        return cardsInHand[cardAddress - 1];
    }

    public void deselectACard(Card selectedCard) {
        selectedCard = null;
    }

    public void discardACard(int place) {
        handOwner.getGraveyard().add(cardsInHand[place - 1]);
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
    public boolean isCardInHand(Card card) {
        for (Card value : cardsInHand)
            if (value == card)
                return true;
        return false;
    }


    // to find cards with specific cards from deck (1) and hand(2) and graveYard (4)
    public ArrayList<Monster> getCardsWithType(int identifier, String type) {
        ArrayList<Monster> found = new ArrayList<>();
        ArrayList<Card> lookingCards = new ArrayList<>();
        if (identifier >= 4) lookingCards.addAll(handOwner.getGraveyard());
        if (identifier % 4 >= 2) lookingCards.addAll(Arrays.asList(this.cardsInHand));
        else if (identifier % 2 == 1) lookingCards.addAll(this.deckToDraw.getMainDeckCards());
        for (Card card : lookingCards) {
            if (card.getCardType().getName().equals(type) && card instanceof Monster) {
                Monster monster = (Monster) card;
                found.add(monster);
            }
        }
        return found;
    }

    public String showCardsInHandToString() {
        int countCardsInHand = 0;
        for (Card card : cardsInHand)
            if (card != null)
                countCardsInHand++;
        StringBuilder showCardsInHand = new StringBuilder();
        for (int i = 0; i < countCardsInHand; i++)
            showCardsInHand.append("C   ");
        int countCInString = 0;
        for (int i = 0; i < showCardsInHand.length(); i++)
            if (showCardsInHand.charAt(i) == 'C')
                countCInString++;
        for (int i = 1; i <= 6 - countCInString; i++)
            showCardsInHand.append("    ");
        return showCardsInHand.toString();
    }

    public String showCardsInHandToStringReverse() {
        StringBuilder reverseString = new StringBuilder(showCardsInHandToString());
        return reverseString.reverse().toString();
    }


    public int getNumberOfRemainingCardsInDeck() {
        return deckToDraw.getNumberOfMainDeckCards();
    }

    public int getMinLevelOfRitualMonstersInHand() {
        int level = 0;
        for (Card card : cardsInHand) {
            if (card instanceof Monster && card.getCardType().getName().equals("Ritual") && ((Monster) card).getLevel() < level)
                level = ((Monster) card).getLevel();
        }
        return level;
    }

    public Deck getDeckToDraw() {
        return deckToDraw;
    }
}
