package view;

import controller.DataController;
import controller.ProgramController;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.card.Card;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImportAndExport implements Menuable {
    private static String cardToExport = null;
    private static File cardToImport = null;
    private static final ArrayList<Animation> delays = new ArrayList<>();
    private static final ArrayList<Stage> stages = new ArrayList<>();
    private boolean canEnlargeCard = true;

    private void clearPreviousErrors() {
        ((Label) ProgramController.currentScene.lookup("#errorExport")).setText("");
        ProgramController.currentScene.lookup("#errorExport").setStyle("");
        ((Label) ProgramController.currentScene.lookup("#errorChooseExport")).setText("");
        ((Label) ProgramController.currentScene.lookup("#errorImport")).setText("");
        ProgramController.currentScene.lookup("#errorImport").setStyle("");
        ((Label) ProgramController.currentScene.lookup("#errorChooseImport")).setText("");
    }

    public void exportCard() {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        clearPreviousErrors();
        if (cardToExport == null) {
            ((Label) ProgramController.currentScene.lookup("#errorExport")).setText("*:You must fill these fields!");
            ProgramController.currentScene.lookup("#errorExport").setStyle("-fx-border-color: black;");
            ((Label) ProgramController.currentScene.lookup("#errorChooseExport")).setText("*");
            return;
        }
        Card card = Card.getCardByName(cardToExport);
        DataController.saveData(card);
        cardToExport = null;
        ((Label) ProgramController.currentScene.lookup("#errorExport")).setText("Card successfully exported!");
        ProgramController.currentScene.lookup("#errorExport").setStyle("-fx-text-fill: green;");
    }

    public void importCard() {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        clearPreviousErrors();
        if (cardToImport == null) {
            ((Label) ProgramController.currentScene.lookup("#errorImport")).setText("*:You must fill these fields!");
            ProgramController.currentScene.lookup("#errorImport").setStyle("-fx-border-color: black;");
            ((Label) ProgramController.currentScene.lookup("#errorChooseImport")).setText("*");
            return;
        }
        Card card = DataController.importCardFromJson(cardToImport.getPath());
        ProgramController.userInGame.addCard(card);
        ((Label) ProgramController.currentScene.lookup("#errorImport")).setText("Card successfully imported!");
        ProgramController.currentScene.lookup("#errorImport").setStyle("-fx-text-fill: green;");
    }


    @Override
    public void showMenu() throws IOException {
        ProgramController.createNewScene(getClass().getResource("/FXMLs/ImportAndExport.fxml"));
        ProgramController.currentScene.addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
            closeAllStages();
        });
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


    public void toExport() throws IOException {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        Stage stage = new Stage();
        Parent pane = FXMLLoader.load(getClass().getResource("/FXMLs/ChooseCardToExport.fxml"));
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
        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event1) {
                    if (event1.getClickCount() == 2) {
                        VBox currentItemSelected = listView.getSelectionModel().getSelectedItem();
                        String name = null;
                        for (Node node : currentItemSelected.getChildren()) {
                            for (Node node1 : ((HBox) node).getChildren())
                                cardToExport = ((Label) node1).getText();
                            ((Label) ProgramController.currentScene.lookup("#cardExport")).setText("");
                            Rectangle cardPicture = ((Rectangle) ProgramController.currentScene.lookup("#imageExport"));
                            cardPicture.setFill(new ImagePattern(new Image(getClass().getResource("/images/cards/" + cardToExport + ".jpg").toExternalForm())));
                            cardPicture.setOnMouseEntered(event -> enlargeCardPicture(cardPicture, event));
                        }
                        stage.close();
                    }
                }
            });
        stage.show();
    }

    public void back() throws IOException {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        ProgramController.currentMenu = new MainMenu();
        ProgramController.currentMenu.showMenu();
    }


    public void toImport(MouseEvent mouseEvent) {
        try {
            ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Json Files (*.Json)",
                    "*.Json");
            fileChooser.getExtensionFilters().add(extensionFilter);
            fileChooser.setSelectedExtensionFilter(extensionFilter);
            Stage stage = new Stage();
            File json = fileChooser.showOpenDialog(stage);
            cardToImport = json;
            ((Label) ProgramController.currentScene.lookup("#cardImport")).setText("");
            Rectangle cardPicture = ((Rectangle) ProgramController.currentScene.lookup("#imageImport"));
            cardPicture.setFill(new ImagePattern(new Image(getClass().getResource("/images/cards/" + DataController.importCardFromJson(cardToImport.getPath()).getName() + ".jpg").toExternalForm())));
            cardPicture.setOnMouseEntered(event1 -> enlargeCardPicture(cardPicture, event1));
        }
        catch (Exception e) {

        }

    }
}