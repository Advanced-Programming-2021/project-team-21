package module;


import module.card.Card;
import module.card.Monster;
import module.card.Spell;
import module.card.effects.*;
import module.card.enums.CardType;
import org.apache.commons.math3.util.Pair;
import view.Responses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Duel {
    private static final int INITIAL_LIFE_POINTS = 8000;
    private final User FIRST_USER, SECOND_USER;
    public ArrayList<Card> specialSummonCards;
    private User userWhoPlaysNow;
    private Card selectedCard;
    private int placeOfSelectedCard;
    private boolean isSelectedCardForOpponent;
    private boolean hasSummonedOrSetOnce;
    private boolean hasChangedPositionOnce;
    private int numberOfTurnsPlayedUpToNow;


    public Duel(User first_user, User second_user) {
        FIRST_USER = first_user;
        SECOND_USER = second_user;
        FIRST_USER.setGraveyard(new ArrayList<>());
        SECOND_USER.setGraveyard(new ArrayList<>());
        FIRST_USER.setHand(new Hand(FIRST_USER));
        SECOND_USER.setHand(new Hand(SECOND_USER));
        FIRST_USER.setBoard(new Board(FIRST_USER));
        SECOND_USER.setBoard(new Board(SECOND_USER));
        FIRST_USER.setLifePoints(INITIAL_LIFE_POINTS);
        SECOND_USER.setLifePoints(INITIAL_LIFE_POINTS);
        userWhoPlaysNow = FIRST_USER;
        setHasSummonedOrSetOnce(false);
        setHasChangedPositionOnce(false);
        setNumberOfTurnsPlayedUpToNow(0);
    }

    public void changeTurn() {
        resetCards();
        if (userWhoPlaysNow.equals(FIRST_USER))
            userWhoPlaysNow = SECOND_USER;
        else
            userWhoPlaysNow = FIRST_USER;
        hasSummonedOrSetOnce = false;
        hasChangedPositionOnce = false;
    }


    public User getRival() {
        if (userWhoPlaysNow.equals(FIRST_USER))
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
        if (fromWhere.equals("hand")) {
            Hand currentHand = userWhoPlaysNow.getHand();
            selectedCard = currentHand.selectACard(cardAddress);
        } else if (ownOrOpponent.equals("own")) {
            isSelectedCardForOpponent = false;
            if (fromWhere.equals("monster"))
                selectedCard = userWhoPlaysNow.getBoard().getCard(cardAddress, 'M');
            else
                selectedCard = userWhoPlaysNow.getBoard().getCard(cardAddress, 'S');
        } else if (ownOrOpponent.equals("opponent")) {
            isSelectedCardForOpponent = true;
            if (fromWhere.equals("monster"))
                selectedCard = getRival().getBoard().getCard(cardAddress, 'M');
            else
                selectedCard = getRival().getBoard().getCard(cardAddress, 'S');
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
            SummonEffects.run((Monster) selectedCard, userWhoPlaysNow, this);
        int placeInBoard = userWhoPlaysNow.getBoard().getAddressToSummon();
        Board currentBoard = userWhoPlaysNow.getBoard();
        currentBoard.addMonsterFaceUp(placeInBoard, selectedCard);
        hasSummonedOrSetOnce = true;
        userWhoPlaysNow.getHand().removeCardFromHand(placeOfSelectedCard);
        deselectACard();
    }

    //TODO implement the changes in flip summon
    public void flipSummon() {
        if (((Monster) selectedCard).isSummonEffect())
            SummonEffects.run((Monster) selectedCard, userWhoPlaysNow, this);
        if (((Monster) selectedCard).isFlipSummonEffect())
            FlipSummonEffects.run((Monster) selectedCard, getRival(), this, userWhoPlaysNow);
        int placeInBoard = getPlaceOfSelectedCard();
        Board currentBoard = userWhoPlaysNow.getBoard();
    }

    public void specialSummon() {
        //TODO implement the body for this function.
    }

    public void tribute(int[] placesOnBoard) {
        for (int placeOnBoard : placesOnBoard) {
            Card cardToTribute = userWhoPlaysNow.getBoard().getCard(placeOnBoard, 'M');
            addCardToGraveyard(cardToTribute, placeOnBoard, userWhoPlaysNow);
        }
    }

    public void setMonster() {
        int placeOnBoard = userWhoPlaysNow.getBoard().getAddressToSummon();
        Board currentBoard = userWhoPlaysNow.getBoard();
        currentBoard.addMonsterFaceDown(placeOnBoard, selectedCard);
        hasSummonedOrSetOnce = true;
        userWhoPlaysNow.getHand().removeCardFromHand(placeOfSelectedCard);
        deselectACard();
    }

    public void setSpellOrTrap() {
        int placeOnBoard = userWhoPlaysNow.getBoard().getAddressToSummon();
        Board currentBoard = userWhoPlaysNow.getBoard();
        currentBoard.addSpellAndTrap(placeOnBoard, selectedCard);
        hasSummonedOrSetOnce = true;
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


    public Pair<String, String> surrender(int remainingRounds, int initialRounds) {
        if (initialRounds == 1){
            return handleEndingOneRoundGames(getRival(), userWhoPlaysNow);
        } else{
            if (remainingRounds == 1)
                return handleEndingThreeRoundGames(getRival(), userWhoPlaysNow);
            else
                return handleEndingOneRoundGames(getRival(), userWhoPlaysNow);
        }
    }

    public boolean isGameEnded() {
        return userWhoPlaysNow.getLifePoints() == 0 || getRival().getLifePoints() == 0
                || userWhoPlaysNow.getHand().getNumberOfRemainingCardsInDeck() == 0
                || getRival().getHand().getNumberOfRemainingCardsInDeck() == 0;

    }

    public Pair<String, String> handleEndingGame() {
        User rival = getRival();
        if (rival.getLifePoints() > userWhoPlaysNow.getLifePoints()) {
            return handleEndingOneRoundGames(rival, userWhoPlaysNow);
        } else {
            return handleEndingOneRoundGames(userWhoPlaysNow, rival);
        }
    }

    public Pair<String, String> handleEndingOneRoundGames(User winner, User loser){
        winner.setCoins(winner.getCoins() + 1000 + winner.getLifePoints());
        winner.setScore(winner.getLifePoints() + 1000);
        loser.setCoins(100 + loser.getCoins());
        return new Pair<>(winner.getUsername(), 1000 + "-" + 0);
    }

    public Pair<String, String> handleEndingTheWholeMatch() {
        User rival = getRival();
        if (rival.getLifePoints() < userWhoPlaysNow.getLifePoints()) {
            userWhoPlaysNow.setMaxLifePoint(userWhoPlaysNow.getLifePoints());
            userWhoPlaysNow.setCoins(userWhoPlaysNow.getCoins() + 3000 + 3 * userWhoPlaysNow.getMaxLifePoint());
            userWhoPlaysNow.setScore(userWhoPlaysNow.getLifePoints() + 3000);
            rival.setCoins(300 + rival.getCoins());
            return new Pair<>(userWhoPlaysNow.getUsername(), 3000 + "-" + 0);
        } else {
            rival.setMaxLifePoint(rival.getLifePoints());
            rival.setCoins(rival.getCoins() + 3000 + 3 * rival.getLifePoints());
            rival.setScore(rival.getScore() + 3000);
            userWhoPlaysNow.setCoins(100 + userWhoPlaysNow.getCoins());
            return new Pair<>(rival.getUsername(), 3000 + "-" + 0);
        }
    }

    public Pair<String, String> handleEndingThreeRoundGames(User winner, User loser){
        winner.setMaxLifePoint(userWhoPlaysNow.getLifePoints());
        winner.setCoins(winner.getCoins() + 3000 + 3 * winner.getMaxLifePoint());
        winner.setScore(winner.getLifePoints() + 3000);
        loser.setCoins(300 + loser.getCoins());
        return new Pair<>(winner.getUsername(), 3000 + "-" + 0);
    }


    public User handleEndingARound() {
        User winner = (getUserWhoPlaysNow().getLifePoints()
                > getRival().getLifePoints()) ?
                getUserWhoPlaysNow() : getRival();
        winner.setMaxLifePoint(winner.getLifePoints());
        winner.setWinsInAMatch(winner.getWinsInAMatch() + 1);
        return winner;
    }

    public Pair<Integer, Integer> attack(int placeInBoard) {
        User rival = getRival();
        Board rivalBoard = rival.getBoard();
        Monster monsterToAttack = (Monster) rivalBoard.getCard(placeInBoard, 'M');
        Monster attackingMonster = (Monster) selectedCard;
        if (monsterToAttack.isBattlePhaseEffectStart() || attackingMonster.isBattlePhaseEffectStart()) {
            if (BattlePhaseStart.run(attackingMonster, monsterToAttack, rival, this))
                return new Pair<>(0, 0);
        }
        ((Monster) selectedCard).setHasAttackedOnceInTurn(true);
        if (monsterToAttack.isATKPosition()) {
            return handleAttackPositionAttack(attackingMonster, monsterToAttack, placeInBoard, rival);
        } else if (monsterToAttack.isFaceUp()) {
            return handleDefencePositionAttack(monsterToAttack, placeInBoard, rival, true);
        } else {
            return handleDefencePositionAttack(monsterToAttack, placeInBoard, rival, false);
        }
    }

    public int attackDirectly() {
        User rival = getRival();
        changeLP(rival, -((Monster) selectedCard).getAtk());
        ((Monster) selectedCard).setHasAttackedOnceInTurn(true);
        return ((Monster) selectedCard).getAtk();
    }

    public void activateEffects() {
        Spell spellToActivate = (Spell) selectedCard;
        if (spellToActivate.isFieldZone()) {
            if (userWhoPlaysNow.getBoard().getFieldZone() != null) {
                addCardToGraveyard(userWhoPlaysNow.getBoard().getFieldZone(), 0, userWhoPlaysNow);
                userWhoPlaysNow.getBoard().removeFieldZone();
            }
            userWhoPlaysNow.getBoard().putCardToFieldZone(spellToActivate);
        } else {
            int addressToPut = userWhoPlaysNow.getBoard().getAddressToPutSpell();
            userWhoPlaysNow.getBoard().addSpellAndTrap(addressToPut, spellToActivate);
            flipSetForSpells(addressToPut);
        }
        // todo call a function to activate spell
    }


    public void changeLP(User player, int amount) {
        player.setLifePoints(Math.max(player.getLifePoints() + amount, 0));
    }


    public String showGraveyard() {
        StringBuilder graveyardToShow = new StringBuilder();
        int i = 1;
        if (userWhoPlaysNow.getGraveyard().size() == 0)
            return Responses.emptinessOfGraveyard;
        for (Card card : userWhoPlaysNow.getGraveyard()) {
            graveyardToShow.append(i).append(". ").append(card.getName()).append(":").append(card.getDescription());
            i++;
        }
        return graveyardToShow.toString();
    }

    public String showSelectedCard() {
        //casting is necessary but the IDE does not understand.
        if (!selectedCard.isFaceUp() && isSelectedCardForOpponent)
            return Responses.unableToShowOpponentCard;
        if (selectedCard instanceof Monster) {
            return selectedCard.toString();
        } else if (selectedCard instanceof Spell) {
            return selectedCard.toString();
        } else {
            return selectedCard.toString();
        }
    }


    public Card getCardFromGraveyard(int identifier) {
        return userWhoPlaysNow.getGraveyard().get(identifier);
    }

    public void addCardToGraveyard(Card card, int placeInBoard, User user) {
        if (placeInBoard != 0) {
            if (card instanceof Monster)
                user.getBoard().removeMonster(placeInBoard);
            else
                user.getBoard().removeSpellAndTrap(placeInBoard);
        }

        Card cardToAdd = Card.getCardByName(card.getName());
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

    public User getSECOND_USER() {
        return SECOND_USER;
    }

    public User getFIRST_USER() {
        return FIRST_USER;
    }

    public boolean canNotSummonSelectedCard() {
        return !(selectedCard instanceof Monster) || !userWhoPlaysNow.getHand().isCardInHand(selectedCard)
                || !(selectedCard.getCardType().equals(CardType.NORMAL));
    }

    public boolean isHasSummonedOrSetOnce() {
        return hasSummonedOrSetOnce;
    }

    public void setHasSummonedOrSetOnce(boolean hasSummonedOrSetOnce) {
        this.hasSummonedOrSetOnce = hasSummonedOrSetOnce;
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
                if (DeathEffects.run(((Monster) selectedCard), monsterToAttack, rival, this, placeOfSelectedCard, userWhoPlaysNow)) {
                    return new Pair<>(0, 0);
                }
            changeLP(rival, -differenceOfATK);
            if (monsterToAttack.isBattlePhaseEffectEnd() || ((Monster) selectedCard).isBattlePhaseEffectEnd()) {
                if (BattlePhaseEnd.run(((Monster) selectedCard), monsterToAttack, rival, this))
                    return new Pair<>(0, 0);
            }
            addCardToGraveyard(monsterToAttack, placeInBoard, rival);
            return new Pair<>(key, differenceOfATK);
        } else if (differenceOfATK == 0) {
            if (monsterToAttack.isBattlePhaseEffectEnd() || ((Monster) selectedCard).isBattlePhaseEffectEnd()) {
                if (BattlePhaseEnd.run(((Monster) selectedCard), monsterToAttack, rival, this))
                    return new Pair<>(0, 0);
            }
            return new Pair<>(key + 1, differenceOfATK);
        } else {
            changeLP(userWhoPlaysNow, differenceOfATK);
            if (monsterToAttack.isBattlePhaseEffectEnd() || ((Monster) selectedCard).isBattlePhaseEffectEnd()) {
                if (BattlePhaseEnd.run(((Monster) selectedCard), monsterToAttack, rival, this))
                    return new Pair<>(0, 0);
            }
            return new Pair<>(key + 2, differenceOfATK);
        }
    }

    private Pair<Integer, Integer> handleAttackPositionAttack(Monster attackingMonster, Monster monsterToAttack, int placeInBoard, User rival) {
        int differenceOfATK = attackingMonster.getAtk() - monsterToAttack.getAtk();
        int toBeNotDuplicate = 0;
        if (differenceOfATK > 0) {
            monsterToAttack.setDead(true);
            if (monsterToAttack.isDeathEffect())
                if (DeathEffects.run(((Monster) selectedCard), monsterToAttack, rival, this, placeOfSelectedCard, userWhoPlaysNow)) {
                    return new Pair<>(toBeNotDuplicate, toBeNotDuplicate);
                }
            changeLP(rival, -differenceOfATK);
            if (monsterToAttack.isBattlePhaseEffectEnd() || ((Monster) selectedCard).isBattlePhaseEffectEnd()) {
                if (BattlePhaseEnd.run(((Monster) selectedCard), monsterToAttack, rival, this))
                    return new Pair<>(toBeNotDuplicate, toBeNotDuplicate);
            }
            addCardToGraveyard(monsterToAttack, placeInBoard, rival);
            return new Pair<>(1, differenceOfATK);
        } else if (differenceOfATK == 0) {
            monsterToAttack.setDead(true);
            attackingMonster.setDead(true);
            if (monsterToAttack.isDeathEffect())
                if (DeathEffects.run(((Monster) selectedCard), monsterToAttack, rival, this, placeOfSelectedCard, userWhoPlaysNow)) {
                    return new Pair<>(0, 0);
                }

            addCardToGraveyard(monsterToAttack, placeInBoard, rival);
            if (userWhoPlaysNow.getBoard().selectOwnMonster(placeInBoard) != null) {
                if (attackingMonster.isDeathEffect())
                    if (DeathEffects.run(((Monster) selectedCard), monsterToAttack, rival, this, placeOfSelectedCard, userWhoPlaysNow)) {
                        return new Pair<>(0, 0);
                    }
                if (monsterToAttack.isBattlePhaseEffectEnd() || ((Monster) selectedCard).isBattlePhaseEffectEnd()) {
                    if (BattlePhaseEnd.run(((Monster) selectedCard), monsterToAttack, rival, this))
                        return new Pair<>(0, 0);
                }
                addCardToGraveyard(selectedCard, placeOfSelectedCard, userWhoPlaysNow);
            }
            return new Pair<>(2, differenceOfATK);
        } else {
            if (((Monster) selectedCard).isDeathEffect())
                if (DeathEffects.run(monsterToAttack, (Monster) selectedCard, rival, this, placeOfSelectedCard, userWhoPlaysNow)) {
                    return new Pair<>(toBeNotDuplicate, toBeNotDuplicate);
                }

            changeLP(userWhoPlaysNow, differenceOfATK);
            if (monsterToAttack.isBattlePhaseEffectEnd() || ((Monster) selectedCard).isBattlePhaseEffectEnd()) {
                if (BattlePhaseEnd.run(((Monster) selectedCard), monsterToAttack, rival, this))
                    return new Pair<>(0, 0);
            }
            addCardToGraveyard(selectedCard, placeOfSelectedCard, userWhoPlaysNow);
            return new Pair<>(3, differenceOfATK);
        }
    }

    public void flipSetForMonsters(int placeOnBoard) {
        Monster monster = (Monster) getRival().getBoard().getCard(placeOnBoard, 'm');
        if (monster == null)
            return;
        if (monster.isSummonEffect())
            SummonEffects.run(monster, getRival(), this);
        if (((Monster) selectedCard).isFlipSummonEffect())
            FlipSummonEffects.run((Monster) selectedCard, userWhoPlaysNow, this, getRival());
        getRival().getBoard().changeFacePositionToAttackForMonsters(placeOnBoard);
    }

    public void flipSetForSpells(int placeOnBoard) {
        userWhoPlaysNow.getBoard().changeFacePositionToAttackForSpells(placeOnBoard);
    }

    @Override
    public String toString() {
        return getRival().getNickname() + ":" + getRival().getLifePoints() +
                "\n" + getRival().getHand().showCardsInHandToStringReverse() + "\n" + getRival().getHand().getNumberOfRemainingCardsInDeck() +
                "\n" + getRival().getBoard().showSpellsAndTrapsToStringReverse() +
                "\n" + getRival().getBoard().showMonstersToStringReverse() +
                "\n" + getRival().getGraveyard().size() + "\t\t\t\t\t\t" + getRival().getBoard().getShowFieldZone() +
                "\n" + "------------------------------\n" +
                userWhoPlaysNow.getBoard().getShowFieldZone() + "\t\t\t\t\t\t" + userWhoPlaysNow.getGraveyard().size() +
                "\n" + userWhoPlaysNow.getBoard().showMonstersToString() +
                "\n" + userWhoPlaysNow.getBoard().showSpellsAndTrapsToString() +
                "\n\t\t\t\t\t\t\t" + userWhoPlaysNow.getHand().getNumberOfRemainingCardsInDeck() +
                "\n" + userWhoPlaysNow.getHand().showCardsInHandToString() +
                "\n" + userWhoPlaysNow.getNickname() + ":" + userWhoPlaysNow.getLifePoints();
    }

    public int getNumberOfTurnsPlayedUpToNow() {
        return numberOfTurnsPlayedUpToNow;
    }

    public void setNumberOfTurnsPlayedUpToNow(int numberOfTurnsPlayedUpToNow) {
        this.numberOfTurnsPlayedUpToNow = numberOfTurnsPlayedUpToNow;
    }


    private void resetCards() {
        Card[] cards = userWhoPlaysNow.getBoard().getMonsters();
        Arrays.stream(cards).filter(Objects::nonNull).forEach(card -> ((Monster) card).setHasAttackedOnceInTurn(false));
    }
}
