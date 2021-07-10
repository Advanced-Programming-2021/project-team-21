package view;

import controller.ProgramController;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.Duel;
import model.User;
import model.card.Card;
import model.card.Monster;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PrintResponses {
    public static void printInvalidFormat() {
        System.out.println(Responses.invalidFormat);
    }


    public static void printFullnessOfMonsterCardZone() {
        System.out.println(Responses.fullnessOfMonsterCardZone);
    }

    public static void printUnableToSummonInTurn() {
        System.out.println(Responses.unableToSummonInTurn);
    }

    public static void printSuccessfulSummon() {
        System.out.println(Responses.successfulSummon);
    }

    public static void printNoCardToTribute() {
        System.out.println(Responses.noCardToTribute);
    }

    public static void printNoMonsterOnAddress() {
        System.out.println(Responses.noMonsterOnAddress);
    }


    public static void printSuccessfulCardSetting() {
        System.out.println(Responses.successfulCardSetting);
    }


    public static void printFullnessOfSpellCardZone() {
        System.out.println(Responses.fullnessOfSpellCardZone);
    }

    public static void printUnableToRitualSummonMonster() {
        System.out.println(Responses.unableToRitualSummonMonster);
    }

    public static void printEmergencyRitualSummon() {
        System.out.println(Responses.emergencyRitualSummon);
    }

    public static void printInequalityOfLevelsOfSelectedAndRitualMonster() {
        System.out.println(Responses.inequalityOfLevelsOfSelectedAndRitualMonster);
    }

    public static String printUnableToSpecialSummonMonster() {
        return Responses.unableToSpecialSummonMonster;
    }


    public static String printHasNoActiveDeck(User user) {
        return user.getUsername() + " has no active deck";
    }

    public static String printInvalidDeck(User user) {
        return user.getUsername() + "’s deck is invalid";
    }

    public static void printPhaseName(Phases phase) {
        System.out.println("phase: " + phase.toString().toLowerCase().replace("_", " "));
    }

    public static void printDrawnCard(Card card) {
        System.out.println("new card added to the hand : " + card.getName());
    }


    public static String printOpponentMonsterDestroyedWithDamage(int damage) {
        return "your opponent’s monster is destroyed and your opponent receives " + damage + " battle damage";
    }

    public static String printOwnMonsterDestroyedInAttackWithDamage(int damage) {
        return "Your monster card is destroyed and you received " + damage + " battle damage";
    }


    public static String printNoCardDestroyedButReceivedDamage(int damage) {
        return "no card is destroyed and you received " + damage + " battle damage";
    }

    public static void printCardNameInAttackIfIsDefenceHide(Card card, MouseEvent mouseEvent) {
        if (card.isFaceUp())
            return;
        Animation delay = new PauseTransition(Duration.seconds(3));
        double notToBeDuplicate = mouseEvent.getScreenX();
        Stage stage = new Stage();
        stage.setX(notToBeDuplicate);
        stage.setY(mouseEvent.getScreenY());
        stage.initStyle(StageStyle.UNDECORATED);
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane, 300, 400);
        Rectangle enlargedPicture = new Rectangle(300, 400);
        enlargedPicture.setFill(new ImagePattern(new Image(card.getCardImageAddress())));
        borderPane.setCenter(enlargedPicture);
        stage.setScene(scene);
        stage.show();
        delay.setOnFinished(e -> stage.close());
        delay.play();
    }

    public static String printDamageInAttackDirectly(int damage) {
        return "you opponent receives " + damage + " battle damage";
    }


    public static void printGameSuccessfullyCreated() {
        System.out.println(Responses.gameCreation);
    }

    public static void printBoard(Duel currentDuel) {
        System.out.println(currentDuel);
    }

    public static void printSpecialSummonCards(ArrayList<Monster> monsters) {
        System.out.println("these are the cards to choose from");
        for (int i = 0; i < monsters.size(); i++) {
            System.out.println(i + ": \n" + monsters.get(i));
            System.out.println("-------------------------------------------------------------------------------------");
        }
    }

    public static String printNotToEquip() {
        return "You can not use equip spell right now";
    }

    public static String printNotTpTribute() {
        return "You can not tribute";
    }

    public static void showError(String errorContent, MouseEvent mouseEvent) {
        ProgramController.startNewAudio("src/main/resources/audios/error.mp3");
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        if (mouseEvent != null) {
            errorAlert.setX(mouseEvent.getScreenX());
            errorAlert.setY(mouseEvent.getScreenY());
        }
        errorAlert.initStyle(StageStyle.UNDECORATED);
        errorAlert.getDialogPane().getStylesheets()
                .add(Objects.requireNonNull(PrintResponses.class.getResource("/CSS/CSS.css")).toExternalForm());
        errorAlert.setContentText(errorContent);
        errorAlert.show();
    }

    public static void showInformation(String informationContent) {
        Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
        informationAlert.initStyle(StageStyle.UNDECORATED);
        informationAlert.getDialogPane().getStylesheets()
                .add(Objects.requireNonNull(PrintResponses.class.getResource("/CSS/CSS.css")).toExternalForm());
        informationAlert.setContentText(informationContent);
        informationAlert.show();
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> informationAlert.close());
        delay.play();
    }


    public static boolean showConfirmation(String confirmationContent) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.initStyle(StageStyle.UNDECORATED);
        confirmationAlert.getDialogPane().getStylesheets()
                .add(Objects.requireNonNull(PrintResponses.class.getResource("/CSS/CSS.css")).toExternalForm());
        confirmationAlert.setContentText(confirmationContent);
        confirmationAlert.showAndWait();
        return confirmationAlert.getResult() == ButtonType.OK || confirmationAlert.getResult() == ButtonType.YES;
    }


    public static void printChooseTribute() {
        System.out.println(Responses.choiceOfDifferentTribute);
    }


    public static void printRoundNumber(int remainingRounds) {
        System.out.println("Round " + (4 - remainingRounds) + " started");
    }


    public static void print(Object object) {
        System.out.println(object);
    }

    public static void printAskToChain(User user, Duel duel) {
        System.out.println("now it will be " + user.getUsername() + "’s turn\n");
        printBoard(duel);
        System.out.println(Responses.askForChain);
    }

    public static void printChainComplete(int chainCount) {
        System.out.println("chain Count: " + chainCount);
    }

    public static void printWrongSpell() {
        System.out.println(Responses.wrongSpell);
    }

    public static void printWrongSpellFormat() {
        System.out.println(Responses.wrongSpellFormat);
    }


    public static void printAskToDiscard() {
        System.out.println(Responses.askToDiscard);
    }

    public static void printAskTpGetCardName() {
        System.out.println(Responses.askToGetCardName);
    }

    public static void printWrongCardName() {
        System.out.println(Responses.wrongCardName);
    }


    public static void printSpellsToDestroy(List<Card> cards) {
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i) == null) continue;
            System.out.println(i + ": " + cards.get(i));
        }
    }

    public static void printChangeOfHeart() {
        System.out.println(Responses.changeOfHeart);
    }

    public static void printWrongControl() {
        System.out.println(Responses.wrongChoiceControl);
    }

    public static void printCanNotControl() {
        System.out.println(Responses.canNotControl);
    }

    public static void printNoSpellFound() {
        System.out.println(Responses.noSpellFound);
    }


    public static void printNoSpecialEffect() {
        System.out.println(Responses.noSpecialEffect);
    }

    public static void printAskToRitualMonster() {
        System.out.println(Responses.askToRitual);
    }

    public static void printEnterTributeOrRitual() {
        System.out.println(Responses.enterTribute);
    }

}
