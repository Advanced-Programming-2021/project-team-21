package controller.Effects;

import controller.menu.DuelMenu;
import module.Board;
import module.Duel;
import module.User;
import module.card.Monster;

import java.util.ArrayList;

public class BattlePhaseStart {
    //command Knight
    // texchanger
    // suijin
    public static boolean run(Monster attack, Monster defense, User secondUser, Duel duel) {
        System.out.println(attack.getName() + " " + defense.getName());
        Board board = secondUser.getBoard();
        // for summoning with specific type
        if (defense.getSummonACardFromEveryWhere().hasEffect()) {
            ArrayList<Monster> cards = secondUser.getHand().getCardsWithType(7, defense
                    .getSummonACardFromEveryWhere().getType());
            DuelMenu.isGetFromGY = true;
            DuelMenu.isGetFromDeck = true;
            DuelMenu.isGetFromHand = true;
            DuelMenu.specialSummonsedCards = new ArrayList<>(cards);
        }
        //for undefeatable effect
        else if (defense.getUndefeatable().hasEffect()) {
            if (defense.getUndefeatable().getContinuousNumber() == 1) {
                defense.getUndefeatable().finishEffect();
                defense.getUndefeatable().setNeedsToBeReset(true);
            }
            return board.getMonsterNumber() > defense.getUndefeatable().getContinuousNumber();
        } else if (defense.getCanChangeTheAttackersATK().hasEffect()) {
            System.out.println("check is battle phase start");
            attack.setAtk(attack.getAtk() + defense.getCanChangeTheAttackersATK().getEffectNumber());
        }
        return false;
    }
}
