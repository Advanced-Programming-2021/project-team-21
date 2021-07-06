package view;

import controller.ProgramController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.card.Monster;
import model.card.Spell;
import model.card.Trap;
import model.card.effects.Effect;
import view.Menuable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;

public class CreateCard implements Menuable {
    public Label Name;
    public MenuButton cardType;
    public AnchorPane cardCreator;
    public TextField name;
    Label atk;
    Label defense;
    TextField atkSet;
    TextField defenseSet;
    File image;
    Stage effectsStage;
    private String cardTypeString;


    public void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("JPG Files (*.jpg)",
                "*.jpg");
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setSelectedExtensionFilter(extensionFilter);
        Stage stage = new Stage();
        image = fileChooser.showOpenDialog(stage);
        System.out.println(image);
    }

    @Override
    public void showMenu() throws IOException {
        ProgramController.createNewScene(getClass().getResource("/FXMLs/createCard.fxml"));
        ProgramController.stage.show();
    }

    public void chooseEffect(MouseEvent event) throws IOException {
        if (effectsStage == null){
            if (errorNotChosen()) return;
            effectsStage = new Stage();
        Parent parent = FXMLLoader.load(getClass().getResource("/FXMLs/effects.fxml"));
        Scene scene = new Scene(parent);
        effectsStage.setScene(scene);
        effectsStage.setTitle("Effects");
        effectsStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    ButtonType aContinue = new ButtonType("continue");
                    ButtonType exit = new ButtonType("exit");
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "If you exit now your progress will not be saved",
                            aContinue, exit);
                    alert.setTitle("Game");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == exit) {
                        effectsStage.close();
                    }else {
                        event.consume();
                    }
                }
            });
        effectsStage.show();
        ChoiceBox userChoiceBox = (ChoiceBox) scene.lookup("#effectChoiceBox");
        String[] groupNames = getGroupNames();

        // create a choiceBox
        userChoiceBox.setItems(FXCollections.observableArrayList(groupNames));
        // add a listener
        // if the item of the list is changed
        userChoiceBox.getSelectionModel().selectedIndexProperty().addListener((ov, value, new_value) -> showEffectsCheckBoxesFromGroup(groupNames[new_value.intValue()], scene));

        userChoiceBox.getItems().addAll();}
    }

    private void showEffectsCheckBoxesFromGroup(String effectGroup, Scene scene) {
        ArrayList<String> effectsList = getEffectsList();
        VBox mainVBox = (VBox) scene.lookup("#mainVBox");
        mainVBox.getChildren().remove(1, mainVBox.getChildren().size());
        for (String effect : effectsList) {
            if ((cardTypeString.equals("Monster") && Monster.getWhichGroup(effect).equals(effectGroup)
                    || (cardTypeString.equals("Spell") &&( (effect.equals("canDestroyOpponentSpellAndTrap") && effectGroup.equals("Quick"))
                            ||Spell.getWhichGroup(effect).equals(effectGroup)))
                    || (cardTypeString.equals("Trap") && Trap.getWhichGroup(effect).equals(effectGroup)))) {
                getEffectVBox(mainVBox, effect);
            }
        }
    }

    private void getEffectVBox(VBox mainVBox, String effect) {
        VBox effectVbox = new VBox();
        CheckBox checkBox = new CheckBox();
        checkBox.setOnAction(event -> {
                for (Node child : ((HBox) checkBox.getParent().getChildrenUnmodifiable().get(1)).getChildren()) {
                    //todo able text fields and setEffects in according to effect Type
                }
        });
        checkBox.setText(effect);
        HBox textFieldsHBox = new HBox();
        for (int j = 0; j < 3; j++) {
            TextField textField = new TextField();
            textField.setDisable(true);
            textField.setPrefWidth(50);
            textField.setPrefHeight(5);
            textField.setEffect(new GaussianBlur());
            textFieldsHBox.getChildren().add(textField);
        }
        effectVbox.getChildren().addAll(checkBox, textFieldsHBox);
        mainVBox.getChildren().add(effectVbox);
    }

    private String[] getGroupNames() {
        if (cardType.getText().equals("Monster")) {
            cardTypeString = "Monster";
            return new String[]{"Normal", "Ritual"};
        } else if (cardType.getText().equals("Spell")) {
            cardTypeString = "Spell";
            return new String[]{"Normal", "Continuous", "Quick", "Field", "Equip", "Ritual"};
        } else {
            cardTypeString = "Trap";
            return new String[]{"Normal", "Counter", "Continuous"};
        }
    }


    public void setMonster(ActionEvent actionEvent) {
        cardType.setText(((MenuItem) actionEvent.getSource()).getText());
        atk = new Label();
        setLabel(atk, "Attack:", 5.0);
        atkSet = new TextField();
        setTextField(atkSet, 55.0);
        defense = new Label();
        setLabel(defense, "Defense:", 105.0);
        defenseSet = new TextField();
        setTextField(defenseSet, 165.0);
        cardCreator.getChildren().add(atk);
        cardCreator.getChildren().add(atkSet);
        cardCreator.getChildren().add(defense);
        cardCreator.getChildren().add(defenseSet);
    }

    private void setTextField(TextField setter, double v) {
        setter.setLayoutX(v);
        setter.setLayoutY(100.0);
        setter.setPrefHeight(10.0);
        setter.setPrefWidth(45.0);
        setter.setStyle("-fx-background-radius: 10");
    }

    private void setLabel(Label atk, String string, double v) {
        atk.setText(string);
        atk.setStyle("-fx-font-size: 15;-fx-text-fill: red;\n" +
                "    -fx-underline: true;\n" +
                "    -fx-background-color: GOLD;");
        atk.setLayoutX(v);
        atk.setLayoutY(100.0);
    }

    public void setSpellAndTrap(ActionEvent actionEvent) {
        cardType.setText(((MenuItem) actionEvent.getSource()).getText());
        if (atk != null) {
            cardCreator.getChildren().remove(atk);
            cardCreator.getChildren().remove(defense);
            cardCreator.getChildren().remove(atkSet);
            cardCreator.getChildren().remove(defenseSet);
        }
    }

//    public void chooseEffect(MouseEvent event) throws IOException {
//        if (Effects == null){
//            if (errorNotChosen()) return;
//            Effects = new Stage();
//            Parent parent = FXMLLoader.load(getClass().getResource("/FXMLs/effects.fxml"));
//            Scene scene = new Scene(parent);
//            Effects.setScene(scene);
//            Effects.setTitle("Effects");
//            Effects.setOnCloseRequest(Event::consume);
//            Effects.show();
//            HBox effects = (HBox) scene.lookup("#effects");
//            VBox effectsVBox = new VBox();
//            VBox attributeVBox = new VBox();
//            ArrayList<String> effectsList = getEffectsList();
//            int i;
//            LinkedHashMap<String , VBox> groups = new LinkedHashMap<>();
//             GetGroups(groups);
//           for ( i = 0; i < effectsList.size(); i++) {
//               CheckBox checkBox = new CheckBox();
//               checkBox.setText(effectsList.get(i));
//               checkBox.setLayoutX(20.0);
//               int finalI = i;
//               checkBox.setOnAction(new EventHandler<ActionEvent>() {
//                   @Override
//                   public void handle(ActionEvent event) {
//                       if (checkForGroupSelection(checkBox , groups)) {
//                           Alert errorAlert = new Alert(Alert.AlertType.ERROR);
//                           errorAlert.setHeaderText("Error!");
//                           errorAlert.setContentText("You can choose multiple Options only from same group");
//                           errorAlert.showAndWait();
//                           checkBox.setSelected(false);
//                           return;
//                       }
//                  //    if (checkBox.isSelected()) addGetAttribute(effectsList , finalI , checkBox , attributeVBox);
//                  //    else removeGetAttribute(effectsList , finalI , attributeVBox);
//                   }
//               });
//               addGroupsCheckBox(effectsList, i, groups, checkBox  , attributeVBox);
//           }
//            effectsVBox.setSpacing(30.0);
//           attributeVBox.setSpacing(10.0);
//            addGroupsEnd(effectsVBox, groups);
//            effects.getChildren().add(effectsVBox);
//            effects.getChildren().add(attributeVBox);
//        }
//    }


    private boolean errorNotChosen() {
        if (cardType.getText().equals("card type")) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error!");
            errorAlert.setContentText("Please choose the card type first");
            errorAlert.showAndWait();
            return true;
        }
        return false;
    }




    private void addGroups(HashMap<String, VBox> groups, VBox... args) {
        for (VBox arg : args) {
            groups.put(((Label) arg.getChildren().get(0)).getText(), arg);
            arg.setSpacing(75);
        }
    }

    private VBox getVBox(String effect2) {
        VBox effect = new VBox();
        Label label = new Label();
        label.setText(effect2);
        effect.getChildren().add(label);
        effect.setSpacing(30.0);
        return effect;
    }



    private ArrayList<String> getEffectsList() {
        if (cardType.getText().equals("Monster")) {
            return new ArrayList<>(Monster.getEffectsList());
        } else if (cardType.getText().equals("Spell")) {
            return new ArrayList<>(Spell.getEffectsList());
        }
        return new ArrayList<>(Trap.getEffectsList());
    }
}
