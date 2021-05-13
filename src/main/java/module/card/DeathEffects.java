package module.card;

import module.Duel;
import module.User;

import java.util.ArrayList;
import java.util.Collections;

public class DeathEffects {
    //command knight
     public static void run(Monster attacked , Monster dead , User rival , Duel duel , int attackingPlace , User userNow) {
         if (dead.getCanIncreaseATK().hasEffect()) {
             ArrayList<Card> monsters = new ArrayList<>();
             Collections.addAll(monsters, rival.getBoard().getMonsters());
             Monster.changeAttackOfMonsters(monsters, -dead.getCanIncreaseATK().getEffectNumber());
         }
         if(dead.getCanKillTheAttacker().hasEffect() && !attacked.isDead()){
             if (attacked.isDeathEffect())DeathEffects.run(dead , attacked , userNow , duel , attackingPlace , rival );
             duel.addCardToGraveyard(attacked , attackingPlace , userNow);
         }
     }
}
