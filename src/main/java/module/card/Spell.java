package module.card;

import module.card.effects.Effect;
import module.card.enums.SpellTrapIcon;
import module.card.enums.SpellTrapStatus;


public class Spell extends Card {
    SpellTrapIcon spellTrapIcon;
    SpellTrapStatus spellTrapStatus;

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
    // it should be called when you attack a face down monster
    boolean isAttackFlipSummon;
    // monster reborn
    private Effect canSummonFromGY;     //(3 , 0)        in spellActivation
    //Terraforming
    private Effect canAddFieldSpellFromDeck;        //(1 , 0)       in spellActivation
    //Pot of Greed
    private Effect canAddFromDeckToHand;            //(3 , 0)       in spellActivation
    //Raigeki           //dark hole
    private Effect canDestroyOpponentMonster;       //(6 , 0)       in spellActivation
    //Change of Heart
    private Effect canControlOpponentMonster;       //(1 , 0)       in spellActivation
    private boolean isDestroyedAfterEndPhase;
    //Harpie’s Feather Duster                                                           //Twin Twisters             //Mystical space typhoon
    private Effect canDestroyOpponentSpell;         //(6 , 0)       in spellActivation              (3 , 0)             (2 . 0)
    private Effect canDestroyOpponentTrap;         //(6 , 0)       in spellActivation              (3 , 0)             (2 , 0)
    //Swords of Revealing Light
    private Effect canChangeFaceOFOpponent;         //(1 , 4)       in spellActivation and mainPhaseEffect
    private Effect canMakeMonstersUndefeatable;     //(1 , 4)       in spellActivation and mainPhaseEffect
    //dark hole
    private Effect canDestroyMyMonster;             //(6 , 0)       in spell activation
    //Supply Squad
    private Effect canDrawACardWhenAMonsterIsDead;          //(2 , 1)       in deathEffect
    //Spell Absorption
    private Effect getLPForEverySpellActivation;          //(501 , 1)         in  spellActivation
    //Messenger of peace
    private Effect MonstersCanNotAttack;                    //(1501 , 1)        in battlePhaseStart
    private Effect costLP;                          //(101 , 1)             in standByPhaseEffect
    //Twin Twisters
    private Effect discardACardToActivate;          //(2 , 0)               in spellActivation
    //Ring of Defense
    private Effect negateAttack;                //(8001 , 0)                in spellActivation
    //Yami                        //Forest                           //UMIIRUKA
    private Effect fieldSpellType1;     //(200 , 200 , "Fiend")           // (200 , 200 , "Insect")             //( 500 , -400 , "Aqua")             in spellActivations
    private Effect fieldSpellType2;     //(200 , 2000 , "SpellCaster")    //(200 , 200 ,"Beast" )                                                    in spellActivation
    private Effect fieldSpellType3;      //(-200 , -200 , "Fairy")         //( 200 , 200, "BeastWarrior")                                            in spellActivation
    private Effect fieldSpellType4;
    private Effect fieldSpellType5;
    //Closed Forest
    private Effect fieldATKIncreaseGY1;      //(100 , 0 , "BeastWarrior")
    private Effect fieldATKIncreaseGY2;
    //Sword of Dark Destruction                 //Black Pendant
    private Effect equipCardNormal1;        //(400 , -200 , "Fiend")                    //(500 , 0 , "")                 in spellActivation
    private Effect equipCardNormal2;        //(400 , -200 , "SpellCaster")                                               in spellActivation
    private Effect equipCardNormal3;
    // United We Stand
    private Effect equipBasedMyUpMonsters;        //(800 , 0 , "")              in spellActivation
    //Magnum Shield
    private Effect equipBasedOnPosition;            //(0 , 0 , "Warrior")           in SpellActivation

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
}
