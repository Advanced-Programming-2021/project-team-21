package controller.Effects;

import controller.ProgramController;
import view.DuelMenu;
import model.Board;
import model.Duel;
import model.User;
import model.card.Monster;
import view.PrintResponses;

import java.util.ArrayList;

public class BattlePhaseStart {
    //command Knight
    // texchanger
    // suijin
    public static boolean run(Monster attack, Monster defense, User secondUser, Duel duel) {
        Board board = secondUser.getBoard();
        // for summoning with specific type
        if (defense.getSummonACardFromEveryWhere().hasEffect()) {
            ArrayList<Monster> cards = secondUser.getHand().getCardsWithType(7, defense
                    .getSummonACardFromEveryWhere().getType());
            DuelMenu.isGetFromGY = true;
            DuelMenu.isGetFromDeck = true;
            DuelMenu.isGetFromHand = true;
            getNormal(cards);
            DuelMenu.specialSummonsedCards = new ArrayList<>(cards);
            if (cards.size() != 0) PrintResponses.printSpecialSummonCards(cards);
            while (DuelMenu.specialSummonsedCards != null) {
                DuelMenu.checkSpecialSummon(ProgramController.scanner.nextLine(), duel, true);
            }
            defense.getSummonACardFromEveryWhere().finishEffect();
            defense.getSummonACardFromEveryWhere().setNeedsToBeReset(true);
            return true;
        }
        //for undefeatable effect
        else if (defense.getUndefeatable().hasEffect()) {
            if (defense.getUndefeatable().getContinuousNumber() == 0) {
                defense.getUndefeatable().finishEffect();
                defense.getUndefeatable().setNeedsToBeReset(true);
            }
            return board.getMonsterNumber() > defense.getUndefeatable().getContinuousNumber();
        } else if (defense.getCanChangeTheAttackersATK().hasEffect()) {
            attack.setAtk(attack.getAtk() + defense.getCanChangeTheAttackersATK().getEffectNumber());
        }
        return false;
    }

    private static void getNormal(ArrayList<Monster> cards) {
        cards.removeIf(card -> !card.getCardType().getName().equals("Normal"));
    }
}
