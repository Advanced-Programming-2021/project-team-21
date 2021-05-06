package module;


import module.card.Card;
import module.card.CardType;
import module.card.Spell;

import java.util.ArrayList;
import java.util.logging.Handler;

public class Duel {
    private static final int INITIAL_LIFE_POINTS = 8000;
    private final User FIRST_USER, SECOND_USER;
    private User userWhoPlaysNow;
    private Card selectedCard;


    public Duel(User first_user, User second_user) {
        FIRST_USER = first_user;
        SECOND_USER = second_user;
        FIRST_USER.setGraveyard(new ArrayList<>());
        SECOND_USER.setGraveyard(new ArrayList<>());
        FIRST_USER.setHand(new Hand(FIRST_USER));
        SECOND_USER.setHand(new Hand(SECOND_USER));
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

    public void drawPhase(){
        Hand currentHand = userWhoPlaysNow.getHand();
        currentHand.shuffleDeck();
        currentHand.drawACard();
    }

    public void mainPhase(){
    }

    public void standByPhase(){}

    public void battlePhase(){}

    public void endPhase(){}

    public void selectCard(int cardAddress){
       // Hand currentHand = userWhoPlaysNow.getHand();
        //selectedCard = currentHand.selectACard(cardAddress);
    }

    public void summonMonster(int placeInBoard){

    }

    public void flipSummon(int placeInBoard){
        Board currentBoard = userWhoPlaysNow.getBoard();
    }

    public void tribute(int amount, int[] placesOnBoard){}

    public void setMonster(int placeOnBoard){}

    public void changeAttackPosition(int placeInBoard){
        Board currentBoard = userWhoPlaysNow.getBoard();
        currentBoard.changeFacePosition(placeInBoard);
    }

    public void checkMainPhaseMonsterEffects(){}

    public void checkSpellEffects(){}

    public void useSpell(int placeInBoard){
        Board currentBoard = userWhoPlaysNow.getBoard();
        Spell spell = (Spell) currentBoard.getCard(placeInBoard, 'S');
    }

    public void attack(int placeInBoard){
        Board rivalBoard = getRival(userWhoPlaysNow).getBoard();

    }

    public void attackDirectly(){}

    public void changeLP(User player, int amount){
        player.setLifePoints(player.getLifePoints() + amount);
    }

    public void getCardFromGraveyard(int identifier){}


    public Card getSelectedCard() {
        return selectedCard;
    }

    public void setSelectedCard(Card selectedCard) {
        this.selectedCard = selectedCard;
    }
}
