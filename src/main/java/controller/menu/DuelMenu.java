package controller.menu;

import controller.ProgramController;
import module.Duel;
import module.User;
import module.card.Card;
import module.card.Monster;
import module.card.Spell;
import module.card.enums.CardType;
import org.apache.commons.math3.util.Pair;
import view.PrintResponses;
import view.Regex;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.regex.Matcher;

public class DuelMenu implements Menuable {
    private Duel currentDuel;
    private Phases phase;
    private boolean isInGame;
    private int remainingRounds;
    private int initialRounds;
    private boolean isFirstRound = true;

    {
        isInGame = true;
    }

    @Override
    public void run(String command) {
        HashMap<String, Consumer<Matcher>> commandMap = createCommandMap();
        boolean isValidCommand = false;

        try {
            for (String string : commandMap.keySet()) {
                if (command.equals(Regex.menuExit)) {
                    exitMenu();
                    return;
                }
                Matcher matcher = Regex.getMatcher(command, string);
                if (currentDuel != null && currentDuel.isGameEnded()) {
                    endTheGame();
                    return;
                } else if (matcher.find() && isInGame) {
                    commandMap.get(string).accept(matcher);
                    isValidCommand = true;
                } else if (!isInGame) {
                    if (command.equals("back")) {
                        back();
                        isValidCommand = true;
                    }
                }
            }
        } catch (Exception ignored) {
        }
        if (!isValidCommand)
            PrintResponses.printInvalidFormat();
    }

    @Override
    public void showCurrentMenu() {
        PrintResponses.printDuelMenuShow();
    }

    @Override
    public void exitMenu() {
        ProgramController.currentMenu = new MainMenu();
    }

    protected HashMap<String, Consumer<Matcher>> createCommandMap() {
        HashMap<String, Consumer<Matcher>> commandMap = new HashMap<>();
        commandMap.put(Regex.createNewDuel, this::createNewDuel);
        commandMap.put(Regex.createNewDuelWithAI, this::createNewDuelWithAI);
        commandMap.put(Regex.selectFromOwn, this::selectCardFromOwn);
        commandMap.put(Regex.selectFromOpponent, this::selectCardFromOpponent);
        commandMap.put(Regex.deselectCard, this::deselectCard);
        commandMap.put(Regex.nextPhase, this::goToNextPhase);
        commandMap.put(Regex.summon, this::summon);
        commandMap.put(Regex.set, this::set);
        commandMap.put(Regex.setPosition, this::setPosition);
        commandMap.put(Regex.flipSummon, this::flipSummon);
        commandMap.put(Regex.attack, this::attack);
        commandMap.put(Regex.attackDirectly, this::attackDirectly);
        commandMap.put(Regex.activateSpell, this::activateSpell);
        commandMap.put(Regex.showGraveyard, this::showGraveyard);
        commandMap.put(Regex.showSelectedCard, this::showSelectedCard);
        commandMap.put(Regex.surrender, this::surrender);
        return commandMap;
    }

    private void createNewDuel(Matcher matcher) {
        User secondPlayer = User.getUserByUsername(matcher.group("player2Username"));
        remainingRounds = Integer.parseInt(matcher.group("rounds"));
        initialRounds = remainingRounds;
        if (secondPlayer == null) {
            PrintResponses.printNoUserExistToPlayWith();
        } else if (ProgramController.userInGame.getActiveDeck() == null) {
            PrintResponses.printHasNoActiveDeck(ProgramController.userInGame);
        } else if (secondPlayer.getActiveDeck() == null) {
            PrintResponses.printHasNoActiveDeck(secondPlayer);
        } else if (!ProgramController.userInGame.getActiveDeck().isValid()) {
            PrintResponses.printInvalidDeck(ProgramController.userInGame);
        } else if (!secondPlayer.getActiveDeck().isValid()) {
            PrintResponses.printInvalidDeck(secondPlayer);
        } else if (!(remainingRounds == 1 || remainingRounds == 3)) {
            PrintResponses.printNonSupportiveRound();
        } else {
            handleSuccessfulGameCreation(secondPlayer);
            PrintResponses.printBoard(currentDuel);
        }
    }

    private void createNewDuelWithAI(Matcher matcher) {

    }

    private void selectCardFromOwn(Matcher matcher) {
        String whereToSelectFrom = matcher.group("where");
        int cardAddress = Integer.parseInt(matcher.group("number"));
        if (!(whereToSelectFrom.equals("monster") || whereToSelectFrom.equals("spell")
                || whereToSelectFrom.equals("field")
                || whereToSelectFrom.equals("hand"))) {
            PrintResponses.printInvalidSelection();
        } else {
            currentDuel.selectCard(cardAddress, whereToSelectFrom, "own");
            if (currentDuel.getSelectedCard() == null) {
                PrintResponses.printNoCardInPosition();
            } else {
                PrintResponses.printSuccessfulCardSelection();
            }
        }
    }

    private void selectCardFromOpponent(Matcher matcher) {
        String whereToSelectFrom = matcher.group("where");
        int cardAddress = Integer.parseInt(matcher.group("number"));
        if (!(whereToSelectFrom.equals("monster") || whereToSelectFrom.equals("spell")
                || whereToSelectFrom.equals("field"))) {
            PrintResponses.printInvalidSelection();
        } else {
            currentDuel.selectCard(cardAddress, whereToSelectFrom, "opponent");
            if (currentDuel.getSelectedCard() == null) {
                PrintResponses.printNoCardInPosition();
            } else {
                PrintResponses.printSuccessfulCardSelection();
            }
        }
    }

    private void deselectCard(Matcher matcher) {
        if (currentDuel.isNoCardSelected()) {
            PrintResponses.printNoCardSelected();
        } else {
            currentDuel.deselectACard();
            PrintResponses.printSuccessfulCardDeselection();
        }
    }

    private void goToNextPhase(Matcher matcher) {
        if (currentDuel == null) {
            PrintResponses.printInvalidFormat();
            return;
        }
        if (phase.equals(Phases.DRAW_PHASE)) {
            phase = Phases.STANDBY_PHASE;
        } else if (phase.equals(Phases.STANDBY_PHASE)) {
            phase = Phases.MAIN_PHASE1;
        } else if (phase.equals(Phases.MAIN_PHASE1)) {
            if (currentDuel.getNumberOfTurnsPlayedUpToNow() != 0)
                phase = Phases.BATTLE_PHASE;
            else {
                handleTransitionFromEndPhaseToDrawPhase();
                handleDrawingACard(currentDuel.drawACard());
                return;
            }
        } else if (phase.equals(Phases.BATTLE_PHASE)) {
            phase = Phases.MAIN_PHASE2;
        } else if (phase.equals(Phases.MAIN_PHASE2)) {
            handleTransitionFromEndPhaseToDrawPhase();
            handleDrawingACard(currentDuel.drawACard());
            return;
        }
        PrintResponses.printPhaseName(phase);
    }

    private void endTheGame() {
        remainingRounds--;
        if (remainingRounds == 0) {
            if (initialRounds == 1)
                PrintResponses.printEndingTheGame(currentDuel.handleEndingGame());
            else
                PrintResponses.printEndingTheWholeMatch(currentDuel.handleEndingTheWholeMatch());
            currentDuel = null;
        } else {
            PrintResponses.printWinnerInRound(currentDuel.handleEndingARound());
            handleSuccessfulGameCreation(currentDuel.getSECOND_USER());
        }
    }

    private void summon(Matcher matcher) {
        if (currentDuel.isNoCardSelected()) {
            PrintResponses.printNoCardSelected();
        } else if (currentDuel.canNotSummonSelectedCard()) {
            PrintResponses.printUnableToSummonCard();
        } else if (isNotInMainPhases()) {
            PrintResponses.printSummonInWrongPhase();
        } else if (currentDuel.getUserWhoPlaysNow().getBoard().getAddressToSummon() == 0) {
            PrintResponses.printFullnessOfMonsterCardZone();
        } else if (currentDuel.isHasSummonedOrSetOnce()) {
            PrintResponses.printUnableToSummonInTurn();
        } else if (((Monster) currentDuel.getSelectedCard()).getLevel() > 4) {
            if (((Monster) currentDuel.getSelectedCard()).getLevel() < 7) {
                if (isNotEnoughCardsForTribute(1)) return;
            } else if (((Monster) currentDuel.getSelectedCard()).getLevel() < 10) {
                if (isNotEnoughCardsForTribute(2)) return;
            } else {
                if (isNotEnoughCardsForTribute(3)) return;
            }
            int[] cardToTributeAddress = Arrays.stream(ProgramController.scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            if (areCardAddressesEmpty(cardToTributeAddress)) return;
            currentDuel.tribute(cardToTributeAddress);
            currentDuel.summonMonster();
            PrintResponses.printSuccessfulSummon();
        } else {
            currentDuel.summonMonster();
            PrintResponses.printSuccessfulSummon();
            PrintResponses.printBoard(currentDuel);
        }

    }

    private void set(Matcher matcher) {
        if (currentDuel.isNoCardSelected()) {
            PrintResponses.printNoCardSelected();
        } else if (!currentDuel.getUserWhoPlaysNow().getHand().isCardInHand(currentDuel.getSelectedCard())) {
            PrintResponses.printUnableToSetCard();
        } else if (isNotInMainPhases()) {
            PrintResponses.printSetCardInWrongPhase();
        } else if (currentDuel.getSelectedCard() instanceof Monster) {
            handleMonsterSet();
        } else {
            handleSpellAndTrapSet();
        }
    }

    private void setPosition(Matcher matcher) {
        String position = matcher.group("position");
        if (currentDuel.isNoCardSelected()) {
            PrintResponses.printNoCardSelected();
        } else if (isSelectedCardNotInMonsterZone()) {
            PrintResponses.printUnableToChangePositionOfCard();
        } else if (isNotInMainPhases()) {
            PrintResponses.printSetCardInWrongPhase();
        } else if (isPositionTheSameAsBefore(position)) {
            PrintResponses.printCardInWantedPosition();
        } else if (currentDuel.isHasChangedPositionOnce()) {
            PrintResponses.printUnableToChangePositionInTurnTwice();
        } else {
            if (position.equals("attack")) {
                currentDuel.changeToAttackPosition();
            } else {
                currentDuel.changeToDefensePosition();
            }
            PrintResponses.printSuccessfulMonsterCardPositionChange();
        }
    }

    private void flipSummon(Matcher matcher) {
        if (currentDuel.isNoCardSelected()) {
            PrintResponses.printNoCardSelected();
        } else if (isSelectedCardNotInMonsterZone()) {
            PrintResponses.printUnableToChangePositionOfCard();
        } else if (isNotInMainPhases()) {
            PrintResponses.printFlipSummonInWrongPhase();
        } else if (currentDuel.isSelectedCardSummonedInThisTurn() || !(!currentDuel.getSelectedCard().isFaceUp()
                && currentDuel.getSelectedCard().isFaceUp())) {
            PrintResponses.printUnableToFlipSummonCard();
        } else {
            currentDuel.flipSummon();
            PrintResponses.printSuccessfulFlipSummon();
        }

    }

    private void attack(Matcher matcher) {
        int address = Integer.parseInt(matcher.group("number"));
        Monster monsterToAttack = (Monster) currentDuel.getRival().getBoard().
                getCard(currentDuel.getPlaceOfSelectedCard(), 'M');
        if (currentDuel.isNoCardSelected()) {
            PrintResponses.printNoCardSelected();
        } else if (isSelectedCardNotInMonsterZone()) {
            PrintResponses.printUnableToAttack();
        } else if (isNotInBattlePhase()) {
            PrintResponses.printAttackInWrongPhase();
        } else if (((Monster) currentDuel.getSelectedCard()).isHasAttackedOnceInTurn()) {
            PrintResponses.printCardAttackedBefore();
        } else if (monsterToAttack == null) {
            PrintResponses.printNoCardToAttackWith();
        } else {
            handleSuccessfulAttack(address, monsterToAttack);
            PrintResponses.printBoard(currentDuel);
        }
    }

    private void attackDirectly(Matcher matcher) {
        if (currentDuel.isNoCardSelected()) {
            PrintResponses.printNoCardSelected();
        } else if (isSelectedCardNotInMonsterZone()) {
            PrintResponses.printUnableToAttack();
        } else if (isNotInBattlePhase()) {
            PrintResponses.printAttackInWrongPhase();
        } else if (((Monster) currentDuel.getSelectedCard()).isHasAttackedOnceInTurn()) {
            PrintResponses.printCardAttackedBefore();
        } else {
            int damage = currentDuel.attackDirectly();
            PrintResponses.printDamageInAttackDirectly(damage);
            currentDuel.deselectACard();
        }
    }

    private void activateSpell(Matcher matcher) {
        if (currentDuel.isNoCardSelected()) {
            PrintResponses.printNoCardSelected();
        } else if (!(currentDuel.getSelectedCard() instanceof Spell)) {
            PrintResponses.printNonSpellCardsToActivateEffect();
        } else if (isNotInMainPhases()) {
            PrintResponses.printUnableToActivateEffectOnTurn();
        } else if (currentDuel.getSelectedCard().isFaceUp()) {
            PrintResponses.printUnableToActivateCardTwice();
        } else if (isSpellZoneFullAndNeedsToBeOnBoard()) {
            PrintResponses.printFullnessOfSpellCardZone();
        } else if (isSpellPreparedToBeActivated()) {
            PrintResponses.printUnfinishedPreparationOfSpell();
        } else {
            if (currentDuel.getSelectedCard().getCardType().equals(CardType.RITUAL)) {
                if (isRitualSummon()) {
                    currentDuel.activateEffects();
                    PrintResponses.printSuccessfulSpellActivation();
                }
            } else {
                currentDuel.activateEffects();
                PrintResponses.printSuccessfulSpellActivation();
            }
        }
    }

    private void showGraveyard(Matcher matcher) {
        isInGame = false;
        PrintResponses.showGraveyard(currentDuel.showGraveyard());
    }

    private void back() {
        isInGame = true;
    }

    private void showSelectedCard(Matcher matcher) {
        PrintResponses.printSelectedCard(currentDuel.showSelectedCard());
    }

    private void surrender(Matcher matcher) {
        PrintResponses.printEndingTheGame(currentDuel.surrender());
    }


    private boolean isRitualSummon() {
        if (!currentDuel.getUserWhoPlaysNow().getBoard().isThereAnyCardWithGivenTypeInMonsters(CardType.RITUAL)
                || currentDuel.getUserWhoPlaysNow().getBoard()
                .isThereASubsetOfMonstersWithSumOfLevelsGreaterThanGivenLevel(currentDuel.getUserWhoPlaysNow().getHand().getMinLevelOfRitualMonstersInHand())) {
            PrintResponses.printUnableToRitualSummonMonster();
            return false;
        }
        handleSelectionForRitualSummon();
        handleSummonForRitualSummon();
        return true;
    }


    private boolean isNotEnoughCardsForTribute(int requiredCardsAmount) {
        if (currentDuel.getUserWhoPlaysNow().getBoard().getMonsters().length == requiredCardsAmount) {
            PrintResponses.printNoCardToTribute();
            return true;
        }
        return false;
    }

    private boolean areCardAddressesEmpty(int[] cardsAddresses) {
        for (int toTributeAddress : cardsAddresses) {
            if (currentDuel.getUserWhoPlaysNow().getBoard().getCard(toTributeAddress, 'M') == null) {
                PrintResponses.printNoMonsterOnAddress();
                return true;
            }
        }
        return false;
    }

    private boolean isPositionTheSameAsBefore(String position) {
        if (position.equals("attack")) {
            return currentDuel.getSelectedCard().isFaceUp();
        } else {
            return !currentDuel.getSelectedCard().isATK() && !currentDuel.getSelectedCard().isFaceUp();
        }
    }

    private boolean isNotInMainPhases() {
        return !(phase.equals(Phases.MAIN_PHASE1) || phase.equals(Phases.MAIN_PHASE2));
    }

    private boolean isSelectedCardNotInMonsterZone() {
        return !currentDuel.getUserWhoPlaysNow().getBoard().isCardOnMonsterZone(currentDuel.getSelectedCard());
    }

    private boolean isNotInBattlePhase() {
        return !phase.equals(Phases.BATTLE_PHASE);
    }

    private boolean isSpellZoneFullAndNeedsToBeOnBoard() {
        //TODO implement if the card **needs** to be on board
        return currentDuel.getUserWhoPlaysNow().getBoard().getAddressToPutSpell() == 0;
    }

    //TODO implement this method
    private boolean isSpellPreparedToBeActivated() {
        return true;
    }


    private void handleSuccessfulGameCreation(User secondPlayer) {
        currentDuel = new Duel(ProgramController.userInGame, secondPlayer);
        phase = Phases.DRAW_PHASE;
        if (isFirstRound) {
            PrintResponses.printGameSuccessfullyCreated();
            isFirstRound = false;
            currentDuel.getSECOND_USER().setWinsInAMatch(0);
            currentDuel.getFIRST_USER().setWinsInAMatch(0);
        } else {
            PrintResponses.printRoundNumber(remainingRounds);
        }
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        handleDrawingACard(currentDuel.drawACard());
    }

    private void handleTransitionFromEndPhaseToDrawPhase() {
        phase = Phases.END_PHASE;
        PrintResponses.printPhaseName(phase);
        currentDuel.changeTurn();
        currentDuel.setNumberOfTurnsPlayedUpToNow(currentDuel.getNumberOfTurnsPlayedUpToNow() + 1);
        PrintResponses.showTurn(currentDuel.getUserWhoPlaysNow());
        phase = Phases.DRAW_PHASE;
        PrintResponses.printPhaseName(phase);
    }

    private void handleDrawingACard(Card card) {
        if (card == null) {
            endTheGame();
        } else {
            PrintResponses.printDrawnCard(card);
            PrintResponses.printBoard(currentDuel);
        }
    }

    private void handleSuccessfulAttack(int address, Monster monsterToAttack) {
        Pair<Integer, Integer> pair = currentDuel.attack(address);
        int key = pair.getKey();
        if (key > 6) {
            key -= 3;
            PrintResponses.printCardNameInAttackIfIsDefenceHide(monsterToAttack.getName());
        }
        switch (key) {
            case 0:
                //print sth if you want!
                break;
            case 1:
                PrintResponses.printOpponentMonsterDestroyedWithDamage(pair.getValue());
                break;
            case 2:
                PrintResponses.printBothCardsDestroyedInAttack();
                break;
            case 3:
                PrintResponses.printOwnMonsterDestroyedInAttackWithDamage(pair.getValue());
                break;
            case 4:
                PrintResponses.printOpponentMonsterInDefenceDestroyed();
            case 5:
                PrintResponses.printNoCardDestroyedInDefence();
                break;
            case 6:
                PrintResponses.printNoCardDestroyedButReceivedDamage(pair.getValue());
                break;
        }
    }

    private void handleSelectionForRitualSummon() {
        while (true) {
            Matcher matcher = Regex.getMatcher(ProgramController.scanner.nextLine(), Regex.selectFromOwn);
            if (matcher.find()) {
                selectCardFromOwn(Regex.getMatcher(ProgramController.scanner.nextLine(), Regex.selectFromOwn));
                break;
            } else {
                PrintResponses.printEmergencyRitualSummon();
            }
        }
    }

    private void handleSummonForRitualSummon() {
        while (true) {
            Matcher matcher = Regex.getMatcher(ProgramController.scanner.nextLine(), Regex.summon);
            if (matcher.find()) {
                while (true) {
                    int[] cardAddresses = Arrays.stream(ProgramController.scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
                    if (currentDuel.getUserWhoPlaysNow().getBoard().areGivenCardsEnoughForRitualSummon(cardAddresses, currentDuel.getSelectedCard())) {
                        currentDuel.tribute(cardAddresses);
                        break;
                    } else {
                        PrintResponses.printInequalityOfLevelsOfSelectedAndRitualMonster();
                    }
                }
                boolean isAttacking;
                while (true) {
                    String input = ProgramController.scanner.nextLine();
                    if (input.equals("attacking")) {
                        isAttacking = true;
                        break;
                    } else if (input.equals("defensive")) {
                        isAttacking = false;
                        break;
                    } else {
                        PrintResponses.printInvalidFormat();
                    }
                }
                if (isAttacking)
                    summon(Regex.getMatcher(ProgramController.scanner.nextLine(), Regex.summon));
                else {
                    currentDuel.setMonster();
                    currentDuel.changeToDefensePosition();
                    PrintResponses.printSuccessfulSummon();
                }
                break;
            } else {
                PrintResponses.printEmergencyRitualSummon();
            }
        }
    }

    private void handleMonsterSet() {
        if (currentDuel.getUserWhoPlaysNow().getBoard().getAddressToSummon() == 0) {
            PrintResponses.printFullnessOfMonsterCardZone();
        } else if (currentDuel.isHasSummonedOrSetOnce()) {
            PrintResponses.printUnableToSummonInTurn();
        } else {
            currentDuel.setMonster();
            PrintResponses.printSuccessfulCardSetting();
        }
    }

    private void handleSpellAndTrapSet() {
        if (currentDuel.getUserWhoPlaysNow().getBoard().getAddressToPutSpell() == 0) {
            PrintResponses.printFullnessOfSpellCardZone();
        } else if (currentDuel.isHasSummonedOrSetOnce()) {
            PrintResponses.printUnableToSummonInTurn();
        } else {
            currentDuel.setSpellOrTrap();
            PrintResponses.printSuccessfulCardSetting();
        }
    }


}
