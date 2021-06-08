package module.card;

import module.User;
import module.card.effects.Effect;
import module.card.effects.MainEffects;
import module.card.enums.SpellTrapIcon;
import module.card.enums.SpellTrapStatus;

import java.util.ArrayList;

// add user a field named canDraw
public class Trap extends Card implements MainEffects {

    SpellTrapIcon spellTrapIcon;
    SpellTrapStatus spellTrapStatus;
    //Magic Jammer              //Negate Attack            Solemn Warning
    private boolean canActivateInOpponentTurn;
    //Magic Cylinder                                            //Negate Attack
    private Effect canNegateWholeAttack;        //(2 , 0)      (1 , 1)                in battlePhaseStart
    private Effect canAttackLP;                //(1 , 0)           in battlePhaseStart
    //Mirror Force
    private Effect destroyAttackMonsters;       //(6 , 0)           in battlePhaseStart
    //Mind Crush
    private Effect canDestroyFromDeckAndHand;    //(4 , 0)          in spellActivation
    //Trap Hole
    private Effect canDestroyMonsterSummonWithATK;           //(1001 , 0)             in summonEffect and flipSummon
    //Torrential Tribute
    private Effect canDestroyAll;                   //(11 , 0)          in summonEffect anf FlipSummon
    //Time Seal
    private Effect canNotDraw;                     //(2 , 0)           in spellAndTrapActivation
    //Magic Jammer
    private Effect discardACard;           //(2 , 0)                   in spellAndTrapActivation
    private Effect negateSpellActivation;          //(2 , 0)               in spellAndTrapActivation
    //Solemn Warning
    private Effect costLP;             //(2001 , 0)                in summonEffects and flipSummon
    private Effect negateASummon;          //(2 , 0)               in summonEffects and flipSummon
    //Call of the Haunted
    private Effect canSummonFromGY;            //( 2 , 1)          in spellActivation
    private Effect killTheSummoned;            //(2 , 1)           in spellActivation

    public Trap(Object[] parameters) {
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
        return "Name: " + name + "\nTrap\nType: " + spellTrapIcon.getName() + "\nDescription: " + getDescription();
    }

    @Override
    public void flipSummonEffect(Card card, User firstUser, User secondUser) {

    }

    @Override
    public void summonEffect(Card card, User firstUser, User secondUser) {

    }

    @Override
    public void mainPhaseEffect(ArrayList<Card> cards, User firstUser, User SecondUser) {

    }

    @Override
    public void activateSpell(Spell spell, User firstUser, User secondUser) {

    }


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
