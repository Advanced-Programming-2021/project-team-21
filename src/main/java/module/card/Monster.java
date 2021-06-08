package module.card;
// must add a method to summon in which decides if the leve is less than five there is 0 tribute if the level is 5 , 6 1tribute
//if it is 7 8 two tributes and 9 10 11 three tribute

import controller.DataController;
import module.User;
import module.card.effects.Effect;
import module.card.effects.MainEffects;
import module.card.enums.Attributes;
import module.card.enums.CardType;
import module.card.enums.MonsterTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Monster extends Card implements MainEffects {
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

    // main phase effect that are chosen by user
    boolean isMainPhaseChosen;

    //it should be an if at the end of each attack to monster
    boolean isBattlePhaseEffectEnd;

    // it should be called when flip summoned or flip set
    boolean isFlipSummonEffect;

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
    //command Knight effect +                                                                                      //texchanger
    /* done */ private Effect undefeatable;     //(1 , 2)       in battlePhaseStart                                      (1 , 1)     in battlePhaseStart and changeTurnEffect
    /* done */ private Effect canIncreaseATK;   //  (401 , 0)     in summonEffects and deathEffect
    //Yomi ship +
    /* done */private Effect canKillTheAttacker;     //( 1 , 0)        in deathEffect
    //suijin +
    /* done */private Effect canChangeTheAttackersATK;     //( -999999 , 0)        in battlePhaseStart and BattlePhaseEnd and changeTurnEffect
    //crab turtle +       skull guardian +
    //man eater bug +
    /* done */private Effect canDestroyMonster;        //(2 , 0)       in flipSummonEffect
    //Scanner +
    private Effect canScan;         //(1 , 2)           in mainPhaseChosen
    //marshmallon +
    /* done*/private Effect notDestroyable;          //( 1 , 1)      in battlePhaseEnd
    /* done */private Effect canDecreaseLP;        //(-999 , 0)       in flipSetEffect
    // beast king barbaros +
    private Effect canBeNotTribute;      //(1101 , 0)        in summonEffect
    private Effect tributeToKillAllMonsterOfOpponent;       //(4 , 1)       in summonEffect
    // TexChanger +
    /* done */private Effect summonACardFromEveryWhere;       //(2 , "1" , "Cyberse")       in battlePhaseStart
    //calculator +
    private Effect alteringAttack;          //(301 , 1)        in summonEffect and deathEffect
    // mirage dragon +
    private Effect disableTrapSummon;      // (1 , 1)          in summonEffect and DeathEffect
    // herald of creation +
    private Effect canSummonFromGYByLevel;      //(8 , 0)           in mainPhaseChosen
    // exploder dragon +
    /* done */private Effect canDestroyBothWithoutLosingLP;        //(1 , 1)      in battlePhaseEnd
    //Terratiger, the Empowered Warrior +
    private Effect canSetFromDeckByMaxLevel;             //(1 , 0)       in summonEffect
    //The Tricky +
    private Effect discardToSpecialSummon;              //(1, 0)        in mainPhase

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
        setPrice((int) parameters[8]);
        DataController.monsterEffectParser((String) parameters[9], this);
        DataController.monsterPairsParser((String) parameters[10], this);
        setHasAttackedOnceInTurn(false);
    }

    public static void changeAttackOfMonsters(ArrayList<Card> monsters, int amount) {
        for (Card monster1 : monsters) {
            if (!(monster1 instanceof Monster)) continue;
            Monster monster = (Monster) monster1;
            monster.setAtk(monster.getAtk() + amount);
        }
    }


    public void setMainPhaseChosen(boolean mainPhaseChosen) {
        isMainPhaseChosen = mainPhaseChosen;
    }

    public void setFlipSetEffect(boolean flipSetEffect) {
        isFlipSetEffect = flipSetEffect;
    }

    public int getAtkHolder() {
        return atkHolder;
    }

    public void setAtkHolder(int atkHolder) {
        this.atkHolder = atkHolder;
    }

    public int getDefHolder() {
        return defHolder;
    }

    public void setDefHolder(int defHolder) {
        this.defHolder = defHolder;
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

    @Override
    public void summonEffect(Card card, User firstUser, User secondUser) {

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

    @Override
    public void flipSummonEffect(Card card, User firstUser, User secondUser) {

    }

    @Override
    public void activateSpell(Spell spell, User firstUser, User secondUser) {

    }

    @Override
    public void mainPhaseEffect(ArrayList<Card> cards, User firstUser, User SecondUser) {

    }

    public boolean isHasAttackedOnceInTurn() {
        return hasAttackedOnceInTurn;
    }

    public void setHasAttackedOnceInTurn(boolean hasAttackedOnceInTurn) {
        this.hasAttackedOnceInTurn = hasAttackedOnceInTurn;
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

    public void setBattlePhaseEffectStart(boolean battlePhaseEffectStart) {
        isBattlePhaseEffectStart = battlePhaseEffectStart;
    }

    public Effect getUndefeatable() {
        return undefeatable;
    }

    public void setUndefeatable(Effect undefeatable) {
        this.undefeatable = undefeatable;
    }

    public Effect getSummonACardFromEveryWhere() {
        return summonACardFromEveryWhere;
    }

    public void setSummonACardFromEveryWhere(Effect summonACardFromEveryWhere) {
        this.summonACardFromEveryWhere = summonACardFromEveryWhere;
    }

    public Effect getCanIncreaseATK() {
        return canIncreaseATK;
    }

    public void setCanIncreaseATK(Effect canIncreaseATK) {
        this.canIncreaseATK = canIncreaseATK;
    }

    public boolean isSummonEffect() {
        return isSummonEffect;
    }

    public void setSummonEffect(boolean summonEffect) {
        isSummonEffect = summonEffect;
    }

    public boolean isDeathEffect() {
        return isDeathEffect;
    }

    public void setDeathEffect(boolean deathEffect) {
        isDeathEffect = deathEffect;
    }

    public Effect getCanKillTheAttacker() {
        return canKillTheAttacker;
    }

    public void setCanKillTheAttacker(Effect canKillTheAttacker) {
        this.canKillTheAttacker = canKillTheAttacker;
    }

    public boolean isChangeTurnEffect() {
        return isChangeTurnEffect;
    }

    public void setChangeTurnEffect(boolean changeTurnEffect) {
        isChangeTurnEffect = changeTurnEffect;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public Effect getCanChangeTheAttackersATK() {
        return canChangeTheAttackersATK;
    }

    public void setCanChangeTheAttackersATK(Effect canChangeTheAttackersATK) {
        this.canChangeTheAttackersATK = canChangeTheAttackersATK;
    }

    public boolean isBattlePhaseEffectEnd() {
        return isBattlePhaseEffectEnd;
    }

    public void setBattlePhaseEffectEnd(boolean battlePhaseEffectEnd) {
        isBattlePhaseEffectEnd = battlePhaseEffectEnd;
    }

    public Effect getCanDestroyMonster() {
        return canDestroyMonster;
    }

    public void setCanDestroyMonster(Effect canDestroyMonster) {
        this.canDestroyMonster = canDestroyMonster;
    }

    public boolean isFlipSummonEffect() {
        return isFlipSummonEffect;
    }

    public void setFlipSummonEffect(boolean flipSummonEffect) {
        isFlipSummonEffect = flipSummonEffect;
    }

    public Effect getNotDestroyable() {
        return notDestroyable;
    }

    public void setNotDestroyable(Effect notDestroyable) {
        this.notDestroyable = notDestroyable;
    }

    public Effect getCanDecreaseLP() {
        return canDecreaseLP;
    }

    public void setCanDecreaseLP(Effect canDecreaseLP) {
        this.canDecreaseLP = canDecreaseLP;
    }

    public Effect getCanDestroyBothWithoutLosingLP() {
        return canDestroyBothWithoutLosingLP;
    }

    public void setCanDestroyBothWithoutLosingLP(Effect canDestroyBothWithoutLosingLP) {
        this.canDestroyBothWithoutLosingLP = canDestroyBothWithoutLosingLP;
    }

    public Effect getCanScan() {
        return canScan;
    }

    public void setCanScan(Effect canScan) {
        this.canScan = canScan;
    }

    public Effect getCanBeNotTribute() {
        return canBeNotTribute;
    }

    public void setCanBeNotTribute(Effect canBeNotTribute) {
        this.canBeNotTribute = canBeNotTribute;
    }

    public Effect getTributeToKillAllMonsterOfOpponent() {
        return tributeToKillAllMonsterOfOpponent;
    }

    public void setTributeToKillAllMonsterOfOpponent(Effect tributeToKillAllMonsterOfOpponent) {
        this.tributeToKillAllMonsterOfOpponent = tributeToKillAllMonsterOfOpponent;
    }

    public Effect getAlteringAttack() {
        return alteringAttack;
    }

    public void setAlteringAttack(Effect alteringAttack) {
        this.alteringAttack = alteringAttack;
    }

    public Effect getDisableTrapSummon() {
        return disableTrapSummon;
    }

    public void setDisableTrapSummon(Effect disableTrapSummon) {
        this.disableTrapSummon = disableTrapSummon;
    }

    public Effect getCanSummonFromGYByLevel() {
        return canSummonFromGYByLevel;
    }

    public void setCanSummonFromGYByLevel(Effect canSummonFromGYByLevel) {
        this.canSummonFromGYByLevel = canSummonFromGYByLevel;
    }

    public Effect getCanSetFromDeckByMaxLevel() {
        return canSetFromDeckByMaxLevel;
    }

    public void setCanSetFromDeckByMaxLevel(Effect canSetFromDeckByMaxLevel) {
        this.canSetFromDeckByMaxLevel = canSetFromDeckByMaxLevel;
    }

    public Effect getDiscardToSpecialSummon() {
        return discardToSpecialSummon;
    }

    public void setDiscardToSpecialSummon(Effect discardToSpecialSummon) {
        this.discardToSpecialSummon = discardToSpecialSummon;
    }


    public Map<String, Consumer<Boolean>> getBooleanMap() {
        Map<String, Consumer<Boolean>> booleanMap = new HashMap<>();
        booleanMap.put("isDead", this::setDead);
        booleanMap.put("isSummonEffect", this::setSummonEffect);
        booleanMap.put("isChangeTurnEffect", this::setChangeTurnEffect);
        booleanMap.put("isBattlePhaseEffectStart", this::setBattlePhaseEffectStart);
        booleanMap.put("isDeathEffect", this::setDeathEffect);
        booleanMap.put("isMainPhaseChosen", this::setMainPhaseChosen);
        booleanMap.put("isBattlePhaseEffectEnd", this::setBattlePhaseEffectEnd);
        booleanMap.put("isFlipSummonEffect", this::setFlipSummonEffect);
        booleanMap.put("isFlipSetEffect", this::setFlipSetEffect);
        return booleanMap;
    }

    public Map<String, Consumer<Effect>> getEffectsMap() {
        Map<String, Consumer<Effect>> effectsMap = new HashMap<>();
        effectsMap.put("undefeatable", this::setUndefeatable);
        effectsMap.put("canIncreaseATK", this::setCanIncreaseATK);
        effectsMap.put("canKillTheAttacker", this::setCanKillTheAttacker);
        effectsMap.put("canChangeTheAttackersATK", this::setCanChangeTheAttackersATK);
        effectsMap.put("canDestroyMonster", this::setCanDestroyMonster);
        effectsMap.put("canScan", this::setCanScan);
        effectsMap.put("notDestroyable", this::setNotDestroyable);
        effectsMap.put("canDecreaseLP", this::setCanDecreaseLP);
        effectsMap.put("canBeNotTribute", this::setCanBeNotTribute);
        effectsMap.put("tributeToKillAllMonsterOfOpponent", this::setTributeToKillAllMonsterOfOpponent);
        effectsMap.put("summonACardFromEveryWhere", this::setSummonACardFromEveryWhere);
        effectsMap.put("alteringAttack", this::setAlteringAttack);
        effectsMap.put("disableTrapSummon", this::setDisableTrapSummon);
        effectsMap.put("canSummonFromGYByLevel", this::setCanSummonFromGYByLevel);
        effectsMap.put("canDestroyBothWithoutLosingLP", this::setCanDestroyBothWithoutLosingLP);
        effectsMap.put("canSetFromDeckByMaxLevel", this::setCanSetFromDeckByMaxLevel);
        effectsMap.put("discardToSpecialSummon", this::setDiscardToSpecialSummon);
        return effectsMap;
    }

}
