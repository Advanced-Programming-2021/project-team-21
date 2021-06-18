package module.card.effects;

import module.Duel;
import module.User;
import module.card.Card;
import module.card.Monster;

import java.util.ArrayList;
import java.util.Arrays;

public class ChangeTurnEffects {
    //texchanger
    //suijin
    public static void run(User user, User user1, Duel duel) {
        ArrayList<Card> monsters = new ArrayList<>();
        monsters.addAll(Arrays.asList(user.getBoard().getMonsters()));
        monsters.addAll(Arrays.asList(user1.getBoard().getMonsters()));
        for (Card card : monsters) {
            if (!(card instanceof Monster)) continue;
            Monster monster = (Monster) card;
            if (monster.getUndefeatable().isNeedsToBeReset()) {
                monster.getUndefeatable().resetEffect();
                monster.getUndefeatable().setNeedsToBeReset(false);
            }
            if (monster.getCanChangeTheAttackersATK().isNeedsToBeReset()) {
                monster.getCanChangeTheAttackersATK().resetEffect();
                monster.getCanChangeTheAttackersATK().setNeedsToBeReset(false);
            }
            if (monster.getUndefeatable().isNeedsToBeReset()){
                monster.getUndefeatable().resetEffect();
                monster.getUndefeatable().setNeedsToBeReset(false);
            }
            if (monster.getCanScan().isNeedsToBeReset()){
                if (SelectEffect.scannerHolder.isATK())duel.getUserWhoPlaysNow().getBoard().addMonsterFaceUp(SelectEffect.scannerPlace , SelectEffect.scannerHolder);
                else duel.getUserWhoPlaysNow().getBoard().addMonsterFaceDown(SelectEffect.scannerPlace , SelectEffect.scannerHolder);
            }
        }
    }
}
