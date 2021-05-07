package module.card;
// must add a method tp summon in which decides if the leve is less than five there is 0 tribute if the level is 5 , 6 1tribute
//if it is 7 8 two tributes and 9 10 11 three tribute

import module.User;

import java.awt.*;
import java.util.ArrayList;

public class Monster extends Card implements MainEffects {
    Integer level;
    Attributes attribute;
    MonsterTypes monsterType;
    int atk;
    int def;
    //command Knight effect                                                                        //marshmallon                            //texchanger
    private Effect undefeatable;     //(1 , 2)       in battlePhaseStart                           (1 , 1)         in battlePhaseEnd            (1 , 0)     in battlePhaseStart
    private Effect canIncreaseATK;   //(401 , 0)     in summonEffects and deathEffect
    //Yomi ship
    private Effect canKillTheAttacker;     //( 1 , 0)        in defenseEffect
    //suijin
    private Effect canChangeTheAttackersATK;     //(1000001 , 0)
    //crab turtle       skull guardian
    private boolean isRitual;       //in summonEffect
    //man eater bug
    private Effect canDestroyMonster;        //(2 , 0)       in flipSummonEffect
    //Scanner
    private Effect canScan;         //(1 , 2)           in mainPhaseChosen
    //marshmallon
    private Effect canDecreaseLP;        //(1001 , 0)       in flipSummonAttackEffect
    // beast king barbaros
    private Effect canBeNotTribute;      //(1101 , 0)        in summonEffect
    private Effect TributeToKillAllMonsterOfOpponent;       //(4 , 1)       in summonEffect
    // TexChanger
    private Effect summonACardFromEveryWhere;       //(2 , 1)       in battlePhaseStart
    //calculator
    private Effect alteringAttack;          //(301 , 1)        in summonEffect and deathEffect
    // mirage dragon
    private Effect disableSpellSummon;      // (1 , 1)          in summonEffect and DeathEffect
    // herald of creation
    private Effect canSummonFromGYByLevel;      //(8 , 0)           in mainPhaseChosen
    // exploder dragon
    private Effect canDestroyBothWithoutLosingLP;        //(1 , 1)      in battlePhaseEnd
    //Terratiger, the Empowered Warrior
    private Effect canSetFromDeckByMaxLevel;             //(1 , 0)       in summonEffect
    //The Tricky
    private Effect discardToSpecialSummon;              //(1, 0)        in mainPhase

    public Monster(Object[] parameters) {
        setName((String) parameters[0]);
        setLevel((int) parameters[1]);
        setAttribute(Attributes.valueOf(((String) parameters[2]).toUpperCase()));
        setMonsterType(MonsterTypes.valueOf(((String) parameters[3]).toUpperCase()));
        setCardType(CardType.valueOf(((String) parameters[4]).toUpperCase()));
        if (cardType.getName().equals("Effect")) this.setHasEffect(true);
        setAtk((int) parameters[5]);
        setDef((int) parameters[6]);
        setDescription((String) parameters[7]);
    }

    @Override
    public void destroyWithoutLosingLifePoints() {

    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Attributes getAttribute() {
        return attribute;
    }

    public void setAttribute(Attributes attribute) {
        this.attribute = attribute;
    }

    public MonsterTypes getMonsterType() {
        return monsterType;
    }

    public void setMonsterType(MonsterTypes monsterType) {
        this.monsterType = monsterType;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = Math.max(atk, 0);
    }

    public int getDef() {
        return def;
    }


    public void setDef(int def) {
        this.def = Math.max(atk, 0);
    }

    @Override
    public String toString() {
        return "Name:" + name + "\nlevel: " + level +
                "\nType: " + monsterType.getName() +
                "\nATK: " + atk +
                "\nDef: " + def +
                "\nDescription: " + getDescription();
    }


    private void increaseAttackOfMonsters(ArrayList<Card> myCards, int amount) {
        for (Card myCard : myCards) {
            if (!(myCard instanceof Monster)) continue;
            Monster monster = (Monster) myCard;
            monster.setAtk(monster.getAtk() + amount);
        }
    }

    @Override
    public void summonEffect(Card card, User firstUser, User secondUser) {

    }

    @Override
    public boolean battlePhaseEffectStart(Monster attacker, Monster defense, User firstUser, User secondUser) {
        return false;
    }

    @Override
    public void deathEffect(Card card, User firstUser, User secondUser) {

    }

    @Override
    public void mainPhaseChosen(ArrayList<Card> cards, User firstUser, User secondUser) {

    }

    @Override
    public boolean battlePhaseEffectEnd(Monster attacker, Monster defense, User firstUser, User secondUser) {
        return false;
    }

    @Override
    public void attackFlipSummon(Monster attacker, Monster defense, User firstUser, User secondUser) {

    }
}
