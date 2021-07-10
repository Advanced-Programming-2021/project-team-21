package view;

import controller.DataController;
import controller.Effects.SelectEffect;
import controller.Effects.StandByEffects;
import controller.ProgramController;
import javafx.animation.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.AI;
import model.Duel;
import model.User;
import model.card.Card;
import model.card.Monster;
import model.card.Spell;
import model.card.enums.CardType;
import org.apache.commons.math3.util.Pair;

import java.io.IOException;
import java.util.*;
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
    private static Stage stageSettings = new Stage();
    private static Scene sceneSettings;
    private final Delta delta = new Delta();
    private final ArrayList<Animation> delays = new ArrayList<>();
    private final ArrayList<Stage> stages = new ArrayList<>();
    private Stage firstUserStage = new Stage();
    private Stage secondUserStage = new Stage();
    private Duel currentDuel;
    private Phases phase;
    private int remainingRounds;
    private int initialRounds;
    private boolean isFirstRound = true;
    private boolean canEnlargeCard = true;


    public static Monster getMonsterForEquip(Duel duel, Spell spell) {
        PrintResponses.printChooseEquip();
        int number;
        while (true) {
            try {
                //todo open a stage to show available cards
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
        // todo show available cards
        return ProgramController.scanner.nextLine();
    }

    public void run(String command) {
        HashMap<String, Consumer<Matcher>> commandMap = createCommandMap();
        for (String string : commandMap.keySet()) {
            Matcher matcher = Regex.getMatcher(command, string);
            if (matcher.find()) {
                commandMap.get(string).accept(matcher);
            }
        }
        reloadCardsOnBoard();
        reloadHands();
    }


    private HashMap<String, Consumer<Matcher>> createCommandMap() {
        HashMap<String, Consumer<Matcher>> commandMap = new HashMap<>();
        commandMap.put(Regex.selectFromOwnField, this::selectCardFromOwnField);
        commandMap.put(Regex.selectFromOpponent, this::selectCardFromOpponent);
        commandMap.put(Regex.selectFromOpponentField, this::selectCardFromOpponentField);
        commandMap.put(Regex.selectFromOpponentField2, this::selectCardFromOpponentField);
        commandMap.put(Regex.flipSummon, this::flipSummon);
        commandMap.put(Regex.increaseLP, this::increaseLP);
        commandMap.put(Regex.setWinner, this::setWinner);
        commandMap.put(Regex.forceSelectHand, this::forceSelectHand);
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


    private void createNewDuel(String otherPlayerUsername, int remainingRounds) {
        User secondPlayer = User.getUserByUsername(otherPlayerUsername);
        initialRounds = remainingRounds;
        this.remainingRounds = remainingRounds;
        if (ProgramController.userInGame.getActiveDeck() == null) {
            PrintResponses.showError(PrintResponses.printHasNoActiveDeck(ProgramController.userInGame), null);
        } else if (secondPlayer.getActiveDeck() == null) {
            PrintResponses.showError(PrintResponses.printHasNoActiveDeck(secondPlayer), null);
        } else if (!ProgramController.userInGame.getActiveDeck().isValid()) {
            PrintResponses.showError(PrintResponses.printInvalidDeck(ProgramController.userInGame), null);
        } else if (!secondPlayer.getActiveDeck().isValid()) {
            PrintResponses.showError(PrintResponses.printInvalidDeck(secondPlayer), null);
        } else {
            showCoinFlipping(ProgramController.userInGame, secondPlayer);
        }
    }

    private void createNewDuelWithAI(int remainingRounds) {
        initialRounds = remainingRounds;
        this.remainingRounds = remainingRounds;
        if (ProgramController.userInGame.getActiveDeck() == null) {
            PrintResponses.showError(PrintResponses.printHasNoActiveDeck(ProgramController.userInGame), null);
        } else if (!ProgramController.userInGame.getActiveDeck().isValid()) {
            PrintResponses.showError(PrintResponses.printInvalidDeck(ProgramController.userInGame), null);
        } else {
            AI ai = new AI("AI", "AI", "AI");
            ai.setCurrentDuel(currentDuel);
            showCoinFlipping(ProgramController.userInGame, ai);
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
        Stage stage = getStage();
        Label label = getLabel();
        AnchorPane b = new AnchorPane();
        HBox hBox = getHBox();
        ImageView first = getImageView(currentDuel.getFIRST_USER());
        ImageView second = getImageView(currentDuel.getSECOND_USER());
        b.getChildren().add(label);
        Scene scene = getScene(b);
        stage.setScene(scene);
        User winner = getUser(currentDuel.getFIRST_USER(), currentDuel.getSECOND_USER(), true);
        User loser = getUser(currentDuel.getFIRST_USER(), currentDuel.getSECOND_USER(), false);
        if (remainingRounds == 0) {
            if (initialRounds == 1) {
                label.setText("  I won and got \n " + (1000 + winner.getLifePoints()) + " coins but\n you lost Ha Ha");
                if (winner == currentDuel.getSECOND_USER()) {
                    hBox.getChildren().add(second);
                    hBox.getChildren().add(first);
                } else {
                    hBox.getChildren().add(first);
                    hBox.getChildren().add(second);
                }
                oneRound(winner, loser);
            } else {
                label.setText("  Nice match but\nat last I won\nand got " +
                        (winner.getCoins() + 3000 + 3 * winner.getMaxLifePoint()) + " coins");
                if (winner == currentDuel.getSECOND_USER()) {
                    hBox.getChildren().add(second);
                    hBox.getChildren().add(first);
                } else {
                    hBox.getChildren().add(first);
                    hBox.getChildren().add(second);
                }
                currentDuel.handleEndingTheWholeMatch();
            }
            endStage(stage, b, hBox);
            currentDuel = null;
        } else {
            User winnerRound = currentDuel.handleEndingARound();
            label.setText("   What a game\nAny way i won this round\nsee you in next one");
            if (winnerRound == currentDuel.getSECOND_USER()) {
                hBox.getChildren().add(second);
                hBox.getChildren().add(first);
            } else {
                hBox.getChildren().add(first);
                hBox.getChildren().add(second);
            }
            if (winnerRound.getWinsInAMatch() == 2) {
                endTheGame();
            } else {
                b.getChildren().add(hBox);
                stage.show();
                Animation delay = new PauseTransition(Duration.seconds(4));
                delay.play();
                delay.setOnFinished(event -> {
                    firstUserStage.close();
                    currentDuel = null;
                    stage.close();
                    firstUserStage = new Stage();
                    secondUserStage = new Stage();
                    ProgramController.stage.show();
                    showCoinFlipping(loser, winner);
                });
            }
        }
    }

    private void oneRound(User winner, User loser) {
        winner.setGraveyard(null);
        winner.setBoard(null);
        winner.setHand(null);
        winner.setCoins(winner.getCoins() + 1000 + winner.getLifePoints());
        winner.setScore(winner.getLifePoints() + 1000);
        loser.setGraveyard(null);
        loser.setBoard(null);
        loser.setHand(null);
        loser.setCoins(100 + loser.getCoins());
    }

    private void endStage(Stage stage, AnchorPane b, HBox hBox) {
        b.getChildren().add(hBox);
        stage.show();
        Animation delay = new PauseTransition(Duration.seconds(4));
        delay.play();
        delay.setOnFinished(e -> {
            stage.close();
            ProgramController.currentMenu = new MainMenu();
            try {
                ProgramController.currentMenu.showMenu();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    private User getUser(User firstUser, User secondUser, boolean isWinner) {
        if (firstUser.getLifePoints() > secondUser.getLifePoints()) {
            if (isWinner) return firstUser;
            else return secondUser;
        } else {
            if (isWinner) return secondUser;
            else return firstUser;
        }
    }

    private Scene getScene(AnchorPane b) {
        Scene scene = new Scene(b);
        Image cave = new Image(Objects.requireNonNull(getClass().getResource("/images/cave.jpg")).toExternalForm());
        BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
        b.setBackground(new Background(new BackgroundImage(cave,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                bSize)));
        return scene;
    }

    private ImageView getImageView(User first_user) {
        ImageView first = new ImageView(new Image(String.valueOf(getClass().getResource(first_user.getAvatar()))));
        first.setFitHeight(140);
        first.setFitWidth(140);
        return first;
    }

    private HBox getHBox() {
        HBox hBox = new HBox();
        hBox.setLayoutY(190);
        hBox.setSpacing(115);
        return hBox;
    }

    private Label getLabel() {
        Label label = new Label();
        Image image = new Image(Objects.requireNonNull(getClass().getResource("/images/dialogueBox.png")).toExternalForm(), true);
        label.setGraphic(new ImageView(image));
        label.setContentDisplay(ContentDisplay.TOP);
        label.setGraphicTextGap(-130.0);
        label.setAlignment(Pos.CENTER);
        label.setStyle("-fx-padding: 20 0 0 70; -fx-font-size: 22; -fx-text-fill: GOLD;");
        return label;
    }

    private Stage getStage() {
        firstUserStage.hide();
        secondUserStage.close();
        Stage stage = new Stage();
        stage.setWidth(390);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setHeight(320);
        return stage;
    }

    private void summon() {
        if (currentDuel == null) return;
        if (currentDuel.canNotSummonSelectedCard()) {
            PrintResponses.showError(Responses.unableToSummonCard, null);
        } else if (isNotInMainPhases()) {
            PrintResponses.showError(Responses.summonInWrongPhase, null);
        } else if (currentDuel.getUserWhoPlaysNow().getBoard().getAddressToSummon() == 0) {
            PrintResponses.showError(Responses.fullnessOfMonsterCardZone, null);
        } else if (currentDuel.isHasSummonedOrSetOnce()) {
            PrintResponses.showError(Responses.unableToSummonInTurn, null);
        } else if (!currentDuel.getUserWhoPlaysNow().isCanSummonMonster()) {
            PrintResponses.showError(Responses.disabledMonsterSummon, null);
        } else if (((Monster) currentDuel.getSelectedCard()).getLevel() > 4) {
            Monster monster = (Monster) currentDuel.getSelectedCard();
            if (monster.isCanHaveDifferentTribute()) {
                // todo show available cards in a new stage for user to select.
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
            }// todo show available cards
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
        if (isNotInMainPhases()) {
            PrintResponses.showError(Responses.setCardInWrongPhase, null);
        } else if (currentDuel.getSelectedCard() instanceof Monster) {
            handleMonsterSet();
        } else {
            handleSpellAndTrapSet();
        }
    }

    private void setPosition(Card card) {
        if (isNotInMainPhases()) {
            PrintResponses.showError(Responses.setCardInWrongPhase, null);
        } else if (currentDuel.isHasChangedPositionOnce()) {
            PrintResponses.showError(Responses.unableToChangePositionInTurnTwice, null);
        } else {
            if (!card.isATK()) {
                currentDuel.changeToAttackPosition();
            } else {
                currentDuel.changeToDefensePosition();
            }
            reloadCardsOnBoard();
        }
    }

    private void flipSummon(Matcher matcher) {
        // todo handle flip summon somehow
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
            ProgramController.startNewAudio("src/main/resources/audios/flipSummon.wav");
            currentDuel.flipSummon();
            PrintResponses.printSuccessfulFlipSummon();
        }

    }

    private void attack(int address, Monster monsterToAttack, MouseEvent event) {
        if (currentDuel.isNoCardSelected()) {
            return;
        } else if (isNotInBattlePhase()) {
            PrintResponses.showError(Responses.attackInWrongPhase, null);
        } else if (((Monster) currentDuel.getSelectedCard()).isHasAttackedOnceInTurn()) {
            PrintResponses.showError(Responses.cardAttackedBefore, null);
            return;
        } else if (!currentDuel.getUserWhoPlaysNow().isCanAttack()) {
            PrintResponses.showError(Responses.canNotAttackDueToEffect, null);
        } else if (currentDuel.getRival().getBoard().getMonsterNumber() == 0) {
            attackDirectly();
        } else {
            ProgramController.startNewAudio("src/main/resources/audios/attack.wav");
            handleSuccessfulAttack(address, monsterToAttack, event);
            currentDuel.setSelectedCard(null);
            reloadLPLabels();
        }
        reloadCardsOnBoard();
        reloadCardsInGraveyard();
    }

    private void attackDirectly() {
        if (currentDuel.isNoCardSelected()) {
            return;
        } else if (isSelectedCardNotInMonsterZone()) {
            PrintResponses.showError(Responses.unableToAttack, null);
        } else if (isNotInBattlePhase()) {
            PrintResponses.showError(Responses.attackInWrongPhase, null);
        } else if (((Monster) currentDuel.getSelectedCard()).isHasAttackedOnceInTurn()) {
            PrintResponses.showError(Responses.cardAttackedBefore, null);
        } else {
            ProgramController.startNewAudio("src/main/resources/audios/directAttack.wav");
            int damage = currentDuel.attackDirectly();
            PrintResponses.showInformation(PrintResponses.printDamageInAttackDirectly(damage));
            reloadLPLabels();
        }
        reloadCardsOnBoard();
    }

    private void activateSpell() {
        if (isNotInMainPhases()) {
            PrintResponses.showError(Responses.unableToActivateEffectOnTurn, null);
        } else if (currentDuel.getSelectedCard().isFaceUp()) {
            PrintResponses.showError(Responses.unableToActivateCardTwice, null);
        } else if (isSpellZoneFullAndNeedsToBeOnBoard()) {
            PrintResponses.showError(Responses.fullnessOfSpellCardZone, null);
        } else if (isSpellNotPreparedToBeActivated()) {
            PrintResponses.showError(Responses.unfinishedPreparationOfSpell, null);
        } else if (currentDuel.getUserWhoPlaysNow().isCanNotSetSpell()) {
            PrintResponses.showError(Responses.disabledSpellSummon, null);
        } else {
            if (((Spell) currentDuel.getSelectedCard()).getSpellTrapIcon().getName().equals("Ritual")) {
                isRitualSummon();
                return;
            }
            ProgramController.startNewAudio("src/main/resources/audios/spellActivation.wav");
            currentDuel.activateEffects();
            PrintResponses.showInformation(Responses.successfulSpellActivation);
        }
        reloadCardsInGraveyard();
        reloadCardsOnBoard();
    }

    private void showGraveyard(ArrayList<Card> graveyard) {
        ProgramController.startNewAudio("src/main/resources/audios/graveyard.m4a");
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/CSS.css")).toExternalForm());
        borderPane.getStyleClass().add("grave-yard");
        Button backButton = new Button("Back");
        backButton.getStyleClass().add("buttonEntrance");
        borderPane.setBottom(backButton);
        backButton.setOnMouseClicked(event -> stage.close());
        Scene scene = new Scene(borderPane, 400, 300);

        ListView<Rectangle> cards = new ListView<>();
        cards.setOrientation(Orientation.HORIZONTAL);
        for (Card card : graveyard) {
            Rectangle cardPicture = new Rectangle(180, 200);
            cardPicture.setFill(new ImagePattern(new Image(card.getCardImageAddress())));
            cards.getItems().add(cardPicture);
        }
        borderPane.setCenter(cards);
        stage.setScene(scene);
        stage.show();
    }

    public void surrender() {
        //todo end the game
        boolean surrenders = PrintResponses.showConfirmation(Responses.surrenderConfirmation);
        if (surrenders) {
            currentDuel.surrender(remainingRounds, initialRounds);
            currentDuel = null;
            ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        }
    }

    private void increaseLP(Matcher matcher) {
        int amount = Integer.parseInt(matcher.group("amount"));
        currentDuel.getUserWhoPlaysNow().setLifePoints(currentDuel.getUserWhoPlaysNow().getLifePoints() + amount);
        PrintResponses.print(Responses.increaseLP);
        reloadLPLabels();
    }

    private void setWinner(Matcher matcher) {
        // todo end the game.
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
            //     PrintResponses.printEndingTheWholeMatch(currentDuel.handleEndingThreeRoundGames(winner, loser));
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
        reloadCardsOnBoard();
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
        ProgramController.startNewAudio("src/main/resources/audios/summon.wav");
    }

    private void handleSuccessfulGameCreation(User firstPlayer, User secondPlayer) {
        currentDuel = new Duel(firstPlayer, secondPlayer);
        loadInformationForBothUsers(firstPlayer, secondPlayer);
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
        currentDuel.changeTurn();
        currentDuel.setNumberOfTurnsPlayedUpToNow(currentDuel.getNumberOfTurnsPlayedUpToNow() + 1);
        if (firstUserStage.getScene().getRoot().getEffect() == null) {
            firstUserStage.getScene().getRoot().setEffect(new GaussianBlur());
            secondUserStage.getScene().getRoot().setEffect(null);
        } else {
            secondUserStage.getScene().getRoot().setEffect(new GaussianBlur());
            firstUserStage.getScene().getRoot().setEffect(null);
        }
        phase = Phases.DRAW_PHASE;
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
            reloadHands();
        }
    }

    private void handleSuccessfulAttack(int address, Monster monsterToAttack, MouseEvent mouseEvent) {
        Pair<Integer, Integer> pair = currentDuel.attack(address);
        int key = pair.getKey();
        if (key > 6) {
            key -= 3;
            PrintResponses.printCardNameInAttackIfIsDefenceHide(monsterToAttack, mouseEvent);
        }
        switch (key) {
            case 0:
                PrintResponses.showInformation(Responses.canNotAttackDueToEffect);
                break;
            case 1:
                PrintResponses.showInformation(PrintResponses.printOpponentMonsterDestroyedWithDamage(pair.getValue()));
                break;
            case 2:
                PrintResponses.showInformation(Responses.destructionOfTwoSideCards);
                break;
            case 3:
                PrintResponses.showInformation(PrintResponses.printOwnMonsterDestroyedInAttackWithDamage(pair.getValue()));
                break;
            case 4:
                PrintResponses.showInformation(Responses.destructionOfDefensePositionMonster);
                break;
            case 5:
                PrintResponses.showInformation(Responses.noDestruction);
                break;
            case 6:
                PrintResponses.showInformation(PrintResponses.printNoCardDestroyedButReceivedDamage(pair.getValue()));
                break;
        }
    }

    private void handleSelectionForRitualSummon() {
        PrintResponses.printAskToRitualMonster();
        while (true) {
            // todo show available cards
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
            // todo show available cards
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
            // show available cards
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
            ProgramController.startNewAudio("src/main/resources/audios/set.wav");
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
            ProgramController.startNewAudio("src/main/resources/audios/set.wav");
            currentDuel.setSpellOrTrap();
            PrintResponses.printSuccessfulCardSetting();
            PrintResponses.print(currentDuel);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @SuppressWarnings("rawtypes")
    @Override
    public void showMenu() throws IOException {
        ProgramController.startNewAudioBackground("src/main/resources/audios/gameSound.mp3");
        ProgramController.createNewScene(getClass().getResource("/FXMLs/DuelMenu.fxml"));
        if (ProgramController.gameToContinue == null) {
            ProgramController.currentScene.lookup("#toContinue").setDisable(true);
            ProgramController.currentScene.lookup("#toContinue").setStyle("-fx-background-color: rgb(212, 29, 29,0.877);");
        }

        ChoiceBox choiceBox = (ChoiceBox) ProgramController.currentScene.lookup("#playerChoiceBox");
        TextField userTextField = (TextField) ProgramController.currentScene.lookup("#usernameTextField");
        choiceBox.getSelectionModel().selectedIndexProperty().addListener((ov, value, new_value) -> {
            userTextField.clear();
            userTextField.setDisable(new_value.intValue() == 1);
        });
    }

    @SuppressWarnings("rawtypes")
    public void startNewGame() {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        ChoiceBox userChoiceBox = (ChoiceBox) ProgramController.currentScene.lookup("#playerChoiceBox"),
                roundChoiceBox = (ChoiceBox) ProgramController.currentScene.lookup("#roundChoiceBox");
        TextField userTextField = (TextField) ProgramController.currentScene.lookup("#usernameTextField");
        String userOrAI = (String) userChoiceBox.getValue();
        userTextField.focusedProperty().addListener((obs, oldValue, newValue) -> userTextField.setStyle("-fx-text-fill: black"));
        if (userOrAI.equals("Another Player") && isTextFieldInvalid(userTextField.getText())) {
            userTextField.setStyle("-fx-text-fill: rgb(250, 0, 0);");
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
        BorderPane pane = (BorderPane) ProgramController.currentScene.lookup("#mainPane");
        pane.getChildren().removeIf(child -> child instanceof Rectangle);
        VBox mainVBox = (VBox) ProgramController.currentScene.lookup("#mainVBox");
        mainVBox.getChildren().clear();
        BorderPane borderPane = (BorderPane) ProgramController.currentScene.lookup("#mainPane");
        borderPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/CSS.css")).toExternalForm());
        borderPane.getStyleClass().add("coinFlippingStage");
        Rectangle rectangle = new Rectangle(265, 200, 80, 80);
        HBox hBox = new HBox();
        Button heads = new Button("heads");
        heads.getStyleClass().add("buttonEntrance");
        Button tails = new Button(("tails"));
        tails.getStyleClass().add("buttonEntrance");
        Image headsImage = new Image(Objects.requireNonNull(getClass().getResource("/images/head.jpg")).toExternalForm());
        Image tailsImage = new Image(Objects.requireNonNull(getClass().getResource("/images/tails.jpg")).toExternalForm());
        Image coin = new Image(Objects.requireNonNull(getClass().getResource("/images/coin.png")).toExternalForm());
        heads.setOnMouseClicked(event -> {
            ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
            ProgramController.startNewAudio("src/main/resources/audios/coinFlip.mp3");
            transition(rectangle, headsImage, tailsImage, 0, starter, invited);
        });
        tails.setOnMouseClicked(event -> {
            ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
            ProgramController.startNewAudio("src/main/resources/audios/coinFlip.mp3");
            transition(rectangle, headsImage, tailsImage, 1, starter, invited);
        });
        rectangle.setFill(new ImagePattern(coin));
        hBox.getChildren().add(heads);
        hBox.getChildren().add(tails);
        hBox.setPadding(new Insets(100, 0, 0, 0));
        hBox.setAlignment(Pos.BOTTOM_CENTER);
        hBox.setSpacing(150.0);
        borderPane.getChildren().add(rectangle);
        mainVBox.getChildren().add(hBox);
    }

    private void transition(Rectangle rectangle, Image heads, Image tails, int chosen, User starter, User invited) {
        PathTransition pathTransition = getPathTransition(rectangle, 230, 75);
        PathTransition pathTransition1 = getPathTransition(rectangle, 75, 230);
        RotateTransition rotateTransition = new RotateTransition();
        rotateTransition.setNode(rectangle);
        rotateTransition.setAxis(Rotate.Y_AXIS);
        rotateTransition.setByAngle(360);
        rotateTransition.setRate(45);
        rotateTransition.setCycleCount(40);
        rotateTransition.setDuration(Duration.millis(1200));
        rotateTransition.setAutoReverse(true);
        ParallelTransition parallelTransition = new ParallelTransition(pathTransition, rotateTransition);
        ParallelTransition parallelTransition1 = new ParallelTransition(pathTransition1, rotateTransition);
        SequentialTransition sequentialTransition = new SequentialTransition(parallelTransition, parallelTransition1);
        sequentialTransition.setOnFinished(event -> {
            Random random = new Random();
            int number = random.nextInt(2);
            if (number == 0) rectangle.setFill(new ImagePattern(heads));
            else rectangle.setFill(new ImagePattern(tails));
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1.5));
            pauseTransition.play();
            pauseTransition.setOnFinished(event1 -> {
                handleCreatingTwoStages();
                if (chosen == number) handleSuccessfulGameCreation(starter, invited);
                else handleSuccessfulGameCreation(invited, starter);
            });
        });
        sequentialTransition.play();

    }

    private PathTransition getPathTransition(Rectangle rectangle, int start, int end) {
        PathTransition pathTransition = new PathTransition();
        pathTransition.setCycleCount(1);
        pathTransition.setDuration(Duration.millis(1200));
        pathTransition.setNode(rectangle);
        Path path = new Path();
        path.getElements().add(new MoveTo(295, start));
        path.getElements().add(new LineTo(295, end));
        pathTransition.setPath(path);
        return pathTransition;
    }

    public void goToMainMenu() throws IOException {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
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
            ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
            if (firstUserStage.getScene().getRoot().getEffect() == null) {
                goToNextPhase();
                reloadPhaseLabels();
            } else {
                PrintResponses.showError(Responses.wrongTurn, event);
            }
        });
        secondUserStage.getScene().lookup("#nextPhaseButton").setOnMouseClicked(event -> {
            ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
            if (secondUserStage.getScene().getRoot().getEffect() == null) {
                goToNextPhase();
                reloadPhaseLabels();
            } else {
                PrintResponses.showError(Responses.wrongTurn, event);
            }
        });
    }


    private void createStageForUser(Stage stage) {
        try {
            Parent pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXMLs/DuelBoard.fxml")));
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setY(200);
            stage.getScene().setOnKeyPressed(event -> {
                if (stage.getScene().getRoot().getEffect() == null) {
                    if (event.getCode().equals(KeyCode.C) && event.isControlDown() && event.isShiftDown()) {
                        showCheatStage();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showCheatStage() {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/CSS.css")).toExternalForm());
        borderPane.getStyleClass().add("cheat-stage");
        Scene scene = new Scene(borderPane, 300, 200);
        TextField textField = new TextField("Enter cheat code and hit enter to exit.");
        textField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                String cheatCode = textField.getText();
                stage.close();
                run(cheatCode);
            }
        });
        borderPane.setCenter(textField);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    private boolean isTextFieldInvalid(String username) {
        if (username.isEmpty() || username.isBlank())
            return true;
        return Objects.requireNonNull(DataController.getAllUsers()).stream()
                .noneMatch(user -> user.getUsername().equals(username) && !user.getUsername().equals(ProgramController.userInGame.getUsername()));
    }

    private void loadInformationForBothUsers(User firstUser, User secondUser) {
        ((Label) firstUserStage.getScene().lookup("#ownUsername")).setText(firstUser.getUsername());
        ((Label) firstUserStage.getScene().lookup("#ownNickname")).setText(firstUser.getNickname());
        ((Label) firstUserStage.getScene().lookup("#ownLP")).setText(String.valueOf(firstUser.getLifePoints()));
        ((Rectangle) firstUserStage.getScene().lookup("#ownAvatar"))
                .setFill(new ImagePattern(new Image(String.valueOf(getClass().getResource(firstUser.getAvatar())))));
        ((Label) firstUserStage.getScene().lookup("#rivalUsername")).setText(secondUser.getUsername());
        ((Label) firstUserStage.getScene().lookup("#rivalNickname")).setText(secondUser.getNickname());
        ((Label) firstUserStage.getScene().lookup("#rivalLP")).setText(String.valueOf(secondUser.getLifePoints()));
        ((Rectangle) firstUserStage.getScene().lookup("#rivalAvatar"))
                .setFill(new ImagePattern(new Image(String.valueOf(getClass().getResource(secondUser.getAvatar())))));
        ((Label) secondUserStage.getScene().lookup("#ownUsername")).setText(secondUser.getUsername());
        ((Label) secondUserStage.getScene().lookup("#ownNickname")).setText(secondUser.getNickname());
        ((Label) secondUserStage.getScene().lookup("#ownLP")).setText(String.valueOf(secondUser.getLifePoints()));
        ((Rectangle) secondUserStage.getScene().lookup("#ownAvatar"))
                .setFill(new ImagePattern(new Image(String.valueOf(getClass().getResource(secondUser.getAvatar())))));
        ((Label) secondUserStage.getScene().lookup("#rivalUsername")).setText(firstUser.getUsername());
        ((Label) secondUserStage.getScene().lookup("#rivalNickname")).setText(firstUser.getNickname());
        ((Label) secondUserStage.getScene().lookup("#rivalLP")).setText(String.valueOf(firstUser.getLifePoints()));
        ((Rectangle) secondUserStage.getScene().lookup("#rivalAvatar"))
                .setFill(new ImagePattern(new Image(String.valueOf(getClass().getResource(firstUser.getAvatar())))));
    }

    private void reloadLPLabels() {
        if (currentDuel.getFIRST_USER().getLifePoints() <= 0 || currentDuel.getSECOND_USER().getLifePoints() <= 0) {
            endTheGame();
            return;
        }
        ((Label) firstUserStage.getScene().lookup("#ownLP")).setText(String.valueOf(currentDuel.getFIRST_USER().getLifePoints()));
        playAnimationForChangeOfLP(((ProgressBar) firstUserStage.getScene().lookup("#ownLPBar")), currentDuel.getFIRST_USER());

        ((Label) firstUserStage.getScene().lookup("#rivalLP")).setText(String.valueOf(currentDuel.getSECOND_USER().getLifePoints()));
        playAnimationForChangeOfLP(((ProgressBar) firstUserStage.getScene().lookup("#rivalLPBar")), currentDuel.getSECOND_USER());

        ((Label) secondUserStage.getScene().lookup("#ownLP")).setText(String.valueOf(currentDuel.getSECOND_USER().getLifePoints()));
        playAnimationForChangeOfLP(((ProgressBar) secondUserStage.getScene().lookup("#ownLPBar")), currentDuel.getSECOND_USER());

        ((Label) secondUserStage.getScene().lookup("#rivalLP")).setText(String.valueOf(currentDuel.getFIRST_USER().getLifePoints()));
        playAnimationForChangeOfLP(((ProgressBar) secondUserStage.getScene().lookup("#rivalLPBar")), currentDuel.getFIRST_USER());
    }

    private void playAnimationForChangeOfLP(ProgressBar lpBar, User user) {
        if ((double) user.getLifePoints() / Duel.getInitialLifePoints() < 0.5) {
            lpBar.getStyleClass().add("red-bar");
        } else {
            lpBar.getStyleClass().remove("red-bar");
        }
        new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(lpBar.progressProperty(), lpBar.getProgress())
                ),
                new KeyFrame(
                        Duration.seconds(2),
                        new KeyValue(lpBar.progressProperty(), ((double) user.getLifePoints() / Duel.getInitialLifePoints()))
                )
        ).play();
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

    private void reloadCardsInGraveyard() {
        if (currentDuel == null)
            return;
        ((Label) firstUserStage.getScene().lookup("#ownGraveyardNumber"))
                .setText(String.valueOf(currentDuel.getFIRST_USER().getGraveyard().size()));
        firstUserStage.getScene().lookup("#ownGraveyard").setOnMouseClicked(event -> {
            if (firstUserStage.getScene().getRoot().getEffect() == null)
                showGraveyard(currentDuel.getFIRST_USER().getGraveyard());
            else
                PrintResponses.showError(Responses.wrongTurn, event);
        });

        ((Label) firstUserStage.getScene().lookup("#rivalGraveyardNumber"))
                .setText(String.valueOf(currentDuel.getSECOND_USER().getGraveyard().size()));
        firstUserStage.getScene().lookup("#rivalGraveyard").setOnMouseClicked(event -> {
            if (firstUserStage.getScene().getRoot().getEffect() == null)
                showGraveyard(currentDuel.getSECOND_USER().getGraveyard());
            else
                PrintResponses.showError(Responses.wrongTurn, event);

        });

        ((Label) secondUserStage.getScene().lookup("#ownGraveyardNumber"))
                .setText(String.valueOf(currentDuel.getSECOND_USER().getGraveyard().size()));
        secondUserStage.getScene().lookup("#ownGraveyard").setOnMouseClicked(event -> {
            if (secondUserStage.getScene().getRoot().getEffect() == null)
                showGraveyard(currentDuel.getSECOND_USER().getGraveyard());
            else
                PrintResponses.showError(Responses.wrongTurn, event);

        });

        ((Label) secondUserStage.getScene().lookup("#rivalGraveyardNumber"))
                .setText(String.valueOf(currentDuel.getFIRST_USER().getGraveyard().size()));
        secondUserStage.getScene().lookup("#rivalGraveyard").setOnMouseClicked(event -> {
            if (secondUserStage.getScene().getRoot().getEffect() == null)
                showGraveyard(currentDuel.getFIRST_USER().getGraveyard());
            else
                PrintResponses.showError(Responses.wrongTurn, event);
        });
    }


    private void reloadPhaseLabels() {
        if (!firstUserStage.isShowing())
            return;
        ((Label) firstUserStage.getScene().lookup("#phaseLabel")).setText(phase.getName());
        ((Label) secondUserStage.getScene().lookup("#phaseLabel")).setText(phase.getName());
    }

    private void reloadCardsOnBoard() {
        if (currentDuel != null) {
            showMonstersForFirstStage();
            showMonstersForSecondStage();
            showSpellsForFirstStage();
            showSpellsForSecondStage();
        }
    }

    private void showMonstersForFirstStage() {
        if (!firstUserStage.isShowing())
            return;
        HBox ownMonsters = (HBox) firstUserStage.getScene().lookup("#ownMonsterHBox");
        HBox rivalMonster = (HBox) firstUserStage.getScene().lookup("#rivalMonsterHBox");
        rivalMonster.setOnMouseClicked(event -> {
            if (firstUserStage.getScene().getRoot().getEffect() == null) {
                if (currentDuel.getRival().getBoard().getMonsterNumber() == 0)
                    attackDirectly();
            } else
                PrintResponses.showError(Responses.wrongTurn, event);

        });
        showCardsOnBoard(ownMonsters, currentDuel.getFIRST_USER().getBoard().getMonsters(),
                currentDuel.getFIRST_USER().getBoard().getFieldZone(),false);
        showCardsOnBoard(rivalMonster, currentDuel.getSECOND_USER().getBoard().getMonsters(),
                currentDuel.getSECOND_USER().getBoard().getFieldZone(),true);
    }

    private void showSpellsForFirstStage() {
        if (!firstUserStage.isShowing())
            return;
        HBox ownSpellsHBox = (HBox) firstUserStage.getScene().lookup("#ownSpellsHBox");
        HBox rivalSpellsHBox = (HBox) firstUserStage.getScene().lookup("#rivalSpellsHBox");
        showCardsOnBoard(ownSpellsHBox, currentDuel.getFIRST_USER().getBoard().getSpellsAndTraps(), null,false);
        showCardsOnBoard(rivalSpellsHBox, currentDuel.getSECOND_USER().getBoard().getSpellsAndTraps(), null,true);
    }

    private void showMonstersForSecondStage() {
        if (!firstUserStage.isShowing())
            return;
        HBox ownMonsters = (HBox) secondUserStage.getScene().lookup("#ownMonsterHBox");
        HBox rivalMonster = (HBox) secondUserStage.getScene().lookup("#rivalMonsterHBox");
        showCardsOnBoard(ownMonsters, currentDuel.getSECOND_USER().getBoard().getMonsters()
                , currentDuel.getSECOND_USER().getBoard().getFieldZone(),false);
        showCardsOnBoard(rivalMonster, currentDuel.getFIRST_USER().getBoard().getMonsters()
                , currentDuel.getFIRST_USER().getBoard().getFieldZone(),true);
    }

    private void showSpellsForSecondStage() {
        if (!firstUserStage.isShowing())
            return;
        HBox ownMonsters = (HBox) secondUserStage.getScene().lookup("#ownSpellsHBox");
        HBox rivalMonster = (HBox) secondUserStage.getScene().lookup("#rivalSpellsHBox");
        showCardsOnBoard(ownMonsters, currentDuel.getSECOND_USER().getBoard().getSpellsAndTraps()
                ,null, false);
        showCardsOnBoard(rivalMonster, currentDuel.getFIRST_USER().getBoard().getSpellsAndTraps()
                , null,true);
    }

    private void showCardsOnBoard(HBox hBox, Card[] cards, Card fieldSpell,boolean isRival) {
        calibrateBoard(hBox);
        showFieldSpellOnBoard(hBox, fieldSpell);
        for (int i = 0; i < cards.length; i++) {
            Card card = cards[i];
            if (card == null) {
                continue;
            }
            Rectangle cardOnBoard = ((Rectangle) hBox.getChildren().get(convertNormalAddressToBoardAddress(i, card, isRival)));
            cardOnBoard.setEffect(null);
            if (card.isFaceUp()) {
                cardOnBoard.setFill(new ImagePattern(new Image(card.getCardImageAddress())));
                if (card.isATK() && cardOnBoard.getTransforms().size() != 0){
                    handleRotatingForCards(cardOnBoard, -90);
                }
                if (!card.isATK()) {
                    if (!card.isATK() && cardOnBoard.getTransforms().size() == 0) {
                        handleRotatingForCards(cardOnBoard, 90);
                    }
                }
                cardOnBoard.setOnMouseEntered(event -> {
                    if (!cardOnBoard.getFill().equals(Color.TRANSPARENT) && cardOnBoard.getScene().getRoot().getEffect() == null)
                        enlargeCardPicture(cardOnBoard, event);
                });
            } else {
                cardOnBoard.setFill(new ImagePattern(new Image(card.getBackPictureAddress())));
                if (!card.isATK() && cardOnBoard.getTransforms().size() == 0) {
                    handleRotatingForCards(cardOnBoard, 90);
                }
                if (!isRival) {
                    Rectangle largeCardPicture = new Rectangle(50, 50);
                    largeCardPicture.setFill(new ImagePattern(new Image(card.getCardImageAddress())));
                    cardOnBoard.setOnMouseEntered(event -> {
                        if (cardOnBoard.getScene().getRoot().getEffect() == null)
                            enlargeCardPicture(largeCardPicture, event);
                    });
                }
            }
            int finalI = i;
            if (!isRival && card instanceof Monster) {
                cardOnBoard.setOnMouseClicked(event -> {
                    if (cardOnBoard.getScene().getRoot().getEffect() == null) {
                        handleSelectingCard(finalI, cardOnBoard, "monster");
                        if (event.getButton() == MouseButton.SECONDARY) {
                            ContextMenu menu = new ContextMenu();
                            handleContextMenuWhenRightClicked(card, menu, cardOnBoard);
                        }
                    } else
                        PrintResponses.showError(Responses.wrongTurn, event);
                });
            }
            if (card instanceof Spell) {
                cardOnBoard.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.SECONDARY) {
                        if (cardOnBoard.getScene().getRoot().getEffect() == null) {
                            handleSelectingCard(finalI, cardOnBoard, "spell");
                            ContextMenu menu = new ContextMenu();
                            handleContextMenuWhenRightClicked(card, menu, cardOnBoard);
                        }
                    } else
                        PrintResponses.showError(Responses.wrongTurn, event);
                });
            } else if (isRival && card instanceof Monster) {
                cardOnBoard.setOnMouseClicked(event -> {
                    if (cardOnBoard.getScene().getRoot().getEffect() == null)
                        attack(finalI + 1, (Monster) card, event);
                    else
                        PrintResponses.showError(Responses.wrongTurn, event);
                });
            }
        }
    }

    private void showFieldSpellOnBoard(HBox hBox, Card fieldSpell) {
        if (hBox.lookup("#ownFieldZone" ) == null || hBox.lookup("#rivalFieldZone" ) == null)
            return;
        Rectangle fieldZoneCardPicture = (Rectangle) ((hBox.lookup("#ownFieldZone" )!= null) ?
                hBox.lookup("#ownFieldZone" ): hBox.lookup("#rivalFieldZone" ));
        if (fieldSpell != null) {
            fieldZoneCardPicture.setFill(new ImagePattern(new Image(fieldSpell.getCardImageAddress())));
        } else {
            fieldZoneCardPicture.setFill(Color.TRANSPARENT);
        }
    }

    private void handleRotatingForCards(Rectangle cardOnBoard, double endingAngel) {
        Rotate rotate = new Rotate();
        rotate.setPivotX(cardOnBoard.getX() + cardOnBoard.getWidth() / 2);
        rotate.setPivotY(cardOnBoard.getScaleY() + cardOnBoard.getHeight() / 2);
        new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(rotate.angleProperty(), (double) 0)
                ),
                new KeyFrame(
                        Duration.seconds(1),
                        new KeyValue(rotate.angleProperty(), endingAngel)
                )
        ).play();
        cardOnBoard.getTransforms().add(rotate);
    }

    private void handleContextMenuWhenRightClicked(Card card, ContextMenu menu, Rectangle cardPicture) {
        if (card instanceof Monster) {
            MenuItem changeFacePosition = new MenuItem("Change Face Position");
            menu.getItems().add(changeFacePosition);
            changeFacePosition.setOnAction(event -> setPosition(card));
        } else if (card instanceof Spell) {
            MenuItem activateEffects = new MenuItem("Activate Effects");
            if (card.isFaceUp())
                activateEffects.setDisable(true);
            menu.getItems().add(activateEffects);
            activateEffects.setOnAction(event -> activateSpell());
        }

        cardPicture.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                menu.show(cardPicture, event.getScreenX(), event.getScreenY());
            } else if (event.getButton() == MouseButton.PRIMARY)
                menu.hide();
        });
    }

    private void calibrateBoard(HBox ownMonsters) {
        for (Node child : ownMonsters.getChildren()) {
            if (child instanceof Rectangle) {
                ((Rectangle) child).setFill(Color.TRANSPARENT);
                child.setEffect(null);
            }
        }
    }

    private void handleSelectingCard(int index, Rectangle cardOnBoard, String where) {
        cardOnBoard.setEffect(new Glow(5));
        selectCardFromOwn(index + 1, where);
    }


    private ArrayList<Rectangle> getHandCardPictures(User user, String showMode) {
        ArrayList<Rectangle> handCardPictures = new ArrayList<>();
        Card[] cardsInHand = user.getHand().getCardsInHand();
        for (int i = 0; i < cardsInHand.length; i++) {
            Card card = cardsInHand[i];
            if (card == null)
                continue;
            Rectangle cardPicture = new Rectangle(90, 100);
            if (showMode.equals("hide")) {
                cardPicture.setFill(new ImagePattern(new Image(card.getBackPictureAddress())));
                setMouseEventsForCardsInHand(cardPicture, i, true);
            } else {
                cardPicture.setFill(new ImagePattern(new Image(card.getCardImageAddress())));
                setMouseEventsForCardsInHand(cardPicture, i, false);
            }
            handCardPictures.add(cardPicture);

        }
        return handCardPictures;
    }

    private int convertNormalAddressToBoardAddress(int index, Card card, boolean isRival) {
        int indexOnBoard;
        switch (index) {
            case 0:
                indexOnBoard = 3;
                break;
            case 1:
                indexOnBoard = 4;
                break;
            case 2:
                indexOnBoard = 2;
                break;
            case 3:
                indexOnBoard = 5;
                break;
            case 4:
                indexOnBoard = 1;
                break;
            default:
                indexOnBoard = 0;
        }
        if (card instanceof Monster || isRival)
            return indexOnBoard;
        return indexOnBoard - 1;
    }


    public void setMouseEventsForCardsInHand(Rectangle cardPicture, int index, boolean isHidden) {
        cardPicture.setOnMousePressed(mouseEvent -> {
            if (cardPicture.getScene().getRoot().getEffect() != null)
                return;
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
            if (cardPicture.getScene().getRoot().getEffect() != null)
                return;
            Stage stage = (firstUserStage.getScene().getRoot().getEffect() == null) ? firstUserStage : secondUserStage;
            cardPicture.setCursor(Cursor.OPEN_HAND);
            HBox ownMonsters = (HBox) stage.getScene().lookup("#ownMonsterHBox");
            if (ownMonsters.contains(mouseEvent.getX(), mouseEvent.getY())) {
                addCardToBoard(mouseEvent);
                ((AnchorPane) stage.getScene().getRoot()).getChildren().remove(cardPicture);
            }
        });

        cardPicture.setOnMouseDragged(mouseEvent -> {
            if (cardPicture.getScene().getRoot().getEffect() != null)
                return;
            cardPicture.setCursor(Cursor.CLOSED_HAND);
            cardPicture.setLayoutX(mouseEvent.getSceneX() + delta.x);
            cardPicture.setLayoutY(mouseEvent.getSceneY() + delta.y);
        });

        Stage stage = (firstUserStage.getScene().getRoot().getEffect() == null) ? firstUserStage : secondUserStage;
        firstUserStage.addEventFilter(MouseEvent.MOUSE_MOVED, event -> closeAllStages());
        secondUserStage.addEventFilter(MouseEvent.MOUSE_MOVED, event -> closeAllStages());
        stage.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (stage.getScene().getRoot().getEffect() != null)
                return;
            if (event.getButton() != MouseButton.SECONDARY)
                canEnlargeCard = true;
        });

        cardPicture.setOnMouseEntered(event -> {
            if (cardPicture.getScene().getRoot().getEffect() != null)
                return;
            cardPicture.setCursor(Cursor.HAND);
            if (!isHidden)
                enlargeCardPicture(cardPicture, event);
        });
    }


    public void addCardToBoard(MouseEvent mouseEvent) {
        if (currentDuel.getSelectedCard() != null) {
            if (currentDuel.getFIRST_USER().getLifePoints() <= 0 || currentDuel.getSECOND_USER().getLifePoints() <= 0)
                endTheGame();
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY))
                summon();
            else
                set();
            reloadCardsOnBoard();
            reloadHands();
        }
    }

    public void showSettings(MouseEvent mouseEvent) throws IOException {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        if (((Button) mouseEvent.getSource()).getScene().getRoot().getEffect() != null) {
            PrintResponses.showError(Responses.wrongTurn, mouseEvent);
            return;
        }
        Parent pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXMLs/settings.fxml")));
        Scene scene = new Scene(pane);
        stageSettings.setScene(scene);
        stageSettings.setResizable(false);
        stageSettings.show();
        scene.lookup("#pause").setOnMouseClicked(event -> pause());
    }

    public void setAudio() throws IOException {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        Parent pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXMLs/Audio.fxml")));
        sceneSettings = new Scene(pane);
        ((Label) sceneSettings.lookup("#volume")).setText(String.format("%.1f", ProgramController.mediaPlayer.getVolume()));
        checkVolumeButton();
        stageSettings.setScene(sceneSettings);
        stageSettings.show();
    }

    private void checkVolumeButton() {
        sceneSettings.lookup("#increaseVolume").setDisable(String.format("%.1f", ProgramController.mediaPlayer.getVolume()).equals("1.0"));
        sceneSettings.lookup("#decreaseVolume").setDisable(String.format("%.1f", ProgramController.mediaPlayer.getVolume()).equals("0.0"));
    }

    public void decreaseVolume() {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        ProgramController.mediaPlayer.setVolume(ProgramController.mediaPlayer.getVolume() - 0.1);
        ProgramController.volume = ProgramController.mediaPlayer.getVolume();
        ProgramController.mediaPlayerBackground.setVolume(ProgramController.volume);
        ((Label) sceneSettings.lookup("#volume")).setText(String.format("%.1f", ProgramController.mediaPlayer.getVolume()));
        checkVolumeButton();
    }

    public void increaseVolume() {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        ProgramController.mediaPlayer.setVolume(ProgramController.mediaPlayer.getVolume() + 0.1);
        ProgramController.volume = ProgramController.mediaPlayer.getVolume();
        ProgramController.mediaPlayerBackground.setVolume(ProgramController.volume);
        checkVolumeButton();
        ((Label) sceneSettings.lookup("#volume")).setText(String.format("%.1f", ProgramController.mediaPlayer.getVolume()));
    }

    public void mute() {
        ProgramController.mediaPlayer.setVolume(0);
        ProgramController.volume = ProgramController.mediaPlayer.getVolume();
        ProgramController.mediaPlayerBackground.setVolume(ProgramController.volume);
        checkVolumeButton();
        ((Label) sceneSettings.lookup("#volume")).setText(String.format("%.1f", ProgramController.mediaPlayer.getVolume()));
    }

    private void enlargeCardPicture(Rectangle rectangle, MouseEvent mouseEvent) {
        if (!canEnlargeCard)
            return;
        Animation delay = new PauseTransition(Duration.seconds(2));
        Stage stage = new Stage();
        stage.setX(mouseEvent.getScreenX());
        stage.setY(mouseEvent.getScreenY());
        stage.initStyle(StageStyle.UNDECORATED);
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane, 300, 400);
        delay.setOnFinished(e -> {
            delays.forEach(Animation::stop);
            stages.forEach(Stage::close);
            Rectangle enlargedPicture = new Rectangle(300, 400);
            enlargedPicture.setFill(rectangle.getFill());
            borderPane.setCenter(enlargedPicture);
            stage.setScene(scene);
            stage.show();
            stages.add(stage);
            delays.add(delay);
        });
        delay.play();
        rectangle.setOnMouseExited(event -> delay.stop());
    }

    public void closeAllStages() {
        stages.forEach(Stage::close);
    }

    public void pause() {
        firstUserStage.close();
        secondUserStage.close();
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        stageSettings.close();
        ProgramController.gameToContinue = this;
        try {
            goToMainMenu();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void continuePausedGame() {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        //this.currentDuel = ProgramController.gameToContinue;
        ProgramController.gameToContinue.firstUserStage.show();
        ProgramController.gameToContinue.secondUserStage.show();

//        firstUserStage.show();
//        secondUserStage.show();
    }
}


class Delta {
    double x, y;
}



