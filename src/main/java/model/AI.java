package model;

import controller.ProgramController;
import model.card.Card;
import model.card.Monster;
import model.card.enums.CardType;

public class AI extends User {


    public AI(String username, String password, String nickname) {
        super(username, password, nickname);
        createDeck();
    }


    /*
     * There is going to be a while (aiTurn) in this function.
     * It will call DuelMenu's run() with a string as a parameter.
     * The parameter will be the command we want to execute.
     */

    public void run() {
    }

    private void createDeck() {
        Deck deck = new Deck("AI Deck");
        int counter = 0;
        for (Card card : ProgramController.allCards.values()) {
            if (card instanceof Monster && card.getCardType().equals(CardType.NORMAL)
                    && ((Monster) card).getLevel() < 5) {
                for (int i = 0; i < 3; i++) {
                    deck.addCardToMainDeck(Card.getCardByName(card.getName()));
                    counter++;
                }
            }
            if (counter == 40)
                break;
        }
        getDecks().add(deck);
        deck.setActive(true);
    }
}
