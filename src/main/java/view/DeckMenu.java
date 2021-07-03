package view;

import controller.DataController;
import controller.ProgramController;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import model.Deck;
import model.User;
import model.card.Card;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;

public class DeckMenu implements Menuable {
    private final User USER = ProgramController.userInGame;

    private final int DECK_WIDTH = 90, DECK_HIGHT = 100;

    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getMatcher(command, Regex.deckCreate)).find()) {
            createDeck(matcher);
        } else if ((matcher = Regex.getMatcher(command, Regex.deckDelete)).find()) {
            deleteDeck(matcher);
        } else if (Regex.getMatcher(command, Regex.menuShow).find()) {
            showCurrentMenu();
        } else if ((matcher = Regex.getMatcher(command, Regex.ActiveDeck)).find()) {
            activateDeck(matcher);
        } else if ((matcher = Regex.getMatcher(command, Regex.addCardSide)).find() ||
                (matcher = Regex.getMatcher(command, Regex.addCardMain)).find()) {
            addCard(matcher);
        } else if ((matcher = Regex.getMatcher(command, Regex.removeCardMain)).find() ||
                (matcher = Regex.getMatcher(command, Regex.removeCardSide)).find()) {
            removeCard(matcher);
        } else if (Regex.getMatcher(command, Regex.menuExit).find()) {
            exitMenu();
        } else if (Regex.getMatcher(command, Regex.showAllDeck).find()) {
            showAllDeck();
        } else if ((matcher = Regex.getMatcher(command, Regex.showDeckSide)).find() ||
                (matcher = Regex.getMatcher(command, Regex.showDeckMain)).find()) {
            showADeck(matcher);
        } else if ((matcher = Regex.getMatcher(command, Regex.showACard)).find()) {
            String cardName = matcher.group("cardName").trim();
            PrintResponses.printACard(Card.getCardByName(cardName));
        } else if (Regex.getMatcher(command, Regex.deckShowCard).find()) {
            showAllCards();
        } else PrintResponses.printInvalidFormat();
    }

    private void showAllCards() {
        ArrayList<Card> allCards = USER.getCards();
        Card.sort(allCards);
        for (Card card : allCards) {
            PrintResponses.printAllCard(card);
        }
    }

    private void showADeck(Matcher matcher) {
        String deckName = matcher.group("deckName");
        Deck deck = USER.getDeckByName(deckName);
        if (deck == null) {
            PrintResponses.printDeckNotExist(deckName);
            return;
        }
        String type;
        try {
            type = matcher.group("side");
        } catch (Exception e) {
            type = "main";
        }
        PrintResponses.printADeck(deck, type);
    }

    private void showAllDeck() {
        PrintResponses.printActive();
        ArrayList<Deck> show = Deck.deckSort(USER.getDecks());
        int index = 0;
        if (show.size() > 0 && show.get(index).isActive()) {
            PrintResponses.printDeckShow(show.get(index));
            index++;
        }
        PrintResponses.printOther();
        for (int i = index; i < show.size(); i++) {
            PrintResponses.printDeckShow(show.get(i));
        }
    }

    private void removeCard(Matcher matcher) {
        String cardName = matcher.group("cardName").trim(), deckName = matcher.group("deckName");
        Deck deck = USER.getDeckByName(deckName);
        if (deck == null) {
            PrintResponses.printDeckNotExist(deckName);
            return;
        }
        Card card;
        try {
            String side = matcher.group("side");
            card = deck.checkCardInDeck(cardName, side);
            if (card == null) {
                PrintResponses.printNoCardTodDelete(cardName, side);
                return;
            }
            deck.removeCardFromSideDeck(card);
        } catch (Exception e) {
            String type = "main";
            card = deck.checkCardInDeck(cardName, type);
            if (card == null) {
                PrintResponses.printNoCardTodDelete(cardName, type);
                return;
            }
            deck.removeCardFromMainDeck(card);
        }
        PrintResponses.printSuccessfulCardRemoval();
    }

    private void addCard(Matcher matcher) {
        String cardName = matcher.group("cardName").trim(), deckName = matcher.group("deckName");
        cardName = cardName.replaceAll("\\s+-", "");
        Card card = Card.getCardByName(cardName);
        Deck deck = USER.getDeckByName(deckName);
        boolean doesHaveCard = false;
        for (Card userInGameCard : ProgramController.userInGame.getCards()) {
            if (userInGameCard.getName().equals(cardName)) {
                doesHaveCard = true;
                break;
            }
        }
        if (!doesHaveCard) {
            PrintResponses.printCardNotExist(cardName);
            return;
        }
        if (deck == null) {
            PrintResponses.printDeckNotExist(deckName);
            return;
        }
        try {
            String side = matcher.group("side");
            if (deck.getNumberOfSideDeckCards() == 15) {
                PrintResponses.printDeckFull(side);
                return;
            }
            if (invalidAdd(cardName, deckName, deck)) return;
            deck.addCardToSideDeck(card);
        } catch (Exception e) {
            String type = "main";
            if (deck.getNumberOfMainDeckCards() == 60) {
                PrintResponses.printDeckFull(type);
                return;
            }
            if (invalidAdd(cardName, deckName, deck)) return;
            deck.addCardToMainDeck(card);
        }
        DataController.saveData(USER);
        PrintResponses.printSuccessfulCardAddition();
    }

    private boolean invalidAdd(String cardName, String deckName, Deck deck) {
        if (deck.getCardNumber(cardName) >= 50) {
            PrintResponses.printInvalidAdd(cardName, deckName);
            return true;
        }
        return false;
    }

    private void activateDeck(Matcher matcher) {
        String name = matcher.group("deckName");
        Deck deck = getDeck(name);
        if (deck == null) return;
        deck.setActive(true);
        USER.deactivateDecks(name);
        PrintResponses.printSuccessfulDeckActivation();
    }

    private Deck getDeck(String name) {
        Deck deck = USER.getDeckByName(name);
        if (deck == null) {
            PrintResponses.printDeckNotExist(name);
            return null;
        }
        return deck;
    }

    private void deleteDeck(Matcher matcher) {
        String name = matcher.group("deckName");
        Deck deck = getDeck(name);
        if (deck == null) return;
        USER.removeDeck(deck);
        PrintResponses.printSuccessfulDeckDeletion();
    }

    private void createDeck(Matcher matcher) {
        String name = matcher.group("deckName");
        if (USER.getDeckByName(name) != null) {
            PrintResponses.printDeckExist(name);
            return;
        }
        Deck deck = new Deck(name);
        USER.addDeck(deck);
        PrintResponses.printSuccessfulDeckCreation();
        DataController.saveData(USER);
    }


    public void exitMenu() {
        ProgramController.currentMenu = new MainMenu();
    }


    public void showCurrentMenu() {
        PrintResponses.printDeckMenuShow();
    }

    @Override
    public void showMenu() throws IOException {
        ProgramController.createNewScene(getClass().getResource("/FXMLs/DeckMenu.fxml"));
        ProgramController.stage.show();
        HBox mainHBox = (HBox) ProgramController.currentScene.lookup("#mainHBox");
        USER.getActiveDeck();
        Rectangle activeDeck = new Rectangle(DECK_WIDTH, DECK_HIGHT);
        activeDeck.setFill(new ImagePattern(
                new Image(getClass().getResource("/images/deck.png").toExternalForm())));
        mainHBox.getChildren().add(activeDeck);
    }

    public void createDeck(MouseEvent mouseEvent) {

    }

    public void goToMainMenu() throws IOException {
        ProgramController.currentMenu = new MainMenu();
        ProgramController.createNewScene(getClass().getResource("/FXMLs/mainMenu.fxml"));
        ProgramController.stage.show();
    }
}
