package view;

import controller.DataController;
import controller.ProgramController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.card.Card;

import java.io.IOException;

public class AdminPanel extends Application {

    private static Stage panelStage;
    private static Scene currentPanelScene;
    private static String cardToChoose = null;
    public TextField count = new TextField();

    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        stage.setTitle("Admin Panel");
        panelStage = stage;
        Parent parent = FXMLLoader.load(getClass().getResource("/FXMLs/Panel.fxml"));
        Scene scene = new Scene(parent);
        panelStage.setScene(scene);
        currentPanelScene = scene;
        panelStage.show();
    }

    public static void main(String[] args) {
        ProgramController.allCards = DataController.getAllCards();
        launch(args);
    }

    public void shopPanel() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/FXMLs/ShopPanel.fxml"));
        Scene scene = new Scene(parent);
        scene.lookup("#buttonForbid").setDisable(true);
        scene.lookup("#buttonSetCount").setDisable(true);
        scene.lookup("#buttonUnForbid").setDisable(true);
        panelStage.setScene(scene);
        currentPanelScene = scene;
        panelStage.show();
    }

    public void toChoose() throws IOException {
        Stage stage = new Stage();
        Parent pane = FXMLLoader.load(getClass().getResource("/FXMLs/ChooseCard.fxml"));
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        ListView<VBox> listView = (ListView<VBox>) scene.lookup("#listOfCards");
        listView.setStyle("-fx-background-radius: 10;");
        for (String name : DataController.getAllCards().keySet()) {
            VBox vBox = new VBox();
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(20);
            hBox.getChildren().add(new Label(name));
            vBox.getChildren().add(hBox);
            listView.getItems().add(vBox);
        }
        listView.setOnMouseClicked(new EventHandler<>() {
            @Override
            public void handle(MouseEvent event1) {
                if (event1.getClickCount() == 2) {
                    VBox currentItemSelected = listView.getSelectionModel().getSelectedItem();
                    for (Node node : currentItemSelected.getChildren()) {
                        for (Node node1 : ((HBox) node).getChildren())
                            cardToChoose = ((Label) node1).getText();
                        ((Label) currentPanelScene.lookup("#selectedCard")).setText(cardToChoose);
                        currentPanelScene.lookup("#buttonForbid").setDisable(false);
                        currentPanelScene.lookup("#buttonSetCount").setDisable(false);
                        currentPanelScene.lookup("#buttonUnForbid").setDisable(false);
                    }
                    stage.close();
                }
            }
        });
        stage.show();
    }

    public void forbid() {
        DataController.toggleCanBuyCard(Card.getCardByName(cardToChoose), false);
    }

    public void setCount() {
        int toSet = Integer.parseInt(count.getText());
        DataController.setAmountForCard(Card.getCardByName(cardToChoose), toSet);
    }

    public void unForbid() {
        DataController.toggleCanBuyCard(Card.getCardByName(cardToChoose), true);
    }
}
