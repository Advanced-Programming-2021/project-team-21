package model.card;


import controller.DataController;
import model.card.effects.Effect;
import model.card.enums.SpellTrapIcon;
import model.card.enums.SpellTrapStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

// add user a field named canDraw
public class Trap extends Card {

    SpellTrapIcon spellTrapIcon;
    SpellTrapStatus spellTrapStatus;
    public static Set<String> effectsList;
    //Magic Cylinder                                            //Negate Attack
    /*done*/private Effect canNegateWholeAttack;        //(2 , 0)      (1 , 1)
    /*done*/private Effect canAttackLP;                //(1 , 0)
    //Mirror Force
    /*done*/private Effect destroyAttackMonsters;       //(6 , 0)
    //Mind Crush
    /*done*/private Effect canDestroyFromDeckAndHand;    //(4 , 0)
    //Trap Hole
    /*done*/private Effect canDestroyMonsterSummonWithATK;           //(1001 , 0)
    //Torrential Tribute
    /*done*/private Effect canDestroyAll;                   //(1 , 0)
    //Time Seal
    /*done*/private Effect canNotDraw;                     //(2 , 0)
    //Magic Jammer
    /*done*/private Effect discardACard;           //(2 , 0)
    /*done*/private Effect negateSpellActivation;          //(2 , 0)
    //Solemn Warning
    /*done*/private Effect costLP;             //(2001 , 0)
    /*done*/private Effect negateASummon;          //(2 , 0)
    //Call of the Haunted
    private Effect canSummonFromGY;            //( 2 , 1)
    private Effect killTheSummoned;            //(2 , 1)

    public Trap(Object[] parameters) {
        setName((String) parameters[0]);
        setSpellTrapIcon(SpellTrapIcon.valueOf(((String) parameters[1]).toUpperCase()));
        setDescription((String) parameters[2]);
        setSpellTrapStatus(SpellTrapStatus.valueOf(((String) parameters[3]).toUpperCase()));
        setPrice(Integer.parseInt((String) parameters[4]));
        Set<String> keys = this.getEffectsMap().keySet();
        for (String key : keys) {
            this.getEffectsMap().get(key).accept(new Effect(0, 0));
        }
        DataController.cardPairsParser((String) parameters[5], this);
    }

    public static String getWhichGroup(String string) {
        if (string.equals("canSummonFromGY"))return "Continuous";
        if (string.equals("discardACard") || string.equals("negateSpellActivation") ||
        string.equals("costLP") || string.equals("negateASummon") ||
        string.equals("canNegateWholeAttack"))return "Counter";
        return "Normal";
    }

    public void setKillTheSummoned(Effect killTheSummoned) {
        this.killTheSummoned = killTheSummoned;
    }

    public SpellTrapIcon getSpellTrapIcon() {
        return spellTrapIcon;
    }

    public void setSpellTrapIcon(SpellTrapIcon spellTrapIcon) {
        this.spellTrapIcon = spellTrapIcon;
    }

    public SpellTrapStatus getSpellTrapStatus() {
        return spellTrapStatus;
    }

    public void setSpellTrapStatus(SpellTrapStatus spellTrapStatus) {
        this.spellTrapStatus = spellTrapStatus;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\nTrap\nType: " + spellTrapIcon.getName() + "\nDescription: " + getDescription();
    }

    public Effect getCanAttackLP() {
        return canAttackLP;
    }

    public void setCanAttackLP(Effect canAttackLP) {
        this.canAttackLP = canAttackLP;
    }

    public Effect getCanNegateWholeAttack() {
        return canNegateWholeAttack;
    }

    public void setCanNegateWholeAttack(Effect canNegateWholeAttack) {
        this.canNegateWholeAttack = canNegateWholeAttack;
    }

    public Effect getDestroyAttackMonsters() {
        return destroyAttackMonsters;
    }

    public void setDestroyAttackMonsters(Effect destroyAttackMonsters) {
        this.destroyAttackMonsters = destroyAttackMonsters;
    }

    public Effect getCanDestroyAll() {
        return canDestroyAll;
    }

    public void setCanDestroyAll(Effect canDestroyAll) {
        this.canDestroyAll = canDestroyAll;
    }

    public Effect getCanDestroyMonsterSummonWithATK() {
        return canDestroyMonsterSummonWithATK;
    }

    public void setCanDestroyMonsterSummonWithATK(Effect canDestroyMonsterSummonWithATK) {
        this.canDestroyMonsterSummonWithATK = canDestroyMonsterSummonWithATK;
    }

    public Effect getNegateASummon() {
        return negateASummon;
    }

    public void setNegateASummon(Effect negateASummon) {
        this.negateASummon = negateASummon;
    }

    public Effect getNegateSpellActivation() {
        return negateSpellActivation;
    }

    public void setNegateSpellActivation(Effect negateSpellActivation) {
        this.negateSpellActivation = negateSpellActivation;
    }

    public Effect getCostLP() {
        return costLP;
    }

    public void setCostLP(Effect costLP) {
        this.costLP = costLP;
    }

    public Effect getDiscardACard() {
        return discardACard;
    }

    public void setDiscardACard(Effect discardACard) {
        this.discardACard = discardACard;
    }

    public Effect getCanDestroyFromDeckAndHand() {
        return canDestroyFromDeckAndHand;
    }

    public void setCanDestroyFromDeckAndHand(Effect canDestroyFromDeckAndHand) {
        this.canDestroyFromDeckAndHand = canDestroyFromDeckAndHand;
    }

    public Effect getCanNotDraw() {
        return canNotDraw;
    }

    public void setCanNotDraw(Effect canNotDraw) {
        this.canNotDraw = canNotDraw;
    }

    public Effect getCanSummonFromGY() {
        return canSummonFromGY;
    }

    public void setCanSummonFromGY(Effect canSummonFromGY) {
        this.canSummonFromGY = canSummonFromGY;
    }

    @Override
    public Map<String, Consumer<Effect>> getEffectsMap() {
        Map<String, Consumer<Effect>> effectsMap = new HashMap<>();
        effectsMap.put("canNegateWholeAttack", this::setCanNegateWholeAttack);
        effectsMap.put("canAttackLP", this::setCanAttackLP);
        effectsMap.put("destroyAttackMonsters", this::setDestroyAttackMonsters);
        effectsMap.put("canDestroyFromDeckAndHand", this::setCanDestroyFromDeckAndHand);
        effectsMap.put("canDestroyMonsterSummonWithATK", this::setCanDestroyMonsterSummonWithATK);
        effectsMap.put("canDestroyAll", this::setCanDestroyAll);
        effectsMap.put("canNotDraw", this::setCanNotDraw);
        effectsMap.put("discardACard", this::setDiscardACard);
        effectsMap.put("negateSpellActivation", this::setNegateSpellActivation);
        effectsMap.put("costLP", this::setCostLP);
        effectsMap.put("negateASummon", this::setNegateASummon);
        effectsMap.put("canSummonFromGY", this::setCanSummonFromGY);
        effectsMap.put("killTheSummoned", this::setKillTheSummoned);
        effectsList = effectsMap.keySet();
        return effectsMap;
    }

    public static Set<String> getEffectsList() {
        return effectsList;
    }
}
