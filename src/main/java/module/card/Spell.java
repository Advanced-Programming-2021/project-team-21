package module.card;

public class Spell extends Card {
    SpellTrapIcon spellTrapIcon;
    SpellTrapStatus spellTrapStatus;
    private int equippedPlace;

    //quickPlay Spells
    //Ring of Defense               Mystical space typhoon          Twin Twisters
    boolean isQuickPlay;
    //for summoned effects
    boolean isSummonEffect;

    //it should be an if at the first of each attack to monster
    boolean isBattlePhaseEffectStart;

    // when a card is destroyed
    boolean isDeathEffect;

    // main phase effect that are chosen by user
    boolean isMainPhaseChosen;

    //it should be an if at the end of each attack to monster
    boolean isBattlePhaseEffectEnd;

    //Yami          //Forest            //Closed Forest         //UMIIRUKA      //Sword of Dark Destruction         //Black Pendant
    boolean isFieldSpellOrEquip;

    //Advanced Ritual Art
    boolean isRitual;
    // monster reborn
    /* done */private Effect canSummonFromGY;     //(2 , 0)
    //Terraforming
    /* done */private Effect canAddFieldSpellFromDeck;        //(2 , 0)
    //Pot of Greed
    /* done */private Effect canAddFromDeckToHand;            //(3 , 0)
    //Raigeki           //dark hole
    /* done*/private Effect canDestroyOpponentMonster;       //(6 , 0)
    //Change of Heart
    /* done */private Effect canControlOpponentMonster;       //(1 , 0)        and changeTurnEffect
    //Harpie’s Feather Duster                                                           //Twin Twisters             //Mystical space typhoon
    /* done */private Effect canDestroyOpponentSpellAndTrap;         //(6 , 0)                  (3 , 0)             (2 . 0)
    //Swords of Revealing Light
    /*done*/private Effect canChangeFaceOFOpponent;         //(1 , 4)        summonEffects and changeTurnEffects
    /*done*/private Effect canMakeMonstersUndefeatable;     //(1 , 4)        changeTurnEffects
    //dark hole
    /* done */private Effect canDestroyMyMonster;             //(6 , 0)
    //Supply Squad
    /*done*/private Effect canDrawACardWhenAMonsterIsDead;          //(2 , 1)       in deathEffect
    //Spell Absorption
    /*done*/private Effect getLPForEverySpellActivation;          //(501 , 1)
    //Messenger of peace
    /*done*/private Effect MonstersCanNotAttack;                    //(1501 , 1)    in battlePhaseStart
    /*done*/private Effect costLP;                          //(101 , 1)             in  standByPhaseEffect
    //Twin Twisters
    /* done*/private Effect discardACardToActivate;          //(2 , 0)
    //Ring of Defense
    private Effect negateAttack;                //(8001 , 0)
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
    }

    @Override
    public void destroyWithoutLosingLifePoints() {

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

    public Effect getCanAddFieldSpellFromDeck() {
        return canAddFieldSpellFromDeck;
    }

    public Effect getCanAddFromDeckToHand() {
        return canAddFromDeckToHand;
    }

    public Effect getCanDestroyOpponentMonster() {
        return canDestroyOpponentMonster;
    }

    public Effect getCanDestroyMyMonster() {
        return canDestroyMyMonster;
    }

    public Effect getCanControlOpponentMonster() {
        return canControlOpponentMonster;
    }

    public Effect getCanDestroyOpponentSpellAndTrap() {
        return canDestroyOpponentSpellAndTrap;
    }

    public Effect getCanChangeFaceOFOpponent() {
        return canChangeFaceOFOpponent;
    }

    public Effect getCanDrawACardWhenAMonsterIsDead() {
        return canDrawACardWhenAMonsterIsDead;
    }

    public Effect getCanMakeMonstersUndefeatable() {
        return canMakeMonstersUndefeatable;
    }

    public Effect getGetLPForEverySpellActivation() {
        return getLPForEverySpellActivation;
    }

    public Effect getMonstersCanNotAttack() {
        return MonstersCanNotAttack;
    }

    public Effect getCostLP() {
        return costLP;
    }

    public Effect getFieldSpellType1() {
        return fieldSpellType1;
    }

    public Effect getFieldSpellType2() {
        return fieldSpellType2;
    }

    public Effect getFieldSpellType3() {
        return fieldSpellType3;
    }

    public Effect getFieldSpellType4() {
        return fieldSpellType4;
    }

    public Effect getFieldSpellType5() {
        return fieldSpellType5;
    }

    public Effect getFieldATKIncreaseGY1() {
        return fieldATKIncreaseGY1;
    }

    public Effect getFieldATKIncreaseGY2() {
        return fieldATKIncreaseGY2;
    }

    public Effect getEquipCardNormal1() {
        return equipCardNormal1;
    }

    public Effect getEquipCardNormal2() {
        return equipCardNormal2;
    }

    public Effect getEquipCardNormal3() {
        return equipCardNormal3;
    }

    public boolean isEquipSPell() {
        return spellTrapIcon.equals(SpellTrapIcon.EQUIP);
    }

    public void setEquippedPlace(int equippedPlace) {
        this.equippedPlace = equippedPlace;
    }

    public int getEquippedPlace() {
        return equippedPlace;
    }

    public Effect getEquipBasedMyUpMonsters() {
        return equipBasedMyUpMonsters;
    }

    public Effect getEquipBasedOnPosition() {
        return equipBasedOnPosition;
    }

    public Effect getDiscardACardToActivate() {
        return discardACardToActivate;
    }

    public Effect getNegateAttack() {
        return negateAttack;
    }
}
