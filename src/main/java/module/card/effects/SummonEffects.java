package module.card.effects;

import module.Duel;
import module.User;
import module.card.Card;
import module.card.Monster;

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
