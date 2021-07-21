package view;

import controller.DataController;
import controller.ProgramController;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.User;
import model.card.Card;
import model.message.Message;
import model.message.MessageInstruction;
import model.message.MessageLabel;
import model.message.MessageTag;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

public class ShopMenu implements Menuable {
    public static HashMap<String , String> paths = new HashMap<>();
    public static ArrayList<String> cardsName = new ArrayList<>();
    public TextField moneyCheat = new TextField();
    private final HashMap<String, Label> countToUpdate = new HashMap<>();
    private static final HashMap<String, Button> buttonToUpdate = new HashMap<>();
    private final ArrayList<Animation> delays = new ArrayList<>();
    private final ArrayList<Stage> stages = new ArrayList<>();
    public TextField cardToAuction = new TextField();
    public TextField instantBuy = new TextField();
    public TextField time = new TextField();

    public void showMenu() throws IOException {
        ProgramController.createNewScene(getClass().getResource("/FXMLs/shopMenu.fxml"));
        Message messageGetUser = new Message(MessageInstruction.USER, MessageLabel.GET , MessageTag.TOKEN);
        messageGetUser.setTagsInOrder(ProgramController.currentToken);
        AppController.sendMessageToServer(messageGetUser);
        User user = (User) AppController.receiveMessageFromServer();
        ((Label) ProgramController.currentScene.lookup("#coin")).setText("Coins : " + user.getCoins());
        ProgramController.currentScene.addEventFilter(MouseEvent.MOUSE_MOVED, event -> closeAllStages());
        Message message = new Message(MessageInstruction.SHOP, MessageLabel.ALL , MessageTag.TOKEN);
        message.setTagsInOrder(ProgramController.currentToken);
        AppController.sendMessageToServer(message);
        HashMap<String, Card> cards = (HashMap<String, Card>) AppController.receiveMessageFromServer();
        cardsName.clear();
        cardsName.addAll(cards.keySet());
        Collections.sort(cardsName);
        ListView<VBox> listView = (ListView<VBox>) ProgramController.currentScene.lookup("#showCards");
        listView.setStyle("-fx-background-radius: 10;");
        for (String name : cardsName) {
            VBox vBox = new VBox();
            vBox.setPrefWidth(120);
            vBox.setStyle("-fx-background-color: gray;");
            vBox.setAlignment(Pos.CENTER);
            vBox.setSpacing(20);
            Rectangle cardPicture = new Rectangle(100, 150);
            try {
                cardPicture.setFill(new ImagePattern(new Image(getClass().getResource("/images/cards/" + name + ".jpg").toExternalForm())));
            }catch (Exception e){
                cardPicture.setFill(new ImagePattern(new Image(new FileInputStream(paths.get(name)))));
            }
            cardPicture.setOnMouseEntered(event -> enlargeCardPicture(cardPicture, event));
            vBox.getChildren().add(cardPicture);
            addLabelsToVBox(name, vBox, cards, user);
            Button button = new Button("Buy");
            button.setPrefWidth(100);
            if (!cards.get(name).isCanBuyCard() || cards.get(name).getAmountInShop() == 0) {
                vBox.setStyle("-fx-background-color: red");
                button.setDisable(true);
            }
            button.setOnMouseClicked(event -> {
                if (cards.get(name).getAmountInShop() == 0 || !cards.get(name).isCanBuyCard()){
                    button.setDisable(true);
                    return;
                }
                buy(DataController.getAllCards().get(name));
            });
            button.getStyleClass().add("buttonEntrance");
            buttonToUpdate.put(name, button);
            vBox.getChildren().add(button);
            listView.getItems().add(vBox);
        }
        updateButtons();
        ProgramController.stage.show();
    }

    private void closeAllStages() {
        stages.forEach(Stage::close);
    }

    private void enlargeCardPicture(Rectangle rectangle, MouseEvent mouseEvent) {
        Animation delay = new PauseTransition(Duration.seconds(1));
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

    private void updateButtons() {
        Message messageGetUser = new Message(MessageInstruction.USER, MessageLabel.GET , MessageTag.TOKEN);
        messageGetUser.setTagsInOrder(ProgramController.currentToken);
        AppController.sendMessageToServer(messageGetUser);
        User user = (User) AppController.receiveMessageFromServer();
        for (String name : cardsName) {
            if (Objects.requireNonNull(Card.getCardByName(name)).getPrice() > user.getCoins()) {
                buttonToUpdate.get(name).setDisable(true);
                buttonToUpdate.get(name).setStyle("-fx-background-color: rgb(212, 29, 29,0.877);");
            }
            else {
                buttonToUpdate.get(name).setDisable(false);
                buttonToUpdate.get(name).setStyle("");
            }
        }
    }


    private void addLabelsToVBox(String name, VBox vBox, HashMap<String, Card> cards, User user) {
        Message messageGetUser = new Message(MessageInstruction.USER, MessageLabel.GET , MessageTag.TOKEN);
        messageGetUser.setTagsInOrder(ProgramController.currentToken);
        Label labelName = new Label(name);
        labelName.setStyle("-fx-text-fill: white;");
        vBox.getChildren().add(labelName);
        int countBought = 0;
        for (Card card : user.getCards())
            if (card.getName().equals(name))
                countBought++;
        Label labelCount = new Label("Count : " + cards.get(name).getAmountInShop());
        countToUpdate.put(name, labelCount);
        labelCount.setStyle("-fx-text-fill: white;");
        vBox.getChildren().add(labelCount);
        Label labelPrice = new Label("Price : " + Objects.requireNonNull(Card.getCardByName(name)).getPrice());
        labelPrice.setStyle("-fx-text-fill: white;");
        vBox.getChildren().add(labelPrice);
    }

    private void buy(Card card) {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        Message message = new Message(MessageInstruction.SHOP, MessageLabel.BUY , MessageTag.CARD, MessageTag.TOKEN);
        message.setTagsInOrder(card.getName(), ProgramController.currentToken);
        AppController.sendMessageToServer(message);
        String result = (String) AppController.receiveMessageFromServer();
        if (result != null && !result.startsWith("Error")) {
            ((Label) ProgramController.currentScene.lookup("#coin")).setText("Coins : " + (Integer.parseInt(((Label) ProgramController.currentScene.lookup("#coin")).getText().substring(8)) - card.getPrice()));
            countToUpdate.get(card.getName()).setText("Count : " + (Integer.parseInt(countToUpdate.get(card.getName()).getText().substring(8)) - 1));
        }
        updateButtons();
    }

    public void back() throws IOException {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        ProgramController.currentMenu = new MainMenu();
        ProgramController.currentMenu.showMenu();
    }

    public void showIncreaseMoneyStage() {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        TextField amount = new TextField();
        amount.setMaxWidth(150);
        amount.setPromptText("Amount");
        Button submitButton = new Button("Submit"), closeButton = new Button("Close");
        closeButton.setOnAction(e -> {
            ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
            stage.close();
            ProgramController.currentScene.getRoot().setEffect(null);
        });
        closeButton.getStyleClass().add("buttonEntrance");
        submitButton.getStyleClass().add("buttonEntrance");
        Platform.runLater(submitButton::requestFocus);

        HBox hbox = new HBox();
        hbox.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/CSS.css")).toExternalForm());
        hbox.setSpacing(40);
        hbox.getChildren().addAll(closeButton, submitButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: rgb(255, 237, 137);");
        borderPane.setCenter(amount);
        borderPane.setBottom(hbox);
        ColorAdjust adj = new ColorAdjust(0, -0.9, -0.5, 0);
        GaussianBlur blur = new GaussianBlur(25); // 55 is just to show edge effect more clearly.
        adj.setInput(blur);
        ProgramController.currentScene.getRoot().setEffect(blur);
        amount.setAlignment(Pos.CENTER);
        hbox.setAlignment(Pos.BOTTOM_CENTER);

        submitButton.setOnAction(e -> {
            ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
            increaseMoney(Integer.parseInt(amount.getText()));
            stage.close();
            ProgramController.currentScene.getRoot().setEffect(null);
        });
        amount.focusedProperty().addListener((obs, oldValue, newValue) -> amount.setStyle("-fx-text-fill: black"));
        createNewScene(borderPane, stage);
    }

    private void createNewScene(BorderPane borderPane, Stage stage) {
        Scene scene = new Scene(borderPane, 250, 150);
        stage.setTitle("Increase money");
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void increaseMoney(int amount) {
        ProgramController.userInGame.setCoins(ProgramController.userInGame.getCoins() + amount);
        ((Label) ProgramController.currentScene.lookup("#coin")).setText("Coins : " + ProgramController.userInGame.getCoins());
        updateButtons();
    }

    public void auction() throws IOException {
        ProgramController.createNewScene(getClass().getResource("/FXMLs/Auction.fxml"));
        ProgramController.stage.show();
    }

    public void setAuction() throws IOException {
        ProgramController.createNewScene(getClass().getResource("/FXMLs/SetAuction.fxml"));
        ProgramController.stage.show();
    }

    public void seeAuction() {
        //TODO to get the list of cards on Auction
        //Button buttonRefresh = new Button("refresh");
        //buttonRefresh.setOnMouseClicked(->seeAuction());
       // ListView<VBox> listView;
       // ArrayList<Card> cards;
       // for (Card card : cards) {
         // VBox vBox = new VBox();
          //vBox.getChildren().add(new Label(card.getName()));
            //vBox.getChildren().add(new Label(instantBuy));
            //vbox.getChildren().add(new Label(timeLeft));
             //Button button = new Button("offer");
            //vbox.getChildren.add(button);
            //button.setOnMouseCLicked(-> setAnOffer);
            // listView.add(vbox);

       // }
    }

    public void doSetAuction() {
        String cardName = cardToAuction.getText();
        String instantToBuy = instantBuy.getText();
        String timeToEnd = time.getText();
        //TODO send message to server
    }
}
