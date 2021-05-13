package module;


import module.card.*;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;

public class Duel {
    private static final int INITIAL_LIFE_POINTS = 8000;
    private final User FIRST_USER, SECOND_USER;
    public ArrayList<Card> specialSummonCards;
    private User userWhoPlaysNow;
    private Card selectedCard;
    private int placeOfSelectedCard;
    private boolean hasSummonedOnce;
    private boolean hasChangedPositionOnce;


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
        setHasChangedPositionOnce(false);
    }

    public void changeTurn() {
        if (userWhoPlaysNow.equals(FIRST_USER))
            userWhoPlaysNow = SECOND_USER;
        else
            userWhoPlaysNow = FIRST_USER;
        hasSummonedOnce = false;
    }


    public User getRival(User user) {
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
        if (((Monster) selectedCard).isSummonEffect())
        SummonEffects.run((Monster)selectedCard , userWhoPlaysNow , this );
        int placeInBoard = userWhoPlaysNow.getBoard().getAddressToSummon();
        Board currentBoard = userWhoPlaysNow.getBoard();
        currentBoard.addMonsterFaceUp(placeInBoard, selectedCard);
        hasSummonedOnce = true;
    }

    //TODO implement the changes in flip summon
    public void flipSummon() {
        if (((Monster) selectedCard).isSummonEffect())
        SummonEffects.run((Monster) selectedCard ,userWhoPlaysNow , this);
        int placeInBoard = getPlaceOfSelectedCard();
        Board currentBoard = userWhoPlaysNow.getBoard();
    }

    public void specialSummon() {
        //TODO implement the body for this function.
    }

    public void tribute(int[] placesOnBoard) {
    }

    public void setMonster() {
        int placeOnBoard = userWhoPlaysNow.getBoard().getAddressToSummon();
        Board currentBoard = userWhoPlaysNow.getBoard();
        currentBoard.addMonsterFaceDown(placeOnBoard, selectedCard);
        hasSummonedOnce = true;
    }


    public void changeToAttackPosition() {
        int placeInBoard = getPlaceOfSelectedCard();
        Board currentBoard = userWhoPlaysNow.getBoard();
        currentBoard.changeFacePositionToAttackForMonsters(placeInBoard);
        setHasChangedPositionOnce(true);
    }

    public void changeToDefensePosition() {
        int placeInBoard = getPlaceOfSelectedCard();
        Board currentBoard = userWhoPlaysNow.getBoard();
        currentBoard.changeFacePositionToDefenceForMonsters(placeInBoard);
        setHasChangedPositionOnce(true);
    }

    public void checkMainPhaseMonsterEffects() {
    }

    public void checkSpellEffects() {
    }

    public void useSpell(int placeInBoard) {
        Board currentBoard = userWhoPlaysNow.getBoard();
        currentBoard.addSpellAndTrap(placeInBoard, selectedCard);
    }

    public Pair<Integer, Integer> attack(int placeInBoard) {
        User rival = getRival(userWhoPlaysNow);
        Board rivalBoard = rival.getBoard();
        Monster monsterToAttack = (Monster) rivalBoard.getCard(placeInBoard, 'M');
        Monster attackingMonster = (Monster) selectedCard;
        if (monsterToAttack.isBattlePhaseEffectStart() || attackingMonster.isBattlePhaseEffectStart()) {
            if (BattlePhaseStart.run( monsterToAttack, rival, this))
                return new Pair<>(0, 0);
        }
        ((Monster) selectedCard).setHasAttackedOnceInTurn(true);
        if (monsterToAttack.isATKPosition()) {
            return handleAttackPositionAttack(attackingMonster, monsterToAttack, placeInBoard, rival);
        } else if (monsterToAttack.isFaceUp()) {
            return handleDefencePositionAttack(monsterToAttack, placeInBoard, rival, true);
        } else {
            return handleDefencePositionAttack(monsterToAttack, placeInBoard,rival, false);
        }
    }

    public int attackDirectly() {
        User rival = getRival(userWhoPlaysNow);
        changeLP(rival, ((Monster) selectedCard).getAtk());
        return ((Monster) selectedCard).getAtk();
    }

    public void activateEffects() {
        Spell spellToActivate = (Spell) selectedCard;
        if (spellToActivate.isFieldZone()){
            if (userWhoPlaysNow.getBoard().getFieldZone() != null) {
                addCardToGraveyard(userWhoPlaysNow.getBoard().getFieldZone(), 0, userWhoPlaysNow);
                userWhoPlaysNow.getBoard().removeFieldZone();
            }
            userWhoPlaysNow.getBoard().putCardToFieldZone(spellToActivate);
        }else {
            int addressToPut = userWhoPlaysNow.getBoard().getAddressToPutSpell();
            userWhoPlaysNow.getBoard().addSpellAndTrap(addressToPut, spellToActivate);
            flipSetForSpells(addressToPut);
        }
        // todo call a function to activate spell
    }

    public void changeLP(User player, int amount) {
        player.setLifePoints(player.getLifePoints() + amount);
    }

    public Card getCardFromGraveyard(int identifier) {
        return userWhoPlaysNow.getGraveyard().get(identifier);
    }

    public void addCardToGraveyard(Card card, int placeInBoard, User user) {
        if (placeInBoard != 0){
            if (card instanceof Monster)
                user.getBoard().removeMonster(placeInBoard);
            else
                user.getBoard().removeSpellAndTrap(placeInBoard);
        }
        Card cardToAdd  = Card.getCardByName(card.getName());
        user.getGraveyard().add(cardToAdd);
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


    public boolean canSummonSelectedCard() {
        return !(selectedCard instanceof Monster) || !userWhoPlaysNow.getHand().isCardInHand(selectedCard)
                || !(selectedCard.getCardType().equals(CardType.NORMAL));
    }

    public boolean isHasSummonedOrSetOnce() {
        return hasSummonedOnce;
    }

    public void setHasSummonedOnce(boolean hasSummonedOnce) {
        this.hasSummonedOnce = hasSummonedOnce;
    }

    public boolean isHasChangedPositionOnce() {
        return hasChangedPositionOnce;
    }

    public void setHasChangedPositionOnce(boolean hasChangedPositionOnce) {
        this.hasChangedPositionOnce = hasChangedPositionOnce;
    }

    public boolean isNoCardSelected() {
        return selectedCard == null;
    }

    public ArrayList<Card> getSpecialSummonCards() {
        return specialSummonCards;
    }

    public void setSpecialSummonCards(ArrayList<Card> specialSummonCards) {
        this.specialSummonCards = specialSummonCards;
    }

    //TODO implement this
    public boolean isSelectedCardSummonedInThisTurn() {
        return false;
    }

    private Pair<Integer, Integer> handleDefencePositionAttack(Monster monsterToAttack, int placeInBoard, User rival, boolean isFaceUp) {
        int key = 4;
        if (!isFaceUp) {
            key += 3;
            flipSetForMonsters(placeInBoard);
        }
        int differenceOfATK = ((Monster) selectedCard).getAtk() - monsterToAttack.getDef();
        if (differenceOfATK > 0) {
            monsterToAttack.setDead(true);
            if (monsterToAttack.isDeathEffect())
            DeathEffects.run(((Monster) selectedCard) , monsterToAttack , rival , this , placeOfSelectedCard , userWhoPlaysNow);
            changeLP(rival, -differenceOfATK);
            addCardToGraveyard(monsterToAttack, placeInBoard, rival);
            return new Pair<>(key, differenceOfATK);
        } else if (differenceOfATK == 0) {
            return new Pair<>(key + 1, differenceOfATK);
        } else {
            changeLP(userWhoPlaysNow, differenceOfATK);
            return new Pair<>(key + 2, differenceOfATK);
        }
    }

    private Pair<Integer, Integer> handleAttackPositionAttack(Monster attackingMonster, Monster monsterToAttack, int placeInBoard, User rival) {
        int differenceOfATK = attackingMonster.getAtk() - monsterToAttack.getAtk();
        if (differenceOfATK > 0) {
            monsterToAttack.setDead(true);
            if (monsterToAttack.isDeathEffect())
            DeathEffects.run(attackingMonster , monsterToAttack , rival , this , placeOfSelectedCard , userWhoPlaysNow);
            changeLP(rival, -differenceOfATK);
            addCardToGraveyard(monsterToAttack, placeInBoard, rival);
            return new Pair<>(1, differenceOfATK);
        } else if (differenceOfATK == 0) {
            monsterToAttack.setDead(true);
            attackingMonster.setDead(true);
            if (monsterToAttack.isDeathEffect())
                DeathEffects.run(attackingMonster , monsterToAttack , rival , this , placeOfSelectedCard , userWhoPlaysNow);
            addCardToGraveyard(monsterToAttack, placeInBoard, rival);
            if (userWhoPlaysNow.getBoard().selectOwnMonster(placeInBoard) != null){
                if (attackingMonster.isDeathEffect())
                DeathEffects.run(monsterToAttack , attackingMonster , userWhoPlaysNow , this , placeOfSelectedCard , rival);
            addCardToGraveyard(selectedCard, placeOfSelectedCard, userWhoPlaysNow);
            }
            return new Pair<>(2, differenceOfATK);
        } else {
            changeLP(userWhoPlaysNow, differenceOfATK);
            addCardToGraveyard(selectedCard, placeOfSelectedCard, userWhoPlaysNow);
            return new Pair<>(3, differenceOfATK);
        }
    }

    public void flipSetForMonsters(int placeOnBoard){
        Monster monster = (Monster) getRival(userWhoPlaysNow) .getBoard().getCard(placeOnBoard , 'm');
        if (monster.isSummonEffect())
        SummonEffects.run(monster , getRival(userWhoPlaysNow) , this);
        getRival(userWhoPlaysNow).getBoard().changeFacePositionToAttackForMonsters(placeOnBoard);
    }

    public void flipSetForSpells(int placeOnBoard){
        userWhoPlaysNow.getBoard().changeFacePositionToAttackForSpells(placeOnBoard);
    }
}
