package controller.menu;

import controller.Effects.SelectEffect;
import controller.ProgramController;
import module.AI;
import module.Duel;
import module.User;
import module.card.Card;
import module.card.Monster;
import module.card.Spell;
import module.card.enums.CardType;
import org.apache.commons.math3.util.Pair;
import view.PrintResponses;
import view.Regex;
import view.Responses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.regex.Matcher;

public class DuelMenu implements Menuable {
    public static ArrayList<Monster> specialSummonsedCards;
    public static boolean isForScan;
    public static boolean isGetFromGY;
    public static boolean isGetFromDeck;
    public static boolean isGetFromHand;
    public static boolean addToHand;
    public static boolean isForSet;
    public static boolean isGetFroOpponentGY;
    private Duel currentDuel;
    private Phases phase;
    private boolean isInGame;
    private int remainingRounds;
    private int initialRounds;
    private boolean isFirstRound = true;

    {
        isInGame = true;
    }

    public static Monster getMonsterForEquip(Duel duel, Spell spell) {
        PrintResponses.printChooseEquip();
        int number;
        while (true) {
            try {
                number = Integer.parseInt(ProgramController.scanner.nextLine());
                if (number > 5 || number < 1) {
                    PrintResponses.printWrongChoice();
                    continue;
                }
                if (duel.getUserWhoPlaysNow().getBoard().getCard(number, 'm') == null) continue;
            } catch (Exception e) {
                PrintResponses.printWrongChoice();
                continue;
            }
            break;
        }
        spell.setEquippedPlace(number);
        return (Monster) duel.getUserWhoPlaysNow().getBoard().getCard(number, 'm');
    }

    public static boolean checkSpecialSummon(String command, Duel currentDuel, boolean isInRivalTurn) {
        Matcher matcher;
        if (specialSummonsedCards != null) {
            Card card = currentDuel.getSelectedCard();
            if (!(matcher = Regex.getMatcher(command, Regex.specialSummon)).matches()) {
                PrintResponses.printEmergencySpecialSummon();
                return true;
            }
            if (specialSummonsedCards.size() == 0) {
                PrintResponses.printUnableToSpecialSummonMonster();
                specialSummonsedCards = null;
                return true;
            }
            int number = Integer.parseInt(matcher.group("cardNumber"));
            if (number < 0 || number >= specialSummonsedCards.size()) {
                PrintResponses.printWrongChoice();
                return true;
            }
            Monster monster = specialSummonsedCards.get(number);
            if (isForScan) {
                if (SelectEffect.scannerHolder.isATK())
                    currentDuel.getUserWhoPlaysNow().getBoard().addMonsterFaceUp(SelectEffect.scannerPlace, monster);
                else currentDuel.getUserWhoPlaysNow().getBoard().addMonsterFaceDown(SelectEffect.scannerPlace, monster);
                isForScan = false;
            } else if (addToHand) {
                currentDuel.getUserWhoPlaysNow().getHand().addCardToHand(monster);
                addToHand = false;
                specialSummonsedCards = null;
            } else if (isForSet) {
                Card temp = currentDuel.getSelectedCard();
                int tempPlace = currentDuel.getPlaceOfSelectedCard();
                currentDuel.setSelectedCard(monster);
                if (currentDuel.getUserWhoPlaysNow().getBoard().getAddressToSummon() == 0) {
                    PrintResponses.printUnableToSpecialSummonMonster();
                    specialSummonsedCards = null;
                    return true;
                }
                removeTheSpecialSummoned(monster, currentDuel);
                int place = currentDuel.getUserWhoPlaysNow().getBoard().getAddressToSummon();
                currentDuel.setMonster();
                currentDuel.flipSetForMonsters(place);
                currentDuel.setSelectedCard(temp);
                currentDuel.setPlaceOfSelectedCard(tempPlace);
                System.out.println(currentDuel.getSelectedCard());
            } else {
                currentDuel.setSelectedCard(monster);
                if (currentDuel.getUserWhoPlaysNow().getBoard().getAddressToSummon() == 0) {
                    PrintResponses.printUnableToSpecialSummonMonster();
                    return true;
                }
                removeTheSpecialSummoned(monster, currentDuel);
                if (isInRivalTurn) changeTurnTemp(currentDuel);
                currentDuel.summonMonster();
                if (isInRivalTurn) changeTurnTemp(currentDuel);
            }
            specialSummonsedCards = null;
            currentDuel.setSelectedCard(card);
            return true;
        }
        return false;
    }

    private static void changeTurnTemp(Duel currentDuel) {
        currentDuel.setUserWhoPlaysNow(currentDuel.getRival());

    }


    public static void removeTheSpecialSummoned(Monster monster, Duel currentDuel) {
        boolean found = false;
        if (isGetFromGY) {
            for (Card card : currentDuel.getUserWhoPlaysNow().getGraveyard()) {
                if (card.getName().equals(monster.getName())) {
                    currentDuel.getUserWhoPlaysNow().getBoard().removeFromGY(card.getName());
                    found = true;
                    break;
                }
            }
        }
        if (isGetFromDeck && !found) {
            for (Card mainDeckCard : currentDuel.getUserWhoPlaysNow().getHand().getDeckToDraw().getMainDeckCards()) {
                if (mainDeckCard.getName().equals(monster.getName())) {
                    currentDuel.getUserWhoPlaysNow().getHand().removeFromDeck(monster.getName());
                    found = true;
                    break;
                }
            }
        }
        if (isGetFromHand && !found) {
            Card[] cards = currentDuel.getUserWhoPlaysNow().getHand().getCardsInHand();
            for (int i = 0; i < cards.length; i++) {
                if (cards[i] == null) continue;
                if (cards[i].getName().equals(monster.getName())) {
                    currentDuel.getUserWhoPlaysNow().getHand().removeCardFromHand(i + 1);
                    break;
                }
            }
        }
        isGetFromHand = false;
        isGetFromDeck = false;
        isGetFromGY = false;
    }

    public static String askForEffectMonster() {
        PrintResponses.printAskForEffectMonster();
        return ProgramController.scanner.nextLine();
    }

    @Override
    public void run(String command) {
        if (checkSpecialSummon(command, currentDuel, false)) return;
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
                    isValidCommand = true;
                    commandMap.get(string).accept(matcher);
                } else if (!isInGame) {
                    if (command.equals("back")) {
                        back();
                        isValidCommand = true;
                    }
                }
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        if (!isValidCommand) {
            PrintResponses.printInvalidFormat();
        }

    }

    private void back() {
        isInGame = true;
    }

    @Override
    public void showCurrentMenu() {
        PrintResponses.printDuelMenuShow();
    }

    @Override
    public void exitMenu() {
        ProgramController.currentMenu = new MainMenu();
    }

    private HashMap<String, Consumer<Matcher>> createCommandMap() {
        HashMap<String, Consumer<Matcher>> commandMap = new HashMap<>();
        commandMap.put(Regex.createNewDuel, this::createNewDuel);
        commandMap.put(Regex.createNewDuelWithAI, this::createNewDuelWithAI);
        commandMap.put(Regex.selectFromOwn, this::selectCardFromOwn);
        commandMap.put(Regex.selectFromOwnField, this::selectCardFromOwnField);
        commandMap.put(Regex.selectFromOpponent, this::selectCardFromOpponent);
        commandMap.put(Regex.selectFromOpponentField, this::selectCardFromOpponentField);
        commandMap.put(Regex.selectFromOpponentField2, this::selectCardFromOpponentField);
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
        commandMap.put(Regex.increaseLP, this::increaseLP);
        commandMap.put(Regex.setWinner, this::setWinner);
        commandMap.put(Regex.forceSelectHand, this::forceSelectHand);
        commandMap.put(Regex.showHand, this::showHand);
        commandMap.put(Regex.showACard, this::showCard);
        commandMap.put(Regex.addCardToHand, this::addCardToHand);
        return commandMap;
    }

    private void selectCardFromOpponentField(Matcher matcher) {
        currentDuel.selectCard(1, "field", "opponent");
        if (currentDuel.getSelectedCard() == null) {
            PrintResponses.printNoCardInPosition();
        } else {
            PrintResponses.printSuccessfulCardSelection();
        }
    }

    private void selectCardFromOwnField(Matcher matcher) {
        currentDuel.selectCard(1, "field", "own");
        if (currentDuel.getSelectedCard() == null) {
            PrintResponses.printNoCardInPosition();
        } else {
            PrintResponses.printSuccessfulCardSelection();
        }
    }

    private void showHand(Matcher matcher) {
        PrintResponses.printHandShow(currentDuel.getUserWhoPlaysNow().getHand());
    }

    private void createNewDuel(Matcher matcher) {
        if (currentDuel != null) {
            PrintResponses.print(Responses.alreadyInGame);
            return;
        }
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
        }
    }

    private void createNewDuelWithAI(Matcher matcher) {
        remainingRounds = Integer.parseInt(matcher.group("rounds"));
        initialRounds = remainingRounds;
        if (ProgramController.userInGame.getActiveDeck() == null) {
            PrintResponses.printHasNoActiveDeck(ProgramController.userInGame);
        } else if (!ProgramController.userInGame.getActiveDeck().isValid()) {
            PrintResponses.printInvalidDeck(ProgramController.userInGame);
        } else if (!(remainingRounds == 1 || remainingRounds == 3)) {
            PrintResponses.printNonSupportiveRound();
        } else {
            handleSuccessfulGameCreation(new AI("AI", "AI", "AI"));
            ((AI) currentDuel.getSECOND_USER()).setCurrentDuel(currentDuel);
        }
    }

    private void selectCardFromOwn(Matcher matcher) {
        String whereToSelectFrom = matcher.group("where");
        int cardAddress = Integer.parseInt(matcher.group("number"));
        if (!(whereToSelectFrom.equals("monster") || whereToSelectFrom.equals("spell")
                || whereToSelectFrom.equals("hand"))
                || cardAddress > 6) {
            PrintResponses.printInvalidSelection();
        } else if ((whereToSelectFrom.equals("monster") || whereToSelectFrom.equals("spell"))
                && cardAddress > 5) {
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
        if (whereToSelectFrom.equals("field")) {
            PrintResponses.printInvalidFormat();
            return;
        }
        int cardAddress = Integer.parseInt(matcher.group("number"));
        if (!(whereToSelectFrom.equals("monster") || whereToSelectFrom.equals("spell"))) {
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
            if (currentDuel.getNumberOfTurnsPlayedUpToNow() != 0) {
                phase = Phases.BATTLE_PHASE;
            } else {
                handleTransitionFromEndPhaseToDrawPhase();
                handleDrawingACard(currentDuel.drawACard(currentDuel.getUserWhoPlaysNow()));
                return;
            }
        } else if (phase.equals(Phases.BATTLE_PHASE)) {
            phase = Phases.MAIN_PHASE2;
        } else if (phase.equals(Phases.MAIN_PHASE2)) {
            handleTransitionFromEndPhaseToDrawPhase();
            handleDrawingACard(currentDuel.drawACard(currentDuel.getUserWhoPlaysNow()));
            return;
        }
        PrintResponses.printPhaseName(phase);
    }

    public void endTheGame() {
        remainingRounds--;
        if (remainingRounds == 0) {
            if (initialRounds == 1)
                PrintResponses.printEndingTheGame(currentDuel.handleEndingGame());
            else
                PrintResponses.printEndingTheWholeMatch(currentDuel.handleEndingTheWholeMatch());
            currentDuel = null;
        } else {
            User winner = currentDuel.handleEndingARound();
            PrintResponses.printWinnerInRound(winner);
            if (winner.getWinsInAMatch() == 2) {
                endTheGame();
            } else
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
        } else if (!currentDuel.getUserWhoPlaysNow().isCanSummonMonster()) {
            PrintResponses.printDisabledSummonMonster();
        } else if (((Monster) currentDuel.getSelectedCard()).getLevel() > 4) {
            Monster monster = (Monster) currentDuel.getSelectedCard();
            if (monster.isCanHaveDifferentTribute()) {
                PrintResponses.printChooseTribute();
                int number = Integer.parseInt(ProgramController.scanner.nextLine());
                if (number > 3 || number < 0) {
                    PrintResponses.printWrongTribute();
                    return;
                }
                if (number == 0 && monster.getCanBeNotTribute().hasEffect()) {
                    monster.setAtk(monster.getAtk() - monster.getCanBeNotTribute().getEffectNumber());
                    handleSuccessfulSummon();
                    return;
                }
                if (number == 0 && monster.getDiscardToSpecialSummon().hasEffect()) {
                    Card card = currentDuel.getUserWhoPlaysNow().getHand().selectARandomCardFromHand();
                    int i;
                    for (i = 0; i < currentDuel.getUserWhoPlaysNow().getHand().getCardsInHand().length; i++) {
                        if (card == currentDuel.getUserWhoPlaysNow().getHand().getCardsInHand()[i])
                            break;
                    }
                    currentDuel.getUserWhoPlaysNow().getHand().discardACard(i);
                    handleSuccessfulSummon();
                    return;
                }
                monster.setRequiredCardsFOrTribute(number);
            }
            if (monster.getLevel() < 7) {
                if (isNotEnoughCardsForTribute(monster.getRequiredCardsFOrTribute())) return;
            } else if (monster.getLevel() < 10) {
                if (isNotEnoughCardsForTribute(monster.getRequiredCardsFOrTribute())) return;
            } else {
                if (isNotEnoughCardsForTribute(monster.getRequiredCardsFOrTribute())) return;
            }
            int[] cardToTributeAddress = Arrays.stream(ProgramController.scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            if (areCardAddressesEmpty(cardToTributeAddress)) return;
            currentDuel.tribute(cardToTributeAddress);
            if (monster.getTributeToKillAllMonsterOfOpponent().hasEffect() && monster.getRequiredCardsFOrTribute() == monster.getTributeToKillAllMonsterOfOpponent().getEffectNumber()) {
                for (int i = 1; i < 6; i++) {
                    Card card = currentDuel.getRival().getBoard().getCard(i, 'm');
                    if (card != null) currentDuel.addCardToGraveyard(card, i, currentDuel.getRival());
                }
            }
            handleSuccessfulSummon();
        } else {
            handleSuccessfulSummon();
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

    private void showCard(Matcher matcher) {
        String cardName = matcher.group("cardName").trim();
        PrintResponses.printACard(Card.getCardByName(cardName));
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
            PrintResponses.print(currentDuel);
        }
    }

    private void flipSummon(Matcher matcher) {
        if (currentDuel.isNoCardSelected()) {
            PrintResponses.printNoCardSelected();
        } else if (isSelectedCardNotInMonsterZone()) {
            PrintResponses.printUnableToChangePositionOfCard();
        } else if (isNotInMainPhases()) {
            PrintResponses.printFlipSummonInWrongPhase();
        } else if (currentDuel.isSelectedCardSummonedInThisTurn() || (currentDuel.getSelectedCard().isFaceUp()
                || currentDuel.getSelectedCard().isATK())) {
            PrintResponses.printUnableToFlipSummonCard();
        } else {
            currentDuel.flipSummon();
            PrintResponses.printSuccessfulFlipSummon();
        }

    }

    private void attack(Matcher matcher) {
        int address = Integer.parseInt(matcher.group("number"));
        Monster monsterToAttack = (Monster) currentDuel.getRival().getBoard().
                getCard(address, 'M');
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
        } else if (!currentDuel.getUserWhoPlaysNow().isCanAttack()){
            PrintResponses.print(Responses.canNotAttackDueToEffect);
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
        } else if (currentDuel.getRival().getBoard().getMonsterNumber() != 0) {
            PrintResponses.print(Responses.unableToAttackDirectly);
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
        } else if (isSpellNotPreparedToBeActivated()) {
            PrintResponses.printUnfinishedPreparationOfSpell();
        } else if (!currentDuel.getUserWhoPlaysNow().isCanSetSpell()) {
            PrintResponses.printDisabledSummonSpell();
        } else {
            if (((Spell)currentDuel.getSelectedCard()).getSpellTrapIcon().getName().equals("Ritual") ){
                isRitualSummon();
                return;
            }
            currentDuel.activateEffects();
            PrintResponses.printSuccessfulSpellActivation();
            PrintResponses.print(currentDuel);
        }
    }

    private void showGraveyard(Matcher matcher) {
        isInGame = false;
        PrintResponses.showGraveyard(currentDuel.showGraveyard());
    }

    private void showSelectedCard(Matcher matcher) {
        PrintResponses.printSelectedCard(currentDuel.showSelectedCard());
    }

    private void surrender(Matcher matcher) {
        PrintResponses.printEndingTheGame(currentDuel.surrender(remainingRounds, initialRounds));
        currentDuel = null;
    }

    private void increaseLP(Matcher matcher) {
        int amount = Integer.parseInt(matcher.group("amount"));
        currentDuel.getUserWhoPlaysNow().setLifePoints(currentDuel.getUserWhoPlaysNow().getLifePoints() + amount);
        PrintResponses.print(Responses.increaseLP);
    }

    private void setWinner(Matcher matcher) {
        String nickname = matcher.group("nickname");
        User winner, loser;
        if (currentDuel.getUserWhoPlaysNow().getNickname().equals(nickname)) {
            winner = currentDuel.getUserWhoPlaysNow();
            loser = currentDuel.getRival();
        } else {
            winner = currentDuel.getRival();
            loser = currentDuel.getUserWhoPlaysNow();
        }
        if (initialRounds == 1)
            PrintResponses.printEndingTheGame(currentDuel.handleEndingOneRoundGames(winner, loser));
        else
            PrintResponses.printEndingTheWholeMatch(currentDuel.handleEndingThreeRoundGames(winner, loser));
        currentDuel = null;

    }

    // when we want to summon unconditionally.

    private void forceSelectHand(Matcher matcher) {
        String cardName = matcher.group("cardName").trim();
        Card[] cardsInHand = currentDuel.getUserWhoPlaysNow().getHand().getCardsInHand();
        int i = 0, cardsInHandLength = cardsInHand.length;
        while (true) {
            if (i >= cardsInHandLength) break;
            Card card1 = cardsInHand[i];
            if (card1 == null){
                PrintResponses.print(Responses.cardNotFoundInHand);
                return;
            }
            if (card1.getName().equals(cardName)) {
                currentDuel.setSelectedCard(card1);
                currentDuel.setPlaceOfSelectedCard(i + 1);
                break;
            }
            i++;
        }
        currentDuel.summonMonster();
        PrintResponses.print(Responses.forceSelectHand);
        PrintResponses.print(currentDuel);
    }

    private void addCardToHand(Matcher matcher) {
        Card card = Card.getCardByName(matcher.group("cardName"));
        if (currentDuel.getUserWhoPlaysNow().getHand().getNumberOfCardsInHand() != 6) {
            currentDuel.getUserWhoPlaysNow().getHand().addCardToHand(card);
            PrintResponses.print(Responses.addACardToHand);
            PrintResponses.print(currentDuel);
        } else
            PrintResponses.print(Responses.handIsFull);
    }

    private boolean isRitualSummon() {
        if (!currentDuel.getUserWhoPlaysNow().getHand().isThereAnyCardWithGivenTypeInMonsters(CardType.RITUAL)
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
            return currentDuel.getSelectedCard().isATK();
        } else {
            return !currentDuel.getSelectedCard().isATK();
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

    private boolean isSpellNotPreparedToBeActivated() {
        Spell spell = (Spell) currentDuel.getSelectedCard();
        return isGYEffectNotPrepared(spell) || isFieldZoneCardDrawNotPrepared(spell)
                || isCanAddFromDeckNotPrepared(spell)
                || isCanDestroyOrControlOpponentMonsterNotPrepared(spell)
                || isCanDestroyOpponentSpellAndTrapNotPrepared(spell)
                || isCanDestroyMyMonsterNotPrepared(spell)
                || isDiscardACardToActivateNotPrepared(spell)
                || isEquipCardNormalsAndEquipBasedMyUpMonstersNotPrepared(spell);

    }

    private boolean isGYEffectNotPrepared(Spell spell) {
        return spell.getCanSummonFromGY().hasEffect() &&
                (currentDuel.getUserWhoPlaysNow().getGraveyard().size() == 0 &&
                        currentDuel.getRival().getGraveyard().size() == 0);
    }

    private boolean isFieldZoneCardDrawNotPrepared(Spell spell) {
        if (spell.getCanAddFieldSpellFromDeck().hasEffect()) {
            boolean label = true;
            for (Card card : currentDuel.getUserWhoPlaysNow().getHand().getDeckToDraw().getMainDeckCards()) {
                if (card instanceof Spell && ((Spell) card).isFieldZone())
                    label = false;
            }
            return label;
        }
        return false;
    }

    private boolean isCanAddFromDeckNotPrepared(Spell spell) {
        return spell.getCanAddFromDeckToHand().hasEffect() && currentDuel.getUserWhoPlaysNow().getHand().getNumberOfCardsInHand() > 4
                && currentDuel.getUserWhoPlaysNow().getHand().getNumberOfRemainingCardsInDeck() < 2;
    }

    private boolean isCanDestroyOrControlOpponentMonsterNotPrepared(Spell spell) {
        return (spell.getCanControlOpponentMonster().hasEffect() || spell.getCanControlOpponentMonster().hasEffect()
                || spell.getEquipBasedMyUpMonsters().hasEffect())
                && currentDuel.getRival().getBoard().getMonsterNumber() == 0;
    }

    private boolean isCanDestroyOpponentSpellAndTrapNotPrepared(Spell spell) {
        return spell.getCanDestroyOpponentSpellAndTrap().hasEffect() && currentDuel.getUserWhoPlaysNow().getBoard().getSpellNumber() == 0;
    }

    private boolean isCanDestroyMyMonsterNotPrepared(Spell spell) {
        return spell.getCanDestroyMyMonster().hasEffect() && currentDuel.getRival().getBoard().getMonsterNumber() == 0
                && currentDuel.getUserWhoPlaysNow().getBoard().getMonsterNumber() == 0;
    }

    private boolean isDiscardACardToActivateNotPrepared(Spell spell) {
        return spell.getDiscardACardToActivate().hasEffect() && currentDuel.getUserWhoPlaysNow().getHand().getNumberOfCardsInHand() == 0
                && currentDuel.getRival().getBoard().getSpellNumber() == 0;
    }

    private boolean isEquipCardNormalsAndEquipBasedMyUpMonstersNotPrepared(Spell spell) {
        if (spell.getEquipCardNormal1().hasEffect() || spell.getEquipCardNormal2().hasEffect()
                || spell.getEquipCardNormal3().hasEffect()
                || spell.getEquipBasedOnPosition().hasEffect()) {
            if (spell.getEquipCardNormal1().hasEffect()) {
                String type = spell.getEquipCardNormal1().getType();
                if (checkForMonsterType(type))
                    return true;
            }
            if (spell.getEquipCardNormal2().hasEffect()) {
                String type = spell.getEquipCardNormal2().getType();
                if (checkForMonsterType(type))
                    return true;
            }
            if (spell.getEquipCardNormal3().hasEffect()) {
                String type = spell.getEquipCardNormal3().getType();
                if (checkForMonsterType(type))
                    return true;
            }
            if (spell.getEquipBasedOnPosition().hasEffect()){
                if (spell.getEquipCardNormal3().hasEffect()) {
                    String type = spell.getEquipCardNormal3().getType();
                    return checkForMonsterType(type);
                }
            }

        }
        return false;
    }

    private boolean checkForMonsterType(String type) {
        if (type.equals(""))
            return currentDuel.getUserWhoPlaysNow().getBoard().getMonsterNumber() == 0;
        for (Card monster : currentDuel.getUserWhoPlaysNow().getBoard().getMonsters()) {
            if (monster != null && ((Monster) monster).getMonsterType().getName().equals(type))
                return false;
        }
        return true;
    }


    private void handleSuccessfulSummon() {
        currentDuel.summonMonster();
        PrintResponses.printSuccessfulSummon();
        PrintResponses.print(currentDuel);
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
        handleDrawingACard(currentDuel.drawACard(currentDuel.getUserWhoPlaysNow()));
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
            if (currentDuel.getUserWhoPlaysNow() instanceof AI) {
                ((AI) currentDuel.getUserWhoPlaysNow()).run();
            }
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
                break;
            case 5:
                PrintResponses.printNoCardDestroyedInDefence();
                break;
            case 6:
                PrintResponses.printNoCardDestroyedButReceivedDamage(pair.getValue());
                break;
        }
    }

    private void handleSelectionForRitualSummon() {
        PrintResponses.printAskToRitualMonster();
        while (true) {
            String input = ProgramController.scanner.nextLine();
            Matcher matcher = Regex.getMatcher(input, Regex.selectFromOwn);
            if (matcher.find()) {
                selectCardFromOwn(matcher);
                if (currentDuel.getSelectedCard().getCardType().getName().equals("Ritual"))
                break;
                else
                    PrintResponses.printEmergencyRitualSummon();
            } else {
                PrintResponses.printEmergencyRitualSummon();
            }
        }
    }

    private void handleSummonForRitualSummon() {

                while (true) {
                    PrintResponses.printEnterTributeOrRitual();
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
                {
                    currentDuel.summonMonster();
                    PrintResponses.printSuccessfulSummon();
                }
                else {
                    currentDuel.setMonster();
                    PrintResponses.printSuccessfulCardSetting();
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
            PrintResponses.print(currentDuel);
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
            PrintResponses.print(currentDuel);
        }
    }

}

