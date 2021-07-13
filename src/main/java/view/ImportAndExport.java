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
import java.util.Objects;

public class ImportAndExport implements Menuable {

   /* public void exportCard() {
        if (cardToExport == null) {
            return;
        }
        Card card = Card.getCardByName(cardToExport);
        DataController.saveData(card);
        cardToExport = null;
    }

    public void importCard() {
        if (cardToImport == null) {
            return;
        }
        Card card = DataController.importCardFromJson(cardToImport.getPath());
        ProgramController.userInGame.addCard(card);
    }*/

}