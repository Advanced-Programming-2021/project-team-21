package module.card;

import controller.menu.DuelMenu;
import module.Board;
import module.Duel;
import module.User;

import java.util.ArrayList;

public class BattlePhaseStart {
    //command Knight
    // texchanger
    // suijin
    public static boolean run(Monster attack ,  Monster defense, User secondUser , Duel duel){
        Board board = secondUser.getBoard();
        // for summoning with specific type
        if (defense.getSummonACardFromEveryWhere().hasEffect()){
           ArrayList<Monster> cards =  secondUser.getHand().getCardsWithType( 7 , defense
                   .getSummonACardFromEveryWhere().getType());
           DuelMenu.isGetFromGY = true;
           DuelMenu.isGetFromDeck = true;
           DuelMenu.isGetFromHand = true;
           DuelMenu.specialSummonsedCards = new ArrayList<>(cards);
        }
        //for undefeatable effect
        else if (defense.getUndefeatable().hasEffect()){
            if (defense.getUndefeatable().getContinuousNumber() == 1) {
                defense.getUndefeatable().finishEffect();
                defense.getUndefeatable().setNeedsToBeReset(true);
            }
            return board.getMonsters().length > defense.getUndefeatable().getContinuousNumber();
        }
        else if (defense.getCanChangeTheAttackersATK().hasEffect()){
            attack.setAtk(attack.getAtk() - defense.getCanChangeTheAttackersATK().getEffectNumber());
        }
        return false;
    }
}
