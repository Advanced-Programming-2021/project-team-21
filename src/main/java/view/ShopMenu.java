package view;

import controller.DataController;
import controller.ProgramController;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;

public class ShopMenu implements Menuable {
    public static ArrayList<String> cardsName;
    public TextField moneyCheat = new TextField();
    private HashMap<String, Label> countToUpdate = new HashMap<>();
    private static HashMap<String, Button> buttonToUpdate = new HashMap<>();
    private final ArrayList<Animation> delays = new ArrayList<>();
    private final ArrayList<Stage> stages = new ArrayList<>();
    private boolean canEnlargeCard = true;

    public void showMenu() throws IOException {
        ProgramController.createNewScene(getClass().getResource("/FXMLs/shopMenu.fxml"));
        ((Label) ProgramController.currentScene.lookup("#coin")).setText("Coins : " + String.valueOf(ProgramController.userInGame.getCoins()));
        ProgramController.currentScene.addEventFilter(MouseEvent.MOUSE_MOVED, event -> closeAllStages());
        cardsName = new ArrayList<>(DataController.getAllCards().keySet());
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
            cardPicture.setFill(new ImagePattern(new Image(getClass().getResource("/images/cards/" + name + ".jpg").toExternalForm())));
            cardPicture.setOnMouseEntered(event -> enlargeCardPicture(cardPicture, event));
            vBox.getChildren().add(cardPicture);
            addLabelsToVBox(name, vBox);
            Button button = new Button("Buy");
            button.setPrefWidth(100);
            button.setOnMouseClicked(event -> buy(DataController.getAllCards().get(name)));
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
        if (!canEnlargeCard)
            return;
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
        for (String name : cardsName) {
            if (Card.getCardByName(name).getPrice() > ProgramController.userInGame.getCoins()) {
                buttonToUpdate.get(name).setDisable(true);
                buttonToUpdate.get(name).setStyle("-fx-background-color: rgb(212, 29, 29,0.877);");
            }
            else {
                buttonToUpdate.get(name).setDisable(false);
                buttonToUpdate.get(name).setStyle("");
            }
        }
    }


    private void addLabelsToVBox(String name, VBox vBox) {
        Label labelName = new Label(name);
        labelName.setStyle("-fx-text-fill: white;");
        vBox.getChildren().add(labelName);
        int countBought = 0;
        for (Card card : ProgramController.userInGame.getCards())
            if (card.getName().equals(name))
                countBought++;
        Label labelCount = new Label("Count : " + String.valueOf(countBought));
        countToUpdate.put(name, labelCount);
        labelCount.setStyle("-fx-text-fill: white;");
        vBox.getChildren().add(labelCount);
        Label labelPrice = new Label("Price : " + String.valueOf(Card.getCardByName(name).getPrice()));
        labelPrice.setStyle("-fx-text-fill: white;");
        vBox.getChildren().add(labelPrice);
    }

    private void buy(Card card) {
        User user = ProgramController.userInGame;
        ProgramController.userInGame.addCard(card);
        user.setCoins(user.getCoins() - (card != null ? card.getPrice() : 0));
        ((Label) ProgramController.currentScene.lookup("#coin")).setText(String.valueOf("Coins : " + ProgramController.userInGame.getCoins()));
        int countBought = 0;
        for (Card cardToCheck : ProgramController.userInGame.getCards())
            if (cardToCheck.getName().equals(card.getName()))
                countBought++;
        countToUpdate.get(card.getName()).setText("Count : " + String.valueOf(countBought));
        updateButtons();
    }

    public void back() throws IOException {
        ProgramController.currentMenu = new MainMenu();
        ProgramController.currentMenu.showMenu();
    }

    public void showIncreaseMoneyStage() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        TextField amount = new TextField();
        amount.setMaxWidth(150);
        amount.setPromptText("Amount");
        Button submitButton = new Button("Submit"), closeButton = new Button("Close");
        closeButton.setOnAction(e -> {
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
        ((Label) ProgramController.currentScene.lookup("#coin")).setText("Coins : " + String.valueOf(ProgramController.userInGame.getCoins()));
        updateButtons();
    }
}
