package module.card.effects;

import module.Duel;
import module.User;
import module.card.Monster;

public class BattlePhaseEnd {
    //suijin
    //mardhmallon
    public static boolean run(Monster attack, Monster defense, User secondUser, Duel duel) {
        if (defense.getCanChangeTheAttackersATK().hasEffect()) {
            attack.setAtk(attack.getAtkHolder());
            defense.getCanChangeTheAttackersATK().finishEffect();
            defense.getCanChangeTheAttackersATK().setNeedsToBeReset(true);
        }
        return defense.getNotDestroyable().hasEffect();
    }
}
