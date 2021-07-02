package model;

import com.rits.cloning.Cloner;
import controller.ProgramController;
import view.DuelMenu;
import model.card.Card;
import model.card.Monster;
import model.card.enums.CardType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class Hand {
    private final Card[] cardsInHand;
    private final Deck deckToDraw;
    private final User handOwner;

    {
        cardsInHand = new Card[6];
    }

    public Hand(User handOwner) {
        Cloner cloner = new Cloner();
        handOwner.setHand(this);
        this.handOwner = handOwner;
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
        shift();
    }

    private void shift() {
        for (int i = 0; i < cardsInHand.length; i++) {
            if (cardsInHand[i] == null) {
                if (i + 1 < cardsInHand.length) {
                    cardsInHand[i] = cardsInHand[i + 1];
                    cardsInHand[i + 1] = null;
                }
            }
        }
    }

    public Card[] getCardsInHand() {
        return cardsInHand;
    }

    public Card drawACard() {
        if (deckToDraw.getMainDeckCards().size() == 0) {
            ((DuelMenu) ProgramController.currentMenu).endTheGame();
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
            if (card instanceof Monster && ((Monster) card).getMonsterType().getName().equals(type)) {
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
        int level = 20;
        for (Card card : cardsInHand) {
            if (card instanceof Monster && card.getCardType().getName().equals("Ritual") && ((Monster) card).getLevel() < level)
                level = ((Monster) card).getLevel();
        }
        return level;
    }

    public int getNumberOfCardsInHand() {
        return (int) Arrays.stream(cardsInHand).filter(Objects::nonNull).count();
    }

    public boolean isThereAnyCardWithGivenTypeInMonsters(CardType cardType) {
        for (Card card : cardsInHand) {
            if (card instanceof Monster) {
                Monster monster = (Monster) card;
                if (monster.getCardType().getName().equals(cardType.getName()))
                    return true;
            }
        }
        return false;
    }

    public Deck getDeckToDraw() {
        return deckToDraw;
    }
}
