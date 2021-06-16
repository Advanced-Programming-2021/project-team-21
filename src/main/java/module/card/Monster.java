package module.card;
// must add a method to summon in which decides if the leve is less than five there is 0 tribute if the level is 5 , 6 1tribute
//if it is 7 8 two tributes and 9 10 11 three tribute

import com.rits.cloning.Cloner;
import module.User;

import java.util.ArrayList;

public class Monster extends Card {
    //for checking death in deathEffects
    boolean isDead;
    //for summoned effects
    boolean isSummonEffect;

    //for effects needed to be reset in change turn
    boolean isChangeTurnEffect;

    //it should be an if at the first of each attack to monster
    boolean isBattlePhaseEffectStart;

    // when a card is destroyed
    boolean isDeathEffect;

    //it should be an if at the end of each attack to monster
    boolean isBattlePhaseEffectEnd;

    // it should be called when flip summoned or flip set
    boolean isFlipSummonEffect;

    //for some effects that user choose to activate
    boolean isSelectEffect;
    boolean isFlipSetEffect;
    private Integer level;
    private Attributes attribute;
    private MonsterTypes monsterType;
    private int atk;
    private int atkHolder;
    private int def;
    private int defHolder;
    private boolean hasAttackedOnceInTurn;
    private boolean isATKPosition;
    private boolean canHaveDifferentTribute;
    private boolean canAttack = true;
    private int requiredCardsFOrTribute;
    //command Knight effect                                                                                       //texchanger
    /* done */ private Effect undefeatable;     //(1 , 2)       in battlePhaseStart                                      (1 , 1)     in battlePhaseStart and changeTurnEffect
    /* done*/private Effect canIncreaseATK;   //(401 , 0)     in summonEffects and deathEffect
    //Yomi ship
    /* done */private Effect canKillTheAttacker;     //( 1 , 0)        in deathEffect
    //suijin
    /* done */private Effect canChangeTheAttackersATK;     //( -999999 , 0)        in battlePhaseStart and BattlePhaseEnd and changeTurnEffect
    //man eater bug
    /* done */private Effect canDestroyMonster;        //(2 , 0)       in flipSummonEffect
    //Scanner
    /* done */private Effect canScan;         //(1 , 1)           in SelectEffects and changeTurnEffects
    //marshmallon
    /* done*/private Effect notDestroyable;          //( 1 , 1)      in battlePhaseEnd
    /* done */private Effect canDecreaseLP;        //(-999 , 0)       in flipSummonAttackEffect
    // beast king barbaros
    /*done*/private Effect canBeNotTribute;      //( -1099, 0)        in summonEffect
    /*done */private Effect TributeToKillAllMonsterOfOpponent;       //(4 , 0)       in summonEffect
    // TexChanger
    /* done */private Effect summonACardFromEveryWhere;       //(2 , "1" , "Cyberse")       in battlePhaseStart
    //calculator
    /* done */private Effect alteringAttack;          //(301 , 1)        in summonEffect and deathEffect
    // mirage dragon
    /* done */private Effect disableTrapSummon;      // (1 , 1)          in summonEffect and DeathEffect
    // herald of creation
    /* done */private Effect canGetFromGYByLevelToHand;      //(8 , 0)           in mainPhaseChosen
    // exploder dragon
    /* done */private Effect canDestroyBothWithoutLosingLP;        //(1 , 1)      in battlePhaseEnd
    //Terratiger, the Empowered Warrior
    /* done */private Effect canSetFromDeckByMaxLevel;             //(5 , 0)       in summonEffect
    //The Tricky
    /* done */ private Effect discardToSpecialSummon;              //(1, 0)        in mainPhase

    public Monster(Object[] parameters) {
        setName((String) parameters[0]);
        setLevel((int) parameters[1]);
        setAttribute(Attributes.valueOf(((String) parameters[2]).toUpperCase()));
        setMonsterType(MonsterTypes.valueOf(((String) parameters[3]).toUpperCase()));
        setCardType(CardType.valueOf(((String) parameters[4]).toUpperCase()));
        if (cardType.getName().equals("Effect")) this.setHasEffect(true);
        setAtk((int) parameters[5]);
        setAtkHolder((int) parameters[5]);
        setDef((int) parameters[6]);
        setDefHolder((int) parameters[6]);
        setDescription((String) parameters[7]);
    }

    public Monster copy(Object object) {
        if (object == null) return null;
        if (!(object instanceof Monster)) return null;
        Cloner cloner = new Cloner();
        Monster monster = (Monster) object;
        return cloner.deepClone(monster);
    }

    public boolean isCanHaveDifferentTribute() {
        return canHaveDifferentTribute;
    }

    public void setRequiredCardsFOrTribute(int requiredCardsFOrTribute) {
        this.requiredCardsFOrTribute = requiredCardsFOrTribute;
    }

    public int getRequiredCardsFOrTribute() {
        return requiredCardsFOrTribute;
    }

    public void setAtkHolder(int atkHolder) {
        this.atkHolder = atkHolder;
    }

    public void setDefHolder(int defHolder) {
        this.defHolder = defHolder;
    }

    public int getAtkHolder() {
        return atkHolder;
    }

    public int getDefHolder() {
        return defHolder;
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


    public static void changeAttackOfMonsters(ArrayList<Card> monsters, int amount) {
        for (Card monster1 : monsters) {
            if (!(monster1 instanceof Monster)) continue;
            Monster monster = (Monster) monster1;
            monster.setAtk(monster.getAtk() + amount);
        }
    }

    public boolean isHasAttackedOnceInTurn() {
        return hasAttackedOnceInTurn;
    }

    public void setHasAttackedOnceInTurn(boolean hasAttackedOnceInTrun) {
        this.hasAttackedOnceInTurn = hasAttackedOnceInTrun;
    }

    public void setIsATKPosition(boolean isATKPosition) {
        this.isATKPosition = isATKPosition;
    }

    public boolean isATKPosition() {
        return this.isATKPosition;
    }

    public boolean isBattlePhaseEffectStart() {
        return isBattlePhaseEffectStart;
    }

    public Effect getUndefeatable() {
        return undefeatable;
    }

    public Effect getSummonACardFromEveryWhere() {
        return summonACardFromEveryWhere;
    }

    public Effect getCanIncreaseATK() {
        return canIncreaseATK;
    }

    public boolean isSummonEffect() {
        return isSummonEffect;
    }

    public boolean isDeathEffect() {
        return isDeathEffect;
    }

    public Effect getCanKillTheAttacker() {
        return canKillTheAttacker;
    }

    public boolean isChangeTurnEffect() {
        return isChangeTurnEffect;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public boolean isDead() {
        return isDead;
    }

    public Effect getCanChangeTheAttackersATK() {
        return canChangeTheAttackersATK;
    }

    public boolean isBattlePhaseEffectEnd() {
        return isBattlePhaseEffectEnd;
    }

    public Effect getCanDestroyMonster() {
        return canDestroyMonster;
    }

    public boolean isFlipSummonEffect() {
        return isFlipSummonEffect;
    }

    public Effect getNotDestroyable() {
        return notDestroyable;
    }

    public Effect getCanDecreaseLP() {
        return canDecreaseLP;
    }

    public Effect getCanDestroyBothWithoutLosingLP() {
        return canDestroyBothWithoutLosingLP;
    }

    public Effect getCanScan() {
        return canScan;
    }

    public Effect getCanBeNotTribute() {
        return canBeNotTribute;
    }

    public Effect getTributeToKillAllMonsterOfOpponent() {
        return TributeToKillAllMonsterOfOpponent;
    }

    public Effect getDisableTrapSummon() {
        return disableTrapSummon;
    }

    public Effect getAlteringAttack() {
        return alteringAttack;
    }

    public boolean isSelectEffect() {
        return isSelectEffect;
    }

    public Effect getCanGetFromGYByLevelToHand() {
        return canGetFromGYByLevelToHand;
    }

    public Effect getCanSetFromDeckByMaxLevel() {
        return canSetFromDeckByMaxLevel;
    }

    public Effect getDiscardToSpecialSummon() {
        return discardToSpecialSummon;
    }

    public boolean isFlipSetEffect() {
        return isFlipSetEffect;
    }

    public boolean isCanAttack() {
        return canAttack;
    }

    public void setCanAttack(boolean canAttack) {
        this.canAttack = canAttack;
    }
}
