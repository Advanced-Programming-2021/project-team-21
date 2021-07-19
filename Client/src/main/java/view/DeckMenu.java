package view;

import controller.DataController;
import controller.ProgramController;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.Deck;
import model.User;
import model.card.Card;
import model.message.Message;
import model.message.MessageInstruction;
import model.message.MessageLabel;
import model.message.MessageTag;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class DeckMenu implements Menuable {
    private static Deck deckToShow;
//    private final User USER = ProgramController.userInGame;
    private final int CARD_WIDTH = 80, CARD_HEIGHT = 90;
    private final ArrayList<Animation> delays = new ArrayList<>();
    private final ArrayList<Stage> stages = new ArrayList<>();
    private boolean canEnlargeCard = true;
    public static HashMap<String , String> paths = new HashMap<>();

    private void deleteCard(Card card, String mainOrSide) {
        Deck deck = deckToShow;
        if (mainOrSide.equals("main")) {
            deck.removeCardFromMainDeck(card);
        } else {
            deck.removeCardFromSideDeck(card);
        }
        editDeckDetails(deckToShow);
    }

    private boolean addCardsToMainDeck(ArrayList<Card> cards) {
        boolean canNotAdd = canAddCard(cards, deckToShow.getMainDeckCards());
        if (canNotAdd)
            return false;

        if (deckToShow.getNumberOfMainDeckCards() == 60) {
            showErrorForAddingCards("main deck is full");
            return false;
        }
        cards.forEach(card -> deckToShow.addCardToMainDeck(card));
//        DataController.saveData(USER); todo to fix
        return true;
    }

    private boolean addCardsToSideDeck(ArrayList<Card> selectedCards) {
        boolean canNotAdd = canAddCard(selectedCards, deckToShow.getSideDeckCards());
        if (canNotAdd)
            return false;
        if (deckToShow.getNumberOfSideDeckCards() == 15) {
            showErrorForAddingCards("side deck is full");
            return false;
        }
        selectedCards.forEach(card -> deckToShow.addCardToSideDeck(card));
//        DataController.saveData(USER); todo to fix
        return true;
    }

    private boolean canAddCard(ArrayList<Card> cards, ArrayList<Card> mainOrSideDeckCards) {
        boolean canNotAdd = false;
        StringBuilder cardsMoreThan3 = new StringBuilder();
        for (Card card : cards) {
            int counter = (int) mainOrSideDeckCards.stream().filter(mainDeckCard -> mainDeckCard.getName().equals(card.getName())).count();
            if (counter >= 3) {
                cardsMoreThan3.append(card.getName()).append(", ");
                canNotAdd = true;
            }
        }
        if (cardsMoreThan3.length() != 0) {
            cardsMoreThan3.deleteCharAt(cardsMoreThan3.length() - 1);
            cardsMoreThan3.deleteCharAt(cardsMoreThan3.length() - 1);
            showErrorForAddingCards("You already have 3 instances of these cards:\n" + cardsMoreThan3);
        }
        return canNotAdd;
    }

    private void showErrorForAddingCards(String errorText) {
        Alert alert = new Alert(Alert.AlertType.ERROR, errorText);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("/CSS/CSS.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("dialog-pane");
        alert.showAndWait();
    }


    private void activateDeck(Deck deckToActivate) {
        if (deckToActivate == null) return;
        Message message = new Message(MessageInstruction.DECK, MessageLabel.ACTIVATE, MessageTag.TOKEN,MessageTag.NAME);
        message.setTagsInOrder(ProgramController.currentToken, deckToActivate.getName());
        AppController.sendMessageToServer(message);
        String result = (String) AppController.receiveMessageFromServer();
        if (result != null && !result.startsWith("Error"))
            reloadMenu();
    }

    private void reloadMenu() {
        try {
            showMenu();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void deleteDeck(Deck deck) {
        if (deck == null) return;
        Message message = new Message(MessageInstruction.DECK,MessageLabel.DELETE, MessageTag.TOKEN ,MessageTag.NAME);
        message.setTagsInOrder(ProgramController.currentToken, deck.getName());
        AppController.sendMessageToServer(message);
        String result = (String) AppController.receiveMessageFromServer();
        if (result != null && !result.startsWith("Error"))
            reloadMenu();
    }



    private boolean canCreateDeck(String name) {
        Message message = new Message(MessageInstruction.DECK, MessageLabel.CREATE, MessageTag.TOKEN ,MessageTag.NAME);
        message.setTagsInOrder(ProgramController.currentToken ,name);
        AppController.sendMessageToServer(message);
        String result = (String) AppController.receiveMessageFromServer();
        return !(result != null && result.startsWith("Error"));
    }


    @Override
    public void showMenu() throws IOException {
        ProgramController.createNewScene(getClass().getResource("/FXMLs/DeckMenu.fxml"));
        ProgramController.stage.show();
        HBox mainHBox = (HBox) ProgramController.currentScene.lookup("#mainHBox");
        ListView<VBox> listView = new ListView<>();
        listView.getStyleClass().add("list-view");
        listView.setOrientation(Orientation.HORIZONTAL);
        listView.setMinWidth(500);
        Message message = new Message(MessageInstruction.DECK, MessageLabel.ALL, MessageTag.TOKEN);
        message.setTagsInOrder(ProgramController.currentToken);
        AppController.sendMessageToServer(message);
        ArrayList<Deck> decks = (ArrayList<Deck>) AppController.receiveMessageFromServer();
        if (decks != null)
            placeAllDecks(mainHBox, listView, decks);
    }

    private void placeAllDecks(HBox mainHBox, ListView<VBox> listView, ArrayList<Deck> decks) {
        for (Deck deck : decks) {
            ContextMenu menu = new ContextMenu();
            VBox vBox = new VBox();
            if (deck.isActive())
                vBox.setEffect(new Glow(0.9));
            handleContextMenuWhenRightClicked(deck, menu, vBox);
            vBox.setAlignment(Pos.CENTER);
            int DECK_HEIGHT = 100;
            int DECK_WIDTH = 90;
            Rectangle deckPicture = new Rectangle(DECK_WIDTH, DECK_HEIGHT);
            deckPicture.setFill(new ImagePattern(new Image(Objects.requireNonNull(getClass().getResource("/images/deck.png")).toExternalForm())));
            Label deckName = new Label(deck.getName());
            deckName.setTextFill(Color.WHITE);
            deckName.setStyle("-fx-font-size: 15pt ;");
            if (deck.isActive())
                deckName.setStyle("-fx-text-fill: rgb(255, 228, 73);-fx-font-size: 15pt ;");
            vBox.getChildren().addAll(deckPicture, deckName);
            listView.getItems().add(vBox);
        }
        mainHBox.getChildren().add(listView);
    }

    private void handleContextMenuWhenRightClicked(Deck deck, ContextMenu menu, VBox vBox) {
        MenuItem activateDeck = new MenuItem("Activate"),
                editDeck = new MenuItem("Edit"),
                deleteDeck = new MenuItem("Delete");
        if (deck.isActive())
            activateDeck.setDisable(true);
        menu.getItems().addAll(activateDeck, editDeck, deleteDeck);
        activateDeck.setOnAction(event -> activateDeck(deck));
        editDeck.setOnAction(event -> editDeckDetails(deck));
        deleteDeck.setOnAction(event -> deleteDeck(deck));
        vBox.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                menu.show(vBox, event.getScreenX(), event.getScreenY());
            } else if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                editDeckDetails(deck);
            } else if (event.getButton() == MouseButton.PRIMARY)
                menu.hide();
        });
    }

    private void editDeckDetails(Deck deck) {
        try {
            deckToShow = deck;
            ProgramController.createNewScene(getClass().getResource("/FXMLs/DeckDetails.fxml"));
            ProgramController.currentScene.addEventFilter(MouseEvent.MOUSE_MOVED, event -> closeAllStages());
            ((Label) ProgramController.currentScene.lookup("#deckName")).setText(deck.getName());
            ProgramController.currentScene.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                if (event.getButton() != MouseButton.SECONDARY)
                    canEnlargeCard = true;
            });
            ArrayList<ListView<Rectangle>> mainListViews = getMainListViews();
            for (int i = 0; i < mainListViews.size(); i++) {
                for (int j = 0; j < Math.min(30, deck.getNumberOfMainDeckCards() - i * 30); j++) {
                    mainListViews.get(i).getItems().add(getCardToShow(deck.getMainDeckCards(), j + i * 30, "main"));
                }
            }
            ListView<Rectangle> sideListView = (ListView<Rectangle>) ProgramController.currentScene.lookup("#sideDeck");
            for (int i = 0; i < deck.getSideDeckCards().size(); i++) {
                sideListView.getItems().add(getCardToShow(deck.getSideDeckCards(), i, "side"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Rectangle getCardToShow(ArrayList<Card> cards, int index, String mainOrSide) throws FileNotFoundException {
        Rectangle card = new Rectangle(CARD_WIDTH, CARD_HEIGHT);
        card.setOnMouseEntered(event -> enlargeCardPicture(card, event));
        card.setOnMouseClicked(event -> openContextMenuForCards(cards, index, card, mainOrSide, event));
        String cardImageAddress = "/images/cards/" + cards.get(index).getName() + ".jpg";
        ImagePattern cardPicture;
        try {
            cardPicture = new ImagePattern(new Image(Objects.requireNonNull(getClass().getResource(cardImageAddress)).toExternalForm()));
        }catch (Exception e){
             cardPicture = new ImagePattern(new Image(new FileInputStream(paths.get(cards.get(index).getName()))));
        }

        card.setFill(cardPicture);
        return card;
    }

    private void openContextMenuForCards(ArrayList<Card> cards, int index, Rectangle card, String mainOrSide, MouseEvent event) {
        ContextMenu menu = new ContextMenu();
        if (event.getButton() == MouseButton.SECONDARY && event.getClickCount() == 1) {
            canEnlargeCard = false;
            MenuItem deleteCard = new MenuItem("Delete");
            menu.getItems().add(deleteCard);
            deleteCard.setOnAction(e -> deleteCard(cards.get(index), mainOrSide));
            menu.show(card, event.getScreenX(), event.getScreenY());
        } else {
            menu.hide();
            canEnlargeCard = true;
        }
    }

    private ArrayList<ListView<Rectangle>> getMainListViews() {
        ArrayList<ListView<Rectangle>> mainDeckHBoxes = new ArrayList<>();
        mainDeckHBoxes.add((ListView<Rectangle>) ProgramController.currentScene.lookup("#firstRow"));
        mainDeckHBoxes.add((ListView<Rectangle>) ProgramController.currentScene.lookup("#secondRow"));
        return mainDeckHBoxes;
    }


    public void createDeck() {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        TextField name = new TextField();
        int notToBeDuplicate = 150;
        name.setMaxWidth(notToBeDuplicate);
        name.setPromptText("Name");
        Button submitButton = new Button("Submit"), closeButton = new Button("Close");
        closeButton.setOnAction(e -> {
            stage.close();
            ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        });
        closeButton.getStyleClass().add("buttonEntrance");
        submitButton.getStyleClass().add("buttonEntrance");
        Platform.runLater(submitButton::requestFocus);
        int notToBeDuplicate2 = 40;
        HBox hbox = new HBox();
        hbox.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/CSS.css")).toExternalForm());
        hbox.setSpacing(notToBeDuplicate2);
        hbox.getChildren().addAll(closeButton, submitButton);
        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: rgb(255, 237, 137);");
        borderPane.setCenter(name);
        borderPane.setBottom(hbox);
        int notBeDuplicate3 = 0;
        ColorAdjust adj = new ColorAdjust(notBeDuplicate3, -0.9, -0.5, 0);
        GaussianBlur blur = new GaussianBlur(notToBeDuplicate2 - 15); // 55 is just to show edge effect more clearly.
        adj.setInput(blur);
        ProgramController.currentScene.getRoot().setEffect(blur);
        name.setAlignment(Pos.CENTER);
        hbox.setAlignment(Pos.BOTTOM_CENTER);

        submitButton.setOnAction(e -> {
            ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
            if (canCreateDeck(name.getText())) {
                stage.close();
            } else
                name.setStyle("-fx-text-fill: rgb(250, 0, 0);");
        });
        name.focusedProperty().addListener((obs, oldValue, newValue) -> name.setStyle("-fx-text-fill: black"));
        createNewScene(borderPane, stage);
    }

    private void createNewScene(BorderPane borderPane, Stage stage) {
        Scene scene = new Scene(borderPane, 250, 150);
        stage.setTitle("Create New Deck");
        stage.setScene(scene);
        stage.showAndWait();
        reloadMenu();
    }


    public void goToMainMenu() throws IOException {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        ProgramController.currentMenu = new MainMenu();
        ProgramController.createNewScene(getClass().getResource("/FXMLs/mainMenu.fxml"));
        ProgramController.stage.show();
    }

    public void addCardToDeck() throws FileNotFoundException {
        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: rgba(155, 155, 155, 0.877);");
        ColorAdjust adj = new ColorAdjust(0, 0, 0.5, 0);
        GaussianBlur blur = new GaussianBlur(25); // 55 is just to show edge effect more clearly.
        adj.setInput(blur);
        ProgramController.currentScene.getRoot().setEffect(adj);

        borderPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/CSS.css")).toExternalForm());
        Scene scene = new Scene(borderPane, 500, 250);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        borderPane.getChildren().clear();
        ListView<Rectangle> userCardsListView = new ListView<>();
        userCardsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        userCardsListView.setMaxWidth(490);
        userCardsListView.setOrientation(Orientation.HORIZONTAL);
        ArrayList<Card> selectedCards = new ArrayList<>();
        HashMap<Rectangle, Card> allUserCards = getRectangleCardHashMap(userCardsListView);
        Button backButton = new Button("Back");
        addVisualComponentsForAddingCardToDeck(borderPane, stage, userCardsListView, selectedCards, allUserCards, backButton);

    }

    private void addVisualComponentsForAddingCardToDeck(BorderPane borderPane, Stage stage, ListView<Rectangle> userCardsListView, ArrayList<Card> selectedCards, HashMap<Rectangle, Card> allUserCards, Button backButton) {
        backButton.setOnMouseClicked(event -> {
            editDeckDetails(deckToShow);
            stage.close();
        });
        Button addCardsToMainDeck = new Button("Add Cards To Main Deck");
        addCardsToMainDeck.setOnMouseClicked(event -> {
            userCardsListView.getSelectionModel().getSelectedItems().stream().map(allUserCards::get).forEach(selectedCards::add);
            if (addCardsToMainDeck(selectedCards)) {
                stage.close();
                editDeckDetails(deckToShow);
            }

        });
        Button addCardsToSideDeck = new Button("Add Cards To Side Deck");
        addCardsToSideDeck.setOnMouseClicked(event -> {
            userCardsListView.getSelectionModel().getSelectedItems().stream().map(allUserCards::get).forEach(selectedCards::add);
            if (addCardsToSideDeck(selectedCards)) {
                stage.close();
                editDeckDetails(deckToShow);
            }
        });
        borderPane.setCenter(userCardsListView);
        backButton.getStyleClass().add("buttonEntrance");
        addCardsToMainDeck.getStyleClass().add("buttonEntrance");
        addCardsToSideDeck.getStyleClass().add("buttonEntrance");
        HBox buttonHBox = new HBox(backButton, addCardsToSideDeck, addCardsToMainDeck);
        buttonHBox.setSpacing(10);
        buttonHBox.setAlignment(Pos.CENTER);
        borderPane.setBottom(buttonHBox);
    }

    private HashMap<Rectangle, Card> getRectangleCardHashMap(ListView<Rectangle> userCardsListView) throws FileNotFoundException {
        HashMap<Rectangle, Card> rectangleCardHashMap = new HashMap<>();
        Message message = new Message(MessageInstruction.DECK, MessageLabel.AVAILABLE_CARDS, MessageTag.TOKEN);
        message.setTagsInOrder(ProgramController.currentToken);
        AppController.sendMessageToServer(message);
        ArrayList<Card> userCards = (ArrayList<Card>) AppController.receiveMessageFromServer();
        if (userCards == null)
            return rectangleCardHashMap;
        for (Card card : userCards) {
            Rectangle cardPicture = new Rectangle(CARD_WIDTH * 2, CARD_HEIGHT * 2);
            try {
                cardPicture.setFill(new ImagePattern(new Image(card.getCardImageAddress())));
            }catch (Exception e){
                cardPicture.setFill(new ImagePattern(new Image(new FileInputStream(paths.get(card.getName())))));
            }
            userCardsListView.getItems().add(cardPicture);
            rectangleCardHashMap.put(cardPicture, card);
        }
        return rectangleCardHashMap;
    }


    public void deleteDeckWithWarning() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this deck?");
        alert.getDialogPane();
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("/CSS/CSS.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("dialog-pane");
        alert.initStyle(StageStyle.UNDECORATED);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            deleteDeck(deckToShow);
            reloadMenu();
        }
    }

    private void enlargeCardPicture(Rectangle rectangle, MouseEvent mouseEvent) {
        if (!canEnlargeCard)
            return;
        Animation delay = new PauseTransition(Duration.seconds(1));
        Stage stage = new Stage();
        stage.setX(mouseEvent.getScreenX());
        stage.setY(mouseEvent.getScreenY());
        int notToBeDuplicate = 400;
        stage.initStyle(StageStyle.UNDECORATED);
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane, 300, notToBeDuplicate);
        delay.setOnFinished(e -> {
            delays.forEach(Animation::stop);
            stages.forEach(Stage::close);
            Rectangle enlargedPicture = new Rectangle(300, notToBeDuplicate);
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

    public void goToDeckMenu() {
        reloadMenu();
    }

    public void closeAllStages() {
        stages.forEach(Stage::close);
    }
}
