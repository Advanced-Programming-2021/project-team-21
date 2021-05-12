package module.card;

import module.Board;
import module.Duel;
import module.User;

import java.util.ArrayList;

public class BattlePhaseStart {
    //command Knight
    // texchanger
    public static boolean run(Monster attacker, Monster defense, User firstUser, User secondUser , Duel duel){
        Board board = secondUser.getBoard();
        // for summoning with specific type
        if (defense.getSummonACardFromEveryWhere().hasEffect()){
           ArrayList<Card> cards =  secondUser.getHand().getCardsWithType( 7 , defense
                   .getSummonACardFromEveryWhere().getType());
           duel.specialSummonCards = new ArrayList<>(cards);
        }
        //for undefeatable effect
        else if (defense.getUndefeatable().hasEffect()){
            return board.getMonsters().length > defense.getUndefeatable().getContinuousNumber();
        }
        return false;
    }
}
