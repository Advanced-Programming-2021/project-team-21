package controller.Effects;

import model.Duel;
import model.User;
import model.card.Card;
import model.card.Monster;
import model.card.Spell;

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
                if (user.isHasSummonedAlteringATK()) {
                    Monster monster1 = (Monster) user.getBoard().getCard(user.getAlteringATKPlace(), 'm');
                    monster1.setAtk(monster1.getAtk() - 300 * monster1.getLevel());
                }
            }
            if (spell.getCanChangeFaceOFOpponent().hasEffect() && spell.isFaceUp()) {
                spell.getCanChangeFaceOFOpponent().setIsEffect(spell.getCanChangeFaceOFOpponent().getIsEffect() - 1);
            }
            if (spell.getCanMakeMonstersUndefeatable().hasEffect() && spell.isFaceUp()) {
                spell.getCanChangeFaceOFOpponent().setIsContinuous(spell.getCanChangeFaceOFOpponent().getContinuousNumber());
                spell.getCanMakeMonstersUndefeatable().setIsContinuous(spell.getCanMakeMonstersUndefeatable().getContinuousNumber());
                if (spell.getCanMakeMonstersUndefeatable().getContinuousNumber() == 0) {
                    duel.addCardToGraveyard(spell, user.getBoard().getAddressByCard(spell), user);
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
            if (monster.getSummonACardFromEveryWhere().isNeedsToBeReset()) {
                monster.getSummonACardFromEveryWhere().resetEffect();
                monster.getSummonACardFromEveryWhere().setNeedsToBeReset(false);
            }
            if (monster.getCanChangeTheAttackersATK().isNeedsToBeReset()) {
                monster.getCanChangeTheAttackersATK().resetEffect();
                monster.getCanChangeTheAttackersATK().setNeedsToBeReset(false);
            }
            if (monster.getUndefeatable().isNeedsToBeReset()) {
                monster.getUndefeatable().resetEffect();
                monster.getUndefeatable().setNeedsToBeReset(false);
            }
            if (monster.getCanGetFromGYByLevelToHand().isNeedsToBeReset()) {
                monster.getCanGetFromGYByLevelToHand().resetEffect();
                monster.getCanGetFromGYByLevelToHand().setNeedsToBeReset(false);
                monster.setSelectEffect(true);
            }
            if (SelectEffect.scannerHolder != null) {
                if (SelectEffect.scannerHolder.isATK())
                    duel.getUserWhoPlaysNow().getBoard().addMonsterFaceUp(SelectEffect.scannerPlace, SelectEffect.scannerHolder);
                else
                    duel.getUserWhoPlaysNow().getBoard().addMonsterFaceDown(SelectEffect.scannerPlace, SelectEffect.scannerHolder);
                SelectEffect.scannerHolder = null;
                SelectEffect.scannerPlace = -1;
            }
        }
    }
}
