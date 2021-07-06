package view;

import controller.ProgramController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import model.card.Monster;
import model.card.Spell;
import model.card.Trap;
import model.card.effects.Effect;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

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
    Stage Effects;

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

    public void chooseEffect(MouseEvent event) throws IOException {
        if (Effects == null){
            if (errorNotChosen()) return;
            Effects = new Stage();
            Parent parent = FXMLLoader.load(getClass().getResource("/FXMLs/effects.fxml"));
            Scene scene = new Scene(parent);
            Effects.setScene(scene);
            Effects.setTitle("Effects");
            Effects.setOnCloseRequest(Event::consume);
            Effects.show();
            HBox effects = (HBox) scene.lookup("#effects");
            VBox effectsVBox = new VBox();
            VBox attributeVBox = new VBox();
            ArrayList<String> effectsList = getEffectsList();
            int i;
            LinkedHashMap<String , VBox> groups = new LinkedHashMap<>();
             GetGroups(groups);
           for ( i = 0; i < effectsList.size(); i++) {
               CheckBox checkBox = new CheckBox();
               checkBox.setText(effectsList.get(i));
               checkBox.setLayoutX(20.0);
               int finalI = i;
               checkBox.setOnAction(new EventHandler<ActionEvent>() {
                   @Override
                   public void handle(ActionEvent event) {
                       if (checkForGroupSelection(checkBox , groups)) {
                           Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                           errorAlert.setHeaderText("Error!");
                           errorAlert.setContentText("You can choose multiple Options only from same group");
                           errorAlert.showAndWait();
                           checkBox.setSelected(false);
                           return;
                       }
                  //    if (checkBox.isSelected()) addGetAttribute(effectsList , finalI , checkBox , attributeVBox);
                  //    else removeGetAttribute(effectsList , finalI , attributeVBox);
                   }
               });
               addGroupsCheckBox(effectsList, i, groups, checkBox  , attributeVBox);
           }
            effectsVBox.setSpacing(30.0);
           attributeVBox.setSpacing(10.0);
            addGroupsEnd(effectsVBox, groups);
            effects.getChildren().add(effectsVBox);
            effects.getChildren().add(attributeVBox);
        }
    }


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

    private boolean checkForGroupSelection(CheckBox checkBox , LinkedHashMap<String, VBox> groups) {
        VBox vBox = (VBox) checkBox.getParent();
        for (String s : groups.keySet()) {
            if (groups.get(s) == vBox)continue;
            for (Node child : groups.get(s).getChildren()) {
                if (!(child instanceof CheckBox))continue;
                CheckBox checkBox1 = (CheckBox) child;
                if (checkBox1.isSelected())return true;
            }
        }
        return false;
    }

    private void addGroupsEnd(VBox effectsVBox, LinkedHashMap<String, VBox> groups) {
        for (String s : groups.keySet()) {
            effectsVBox.getChildren().add(groups.get(s));
            Line line = new Line();
            line.setEndX(50);
            effectsVBox.getChildren().add(line);
        }
    }

    private void addGroupsCheckBox(ArrayList<String> effectsList, int i, LinkedHashMap<String, VBox> groups,
                                   CheckBox checkBox , VBox attribute) {
        if (cardType.getText().equals("Monster")){
            if (effectsList.get(i).equals("isRitual")){
                groups.get("Ritual").getChildren().add(checkBox);
            }else groups.get("Effect").getChildren().add(checkBox);
        }else if (cardType.getText().equals("Spell")){
            if (effectsList.get(i).equals("canDestroyOpponentSpellAndTrap")){
                groups.get("Normal").getChildren().add(checkBox);
                groups.get("Quick").getChildren().add(checkBox);
                return;
            }
            String whichGroup = Spell.getWhichGroup(effectsList.get(i));
            groups.get(whichGroup).getChildren().add(checkBox);
        }else {
            String whichGroup = Trap.getWhichGroup(effectsList.get(i));
            groups.get(whichGroup).getChildren().add(checkBox);
        }
        VBox vBox = new VBox();
        attribute.getChildren().add(vBox);
        vBox.setSpacing(5);
        for (int j = 0; j < 3; j++) {
            TextField textField = new TextField();
            textField.setDisable(true);
            textField.setPrefWidth(40);
            textField.setPrefHeight(5);
            //    textField.setEffect(new GaussianBlur()); ;
            vBox.getChildren().add(textField);
        }
    }

    private void GetGroups(LinkedHashMap<String , VBox> groups) {
        if (cardType.getText().equals("Monster")) {
            VBox effect = getVBox("Effect");
            VBox ritual = getVBox("Ritual");
            addGroups(groups , effect , ritual);
        }else if (cardType.getText().equals("Spell")){
            VBox normal = getVBox("Normal");
            VBox continuous = getVBox("Continuous");
            VBox field = getVBox("Field");
            VBox equip = getVBox("Equip");
            VBox quick = getVBox("Quick");
            VBox ritual = getVBox("Ritual");
            addGroups(groups, normal, continuous, field, equip, quick, ritual);
        }else{
            VBox normal = getVBox("Normal");
            VBox counter = getVBox("Counter");
            VBox continuous = getVBox("Continuous");
            addGroups(groups , normal , counter , continuous);
        }
    }

    private void addGroups(HashMap<String, VBox> groups, VBox... args) {
        for (VBox arg : args) {
            groups.put(((Label)arg.getChildren().get(0)).getText() , arg);
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

    private void removeGetAttribute(ArrayList<String> effectsList, int finalI , VBox attribute) {
        String effect = effectsList.get(finalI);
        for (String zero : Effect.getEffectsWithNoSetter()) {
            if (effect.equals(zero)) return;
        }
        for (String one : Effect.getEffectsWithOneSetter()) {
            if (effect.equals(one)){
                System.out.println("HOYYYYYYYYYYY");
                VBox vBox = (VBox) attribute.getChildren().get(finalI + 1);
                vBox.getChildren().clear();
                System.out.println(vBox.getChildren().size());
            }
        }
    }

    private void addGetAttribute(ArrayList<String> effectsList, int finalI , CheckBox checkBox , VBox main) {
        String effect = effectsList.get(finalI);
            for (String zero : Effect.getEffectsWithNoSetter()) {
                if (effect.equals(zero)) return;
            }
        for (String one : Effect.getEffectsWithOneSetter()) {
            if (effect.equals(one)){
               TextField textField = new TextField();
               VBox vBox = (VBox) main.getChildren().get(finalI + 1);
               vBox.getChildren().add(textField);
            }
        }

    }


    private ArrayList<String> getEffectsList() {
        if (cardType.getText().equals("Monster")){
            return new ArrayList<>(Monster.getEffectsList());
        }else if (cardType.getText().equals("Spell")){
            return new ArrayList<>(Spell.getEffectsList());
        }
        return new ArrayList<>(Trap.getEffectsList());
    }
}
