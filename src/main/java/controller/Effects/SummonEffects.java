package controller.Effects;

import controller.ProgramController;
import view.DuelMenu;
import model.Duel;
import model.User;
import model.card.Card;
import model.card.Monster;
import model.card.Spell;
import view.PrintResponses;

import java.util.ArrayList;
import java.util.Collections;

public class SummonEffects {
    //command knight
    // mirage dragon
    // the calculator
    // terra tiger
    //swords of revealing light
    public static void run(Monster summoned, User user, User rival, Duel duel) {
        if (summoned.getCanIncreaseATK().hasEffect()) {
            ArrayList<Card> monsters = new ArrayList<>();
            Collections.addAll(monsters, user.getBoard().getMonsters());
            Monster.changeAttackOfMonsters(monsters, summoned.getCanIncreaseATK().getEffectNumber());
            user.setIncreaseATK(user.getIncreaseATK() + summoned.getCanIncreaseATK().getEffectNumber());
        }
        if (summoned.getDisableTrapSummon().hasEffect()) {
            rival.setCanSummonTrap(false);
        }
        if (summoned.getAlteringAttack().hasEffect()) {
            int levelSum = 0;
            for (Card monster : user.getBoard().getMonsters()) {
                if (!(monster instanceof Monster)) continue;
                Monster getLevel = (Monster) monster;
                levelSum += getLevel.getLevel();
            }
            summoned.setAtk(summoned.getAlteringAttack().getEffectNumber() * levelSum);
            if (summoned.getAlteringAttack().getContinuousNumber() == 0 ){
                user.setHasSummonedAlteringATK(true);
                user.setAlteringATKPlace(user.getBoard().getAddressToSummon());
            }
        }
        if (summoned.getCanSetFromDeckByMaxLevel().hasEffect()) {
            DuelMenu.isGetFromHand = true;
            DuelMenu.isForSet = true;
            ArrayList<Monster> handSpecial = new ArrayList<>();
            for (Card card : user.getHand().getCardsInHand()) {
                if (!(card instanceof Monster)) continue;
                Monster monster = (Monster) card;
                if (monster.getLevel() <= summoned.getCanSetFromDeckByMaxLevel().getEffectNumber())
                    handSpecial.add(monster);
            }
            DuelMenu.specialSummonsedCards = handSpecial;
            if (handSpecial.size() != 0) PrintResponses.printSpecialSummonCards(handSpecial);
            DuelMenu.specialSummonsedCards = handSpecial;
            while (DuelMenu.specialSummonsedCards != null)
                DuelMenu.checkSpecialSummon(ProgramController.scanner.nextLine(), duel, false);
        }
        checkHasContinuousSpell(summoned, rival, user, duel);
    }

    private static void checkHasContinuousSpell(Monster monster, User rival, User userNow, Duel duel) {
        Card[] spells = rival.getBoard().getSpellsAndTraps();
        for (Card spell : spells) {
            if (spell instanceof Spell) {
                Spell continuous = (Spell) spell;
                if (continuous.getCanChangeFaceOFOpponent().hasEffect() && continuous.isFaceUp()) {
                    if (!monster.isFaceUp()) duel.flipSetForMonsters(userNow.getBoard().getAddressByCard(monster));
                }
                if (continuous.getMonstersCanNotAttack().hasEffect() && continuous.isFaceUp()) {
                    if (monster.getAtk() <= continuous.getMonstersCanNotAttack().getEffectNumber())
                        monster.setCanAttack(false);
                }
                if (continuous.getEquipBasedMyUpMonsters().hasEffect() && continuous.isFaceUp()) {
                    SpellActivation.run(continuous, userNow, rival, duel,
                            userNow.getBoard().getAddressByCard(continuous), true,
                            (Monster) userNow.getBoard().getCard(continuous.getEquippedPlace(), 'm'), null);
                }
            }
        }
    }
}
