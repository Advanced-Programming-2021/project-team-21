package module.card;

import module.Duel;
import module.User;

public class SetFlip {
    // marshmallon
    public static void run(Monster flipped , User rival , Duel duel){
        if (flipped.getCanDecreaseLP().hasEffect()){
            duel.changeLP(rival , flipped.getCanDecreaseLP().getEffectNumber());
        }
    }
}
