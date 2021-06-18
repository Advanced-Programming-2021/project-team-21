package module;


import controller.Effects.*;
import controller.ProgramController;
import controller.menu.DuelMenu;
import module.card.*;
import org.apache.commons.math3.util.Pair;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;


import java.util.ArrayList;

public class Duel {
    private static final int INITIAL_LIFE_POINTS = 8000;
    private final User FIRST_USER, SECOND_USER;
    private User userWhoPlaysNow;
    private Card selectedCard;
    private int placeOfSelectedCard;
    private boolean isSelectedCardForOpponent;
    private boolean hasSummonedOnce;
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
        FIRST_USER.setIncreaseATK(0);
        FIRST_USER.setIncreaseDEF(0);
        SECOND_USER.setIncreaseATK(0);
        SECOND_USER.setIncreaseDEF(0);
        FIRST_USER.setCanSummonTrap(true);
        FIRST_USER.setCanSummonSpell(true);
        FIRST_USER.setCanSummonMonster(true);
        SECOND_USER.setCanSummonMonster(true);
        SECOND_USER.setCanSummonTrap(true);
        SECOND_USER.setCanSummonSpell(true);
        setHasSummonedOnce(false);
        setHasChangedPositionOnce(false);
        setNumberOfTurnsPlayedUpToNow(0);
    }

    public void changeTurn() {
        ChangeTurnEffects.run(userWhoPlaysNow, getRival(), this);
        if (userWhoPlaysNow.equals(FIRST_USER))
            userWhoPlaysNow = SECOND_USER;
        else
            userWhoPlaysNow = FIRST_USER;
        hasSummonedOnce = false;
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
            if (fromWhere.equals("monster")) {
                selectedCard = userWhoPlaysNow.getBoard().getCard(cardAddress, 'M');
                if (((Monster) selectedCard).isSelectEffect()) {
                    PrintResponses.printAskForEffectMonster();
                    if (ProgramController.scanner.nextLine().equals("Yes")) {
                        SelectEffect.run((Monster) selectedCard, getRival(), getUserWhoPlaysNow(), this, cardAddress);
                    }
                }
            } else
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
        if (ChainHandler.run(this, ChainHandler.getChainCommand(new ArrayList<>(), userWhoPlaysNow, this,
                WhereToChain.SUMMON, selectedCard), userWhoPlaysNow, getRival(),
                new Chain(selectedCard, null, null), WhereToChain.SUMMON)) return;
        if (((Monster) selectedCard).isSummonEffect())
            SummonEffects.run((Monster) selectedCard, userWhoPlaysNow, getRival(), this);
        int placeInBoard = userWhoPlaysNow.getBoard().getAddressToSummon();
        ((Monster) selectedCard).setAtk(userWhoPlaysNow.getIncreaseATK() + ((Monster) selectedCard).getAtk());
        ((Monster) selectedCard).setDef(getUserWhoPlaysNow().getIncreaseDEF() + ((Monster) selectedCard).getDef());
        Board currentBoard = userWhoPlaysNow.getBoard();
        if (userWhoPlaysNow.isHasSummonedAlteringATK()) {
            Monster monster = (Monster) userWhoPlaysNow.getBoard().getCard(userWhoPlaysNow.getAlteringATKPlace(), 'm');
            monster.setAtk(monster.getAtk() + 300 * ((Monster) selectedCard).getLevel());
        }
        currentBoard.addMonsterFaceUp(placeInBoard, selectedCard);
        hasSummonedOnce = true;
        userWhoPlaysNow.getHand().removeCardFromHand(placeOfSelectedCard);
        selectedCard = null;
    }

    //TODO implement the changes in flip summon
    public void flipSummon() {
        if (ChainHandler.run(this, ChainHandler.getChainCommand(new ArrayList<>(), userWhoPlaysNow, this,
                WhereToChain.SUMMON, selectedCard), userWhoPlaysNow, getRival(),
                new Chain(selectedCard, null, null), WhereToChain.SUMMON)) return;
        if (((Monster) selectedCard).isSummonEffect())
            SummonEffects.run((Monster) selectedCard, userWhoPlaysNow, getRival(), this);
        if (((Monster) selectedCard).isFlipSummonEffect())
            FlipSummonEffects.run((Monster) selectedCard, getRival(), this, userWhoPlaysNow);
        int placeInBoard = getPlaceOfSelectedCard();
        Board currentBoard = userWhoPlaysNow.getBoard();
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


    public Pair<String, String> endTheGame() {
        User rival = getRival();
        return new Pair<>(rival.getUsername(), rival.getScore() + "-" + userWhoPlaysNow.getScore());
    }


    public Pair<Integer, Integer> attack(int placeInBoard) {
        User rival = getRival();
        Board rivalBoard = rival.getBoard();
        Monster monsterToAttack = (Monster) rivalBoard.getCard(placeInBoard, 'M');
        Monster attackingMonster = (Monster) selectedCard;
        if (ChainHandler.run(this, ChainHandler.getChainCommand(new ArrayList<>(),
                getRival(), this, WhereToChain.ATTACK, attackingMonster), userWhoPlaysNow,
                rival, new Chain(attackingMonster, null, ""), WhereToChain.ATTACK)) {
            return new Pair<>(0, 0);
        }
        checkForDisabledAttack();
        if (monsterToAttack.isBattlePhaseEffectStart() || attackingMonster.isBattlePhaseEffectStart()) {
            if (BattlePhaseStart.run(attackingMonster, monsterToAttack, rival, userWhoPlaysNow))
                return new Pair<>(0, 0);
        }
        if (!attackingMonster.isCanAttack()) return new Pair<>(0, 0);
        ((Monster) selectedCard).setHasAttackedOnceInTurn(true);
        if (monsterToAttack.isATKPosition()) {
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
            Monster monster1 = (Monster) monster;
            monster1.setCanAttack(true);
        }
    }

    public int attackDirectly() {
        User rival = getRival();
        changeLP(rival, ((Monster) selectedCard).getAtk());
        ((Monster) selectedCard).setHasAttackedOnceInTurn(true);
        return ((Monster) selectedCard).getAtk();
    }

    public void activateEffects() {
        Spell spellToActivate = (Spell) selectedCard;
        boolean isCancelled = ChainHandler.run(this, ChainHandler.getChainCommand(new ArrayList<>(),
                getRival(), this, WhereToChain.EFFECT_ACTIVATE, spellToActivate),
                userWhoPlaysNow, getRival(), new Chain(spellToActivate, null, null),
                WhereToChain.EFFECT_ACTIVATE);
        if (isCancelled) {
            addCardToGraveyard(spellToActivate, 10, userWhoPlaysNow);
            return;
        }
        if (spellToActivate.isFieldZone()) {
            if (userWhoPlaysNow.getBoard().getFieldZone() != null) {
                SpellActivation.run((Spell) userWhoPlaysNow.getBoard().getFieldZone(), userWhoPlaysNow, getRival()
                        , this, placeOfSelectedCard, false, null, null);
                addCardToGraveyard(userWhoPlaysNow.getBoard().getFieldZone(), 0, userWhoPlaysNow);
                userWhoPlaysNow.getBoard().removeFieldZone();
            }
            userWhoPlaysNow.getBoard().putCardToFieldZone(spellToActivate);
            SpellActivation.run(spellToActivate, userWhoPlaysNow, getRival(), this, placeOfSelectedCard,
                    true, null, null);

        } else if (spellToActivate.isEquipSPell()) {
            Monster monster = DuelMenu.getMonsterForEquip(this, spellToActivate);
            SpellActivation.run(spellToActivate, userWhoPlaysNow, getRival(), this, placeOfSelectedCard,
                    true, monster, null);
        } else {
            int addressToPut = userWhoPlaysNow.getBoard().getAddressToPutSpell();
            userWhoPlaysNow.getBoard().addSpellAndTrap(addressToPut, spellToActivate);
            flipSetForSpells(addressToPut);
        }
        SpellActivation.run(spellToActivate, userWhoPlaysNow, getRival(), this, placeOfSelectedCard,
                false, null, null);
    }


    public void changeLP(User player, int amount) {
        player.setLifePoints(player.getLifePoints() + amount);
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
                if (card instanceof Spell && ((Spell) card).isEquipSPell()) {
                    Spell spell = (Spell) card;
                    Monster monster = (Monster) userWhoPlaysNow.getBoard().getCard(spell.getEquippedPlace(), 'm');
                    SpellActivation.run(spell, userWhoPlaysNow, getRival(), this, placeInBoard,
                            false, monster, null);
                }
            }
        }
        if (user.isHasSummonedAlteringATK() && card instanceof Monster) {
            Monster monster = (Monster) user.getBoard().getCard(userWhoPlaysNow.getAlteringATKPlace(), 'm');
            monster.setAtk(monster.getAtk() - 300 * ((Monster) card).getLevel());
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


    public boolean canNotSummonSelectedCard() {
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
        if (monster.isSummonEffect())
            SummonEffects.run(monster, userWhoPlaysNow, getRival(), this);
        if (((Monster) selectedCard).isFlipSummonEffect())
            FlipSummonEffects.run((Monster) selectedCard, userWhoPlaysNow, this, getRival());
        if (monster.isFlipSetEffect()) {
            SetFlip.run(monster, getRival(), this);
        }
        getRival().getBoard().changeFacePositionToAttackForMonsters(placeOnBoard);
    }

    public void flipSetForSpells(int placeOnBoard) {
        userWhoPlaysNow.getBoard().changeFacePositionToAttackForSpells(placeOnBoard);
    }

    @Override
    public String toString() {
        return getRival().getHand().showCardsInHandToStringReverse() + "\n" + getRival().getHand().getNumberOfRemainingCardsInDeck() +
                "\n" + getRival().getBoard().showSpellsAndTrapsToStringReverse() +
                "\n" + getRival().getBoard().showMonstersToStringReverse() +
                "\n" + getRival().getGraveyard().size() + "\t\t\t\t\t\t" + getRival().getBoard().getShowFieldZone() +
                "\n" + "--------------------------\n" +
                userWhoPlaysNow.getBoard().getShowFieldZone() + "\t\t\t\t\t\t" + userWhoPlaysNow.getGraveyard().size() +
                "\n" + userWhoPlaysNow.getBoard().showMonstersToString() +
                "\n" + userWhoPlaysNow.getBoard().showSpellsAndTrapsToString() +
                "\n\t\t\t\t\t\t" + userWhoPlaysNow.getHand().getNumberOfRemainingCardsInDeck() +
                "\n" + userWhoPlaysNow.getHand().showCardsInHandToString();
    }

    public int getNumberOfTurnsPlayedUpToNow() {
        return numberOfTurnsPlayedUpToNow;
    }

    public void setNumberOfTurnsPlayedUpToNow(int numberOfTurnsPlayedUpToNow) {
        this.numberOfTurnsPlayedUpToNow = numberOfTurnsPlayedUpToNow;
    }

    public void setTrap() {
    }

    public void setSpell() {
    }

}
