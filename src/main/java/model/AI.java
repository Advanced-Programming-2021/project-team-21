package model;

import controller.ProgramController;
import model.card.Card;
import model.card.Monster;
import model.card.enums.CardType;

public class AI extends User {


    private Duel currentDuel;

    public AI(String username, String password, String nickname) {
        super(username, password, nickname);
        createDeck();
    }


    /*
     * There is going to be a while (aiTurn) in this function.
     * It will call DuelMenu's run() with a string as a parameter.
     * The parameter will be the command we want to execute.
     */

    public void setCurrentDuel(Duel currentDuel) {
        this.currentDuel = currentDuel;
    }

    public void run() {
        //startStrategy();
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

   // private void startStrategy() {
     //   IntStream.range(0, 2).forEach(i -> ProgramController.currentMenu.run("next phase"));
      //  if (getHand().getNumberOfCardsInHand() > 0 && getBoard().getMonsterNumber() < 4) {
       //     ProgramController.currentMenu.run("select --hand 1");
     //       ProgramController.currentMenu.run("summon");
       //     ProgramController.currentMenu.run("next phase");
       //     ProgramController.currentMenu.run("select --monster 1");
       //     if (currentDuel.getRival().getBoard().getMonsterNumber() > 0)
       //         ProgramController.currentMenu.run("attack 1");
       //     else
     //           ProgramController.currentMenu.run("attack direct");
      //      IntStream.range(0, 2).forEach(i -> ProgramController.currentMenu.run("next phase"));
      //  }
   // }
}
