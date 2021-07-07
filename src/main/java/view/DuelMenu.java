package view;

import controller.DataController;
import controller.Effects.SelectEffect;
import controller.Effects.StandByEffects;
import controller.ProgramController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.AI;
import model.Duel;
import model.User;
import model.card.Card;
import model.card.Monster;
import model.card.Spell;
import model.card.enums.CardType;
import org.apache.commons.math3.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
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
    private int remainingRounds;
    private int initialRounds;
    private boolean isFirstRound = true;

    private final Delta delta = new Delta();
    private final Stage firstUserStage = new Stage();
    private final Stage secondUserStage = new Stage();


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


    private HashMap<String, Consumer<Matcher>> createCommandMap() {
        HashMap<String, Consumer<Matcher>> commandMap = new HashMap<>();
        commandMap.put(Regex.selectFromOwnField, this::selectCardFromOwnField);
        commandMap.put(Regex.selectFromOpponent, this::selectCardFromOpponent);
        commandMap.put(Regex.selectFromOpponentField, this::selectCardFromOpponentField);
        commandMap.put(Regex.selectFromOpponentField2, this::selectCardFromOpponentField);
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

    private void createNewDuel(String otherPlayerUsername, int remainingRounds) {
        if (currentDuel != null) {
            PrintResponses.print(Responses.alreadyInGame);
            return;
        }
        User secondPlayer = User.getUserByUsername(otherPlayerUsername);
        initialRounds = remainingRounds;
        if (ProgramController.userInGame.getActiveDeck() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, PrintResponses.printHasNoActiveDeck(ProgramController.userInGame));
            alert.showAndWait();
        } else if (secondPlayer.getActiveDeck() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, PrintResponses.printHasNoActiveDeck(secondPlayer));
            alert.showAndWait();
        } else if (!ProgramController.userInGame.getActiveDeck().isValid()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, PrintResponses.printInvalidDeck(ProgramController.userInGame));
            alert.showAndWait();
        } else if (!secondPlayer.getActiveDeck().isValid()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, PrintResponses.printInvalidDeck(secondPlayer));
            alert.showAndWait();
        } else {
            showCoinFlipping(ProgramController.userInGame, secondPlayer); //todo return a Pair that includes first and second user (ordered)
        }
    }

    private void createNewDuelWithAI(int remainingRounds) {
        initialRounds = remainingRounds;
        if (ProgramController.userInGame.getActiveDeck() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, PrintResponses.printHasNoActiveDeck(ProgramController.userInGame));
            alert.showAndWait();
        } else if (!ProgramController.userInGame.getActiveDeck().isValid()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, PrintResponses.printInvalidDeck(ProgramController.userInGame));
            alert.showAndWait();
        } else {
            handleSuccessfulGameCreation(ProgramController.userInGame, new AI("AI", "AI", "AI"));
            ((AI) currentDuel.getSECOND_USER()).setCurrentDuel(currentDuel);
        }
    }

    private void selectCardFromOwn(int cardAddress, String whereToSelectFrom) {
        currentDuel.selectCard(cardAddress, whereToSelectFrom, "own");
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

    private void deselectCard() {
        if (currentDuel.isNoCardSelected()) {
            PrintResponses.printNoCardSelected();
        } else {
            currentDuel.deselectACard();
            PrintResponses.printSuccessfulCardDeselection();
        }
    }

    private void goToNextPhase() {
        if (phase.equals(Phases.DRAW_PHASE)) {
            phase = Phases.STANDBY_PHASE;
        } else if (phase.equals(Phases.STANDBY_PHASE)) {
            StandByEffects.run(currentDuel.getRival(), currentDuel, currentDuel.getUserWhoPlaysNow());
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
                handleSuccessfulGameCreation(ProgramController.userInGame, currentDuel.getSECOND_USER());
        }
    }

    private void summon() {
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
                if (number == 0 && monster.getDiscardToSpecialSummon().hasEffect() &&
                        currentDuel.getUserWhoPlaysNow().getHand().getNumberOfCardsInHand() >=
                                monster.getDiscardToSpecialSummon().getEffectNumber()) {
                    for (int j = 0; j < monster.getDiscardToSpecialSummon().getEffectNumber(); j++) {
                        Card card = currentDuel.getUserWhoPlaysNow().getHand().selectARandomCardFromHand();
                        int i;
                        for (i = 0; i < currentDuel.getUserWhoPlaysNow().getHand().getCardsInHand().length; i++) {
                            if (card == currentDuel.getUserWhoPlaysNow().getHand().getCardsInHand()[i])
                                break;
                        }
                        currentDuel.getUserWhoPlaysNow().getHand().discardACard(i);
                    }
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

    private void set() {
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
        } else if (!currentDuel.getUserWhoPlaysNow().isCanAttack()) {
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
        } else if (currentDuel.getUserWhoPlaysNow().isCanNotSetSpell()) {
            PrintResponses.printDisabledSummonSpell();
        } else {
            if (((Spell) currentDuel.getSelectedCard()).getSpellTrapIcon().getName().equals("Ritual")) {
                isRitualSummon();
                return;
            }
            currentDuel.activateEffects();
            PrintResponses.printSuccessfulSpellActivation();
            PrintResponses.print(currentDuel);
        }
    }

    private void showGraveyard(Matcher matcher) {
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
            if (card1 == null) {
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

    private void isRitualSummon() {
        if (!currentDuel.getUserWhoPlaysNow().getHand().isThereAnyCardWithGivenTypeInMonsters(CardType.RITUAL)
                || currentDuel.getUserWhoPlaysNow().getBoard()
                .isThereASubsetOfMonstersWithSumOfLevelsGreaterThanGivenLevel(currentDuel.getUserWhoPlaysNow().getHand().getMinLevelOfRitualMonstersInHand())) {
            PrintResponses.printUnableToRitualSummonMonster();
            return;
        }
        handleSelectionForRitualSummon();
        handleSummonForRitualSummon();
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
            if (spell.getEquipBasedOnPosition().hasEffect()) {
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
            if (monster != null && ((Monster) monster).getMonsterType().getName().toLowerCase().equals(type))
                return true;
        }
        return false;
    }


    private void handleSuccessfulSummon() {
        currentDuel.summonMonster();
        PrintResponses.printSuccessfulSummon();
        PrintResponses.print(currentDuel);
    }

    private void handleSuccessfulGameCreation(User firstPlayer, User secondPlayer) {
        //todo handle starting player.
        currentDuel = new Duel(firstPlayer, secondPlayer);
        showLabelsForBothUsers(firstPlayer, secondPlayer);
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
        reloadHands();
        reloadPhaseLabels();
    }


    private void handleTransitionFromEndPhaseToDrawPhase() {
        phase = Phases.END_PHASE;
        PrintResponses.printPhaseName(phase);
        currentDuel.changeTurn();
        currentDuel.setNumberOfTurnsPlayedUpToNow(currentDuel.getNumberOfTurnsPlayedUpToNow() + 1);
        if (firstUserStage.getScene().getRoot().getEffect() == null) {
            firstUserStage.getScene().getRoot().setEffect(new GaussianBlur());
            secondUserStage.getScene().getRoot().setEffect(null);
        } else {
            secondUserStage.getScene().getRoot().setEffect(new GaussianBlur());
            firstUserStage.getScene().getRoot().setEffect(null);
        }
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
            reloadCardsLeftInDeck();
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
                PrintResponses.print(Responses.canNotAttackDueToEffect);
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
//                selectCardFromOwn(matcher);
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
        if (isAttacking) {
            currentDuel.summonMonster();
            PrintResponses.printSuccessfulSummon();
        } else {
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

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @SuppressWarnings("rawtypes")
    @Override
    public void showMenu() throws IOException {
        ProgramController.createNewScene(getClass().getResource("/FXMLs/DuelMenu.fxml"));
        ChoiceBox choiceBox = (ChoiceBox) ProgramController.currentScene.lookup("#playerChoiceBox");
        TextField userTextField = (TextField) ProgramController.currentScene.lookup("#usernameTextField");
        choiceBox.getSelectionModel().selectedIndexProperty().addListener((ov, value, new_value) -> {
            userTextField.clear();
            userTextField.setDisable(new_value.intValue() == 1);
        });
    }

    @SuppressWarnings("rawtypes")
    public void startNewGame() {
        ChoiceBox userChoiceBox = (ChoiceBox) ProgramController.currentScene.lookup("#playerChoiceBox"),
                roundChoiceBox = (ChoiceBox) ProgramController.currentScene.lookup("#roundChoiceBox");
        TextField userTextField = (TextField) ProgramController.currentScene.lookup("#usernameTextField");
        String userOrAI = (String) userChoiceBox.getValue();
        userTextField.focusedProperty().addListener((obs, oldValue, newValue) -> userTextField.setStyle("-fx-text-fill: black"));
        if (userOrAI.equals("Another Player") && isTextFieldInvalid(userTextField.getText())) {
            userTextField.setStyle("-fx-text-fill: rgb(250, 0, 0);");
            //todo show an error message if you want
            return;
        }
        String rounds = (String) roundChoiceBox.getValue();
        if (userTextField.getText().isEmpty()) {
            createNewDuelWithAI(Integer.parseInt(rounds.replaceAll("\\D+", "")));
        } else {
            createNewDuel(userTextField.getText(), Integer.parseInt(rounds.replaceAll("\\D+", "")));
        }
    }

    private void showCoinFlipping(User starter, User invited) {
        VBox mainVBox = (VBox) ProgramController.currentScene.lookup("#mainVBox");
        mainVBox.getChildren().clear();
        Button okayButton = new Button("Okay");
        okayButton.setOnMouseClicked(event -> {
            handleCreatingTwoStages();
            handleSuccessfulGameCreation(starter, invited);
        });
        mainVBox.getChildren().add(okayButton);
        //todo A private field to store starting user (or returns the user)
    }

    public void goToMainMenu() throws IOException {
        ProgramController.currentMenu = new MainMenu();
        ProgramController.createNewScene(getClass().getResource("/FXMLs/mainMenu.fxml"));
        ProgramController.stage.show();
    }

    public void handleCreatingTwoStages() {
        ProgramController.stage.close();
        createStageForUser(firstUserStage);
        createStageForUser(secondUserStage);
        firstUserStage.setX(300);
        secondUserStage.setX(900);
        secondUserStage.show();
        firstUserStage.show();
        secondUserStage.getScene().getRoot().setEffect(new GaussianBlur());
        firstUserStage.getScene().lookup("#nextPhaseButton").setOnMouseClicked(event -> {
            goToNextPhase();
            reloadPhaseLabels();
        });
        secondUserStage.getScene().lookup("#nextPhaseButton").setOnMouseClicked(event -> {
            goToNextPhase();
            reloadPhaseLabels();
        });
    }


    private void createStageForUser(Stage stage) {
        try {
            Parent pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXMLs/DuelBoard.fxml")));
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setY(200);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isTextFieldInvalid(String username) {
        if (username.isEmpty() || username.isBlank())
            return true;
        return Objects.requireNonNull(DataController.getAllUsers()).stream()
                .noneMatch(user -> user.getUsername().equals(username) && !user.getUsername().equals(ProgramController.userInGame.getUsername()));
    }

    private void showLabelsForBothUsers(User firstUser, User secondUser) {
        ((Label) firstUserStage.getScene().lookup("#ownUsername")).setText(firstUser.getUsername());
        ((Label) firstUserStage.getScene().lookup("#ownNickname")).setText(firstUser.getNickname());
        ((Label) firstUserStage.getScene().lookup("#ownLP")).setText(String.valueOf(firstUser.getLifePoints()));
        ((Label) firstUserStage.getScene().lookup("#rivalUsername")).setText(secondUser.getUsername());
        ((Label) firstUserStage.getScene().lookup("#rivalNickname")).setText(secondUser.getNickname());
        ((Label) firstUserStage.getScene().lookup("#rivalLP")).setText(String.valueOf(secondUser.getLifePoints()));
        ((Label) firstUserStage.getScene().lookup("#ownUsername")).setText(firstUser.getUsername());
        ((Label) secondUserStage.getScene().lookup("#ownUsername")).setText(secondUser.getUsername());
        ((Label) secondUserStage.getScene().lookup("#ownNickname")).setText(secondUser.getNickname());
        ((Label) secondUserStage.getScene().lookup("#ownLP")).setText(String.valueOf(secondUser.getLifePoints()));
        ((Label) secondUserStage.getScene().lookup("#rivalUsername")).setText(firstUser.getUsername());
        ((Label) secondUserStage.getScene().lookup("#rivalNickname")).setText(firstUser.getNickname());
        ((Label) secondUserStage.getScene().lookup("#rivalLP")).setText(String.valueOf(firstUser.getLifePoints()));
    }

    @SuppressWarnings("rawtypes")
    private void reloadHands() {
        ((ListView) firstUserStage.getScene().lookup("#rivalHandListView")).getItems().clear();
        ((ListView) firstUserStage.getScene().lookup("#rivalHandListView")).getItems().addAll(getHandCardPictures(currentDuel.getSECOND_USER(), "hide"));
        ((ListView) firstUserStage.getScene().lookup("#ownHandListView")).getItems().clear();
        ((ListView) firstUserStage.getScene().lookup("#ownHandListView")).getItems().addAll(getHandCardPictures(currentDuel.getFIRST_USER(), "visible"));
        ((ListView) secondUserStage.getScene().lookup("#rivalHandListView")).getItems().clear();
        ((ListView) secondUserStage.getScene().lookup("#rivalHandListView")).getItems().addAll(getHandCardPictures(currentDuel.getFIRST_USER(), "hide"));
        ((ListView) secondUserStage.getScene().lookup("#ownHandListView")).getItems().clear();
        ((ListView) secondUserStage.getScene().lookup("#ownHandListView")).getItems().addAll(getHandCardPictures(currentDuel.getSECOND_USER(), "visible"));
        loadDeckPictures();
        reloadCardsLeftInDeck();
    }

    private void loadDeckPictures() {
        ((Rectangle) firstUserStage.getScene().lookup("#ownDeck"))
                .setFill(new ImagePattern(new Image(Objects.requireNonNull(getClass().getResource("/images/deck.png")).toExternalForm())));
        ((Rectangle) firstUserStage.getScene().lookup("#rivalDeck"))
                .setFill(new ImagePattern(new Image(Objects.requireNonNull(getClass().getResource("/images/deck.png")).toExternalForm())));
        ((Rectangle) secondUserStage.getScene().lookup("#ownDeck"))
                .setFill(new ImagePattern(new Image(Objects.requireNonNull(getClass().getResource("/images/deck.png")).toExternalForm())));
        ((Rectangle) secondUserStage.getScene().lookup("#rivalDeck"))
                .setFill(new ImagePattern(new Image(Objects.requireNonNull(getClass().getResource("/images/deck.png")).toExternalForm())));
    }

    private void reloadCardsLeftInDeck() {
        ((Label) firstUserStage.getScene().lookup("#numberOfOwnDeckCards"))
                .setText(String.valueOf(currentDuel.getFIRST_USER().getHand().getNumberOfRemainingCardsInDeck()));
        ((Label) firstUserStage.getScene().lookup("#numberOfRivalDeckCards"))
                .setText(String.valueOf(currentDuel.getSECOND_USER().getHand().getNumberOfRemainingCardsInDeck()));
        ((Label) secondUserStage.getScene().lookup("#numberOfOwnDeckCards"))
                .setText(String.valueOf(currentDuel.getSECOND_USER().getHand().getNumberOfRemainingCardsInDeck()));
        ((Label) secondUserStage.getScene().lookup("#numberOfRivalDeckCards"))
                .setText(String.valueOf(currentDuel.getFIRST_USER().getHand().getNumberOfRemainingCardsInDeck()));
    }

    private void reloadPhaseLabels() {
        ((Label) firstUserStage.getScene().lookup("#phaseLabel")).setText(phase.getName());
        ((Label) secondUserStage.getScene().lookup("#phaseLabel")).setText(phase.getName());
    }

    private void reloadCardsOnBoard() {
        showMonstersForFirstStage();
        showMonstersForSecondStage();
    }

    private void showMonstersForFirstStage() {
        HBox ownMonsters = (HBox) firstUserStage.getScene().lookup("#ownMonsterHBox");
        HBox rivalMonster = (HBox) firstUserStage.getScene().lookup("#rivalMonsterHBox");
        showMonsterZone(ownMonsters, currentDuel.getFIRST_USER().getBoard().getMonsters());
        showMonsterZone(rivalMonster, currentDuel.getSECOND_USER().getBoard().getMonsters());
    }

    private void showMonstersForSecondStage() {
        HBox ownMonsters = (HBox) secondUserStage.getScene().lookup("#ownMonsterHBox");
        HBox rivalMonster = (HBox) secondUserStage.getScene().lookup("#rivalMonsterHBox");
        showMonsterZone(ownMonsters, currentDuel.getSECOND_USER().getBoard().getMonsters());
        showMonsterZone(rivalMonster, currentDuel.getFIRST_USER().getBoard().getMonsters());
    }

    private void showMonsterZone(HBox ownMonsters, Card[] monsters) {
        for (int i = 0; i < monsters.length; i++) {
            Card monster = monsters[i];
            if (monster == null)
                continue;
            Rectangle monsterCardOnBoard = ((Rectangle) ownMonsters.getChildren().get(convertNormalAddressToBoardAddress(i)));
            if (monster.isFaceUp()) {
                monsterCardOnBoard.setFill(new ImagePattern(new Image(monster.getCardImageAddress())));
                if (!monster.isATK()) {
                    //todo rotate the image itself to show set card
                    monsterCardOnBoard.setRotate(50);
                }
                // todo show hide cards in both attack and defence
            }
        }
    }

    // todo implement this method like the previous one, but for spells and traps.
    private void showSpellZone() {
    }

    private ArrayList<Rectangle> getHandCardPictures(User user, String showMode) {
        ArrayList<Rectangle> handCardPictures = new ArrayList<>();
        Card[] cardsInHand = user.getHand().getCardsInHand();
        for (int i = 0; i < cardsInHand.length; i++) {
            Card card = cardsInHand[i];
            if (card == null)
                continue;
            Rectangle cardPicture = new Rectangle(90, 100);
            if (showMode.equals("hide"))
                cardPicture.setFill(new ImagePattern(new Image(card.getBackPictureAddress())));
            else
                cardPicture.setFill(new ImagePattern(new Image(card.getCardImageAddress())));
            handCardPictures.add(cardPicture);

            dragNode(cardPicture, i);
        }
        return handCardPictures;
    }

    private int convertNormalAddressToBoardAddress(int index) {
        switch (index) {
            case 0:
                return 3;
            case 1:
                return 4;
            case 2:
                return 2;
            case 3:
                return 5;
            case 4:
                return 1;
            default:
                return 0;
        }
    }


    // Handles all dragging EventHandlers for any cardPicture object
    public void dragNode(Rectangle cardPicture, int index) {
        // Custom object to hold x and y positions

        cardPicture.setOnMousePressed(mouseEvent -> {
            Stage stage = (firstUserStage.getScene().getRoot().getEffect() == null) ? firstUserStage : secondUserStage;
            delta.x = mouseEvent.getX();
            delta.y = mouseEvent.getY();
            cardPicture.setCursor(Cursor.CLOSED_HAND);
            if (!((AnchorPane) stage.getScene().getRoot()).getChildren().contains(cardPicture)) {
                cardPicture.setTranslateX(mouseEvent.getSceneX() - 40);
                cardPicture.setTranslateY(mouseEvent.getSceneY() - 40);
                ((AnchorPane) stage.getScene().getRoot()).getChildren().add(cardPicture);
            }
            delta.x = cardPicture.getLayoutX() - mouseEvent.getSceneX();
            delta.y = cardPicture.getLayoutY() - mouseEvent.getSceneY();
            selectCardFromOwn(index + 1, "hand");
        });

        cardPicture.setOnMouseReleased(mouseEvent -> {
            Stage stage = (firstUserStage.getScene().getRoot().getEffect() == null) ? firstUserStage : secondUserStage;
            cardPicture.setCursor(Cursor.OPEN_HAND);
            HBox ownMonsters = (HBox) stage.getScene().lookup("#ownMonsterHBox");
            System.out.println(mouseEvent.getTarget().equals(mouseEvent.getSource()));
            if (ownMonsters.contains(mouseEvent.getX(), mouseEvent.getY())) {
                addCardToBoard(mouseEvent);
                ((AnchorPane) stage.getScene().getRoot()).getChildren().remove(cardPicture);
            }
        });

        cardPicture.setOnMouseDragged(mouseEvent -> {
            cardPicture.setCursor(Cursor.CLOSED_HAND);
            cardPicture.setLayoutX(mouseEvent.getSceneX() + delta.x);
            cardPicture.setLayoutY(mouseEvent.getSceneY() + delta.y);
        });

        cardPicture.setOnMouseEntered(event -> cardPicture.setCursor(Cursor.HAND));
    }


    public void addCardToBoard(MouseEvent mouseEvent) {
        if (currentDuel.getSelectedCard() != null) {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY))
                summon();
            else
                set();
            reloadCardsOnBoard();
            reloadHands();
        }
    }

    public void showSettings() {
        //todo implement settings to display pause, surrender, sound off
    }
}

class Delta {
    double x, y;
}



