package controller.Effects;

import model.card.Monster;

public class BattlePhaseEnd {
    //suijin
    //mardhmallon
    public static boolean run(Monster attack, Monster defense) {
        if (defense.getCanChangeTheAttackersATK().hasEffect()) {
            attack.setAtk(attack.getAtkHolder());
            defense.getCanChangeTheAttackersATK().finishEffect();
            defense.getCanChangeTheAttackersATK().setNeedsToBeReset(true);
        }
        if (defense.getNotDestroyable().hasEffect()) {
            if (defense.getNotDestroyable().getContinuousNumber() == 0) {
                defense.getNotDestroyable().finishEffect();
                defense.getNotDestroyable().setNeedsToBeReset(true);
            }
            return true;
        }
        return false;
    }
}
