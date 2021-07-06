package view;

import controller.DataController;
import controller.ProgramController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.User;
import model.card.Card;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;

public class ShopMenu implements Menuable {
    public static ArrayList<String> cardsName;
    private Stage stageCheat = new Stage();
    public TextField moneyCheat = new TextField();

    public void showMenu() throws IOException {
        ProgramController.createNewScene(getClass().getResource("/FXMLs/shopMenu.fxml"));
        ((Label) ProgramController.currentScene.lookup("#coin")).setText(String.valueOf(ProgramController.userInGame.getCoins()));
        cardsName = new ArrayList<>(DataController.getAllCards().keySet());
        Collections.sort(cardsName);
        ListView<VBox> listView = (ListView<VBox>) ProgramController.currentScene.lookup("#showCards");
        for (String name : cardsName) {
            VBox vBox = new VBox();
            vBox.setStyle("-fx-background-color: gray;");
            vBox.setAlignment(Pos.CENTER);
            vBox.setSpacing(20);
            Rectangle cardPicture = new Rectangle(100, 150);
            cardPicture.setFill(new ImagePattern(new Image(getClass().getResource("/images/cards/" + name + ".jpg").toExternalForm())));
            vBox.getChildren().add(cardPicture);
            addLabelsToVBox(name, vBox);
            Button button = new Button("Buy");
            button.setPrefWidth(100);
            button.setOnMouseClicked(event -> buy(DataController.getAllCards().get(name)));
            if (ProgramController.userInGame.getCoins() < Card.getCardByName(name).getPrice())
                button.setDisable(true);
            vBox.getChildren().add(button);
            listView.getItems().add(vBox);
        }
        ProgramController.stage.show();
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
        ((Label) ProgramController.currentScene.lookup("#coin")).setText(String.valueOf(ProgramController.userInGame.getCoins()));
    }

    public void back() throws IOException {
        ProgramController.currentMenu = new MainMenu();
        ProgramController.currentMenu.showMenu();
    }

    public void showIncreaseMoneyStage() throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource("/FXMLs/IncreaseMoney.fxml"));
        Scene scene = new Scene(pane);
        stageCheat.setScene(scene);
        stageCheat.show();
    }

    public void increaseMoney() {
        stageCheat.close();
        int amount = Integer.parseInt(moneyCheat.getText());
        ProgramController.userInGame.setCoins(ProgramController.userInGame.getCoins() + amount);
        ((Label) ProgramController.currentScene.lookup("#coin")).setText(String.valueOf(ProgramController.userInGame.getCoins()));
    }
}
