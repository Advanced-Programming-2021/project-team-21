package module.card;

import module.User;

import java.util.ArrayList;

//first effectOnMyMonsters should be called at end ao main phase one
public interface MainEffects {
    //for summoned effects
    void summonEffect(Card card, User firstUser, User secondUser);

    //it should be an if at the first of each attack to monster
    boolean battlePhaseEffectStart(Monster attacker, Monster defense, User firstUser, User secondUser);

    // when a card is destroyed
    void deathEffect(Card card, User firstUser, User secondUser);

    // main phase effect that are chosen by user
    void mainPhaseChosen(ArrayList<Card> cards, User firstUser, User secondUser);

    //it should be an if at the end of each attack to monster
    boolean battlePhaseEffectEnd(Monster attacker, Monster defense, User firstUser, User secondUser);

    // it should be called when you attack a face down monster
    void attackFlipSummon(Monster attacker, Monster defense, User firstUser, User secondUser);

    //it should be called when there is a flip summon
    void flipSummonEffect(Card card, User firstUser, User secondUser);

    // when a spell activates
    void activateSpell(Spell spell , User firstUser, User secondUser);

    // at the first of main phase 1
    void mainPhaseEffect(ArrayList<Card>cards , User firstUser , User SecondUser);
}
