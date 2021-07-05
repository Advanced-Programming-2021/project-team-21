package view;

import controller.DataController;
import controller.ProgramController;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import model.User;
import model.card.Card;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;

public class ShopMenu implements Menuable {
    public static ArrayList<String> cardsName;

    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getMatcher(command, Regex.buyACard)).find()){

        }

    }


    public void showMenu() throws IOException {
        ProgramController.createNewScene(getClass().getResource("/FXMLs/shopMenu.fxml"));
        ((Label) ProgramController.currentScene.lookup("#coin")).setText(String.valueOf(ProgramController.userInGame.getCoins()));
        cardsName = new ArrayList<>(DataController.getAllCards().keySet());
        Collections.sort(cardsName);
        ListView<VBox> listView = ((ListView<VBox>) ProgramController.currentScene.lookup("#showCards"));
        for (String name : cardsName) {
            VBox vBox = new VBox();
            vBox.setStyle("-fx-background-color: gray;");
            vBox.setAlignment(Pos.CENTER);
            vBox.setSpacing(20);
            Rectangle cardPicture = new Rectangle(100, 150);
            cardPicture.setFill(new ImagePattern(new Image(getClass().getResource("/images/cards/" + name + ".jpg").toExternalForm())));
            vBox.getChildren().add(cardPicture);
            vBox.getChildren().add(new Label(name));
            //vBox.getChildren().add(new Label("Price : " + String.valueOf(Card.getCardByName(name).getPrice())));
            Button button = new Button("Buy");
            button.setOnMouseClicked(event -> buy(DataController.getAllCards().get(name)));
            vBox.getChildren().add(button);
            listView.getItems().add(vBox);
        }




        ProgramController.stage.show();
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

}
