package module.card;

import module.Duel;
import module.User;

public class BattlePhaseEnd {
    //suijin
    //mardhmallon
    public static boolean run(Monster attack , Monster defense, User secondUser , Duel duel){
        if (defense.getCanChangeTheAttackersATK().hasEffect()){
            attack.setAtk(attack.getAtkHolder());
            defense.getCanChangeTheAttackersATK().finishEffect();
            defense.getCanChangeTheAttackersATK().setNeedsToBeReset(true);
        }
        if (defense.getNotDestroyable().hasEffect()){
            return true;
        }
        return false;
    }
}
