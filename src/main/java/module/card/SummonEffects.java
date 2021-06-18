package module.card;

import controller.menu.DuelMenu;
import module.Duel;
import module.User;

import java.util.ArrayList;
import java.util.Collections;

public class SummonEffects {
    //command knight
    // mirage dragon
    // the calculator
    // terra tiger
    public static void run(Monster summoned , User user , Duel duel){
        if (summoned.getCanIncreaseATK().hasEffect()){
            ArrayList <Card> monsters = new ArrayList<>();
            Collections.addAll(monsters , user.getBoard().getMonsters());
        Monster.changeAttackOfMonsters(monsters , summoned.getCanIncreaseATK().getEffectNumber());
        user.setIncreaseATK(user.getIncreaseATK() + summoned.getCanIncreaseATK().getEffectNumber());
        }
        if (summoned.getDisableTrapSummon().hasEffect()){
            user.setCanSummonTrap(false);
        }
        if (summoned.getAlteringAttack().hasEffect()){
            user.setHasSummonedAlteringATK(true);
            int levelSum = 0;
            for (Card monster : user.getBoard().getMonsters()) {
                if (!(monster instanceof  Monster))continue;
                Monster getLevel = (Monster) monster;
                levelSum += getLevel.getLevel();
            }
            summoned.setAtk(summoned.getAlteringAttack().getEffectNumber() * levelSum);
            user.setAlteringATKPlace(duel.getPlaceOfSelectedCard());
        }
        if (summoned.getCanSetFromDeckByMaxLevel().hasEffect()){
            DuelMenu.isGetFromHand = true;
            ArrayList<Monster> handSpecial = new ArrayList<>();
            for (Card card : user.getHand().getCardsInHand()) {
                if (!(card instanceof  Monster))continue;
                Monster monster = (Monster) card;
                if (monster.getLevel() < summoned.getCanSetFromDeckByMaxLevel().getEffectNumber())
                    handSpecial.add(monster);
            }
            DuelMenu.specialSummonsedCards = handSpecial;
        }
    }
}
