package module.card;


import module.card.effects.Effect;
import module.card.enums.SpellTrapIcon;
import module.card.enums.SpellTrapStatus;

// add user a field named canDraw
public class Trap extends Card {

    SpellTrapIcon spellTrapIcon;
    SpellTrapStatus spellTrapStatus;
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
        setPrice((int) parameters[4]);
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

    public Effect getCanNegateWholeAttack() {
        return canNegateWholeAttack;
    }

    public Effect getDestroyAttackMonsters() {
        return destroyAttackMonsters;
    }

    public Effect getCanDestroyAll() {
        return canDestroyAll;
    }

    public Effect getCanDestroyMonsterSummonWithATK() {
        return canDestroyMonsterSummonWithATK;
    }

    public Effect getNegateASummon() {
        return negateASummon;
    }

    public Effect getNegateSpellActivation() {
        return negateSpellActivation;
    }

    public Effect getCostLP() {
        return costLP;
    }

    public Effect getDiscardACard() {
        return discardACard;
    }

    public Effect getCanDestroyFromDeckAndHand() {
        return canDestroyFromDeckAndHand;
    }

    public void setCanAttackLP(Effect canAttackLP) {
        this.canAttackLP = canAttackLP;
    }

    public Effect getCanNotDraw() {
        return canNotDraw;
    }

    public Effect getCanSummonFromGY() {
        return canSummonFromGY;
    }
}
