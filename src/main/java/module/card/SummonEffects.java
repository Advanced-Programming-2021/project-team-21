package module.card;

import module.Duel;
import module.User;

import java.util.ArrayList;
import java.util.Collections;

public class SummonEffects {
    //command knight
    public static void run(Monster summoned, User user, Duel duel) {
        if (summoned.getCanIncreaseATK().hasEffect()) {
            ArrayList<Card> monsters = new ArrayList<>();
            Collections.addAll(monsters, user.getBoard().getMonsters());
            Monster.changeAttackOfMonsters(monsters, summoned.getCanIncreaseATK().getEffectNumber());
        }
    }
}
