package controller.Effects;

import view.DuelMenu;
import model.Duel;
import model.User;
import model.card.Card;
import model.card.Chain;
import model.card.Monster;
import model.card.Trap;
import view.PrintResponses;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class TrapActivation {

    public static boolean run(Trap trap, User userNow, User rival, Duel duel, Chain card, Chain first) throws FileNotFoundException {
        if (trap.getCanAttackLP().hasEffect()) {
            handleAttackLP(trap, rival, duel, (Monster) first.getCard());
        }
        if (trap.getDestroyAttackMonsters().hasEffect()) {
            handleDestroyAttackMonsters(rival, duel);
        }
        if (trap.getCanDestroyFromDeckAndHand().hasEffect()) {
            handleDestroyCardsFromHandAndDeck(userNow, rival, duel, card,
                    trap.getCanDestroyFromDeckAndHand().getEffectNumber());
        }
        if (trap.getCostLP().hasEffect()) {
            duel.changeLP(userNow, trap.getCostLP().getEffectNumber());
        }
        if (trap.getCanNegateWholeAttack().hasEffect() || trap.getNegateASummon().hasEffect()) {
            return true;
        }
        if (trap.getCanDestroyMonsterSummonWithATK().hasEffect()) {
            duel.addCardToGraveyard(first.getCard(), 10, userNow);
            return true;
        }
        if (trap.getCanNotDraw().hasEffect()) {
            userNow.setCanNotDrawRounds(trap.getCanNotDraw().getEffectNumber());
        }
        if (trap.getCanDestroyAll().hasEffect()) {
            handleDestroyAll(userNow, rival, duel, first);
            return true;
        }
        if (trap.getCanSummonFromGY().hasEffect()) {
            ArrayList<Monster> cards = new ArrayList<>();
            for (Card card1 : userNow.getGraveyard()) {
                if (card1 instanceof Monster) cards.add((Monster) card1);
            }
            DuelMenu.specialSummonsedCards = cards;
            DuelMenu.isGetFroOpponentGY = true;
            PrintResponses.printSpecialSummonCards(cards);
            while (true) {
                if (!DuelMenu.checkSpecialSummon( duel, false)) break;
            }
        }
        return false;
    }

    private static void handleDestroyAll(User userNow, User rival, Duel duel, Chain first) {
        duel.addCardToGraveyard(first.getCard(), 10, userNow);
        for (Card monster : rival.getBoard().getMonsters()) {
           if (monster != null)duel.addCardToGraveyard(monster, rival.getBoard().getAddressByCard(monster), rival);
        }
        for (Card monster : userNow.getBoard().getMonsters()) {
          if (monster!= null) duel.addCardToGraveyard(monster, userNow.getBoard().getAddressByCard(monster), userNow);
        }
    }

    private static void handleDestroyCardsFromHandAndDeck(User userNow, User rival, Duel duel, Chain card, int number) {
        int destroyed = 0;
        for (int i = 0; i < rival.getHand().getDeckToDraw().getMainDeckCards().size(); i++) {
            Card mainDeckCard = rival.getHand().getDeckToDraw().getMainDeckCards().get(i);
            if (mainDeckCard.getName().equals(card.getCardName())) {
                destroyed++;
                if (destroyed == number) return;
                rival.getHand().getDeckToDraw().removeCardFromMainDeck(mainDeckCard);
                duel.addCardToGraveyard(mainDeckCard, 10, rival);
            }
        }
        for (int i = 0; i < rival.getHand().getCardsInHand().length; i++) {
            Card card1 = rival.getHand().getCardsInHand()[i];
            if (card1 == null) continue;
            if (card1.getName().equals(card.getCardName())) {
                destroyed++;
                if (destroyed == number) return;
                rival.getHand().removeCardFromHand(i);
                duel.addCardToGraveyard(card1, 10, rival);
            }
        }
        if (destroyed == 0) {
            Card random = userNow.getHand().selectARandomCardFromHand();
            for (int i = 0; i < userNow.getHand().getCardsInHand().length; i++) {
                Card card1 = userNow.getHand().getCardsInHand()[i];
                if (card1 == random) {
                    userNow.getHand().removeCardFromHand(i);
                    duel.addCardToGraveyard(card1, 10, userNow);
                }
            }
        }
    }

    private static void handleDestroyAttackMonsters(User rival, Duel duel) {
        for (Card card1 : rival.getBoard().getMonsters()) {
            if (card1 == null)continue;
            Monster monster = (Monster) card1;

            if (monster.isATK()) {
                rival.getBoard().removeMonster(rival.getBoard().getAddressByCard(monster));
                duel.addCardToGraveyard(monster, rival.getBoard().getAddressByCard(monster), rival);
            }
        }
    }

    private static void handleAttackLP(Trap trap, User rival, Duel duel, Monster card) {
        int number;
        if (trap.getCanAttackLP().getEffectNumber() == 1) {
            number = card.getAtk();
        } else {
            number = trap.getCanAttackLP().getEffectNumber();
        }
        duel.changeLP(rival, -number);
    }
}

