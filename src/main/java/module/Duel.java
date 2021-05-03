package module;


import java.util.ArrayList;

public class Duel {
    private static final int INITIAL_LIFE_POINTS = 8000;
    private final User FIRST_USER, SECOND_USER;
    private User userWhoPlaysNow;



    public Duel(User first_user, User second_user) {
        FIRST_USER = first_user;
        SECOND_USER = second_user;
        FIRST_USER.setGraveyard(new ArrayList<>());
        SECOND_USER.setGraveyard(new ArrayList<>());
        FIRST_USER.setHand(new Hand());
        SECOND_USER.setHand(new Hand());
        FIRST_USER.setLifePoints(INITIAL_LIFE_POINTS);
        SECOND_USER.setLifePoints(INITIAL_LIFE_POINTS);
        userWhoPlaysNow = FIRST_USER;
    }

    private void changeTurn(){
        if (userWhoPlaysNow.equals(FIRST_USER))
            userWhoPlaysNow = SECOND_USER;
        else
            userWhoPlaysNow = FIRST_USER;
    }

    private User getRival(User user){
        if (user.equals(FIRST_USER))
            return SECOND_USER;
        else
            return FIRST_USER;
    }

    public void drawPhase(){}

    public void mainPhase(){}

    public void standByPhase(){}

    public void battlePhase(){}

    public void endPhase(){}

    public void selectCard(int cardAddress){}

    public void summonMonster(int placeInBoard){}

    public void flipSummon(int placeInBoard){}

    public void tribute(int amount, int[] placesOnBoard){}

    public void setMonster(int placeOnBoard){}

    public void changeAttackPosition(int placeInBoard){}

    public void checkMainPhaseMonsterEffects(){}

    public void checkSpellEffects(){}

    public void useSpell(int placeInBoard){}

    public void attack(int placeInBoard){}

    public void attackDirectly(){}

    public void changeLP(User player, int amount){
        player.setLifePoints(player.getLifePoints() + amount);
    }

    public void getCardFromGraveyard(int identifier){}



}
