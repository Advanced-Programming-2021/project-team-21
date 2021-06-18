package controller.Effects;

import module.Duel;
import module.User;
import module.card.*;

import java.util.ArrayList;
import java.util.Arrays;

public class ChangeTurnEffects {
    //texchanger
    //suijin
    // change of heart
    public static void run(User user, User user1, Duel duel) {
        checkMonsters(user, duel);
        checkMonsters(user1, duel);
        checkSpells(user, user1, duel);
        checkSpells(user1, user, duel);
    }

    private static void checkSpells(User user, User rival, Duel duel) {
        ArrayList<Card> spells = new ArrayList<>(Arrays.asList(user.getBoard().getSpellsAndTraps()));
        for (Card card : spells) {
            if (!(card instanceof Spell)) continue;
            Spell spell = (Spell) card;
            if (spell.isFaceUp() && spell.getCanControlOpponentMonster().hasEffect()) {
                Monster monster = (Monster) user.getBoard().getCard(SpellActivation.newPlaceHolder, 'M');
                if (monster != null && monster.getName().equals(SpellActivation.changedHeartName)) {
                    user.getBoard().removeMonster(SpellActivation.newPlaceHolder);
                    rival.getBoard().addMonsterFaceUp(SpellActivation.oldPlace, monster);
                }
                duel.addCardToGraveyard(user.getBoard().getCard(SpellActivation.changeSpellPlace, 'S'), SpellActivation.changeSpellPlace, rival);
            }
            if (spell.getCanChangeFaceOFOpponent().hasEffect() && spell.isFaceUp()) {
                spell.getCanChangeFaceOFOpponent().setIsEffect(spell.getCanChangeFaceOFOpponent().getIsEffect() - 1);
            }
            if (spell.getCanMakeMonstersUndefeatable().hasEffect() && spell.isFaceUp()) {
                spell.getCanChangeFaceOFOpponent().setIsEffect(spell.getCanMakeMonstersUndefeatable().getIsEffect() - 1);
                if (spell.getCanMakeMonstersUndefeatable().getIsEffect() == 0) {
                    spell.getCanMakeMonstersUndefeatable().resetEffect();
                    rival.setCanAttack(true);
                }
            }
        }
    }

    private static void checkMonsters(User user, Duel duel) {
        ArrayList<Card> monsters = new ArrayList<>(Arrays.asList(user.getBoard().getMonsters()));
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
            if (monster.getUndefeatable().isNeedsToBeReset()) {
                monster.getUndefeatable().resetEffect();
                monster.getUndefeatable().setNeedsToBeReset(false);
            }
            if (monster.getCanScan().isNeedsToBeReset()) {
                if (SelectEffect.scannerHolder.isATK())
                    duel.getUserWhoPlaysNow().getBoard().addMonsterFaceUp(SelectEffect.scannerPlace, SelectEffect.scannerHolder);
                else
                    duel.getUserWhoPlaysNow().getBoard().addMonsterFaceDown(SelectEffect.scannerPlace, SelectEffect.scannerHolder);
            }
        }
    }
}
