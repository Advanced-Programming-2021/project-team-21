package module.card;

import controller.DataController;
import module.card.effects.Effect;
import module.card.enums.SpellTrapIcon;
import module.card.enums.SpellTrapStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class Spell extends Card {
    SpellTrapIcon spellTrapIcon;
    SpellTrapStatus spellTrapStatus;
    private int equippedPlace;
    // monster reborn
    /* done */private Effect canSummonFromGY;     //(2 , 0)
    //Terraforming
    /* done */private Effect canAddFieldSpellFromDeck;        //(2 , 0)
    //Pot of Greed
    /* done */private Effect canAddFromDeckToHand;            //(3 , 0)
    //Raigeki           //dark hole
    /* done*/private Effect canDestroyOpponentMonster;       //(6 , 0)
    //Change of Heart
    /* done */private Effect canControlOpponentMonster;       //(1 , 0)
    //Harpie’s Feather Duster                                                           //Twin Twisters             //Mystical space typhoon
    /* done */private Effect canDestroyOpponentSpellAndTrap;         //(6 , 0)                  (3 , 0)             (2 , 0)
    //Swords of Revealing Light
    /*done*/private Effect canChangeFaceOFOpponent;         //(1 , 4)
    /*done*/private Effect canMakeMonstersUndefeatable;     //(1 , 4)
    //dark hole
    /* done */private Effect canDestroyMyMonster;             //(6 , 0)
    //Supply Squad
    /*done*/private Effect canDrawACardWhenAMonsterIsDead;          //(2 , 1)
    //Spell Absorption
    /*done*/private Effect getLPForEverySpellActivation;          //(501 , 1)
    //Messenger of peace
    /*done*/private Effect MonstersCanNotAttack;                    //(1501 , 1)
    /*done*/private Effect costLP;                          //(101 , 1)
    //Twin Twisters
    /* done*/private Effect discardACardToActivate;          //(2 , 0)
    //Ring of Defense
    /*done*/private Effect negateTrap;                //(1 , 0)
    //Yami                        //Forest                           //UMIIRUKA
    /*done*/private Effect fieldSpellType1;     //(200 , 200 , "Fiend")           // (200 , 200 , "Insect")             //( 500 , -400 , "Aqua")
    /*done*/private Effect fieldSpellType2;     //(200 , 2000 , "SpellCaster")    //(200 , 200 ,"Beast" )
    /*done*/private Effect fieldSpellType3;      //(-200 , -200 , "Fairy")         //( 200 , 200, "BeastWarrior")
    /*done*/private Effect fieldSpellType4;
    /*done*/private Effect fieldSpellType5;
    //Closed Forest
    /*done*/private Effect fieldATKIncreaseGY1;      //(100 , 0 , "BeastWarrior")
    /*done*/private Effect fieldATKIncreaseGY2;
    //Sword of Dark Destruction                 //Black Pendant
    /*done*/private Effect equipCardNormal1;        //(400 , -200 , "Fiend")                    //(500 , 0 , "")
    /*done*/private Effect equipCardNormal2;        //(400 , -200 , "SpellCaster")
    /*done*/private Effect equipCardNormal3;
    // United We Stand
    /*done*/private Effect equipBasedMyUpMonsters;        //(800 , 0 , "")
    //Magnum Shield
    /*done*/private Effect equipBasedOnPosition;            //(0 , 0 , "Warrior")

    public Spell(Object[] parameters) {
        setName((String) parameters[0]);
        setSpellTrapIcon(SpellTrapIcon.valueOf(((String) parameters[1]).toUpperCase()));
        setDescription((String) parameters[2]);
        setSpellTrapStatus(SpellTrapStatus.valueOf(((String) parameters[3]).toUpperCase()));
        setPrice((int) parameters[4]);
        Set<String> keys = this.getEffectsMap().keySet();
        for (String key : keys) {
            this.getEffectsMap().get(key).accept(new Effect(0, 0));
        }
        DataController.cardPairsParser((String) parameters[5], this);
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
        return "Name: " + name + "\nSpell\nType: " + spellTrapIcon.getName() + "\nDescription: " + getDescription();
    }


    public boolean isFieldZone() {
        return spellTrapIcon.equals(SpellTrapIcon.FIELD);
    }

    public Effect getCanSummonFromGY() {
        return canSummonFromGY;
    }

    public void setCanSummonFromGY(Effect canSummonFromGY) {
        this.canSummonFromGY = canSummonFromGY;
    }

    public Effect getCanAddFieldSpellFromDeck() {
        return canAddFieldSpellFromDeck;
    }

    public void setCanAddFieldSpellFromDeck(Effect canAddFieldSpellFromDeck) {
        this.canAddFieldSpellFromDeck = canAddFieldSpellFromDeck;
    }

    public Effect getCanAddFromDeckToHand() {
        return canAddFromDeckToHand;
    }

    public void setCanAddFromDeckToHand(Effect canAddFromDeckToHand) {
        this.canAddFromDeckToHand = canAddFromDeckToHand;
    }

    public Effect getCanDestroyOpponentMonster() {
        return canDestroyOpponentMonster;
    }

    public void setCanDestroyOpponentMonster(Effect canDestroyOpponentMonster) {
        this.canDestroyOpponentMonster = canDestroyOpponentMonster;
    }

    public Effect getCanDestroyMyMonster() {
        return canDestroyMyMonster;
    }

    public void setCanDestroyMyMonster(Effect canDestroyMyMonster) {
        this.canDestroyMyMonster = canDestroyMyMonster;
    }

    public Effect getCanControlOpponentMonster() {
        return canControlOpponentMonster;
    }

    public void setCanControlOpponentMonster(Effect canControlOpponentMonster) {
        this.canControlOpponentMonster = canControlOpponentMonster;
    }

    public Effect getCanDestroyOpponentSpellAndTrap() {
        return canDestroyOpponentSpellAndTrap;
    }

    public void setCanDestroyOpponentSpellAndTrap(Effect canDestroyOpponentSpellAndTrap) {
        this.canDestroyOpponentSpellAndTrap = canDestroyOpponentSpellAndTrap;
    }

    public Effect getCanChangeFaceOFOpponent() {
        return canChangeFaceOFOpponent;
    }

    public void setCanChangeFaceOFOpponent(Effect canChangeFaceOFOpponent) {
        this.canChangeFaceOFOpponent = canChangeFaceOFOpponent;
    }

    public Effect getCanDrawACardWhenAMonsterIsDead() {
        return canDrawACardWhenAMonsterIsDead;
    }

    public void setCanDrawACardWhenAMonsterIsDead(Effect canDrawACardWhenAMonsterIsDead) {
        this.canDrawACardWhenAMonsterIsDead = canDrawACardWhenAMonsterIsDead;
    }

    public Effect getCanMakeMonstersUndefeatable() {
        return canMakeMonstersUndefeatable;
    }

    public void setCanMakeMonstersUndefeatable(Effect canMakeMonstersUndefeatable) {
        this.canMakeMonstersUndefeatable = canMakeMonstersUndefeatable;
    }

    public Effect getGetLPForEverySpellActivation() {
        return getLPForEverySpellActivation;
    }

    public void setGetLPForEverySpellActivation(Effect getLPForEverySpellActivation) {
        this.getLPForEverySpellActivation = getLPForEverySpellActivation;
    }

    public Effect getMonstersCanNotAttack() {
        return MonstersCanNotAttack;
    }

    public void setMonstersCanNotAttack(Effect monstersCanNotAttack) {
        MonstersCanNotAttack = monstersCanNotAttack;
    }

    public Effect getCostLP() {
        return costLP;
    }

    public void setCostLP(Effect costLP) {
        this.costLP = costLP;
    }

    public Effect getFieldSpellType1() {
        return fieldSpellType1;
    }

    public void setFieldSpellType1(Effect fieldSpellType1) {
        this.fieldSpellType1 = fieldSpellType1;
    }

    public Effect getFieldSpellType2() {
        return fieldSpellType2;
    }

    public void setFieldSpellType2(Effect fieldSpellType2) {
        this.fieldSpellType2 = fieldSpellType2;
    }

    public Effect getFieldSpellType3() {
        return fieldSpellType3;
    }

    public void setFieldSpellType3(Effect fieldSpellType3) {
        this.fieldSpellType3 = fieldSpellType3;
    }

    public Effect getFieldSpellType4() {
        return fieldSpellType4;
    }

    public void setFieldSpellType4(Effect fieldSpellType4) {
        this.fieldSpellType4 = fieldSpellType4;
    }

    public Effect getFieldSpellType5() {
        return fieldSpellType5;
    }

    public void setFieldSpellType5(Effect fieldSpellType5) {
        this.fieldSpellType5 = fieldSpellType5;
    }

    public Effect getFieldATKIncreaseGY1() {
        return fieldATKIncreaseGY1;
    }

    public void setFieldATKIncreaseGY1(Effect fieldATKIncreaseGY1) {
        this.fieldATKIncreaseGY1 = fieldATKIncreaseGY1;
    }

    public Effect getFieldATKIncreaseGY2() {
        return fieldATKIncreaseGY2;
    }

    public void setFieldATKIncreaseGY2(Effect fieldATKIncreaseGY2) {
        this.fieldATKIncreaseGY2 = fieldATKIncreaseGY2;
    }

    public Effect getEquipCardNormal1() {
        return equipCardNormal1;
    }

    public void setEquipCardNormal1(Effect equipCardNormal1) {
        this.equipCardNormal1 = equipCardNormal1;
    }

    public Effect getEquipCardNormal2() {
        return equipCardNormal2;
    }

    public void setEquipCardNormal2(Effect equipCardNormal2) {
        this.equipCardNormal2 = equipCardNormal2;
    }

    public Effect getEquipCardNormal3() {
        return equipCardNormal3;
    }

    public void setEquipCardNormal3(Effect equipCardNormal3) {
        this.equipCardNormal3 = equipCardNormal3;
    }

    public boolean isEquipSpell() {
        return spellTrapIcon.equals(SpellTrapIcon.EQUIP);
    }

    public int getEquippedPlace() {
        return equippedPlace;
    }

    public void setEquippedPlace(int equippedPlace) {
        this.equippedPlace = equippedPlace;
    }

    public Effect getEquipBasedMyUpMonsters() {
        return equipBasedMyUpMonsters;
    }

    public void setEquipBasedMyUpMonsters(Effect equipBasedMyUpMonsters) {
        this.equipBasedMyUpMonsters = equipBasedMyUpMonsters;
    }

    public Effect getEquipBasedOnPosition() {
        return equipBasedOnPosition;
    }

    public void setEquipBasedOnPosition(Effect equipBasedOnPosition) {
        this.equipBasedOnPosition = equipBasedOnPosition;
    }

    public Effect getDiscardACardToActivate() {
        return discardACardToActivate;
    }

    public void setDiscardACardToActivate(Effect discardACardToActivate) {
        this.discardACardToActivate = discardACardToActivate;
    }

    public Effect getNegateTrap() {
        return negateTrap;
    }

    public void setNegateTrap(Effect negateTrap) {
        this.negateTrap = negateTrap;
    }

    @Override
    public Map<String, Consumer<Effect>> getEffectsMap() {
        Map<String, Consumer<Effect>> effectsMap = new HashMap<>();
        effectsMap.put("canSummonFromGY", this::setCanSummonFromGY);
        effectsMap.put("canAddFieldSpellFromDeck", this::setCanAddFieldSpellFromDeck);
        effectsMap.put("canAddFromDeckToHand", this::setCanAddFromDeckToHand);
        effectsMap.put("canDestroyOpponentMonster", this::setCanDestroyOpponentMonster);
        effectsMap.put("canControlOpponentMonster", this::setCanControlOpponentMonster);
        effectsMap.put("canDestroyOpponentSpellAndTrap", this::setCanDestroyOpponentSpellAndTrap);
        effectsMap.put("canChangeFaceOFOpponent", this::setCanChangeFaceOFOpponent);
        effectsMap.put("canMakeMonstersUndefeatable", this::setCanMakeMonstersUndefeatable);
        effectsMap.put("canDestroyMyMonster", this::setCanDestroyMyMonster);
        effectsMap.put("canDrawACardWhenAMonsterIsDead", this::setCanDrawACardWhenAMonsterIsDead);
        effectsMap.put("getLPForEverySpellActivation", this::setGetLPForEverySpellActivation);
        effectsMap.put("monstersCanNotAttack", this::setMonstersCanNotAttack);
        effectsMap.put("costLP", this::setCostLP);
        effectsMap.put("discardACardToActivate", this::setDiscardACardToActivate);
        effectsMap.put("negateTrap", this::setNegateTrap);
        effectsMap.put("fieldSpellType1", this::setFieldSpellType1);
        effectsMap.put("fieldSpellType2", this::setFieldSpellType2);
        effectsMap.put("fieldSpellType3", this::setFieldSpellType3);
        effectsMap.put("fieldSpellType4", this::setFieldSpellType4);
        effectsMap.put("fieldSpellType5", this::setFieldSpellType5);
        effectsMap.put("fieldATKIncreaseGY1", this::setFieldATKIncreaseGY1);
        effectsMap.put("fieldATKIncreaseGY2", this::setFieldATKIncreaseGY2);
        effectsMap.put("equipCardNormal1", this::setEquipCardNormal1);
        effectsMap.put("equipCardNormal2", this::setEquipCardNormal2);
        effectsMap.put("equipCardNormal3", this::setEquipCardNormal3);
        effectsMap.put("equipBasedMyUpMonsters", this::setEquipBasedMyUpMonsters);
        effectsMap.put("equipBasedOnPosition", this::setEquipBasedOnPosition);

        return effectsMap;
    }
}