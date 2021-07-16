package controller.Effects;


import model.Duel;
import model.User;
import model.card.Monster;

public class SetFlip {
    // marshmallon
    public static void run(Monster flipped, User rival, Duel duel) {
        if (flipped.getCanDecreaseLP().hasEffect()) {
            duel.changeLP(rival, flipped.getCanDecreaseLP().getEffectNumber());
        }
    }
}
