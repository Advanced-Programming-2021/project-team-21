package controller.Effects;

import module.Duel;
import module.User;
import module.card.Card;
import module.card.Monster;
import module.card.Spell;

import java.util.ArrayList;
import java.util.Collections;

public class DeathEffects {
    //command knight
    //yami ship
    // exploder dragon
    // supply squad
    public static boolean run(Monster attacked, Monster dead, User rival, Duel duel, int attackingPlace, User userNow , int deadPlace) {
        CheckSpells(userNow);
        if (dead.getDisableTrapSummon().hasEffect()) {
            rival.setCanSummonTrap(true);
        }
        if (dead.getCanIncreaseATK().hasEffect()) {
            ArrayList<Card> monsters = new ArrayList<>();
            Collections.addAll(monsters, rival.getBoard().getMonsters());
            Monster.changeAttackOfMonsters(monsters, -dead.getCanIncreaseATK().getEffectNumber());
            userNow.setIncreaseATK(userNow.getIncreaseATK() - dead.getCanIncreaseATK().getEffectNumber());
        }
        if (dead.getCanKillTheAttacker().hasEffect() && !attacked.isDead()) {
            if (attacked.isDeathEffect()) DeathEffects.run(dead, attacked, userNow, duel, attackingPlace, rival , deadPlace);
            duel.addCardToGraveyard(attacked, attackingPlace, userNow);
        }
        if (dead.getCanDestroyBothWithoutLosingLP().hasEffect()) {
            if (attacked.isDeathEffect()) DeathEffects.run(dead, attacked, userNow, duel, attackingPlace, rival , deadPlace);
            duel.addCardToGraveyard(attacked, attackingPlace, userNow);
            duel.addCardToGraveyard(dead , deadPlace , rival);
            return true;
        }
        if (dead.getDisableTrapSummon().hasEffect()) {
            userNow.setCanSummonTrap(true);
        }
        if (dead.getAlteringAttack().hasEffect()) {
            userNow.setAlteringATKPlace(-1);
            userNow.setHasSummonedAlteringATK(false);
        }
        return false;
    }

    public static void CheckSpells(User userNow) {
        for (Card spellsAndTrap : userNow.getBoard().getSpellsAndTraps()) {
            if (spellsAndTrap instanceof Spell) {
                System.out.println(spellsAndTrap.getName());
                Spell spell = (Spell) spellsAndTrap;
                if (spell.getCanDrawACardWhenAMonsterIsDead().hasEffect() && spell.isFaceUp()) {
                    ArrayList<Card> deckCards = userNow.getHand().getDeckToDraw().getMainDeckCards();
                    Card card = deckCards.get(0);
                    userNow.getHand().addCardToHand(card);
                    userNow.getHand().getDeckToDraw().removeCardFromMainDeck(card);
                }
            }
        }
    }
}
