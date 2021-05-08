package module;


import module.card.Card;
import module.card.Monster;

import java.util.ArrayList;

public class Duel {
    private static final int INITIAL_LIFE_POINTS = 8000;
    private final User FIRST_USER, SECOND_USER;
    private User userWhoPlaysNow;
    private Card selectedCard;
    private int placeOfSelectedCard;
    private boolean hasSummonedOnce;


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
        setHasSummonedOnce(false);
    }

    public void changeTurn() {
        if (userWhoPlaysNow.equals(FIRST_USER))
            userWhoPlaysNow = SECOND_USER;
        else
            userWhoPlaysNow = FIRST_USER;
        hasSummonedOnce = false;
    }

    private User getRival(User user) {
        if (user.equals(FIRST_USER))
            return SECOND_USER;
        else
            return FIRST_USER;
    }

    public Card drawACard() {
        Hand currentHand = userWhoPlaysNow.getHand();
        currentHand.shuffleDeck();
        return currentHand.drawACard();
    }


    public void standByPhase() {
    }


    public void endPhase() {
    }

    public void selectCard(int cardAddress, String fromWhere, String ownOrOpponent) {
        if (fromWhere.equals("Hand")) {
            Hand currentHand = userWhoPlaysNow.getHand();
            selectedCard = currentHand.selectACard(cardAddress);
        } else if (ownOrOpponent.equals("own")) {
            if (fromWhere.equals("Monster"))
                selectedCard = userWhoPlaysNow.getBoard().getCard(cardAddress, 'M');
            else
                selectedCard = userWhoPlaysNow.getBoard().getCard(cardAddress, 'S');
        } else if (ownOrOpponent.equals("opponent")) {
            if (fromWhere.equals("Monster"))
                selectedCard = getRival(userWhoPlaysNow).getBoard().getCard(cardAddress, 'M');
            else
                selectedCard = getRival(userWhoPlaysNow).getBoard().getCard(cardAddress, 'S');
        }
        if (selectedCard != null)
            setPlaceOfSelectedCard(cardAddress);
    }

    public void deselectACard() {
        setSelectedCard(null);
        setPlaceOfSelectedCard(0);
    }

    public void summonMonster() {
        int placeInBoard = userWhoPlaysNow.getBoard().getAddressToSummon();
        Board currentBoard = userWhoPlaysNow.getBoard();
        currentBoard.addMonsterFaceUp(placeInBoard, selectedCard);
        hasSummonedOnce = true;
    }


    public void flipSummon(int placeInBoard) {
        Board currentBoard = userWhoPlaysNow.getBoard();
    }

    public void tribute(int[] placesOnBoard) {
    }

    public void setMonster(int placeOnBoard) {
        Board currentBoard = userWhoPlaysNow.getBoard();
        currentBoard.addMonsterFaceDown(placeOnBoard, selectedCard);
    }

    public void changeToAttackPosition(int placeInBoard) {
        Board currentBoard = userWhoPlaysNow.getBoard();
        currentBoard.changeFacePositionToAttack(placeInBoard);
    }

    public void changeToDefensePosition(int placeInBoard) {
        Board currentBoard = userWhoPlaysNow.getBoard();
        currentBoard.changeFacePositionToDefence(placeInBoard);
    }

    public void checkMainPhaseMonsterEffects() {
    }

    public void checkSpellEffects() {
    }

    public void useSpell(int placeInBoard) {
        Board currentBoard = userWhoPlaysNow.getBoard();
        currentBoard.addSpellAndTrap(placeInBoard, selectedCard);
    }

    public void attack(int placeInBoard) {
        User rival = getRival(userWhoPlaysNow);
        Board rivalBoard = rival.getBoard();
        Monster monsterToAttack = (Monster) rivalBoard.getCard(placeInBoard, 'M');
        if (monsterToAttack.isFaceUp()) {
            int differenceOfATK = ((Monster) selectedCard).getAtk() - monsterToAttack.getAtk();
            if (differenceOfATK > 0) {
                changeLP(rival, -differenceOfATK);
                addCardToGraveyard(monsterToAttack, placeInBoard, rival);
            } else if (differenceOfATK == 0) {
                addCardToGraveyard(monsterToAttack, placeInBoard, rival);
                addCardToGraveyard(selectedCard, placeOfSelectedCard, userWhoPlaysNow);
            } else {
                changeLP(userWhoPlaysNow, differenceOfATK);
                addCardToGraveyard(selectedCard, placeOfSelectedCard, userWhoPlaysNow);
            }
        } else {
            int differenceOfATK = ((Monster) selectedCard).getAtk() - monsterToAttack.getDef();
            if (differenceOfATK > 0) {
                changeLP(rival, -differenceOfATK);
                addCardToGraveyard(monsterToAttack, placeInBoard, rival);
            } else {
                changeLP(userWhoPlaysNow, differenceOfATK);
            }
        }
    }

    public void attackDirectly() {
        User rival = getRival(userWhoPlaysNow);
        changeLP(rival, ((Monster) selectedCard).getAtk());
    }

    public void activateEffects() {

    }

    public void changeLP(User player, int amount) {
        player.setLifePoints(player.getLifePoints() + amount);
    }

    public Card getCardFromGraveyard(int identifier) {
        return userWhoPlaysNow.getGraveyard().get(identifier);
    }

    public void addCardToGraveyard(Card card, int placeInBoard, User user) {
        if (card instanceof Monster)
            user.getBoard().removeMonster(placeInBoard);
        else
            user.getBoard().removeSpellAndTrap(placeInBoard);

        user.getGraveyard().add(card);
    }

    public Card getSelectedCard() {
        return selectedCard;
    }

    public void setSelectedCard(Card selectedCard) {
        this.selectedCard = selectedCard;
    }

    public int getPlaceOfSelectedCard() {
        return placeOfSelectedCard;
    }

    public void setPlaceOfSelectedCard(int placeOfSelectedCard) {
        this.placeOfSelectedCard = placeOfSelectedCard;
    }

    public User getUserWhoPlaysNow() {
        return userWhoPlaysNow;
    }

    public boolean canSummonSelectedCard(){
        return !(selectedCard instanceof Monster);
    }

    public boolean isHasSummonedOnce() {
        return hasSummonedOnce;
    }

    public void setHasSummonedOnce(boolean hasSummonedOnce) {
        this.hasSummonedOnce = hasSummonedOnce;
    }
}
