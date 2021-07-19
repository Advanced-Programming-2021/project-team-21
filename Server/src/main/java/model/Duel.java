package model;


import controller.Effects.*;
import controller.ProgramController;
import model.card.Card;
import model.card.Monster;
import model.card.Spell;
import model.card.enums.CardType;
import org.apache.commons.math3.util.Pair;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Duel {
    private static final int INITIAL_LIFE_POINTS = 8000;
    private final User FIRST_USER, SECOND_USER;
    private User userWhoPlaysNow;
    private Card selectedCard;
    private int placeOfSelectedCard;
    private boolean hasSummonedOrSetOnce;
    private boolean hasChangedPositionOnce;
    private int numberOfTurnsPlayedUpToNow;
    private int summonedOrSetPlace = -1;

    public Duel(User first_user, User second_user) {
        FIRST_USER = first_user;
        SECOND_USER = second_user;
        userWhoPlaysNow = FIRST_USER;
        setHasSummonedOrSetOnce(false);
        initializeUser(FIRST_USER);
        initializeUser(SECOND_USER);
        setHasChangedPositionOnce(false);
        setNumberOfTurnsPlayedUpToNow(0);
    }

    public static int getInitialLifePoints() {
        return INITIAL_LIFE_POINTS;
    }

    private void initializeUser(User user) {
        user.setGraveyard(new ArrayList<>());
        user.setHand(new Hand(user));
        user.setBoard(new Board(user));
        user.setLifePoints(INITIAL_LIFE_POINTS);
        user.setIncreaseATK(0);
        user.setIncreaseDEF(0);
        user.setCanSummonTrap(true);
        user.setCanSummonSpell(true);
        user.setCanSummonMonster(true);
        user.setCanAttack(true);
        for (int i = 0; i < 5; i++) {
            drawACard(user);
        }
    }

    public void changeTurn() {
        deselectACard();
        summonedOrSetPlace = -1;
        resetCards();
        ChangeTurnEffects.run(userWhoPlaysNow, getRival(), this);
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

    public Card drawACard(User user) {
        Hand currentHand = user.getHand();
        currentHand.shuffleDeck();
        return currentHand.drawACard();
    }


    public void selectCard(int cardAddress, String fromWhere, String ownOrOpponent) throws FileNotFoundException {
        if (fromWhere.equals("hand")) {
            Hand currentHand = userWhoPlaysNow.getHand();
            selectedCard = currentHand.selectACard(cardAddress);
        } else if (ownOrOpponent.equals("own")) {
            if (fromWhere.equals("monster")) {
                selectedCard = userWhoPlaysNow.getBoard().getCard(cardAddress, 'M');
                if (selectedCard == null) return;
                if (((Monster) selectedCard).isSelectEffect()) {
//                    if (PrintResponses.showConfirmation("Are you sure to activate this card's effect?")) {
//                        SelectEffect.run((Monster) selectedCard, getRival(), getUserWhoPlaysNow(), this, cardAddress);
//                    }
                }
            } else
                selectedCard = userWhoPlaysNow.getBoard().getCard(cardAddress, 'S');
        } else if (ownOrOpponent.equals("opponent")) {
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

    public void summonMonster() throws FileNotFoundException {
        if (((Monster) selectedCard).isSummonEffect())
            SummonEffects.run((Monster) selectedCard, userWhoPlaysNow, getRival(), this);
        int placeInBoard = userWhoPlaysNow.getBoard().getAddressToSummon();
        ((Monster) selectedCard).setAtk(userWhoPlaysNow.getIncreaseATK() + ((Monster) selectedCard).getAtk());
        ((Monster) selectedCard).setDef(userWhoPlaysNow.getIncreaseDEF() + ((Monster) selectedCard).getDef());
        Board currentBoard = userWhoPlaysNow.getBoard();
        currentBoard.addMonsterFaceUp(placeInBoard, selectedCard);
        summonedOrSetPlace = placeInBoard;
        if (userWhoPlaysNow.isHasSummonedAlteringATK()) {
            Monster monster = (Monster) userWhoPlaysNow.getBoard().getCard(userWhoPlaysNow.getAlteringATKPlace(), 'M');
            monster.setAtk(monster.getAtk() + 300 * ((Monster) selectedCard).getLevel());
        }
        handleSuccessfulSummonOrSet();
    }

    public void flipSummon() throws FileNotFoundException {
        if (((Monster) selectedCard).isSummonEffect())
            SummonEffects.run((Monster) selectedCard, userWhoPlaysNow, getRival(), this);
        if (((Monster) selectedCard).isFlipSummonEffect())
            FlipSummonEffects.run((Monster) selectedCard, getRival(), this);
        int placeInBoard = getPlaceOfSelectedCard();
        Board currentBoard = userWhoPlaysNow.getBoard();
        summonedOrSetPlace = placeInBoard;
        currentBoard.changeFacePositionToAttackForMonsters(placeInBoard);
    }

    public void tribute(int[] placesOnBoard) throws FileNotFoundException {
        for (int placeOnBoard : placesOnBoard) {
            if (placeOnBoard == -1) continue;
            Card cardToTribute = userWhoPlaysNow.getBoard().getCard(placeOnBoard, 'M');
            addCardToGraveyard(cardToTribute, placeOnBoard, userWhoPlaysNow);
        }
    }

    public void setMonster() {
        int placeOnBoard = userWhoPlaysNow.getBoard().getAddressToSummon();
        Board currentBoard = userWhoPlaysNow.getBoard();
        summonedOrSetPlace = placeOnBoard;
        currentBoard.addMonsterFaceDown(placeOnBoard, selectedCard);
        handleSuccessfulSummonOrSet();
    }

    public void setSpellOrTrap() {
        int placeOnBoard = userWhoPlaysNow.getBoard().getAddressToPutSpell();
        Board currentBoard = userWhoPlaysNow.getBoard();
        currentBoard.addSpellAndTrap(placeOnBoard, selectedCard);
        handleSuccessfulSummonOrSet();
    }

    private void handleSuccessfulSummonOrSet() {
        if (selectedCard instanceof Monster) hasSummonedOrSetOnce = true;
        userWhoPlaysNow.getHand().removeCardFromHand(placeOfSelectedCard);
        deselectACard();
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

    public void handleEndingTheWholeMatch() {
        User rival = getRival();
        if (rival.getLifePoints() < userWhoPlaysNow.getLifePoints()) {
            getStringStringPair(userWhoPlaysNow, rival, userWhoPlaysNow, userWhoPlaysNow.getMaxLifePoint(), userWhoPlaysNow.getLifePoints(), 300);
        } else {
            getStringStringPair(rival, userWhoPlaysNow, rival, rival.getLifePoints(), rival.getScore(), 100);
        }
    }

    private void getStringStringPair(User winner, User loser, User userWhoPlaysNow, int maxLifePoint, int lifePoints, int i) {
        winner.setBoard(null);
        loser.setBoard(null);
        winner.setGraveyard(null);
        loser.setGraveyard(null);
        winner.setHand(null);
        loser.setHand(null);
        winner.setMaxLifePoint(userWhoPlaysNow.getLifePoints());
        winner.setCoins(winner.getCoins() + 3000 + 3 * maxLifePoint);
        winner.setScore(lifePoints + 3000);
        loser.setCoins(i + loser.getCoins());
    }


    public User handleEndingARound() {
        User winner = (getUserWhoPlaysNow().getLifePoints()
                > getRival().getLifePoints()) ?
                getUserWhoPlaysNow() : getRival();
        winner.setMaxLifePoint(winner.getLifePoints());
        winner.setWinsInAMatch(winner.getWinsInAMatch() + 1);
        return winner;
    }

    public Pair<Integer, Integer> attack(int placeInBoard) throws FileNotFoundException {
        User rival = getRival();
        Board rivalBoard = rival.getBoard();
        Monster monsterToAttack = (Monster) rivalBoard.getCard(placeInBoard, 'M');
        Monster attackingMonster = (Monster) selectedCard;
        checkForDisabledAttack();
        if (monsterToAttack.isBattlePhaseEffectStart() || attackingMonster.isBattlePhaseEffectStart()) {
            if (BattlePhaseStart.run(attackingMonster, monsterToAttack, rival, this))
                return new Pair<>(0, 0);
        }
        if (!attackingMonster.isCanAttack()) return new Pair<>(0, 0);
        ((Monster) selectedCard).setHasAttackedOnceInTurn(true);
        if (monsterToAttack.isATK()) {
            return handleAttackPositionAttack(attackingMonster, monsterToAttack, placeInBoard, rival);
        } else if (monsterToAttack.isFaceUp()) {
            return handleDefencePositionAttack(monsterToAttack, placeInBoard, rival, true);
        } else {
            return handleDefencePositionAttack(monsterToAttack, placeInBoard, rival, false);
        }
    }

    private void checkForDisabledAttack() {
        for (Card spellsAndTrap : getRival().getBoard().getSpellsAndTraps()) {
            if (spellsAndTrap instanceof Spell) {
                Spell spell = (Spell) spellsAndTrap;
                if (spell.getMonstersCanNotAttack().hasEffect()) return;
            }
        }
        for (Card monster : userWhoPlaysNow.getBoard().getMonsters()) {
            if (monster == null) continue;
            Monster monster1 = (Monster) monster;
            monster1.setCanAttack(true);
        }
    }

    public int attackDirectly() {
        User rival = getRival();
        changeLP(rival, -((Monster) selectedCard).getAtk());
        ((Monster) selectedCard).setHasAttackedOnceInTurn(true);
        return ((Monster) selectedCard).getAtk();
    }

    public boolean activateEffects() throws FileNotFoundException {
        Spell spellToActivate = (Spell) selectedCard;
        int addressToPut = placeOfSelectedCard;
        if (userWhoPlaysNow.getHand().isCardInHand(selectedCard)) {
            addressToPut = userWhoPlaysNow.getBoard().getAddressToPutSpell();
            userWhoPlaysNow.getBoard().addSpellAndTrap(addressToPut, spellToActivate);
            flipSetForSpells(addressToPut);
        }

        if (spellToActivate.isFieldZone()) {
            if (userWhoPlaysNow.getBoard().getFieldZone() != null) {
                SpellActivation.run((Spell) userWhoPlaysNow.getBoard().getFieldZone(), userWhoPlaysNow, getRival()
                        , this, placeOfSelectedCard, false, null, null);
                addCardToGraveyard(userWhoPlaysNow.getBoard().getFieldZone(), 0, userWhoPlaysNow);
                userWhoPlaysNow.getBoard().removeFieldZone();
            }
            userWhoPlaysNow.getBoard().putCardToFieldZone(spellToActivate);
            userWhoPlaysNow.getBoard().removeSpellAndTrap(placeOfSelectedCard);
            SpellActivation.run(spellToActivate, userWhoPlaysNow, getRival(), this, placeOfSelectedCard,
                    true, null, null);
            return true;
        }
//        else if (spellToActivate.isEquipSpell()) {
//            Monster monster = DuelMenu.getMonsterForEquip(this, spellToActivate);
//            if (monster == null) return false;
//            SpellActivation.run(spellToActivate, userWhoPlaysNow, getRival(), this, placeOfSelectedCard,
//                    true, monster, null);
//            userWhoPlaysNow.getBoard().changeFacePositionToAttackForSpells(addressToPut);
//            return true;
//        }
        SpellActivation.run(spellToActivate, userWhoPlaysNow, getRival(), this, placeOfSelectedCard,
                false, null, null);
        userWhoPlaysNow.getBoard().changeFacePositionToAttackForSpells(addressToPut);
        return true;
    }

    public void changeLP(User player, int amount) {
        player.setLifePoints(Math.max(player.getLifePoints() + amount, 0));
    }


    public void addCardToGraveyard(Card card, int placeInBoard, User user) {
        if (placeInBoard == 10) {
            Card cardToAdd = Card.getCardByName(card.getName());
            user.getGraveyard().add(cardToAdd);
            return;
        }
        if (placeInBoard != 0) {
            if (card instanceof Monster)
                user.getBoard().removeMonster(placeInBoard);
            else {
                user.getBoard().removeSpellAndTrap(placeInBoard);
                if (card instanceof Spell && ((Spell) card).isEquipSpell()) {
                    Spell spell = (Spell) card;
                    Monster monster = (Monster) userWhoPlaysNow.getBoard().getCard(spell.getEquippedPlace(), 'm');
                    try {
                        SpellActivation.run(spell, userWhoPlaysNow, getRival(), this, placeInBoard,
                                false, monster, null);
                    } catch (Exception ignored) {

                    }
                }
            }
        }
        if (user.isHasSummonedAlteringATK() && card instanceof Monster) {
            Monster monster = (Monster) user.getBoard().getCard(userWhoPlaysNow.getAlteringATKPlace(), 'm');
            monster.setAtk(monster.getAtk() - 300 * ((Monster) card).getLevel());
        }
        DeathEffects.CheckSpells(user);
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

    public void setUserWhoPlaysNow(User userWhoPlaysNow) {
        this.userWhoPlaysNow = userWhoPlaysNow;
    }

    public User getSECOND_USER() {
        return SECOND_USER;
    }

    public User getFIRST_USER() {
        return FIRST_USER;
    }

    public boolean canNotSummonSelectedCard() {
        return !(selectedCard instanceof Monster) || !userWhoPlaysNow.getHand().isCardInHand(selectedCard)
                || (selectedCard.getCardType().equals(CardType.RITUAL));
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

    public boolean isSelectedCardSummonedInThisTurn() {
        return placeOfSelectedCard == summonedOrSetPlace;
    }

    private Pair<Integer, Integer> handleDefencePositionAttack(Monster monsterToAttack, int placeInBoard, User
            rival, boolean isFaceUp) throws FileNotFoundException {
        int key = 4;
        if (!isFaceUp) {
            key += 3;
            flipSetForMonsters(placeInBoard);
        }
        int differenceOfATK = ((Monster) selectedCard).getAtk() - monsterToAttack.getDef();
        if (differenceOfATK > 0) {
            monsterToAttack.setDead(true);
            if (monsterToAttack.isDeathEffect())
                if (DeathEffects.run(((Monster) selectedCard), monsterToAttack, rival, this, placeOfSelectedCard, userWhoPlaysNow, placeInBoard)) {
                    return new Pair<>(0, 0);
                }
            if (monsterToAttack.isBattlePhaseEffectEnd() || ((Monster) selectedCard).isBattlePhaseEffectEnd()) {
                if (BattlePhaseEnd.run(((Monster) selectedCard), monsterToAttack))
                    return new Pair<>(0, 0);
            }
            addCardToGraveyard(monsterToAttack, placeInBoard, rival);
            return new Pair<>(key, differenceOfATK);
        } else if (differenceOfATK == 0) {
            if (monsterToAttack.isBattlePhaseEffectEnd() || ((Monster) selectedCard).isBattlePhaseEffectEnd()) {
                if (BattlePhaseEnd.run(((Monster) selectedCard), monsterToAttack))
                    return new Pair<>(0, 0);
            }
            return new Pair<>(key + 1, differenceOfATK);
        } else {
            changeLP(userWhoPlaysNow, differenceOfATK);
            if (monsterToAttack.isBattlePhaseEffectEnd() || ((Monster) selectedCard).isBattlePhaseEffectEnd()) {
                if (BattlePhaseEnd.run(((Monster) selectedCard), monsterToAttack))
                    return new Pair<>(0, 0);
            }
            return new Pair<>(key + 2, differenceOfATK);
        }
    }

    private Pair<Integer, Integer> handleAttackPositionAttack(Monster attackingMonster, Monster monsterToAttack,
                                                              int placeInBoard, User rival) {
        int differenceOfATK = attackingMonster.getAtk() - monsterToAttack.getAtk();
        int toBeNotDuplicate = 0;
        if (differenceOfATK > 0) {
            monsterToAttack.setDead(true);
            if (monsterToAttack.isDeathEffect())
                if (DeathEffects.run(((Monster) selectedCard), monsterToAttack, rival, this, placeOfSelectedCard, userWhoPlaysNow, placeInBoard)) {
                    return new Pair<>(toBeNotDuplicate, toBeNotDuplicate);
                }
            changeLP(rival, -differenceOfATK);
            if (monsterToAttack.isBattlePhaseEffectEnd() || ((Monster) selectedCard).isBattlePhaseEffectEnd()) {
                if (BattlePhaseEnd.run(((Monster) selectedCard), monsterToAttack)) {
                    return new Pair<>(toBeNotDuplicate, toBeNotDuplicate);
                }
            }
            addCardToGraveyard(monsterToAttack, placeInBoard, rival);
            return new Pair<>(1, differenceOfATK);
        } else if (differenceOfATK == 0) {
            monsterToAttack.setDead(true);
            attackingMonster.setDead(true);
            if (monsterToAttack.isDeathEffect())
                if (DeathEffects.run(((Monster) selectedCard), monsterToAttack, rival, this, placeOfSelectedCard, userWhoPlaysNow, placeInBoard)) {
                    return new Pair<>(0, 0);
                }

            addCardToGraveyard(monsterToAttack, placeInBoard, rival);
            if (userWhoPlaysNow.getBoard().selectOwnMonster(placeInBoard) != null) {
                int notDuplicate = 0;
                if (attackingMonster.isDeathEffect())
                    if (DeathEffects.run(((Monster) selectedCard), monsterToAttack, rival, this, placeOfSelectedCard, userWhoPlaysNow, placeInBoard)) {
                        return new Pair<>(notDuplicate, 0);
                    }
                if (monsterToAttack.isBattlePhaseEffectEnd() || ((Monster) selectedCard).isBattlePhaseEffectEnd()) {
                    if (BattlePhaseEnd.run(((Monster) selectedCard), monsterToAttack))
                        return new Pair<>(0, 0);
                }
                addCardToGraveyard(selectedCard, placeOfSelectedCard, userWhoPlaysNow);
            }
            return new Pair<>(2, differenceOfATK);
        } else {
            if (((Monster) selectedCard).isDeathEffect())
                if (DeathEffects.run(monsterToAttack, (Monster) selectedCard, rival, this, placeOfSelectedCard, userWhoPlaysNow, placeInBoard)) {
                    return new Pair<>(toBeNotDuplicate, toBeNotDuplicate);
                }

            changeLP(userWhoPlaysNow, differenceOfATK);
            if (monsterToAttack.isBattlePhaseEffectEnd() || ((Monster) selectedCard).isBattlePhaseEffectEnd()) {
                if (BattlePhaseEnd.run(((Monster) selectedCard), monsterToAttack))
                    return new Pair<>(0, 0);
            }
            addCardToGraveyard(selectedCard, placeOfSelectedCard, userWhoPlaysNow);
            return new Pair<>(3, differenceOfATK);
        }
    }

    public void flipSetForMonsters(int placeOnBoard) throws FileNotFoundException {
        Monster monster = (Monster) getRival().getBoard().getCard(placeOnBoard, 'm');
        if (monster == null)
            return;
        if (monster.isSummonEffect())
            SummonEffects.run(monster, userWhoPlaysNow, getRival(), this);
        if (monster.isFlipSummonEffect())
            FlipSummonEffects.run(monster, userWhoPlaysNow, this);
        if (monster.isFlipSetEffect()) {
            SetFlip.run(monster, userWhoPlaysNow, this);
        }
        getRival().getBoard().changeFacePositionToDefenceForMonsters(placeOnBoard);
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

