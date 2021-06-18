package controller.Effects;

import module.Duel;
import module.User;
import module.card.Card;
import module.card.Spell;

public class StandByEffects {
    public static void run(User rival, Duel duel, User userNow) {
        checkUser(userNow, duel);
        checkUser(rival, duel);
    }

    private static void checkUser(User userNow, Duel duel) {
        for (Card spellsAndTrap : userNow.getBoard().getSpellsAndTraps()) {
            if (spellsAndTrap instanceof Spell) {
                Spell spell = (Spell) spellsAndTrap;
                if (spell.getCostLP().hasEffect()) {
                    duel.changeLP(userNow, spell.getCostLP().getEffectNumber());
                }
            }
        }
    }

}
