package view;

import controller.DataController;
import controller.Effects.EffectsHolder;
import controller.ProgramController;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.card.Card;
import model.card.Monster;
import model.card.Spell;
import model.card.Trap;
import model.card.effects.Effect;
import model.card.enums.Attributes;
import model.card.enums.MonsterTypes;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CreateCard {
    public TextField description;
    public Label Name;
    public MenuButton cardType;
    public AnchorPane cardCreator;
    public TextField name;
    Scene sceneSave;
    int atk;
    int defense;
    TextField atkSet;
    TextField defenseSet;
    ChoiceBox<String> monsterTypes;
    ChoiceBox<String> attribute;
    File image;
    Stage effectsStage;
    EffectsHolder effectsHolder;
    StringBuilder finalEffect;
    String effectsGroup;
    HashMap<TextField, Integer> setters;
    private String cardTypeString;
    private int price;
    private int priceEffect;
    private final HashMap<String, String> CHOSEN_EFFECTS;
    private final ArrayList<Animation> delays = new ArrayList<>();
    private final ArrayList<Stage> stages = new ArrayList<>();


    {
        finalEffect = new StringBuilder();
        setters = new HashMap<>();
        CHOSEN_EFFECTS = new HashMap<>();
    }

    public void chooseImage() {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        FileChooser fileChooser = new FileChooser();
        String string = "Open Resource File";
        fileChooser.setTitle(string);
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("JPG Files (*.jpg)",
                "*.jpg");
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setSelectedExtensionFilter(extensionFilter);
        Stage stage = new Stage();
        image = fileChooser.showOpenDialog(stage);
    }

    @Override
    public void showMenu() throws IOException {
        ProgramController.createNewScene(getClass().getResource("/FXMLs/createCard.fxml"));
        ProgramController.stage.show();
    }

    public void chooseEffect() throws IOException {
        if (effectsStage == null) {
            if (errorNotChosen()) return;
            effectsStage = new Stage();
            Parent parent = FXMLLoader.load(getClass().getResource("/FXMLs/effects.fxml"));
            Scene scene = new Scene(parent);
            sceneSave = scene;
            effectsStage.setScene(scene);
            effectsStage.setResizable(false);
            effectsStage.setTitle("Effects");
            effectsStage.getScene().addEventFilter(MouseEvent.MOUSE_MOVED, event2 -> closeAllStages());

            effectsStage.setOnCloseRequest(event1 -> {
                ButtonType aContinue = new ButtonType("continue");
                ButtonType exit = new ButtonType("exit");
                ButtonType save = new ButtonType("save");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                        "If you exit now your progress will not be saved",
                        aContinue, exit, save);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.getDialogPane().getStylesheets().add(getClass().getResource("/CSS/CSS.css").toExternalForm());
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == exit) {
                    effectsStage.close();
                } else if (result.isPresent() && result.get() == save) {
                    event1.consume();
                    saveEffects();
                    effectsStage.close();
                } else {
                    event1.consume();
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

            userChoiceBox.getItems().addAll();
        }
    }

    private void showEffectsCheckBoxesFromGroup(String effectGroup, Scene scene) {
        effectsGroup = effectGroup;
        ArrayList<String> effectsList = getEffectsList();
        VBox mainVBox = (VBox) scene.lookup("#mainVBox");
        mainVBox.getChildren().remove(1, mainVBox.getChildren().size());
        for (String effect : effectsList) {
            if ((cardTypeString.equals("Monster") && Monster.getWhichGroup(effect).equals(effectGroup)
                    || (cardTypeString.equals("Spell") && ((effect.equals("canDestroyOpponentSpellAndTrap") && effectGroup.equals("Quick"))
                    || Spell.getWhichGroup(effect).equals(effectGroup)))
                    || (cardTypeString.equals("Trap") && Trap.getWhichGroup(effect).equals(effectGroup)))) {
                getEffectVBox(mainVBox, effect);
            }
        }
    }

    private void getEffectVBox(VBox mainVBox, String effect) {
        VBox effectVbox = new VBox();
        effectVbox.setSpacing(15);
        effectVbox.setAlignment(Pos.CENTER);
        HBox textFieldsHBox = new HBox();
        textFieldsHBox.setAlignment(Pos.CENTER);
        CheckBox checkBox = new CheckBox();
        checkBox.setText(effect);
        checkBox.setPrefWidth(300);
        checkBox.setStyle("-fx-text-fill: white");
        effectVbox.setOnMouseEntered(event -> showDetails(effect, event));
        checkBox.setOnAction(event -> {
            if (checkBox.isSelected()) {
                for (TextField setter : setters.keySet()) {
                    if (setter.getText().equals("")) {
                        alert("You should enter all the fields of an effect" +
                                " before checking another one");
                        checkBox.setSelected(false);
                        return;
                    }
                }
            }
            if (effectsHolder.getDescription().get(checkBox.getText()).get(0).equals("")) {
                checkIsEffectFinished(checkBox, textFieldsHBox);
            }
            checkBoxAction(checkBox);
        });
        for (int j = 0; j < 3; j++) {
            TextField textField = new TextField();
            textField.setDisable(true);
            textField.setPrefWidth(70);
            textField.setPrefHeight(5);
            textField.setEffect(new GaussianBlur());
            int finalJ = j;
            textField.setOnKeyReleased(event -> {
                if (textField.isDisable()) return;
                if (textField.getText().equals("")) setters.remove(textField);
                else setters.put(textField, finalJ);
                if (event.getCode().equals(KeyCode.ENTER)) {
                    checkIsEffectFinished(checkBox, textFieldsHBox);
                }
                findOutPrice();
            });
            textFieldsHBox.getChildren().add(textField);
        }
        effectVbox.getChildren().addAll(checkBox, textFieldsHBox);
        mainVBox.getChildren().add(effectVbox);
    }

    private void checkIsEffectFinished(CheckBox checkBox, HBox textFieldsHBox) {
        for (Node child : textFieldsHBox.getChildren()) {
            TextField textField = (TextField) child;
            if (!textField.isDisable() && textField.getText().equals("")) return;
        }
        String effectName = checkBox.getText();
        if (checkForValidEntry(effectName, textFieldsHBox)) {
            alert("You should enter a valid entry otherwise the effect will not be created");
            return;
        }
        StringBuilder effect = new StringBuilder(effectName + "=");
        for (int i = 0; i < textFieldsHBox.getChildren().size(); i++) {
            if (effectsHolder.getDescription().get(effectName).get(0).equals("")) {
                effect.append("1_0");
                break;
            }
            TextField textField = (TextField) textFieldsHBox.getChildren().get(i);
            if (textField.isDisable()) continue;
            if (effectsHolder.getDescription().get(effectName).size() == 1) {
                if (effectName.equals("summonACardFromEveryWhere")) {
                    effect.append("1_\"0\"_\"").append(textField.getText()).append("\"");
                }
                if (effectName.equals("notDestroyable") || effectName.equals("undefeatable") ||
                        effectName.equals("canChangeFaceOFOpponent") || effectName.equals("canMakeMonstersUndefeatable")) {
                    effect.append("1_").append(Integer.parseInt(textField.getText()) + 1);
                } else {
                    effect.append(Integer.parseInt(textField.getText()) + 1).append("_0");
                }
            } else if (effectsHolder.getDescription().get(effectName).size() == 2) {
                effect.append(Integer.parseInt(textField.getText()) + 1);
                if (i == 0) effect.append("_");
            } else {
                if (i != 2) effect.append(Integer.parseInt(textField.getText() + 1)).append("_");
                else effect.append("\"").append(textField.getText()).append("\"");
            }
        }
        CHOSEN_EFFECTS.put(effectName, effect.toString());
    }

    private boolean checkForValidEntry(String effectName, HBox textFieldsHBox) {
        for (Node child : textFieldsHBox.getChildren()) {
            TextField textField = (TextField) child;
            if (textField.isDisable()) continue;
            if (!textField.getText().matches("-?\\d+") && !textField.getPromptText().equals("type")) {
                return true;
            }
            if (textField.getPromptText().equals("type") && !textField.getText().matches("[A-Za-z -]") &&
                    !checkForMonsterType(textField.getText())) {
                return true;
            }
            if (!textField.getPromptText().equals("type") && Integer.parseInt(textField.getText()) < 0 &&
                    !Effect.getEffectsThatCanHaveMinusEntry().contains(effectName)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkForMonsterType(String text) {
        return MonsterTypes.monsterTypes().contains(text);
    }

    private void findOutPrice() {
        int wholePrice = 0;
        for (TextField setter : setters.keySet()) {
            int number = effectsHolder.getPrice().get(((CheckBox) ((VBox) setter.getParent().getParent()).getChildren().get(0)).getText()).get(setters.get(setter));
            if (setter.getText().matches("\\d+")) {
                wholePrice += number * Integer.parseInt(setter.getText());
            }
        }
        changePrice(String.valueOf(wholePrice), "effect");
    }

    private void checkBoxAction(CheckBox checkBox) {
        for (int i = 0; i < ((HBox) checkBox.getParent().getChildrenUnmodifiable().get(1)).getChildren().size(); i++) {
            TextField textField = (TextField) ((HBox) checkBox.getParent().getChildrenUnmodifiable().
                    get(1)).getChildren().get(i);
            if (i >= effectsHolder.getDescription().get(checkBox.getText()).size()) continue;
            if (effectsHolder.getDescription().get(checkBox.getText()).get(i).equals("") ||
                    checkBox.getText().equals("summonACardFromEveryWhere")) {
                if (checkBox.isSelected())
                    changePrice(String.valueOf(effectsHolder.getPrice().get(checkBox.getText()).get(0)), "one");
                else changePrice(String.valueOf(-effectsHolder.getPrice().get(checkBox.getText()).get(0)), "one");
                if (!checkBox.getText().equals("summonACardFromEveryWhere")) continue;
            }
            textField.setPromptText(effectsHolder.getDescription().get(checkBox.getText()).get(i));
            if (checkBox.isSelected()) {
                textField.setDisable(false);
                textField.setEffect(null);
                setters.put(textField, i);
            } else {
                textField.setDisable(true);
                textField.setEffect(new GaussianBlur());
                setters.remove(textField);
            }
        }
    }

    private String[] getGroupNames() {
        if (cardType.getText().equals("Monster")) {
            cardTypeString = "Monster";
            effectsHolder = DataController.monster;
            return new String[]{"Normal", "Ritual"};
        } else if (cardType.getText().equals("Spell")) {
            cardTypeString = "Spell";
            effectsHolder = DataController.spell;
            return new String[]{"Normal", "Continuous", "Quick", "Field", "Equip", "Ritual"};
        } else {
            effectsHolder = DataController.trap;
            cardTypeString = "Trap";
            return new String[]{"Normal", "Counter", "Continuous"};
        }
    }


    public void setMonster(ActionEvent actionEvent) {
        cardType.setText(((MenuItem) actionEvent.getSource()).getText());
        atkSet = new TextField();
        setTextField(atkSet, 20.0, "Attack");
        defenseSet = new TextField();
        setTextField(defenseSet, 105.0, "Defense");
        cardCreator.getChildren().add(atkSet);
        cardCreator.getChildren().add(defenseSet);
        monsterTypes = new ChoiceBox<>();
        for (String monsterType : MonsterTypes.monsterTypes()) {
            monsterTypes.getItems().add(monsterType);
        }
        monsterTypes.setValue("Monster type");
        monsterTypes.setLayoutX(195.0);
        monsterTypes.setLayoutY(110.0);
        cardCreator.getChildren().add(monsterTypes);
        attribute = new ChoiceBox<>();
        for (String type : Attributes.getAttributes()) {
            attribute.getItems().add(type);
        }
        attribute.setValue("Attribute");
        attribute.setLayoutX(320.0);
        attribute.setLayoutY(110.0);
        cardCreator.getChildren().add(attribute);
    }

    private void setTextField(TextField setter, double v, String text) {
        setter.setLayoutX(v);
        setter.setLayoutY(110.0);
        setter.setPrefHeight(10.0);
        setter.setPrefWidth(75.0);
        setter.setPromptText(text);
        setter.setStyle("-fx-background-radius: 10");
        setter.setOnKeyTyped(event -> changePrice(setter.getText(), setter.getPromptText()));
    }

    private void changePrice(String text, String type) {
        if (type.equals("Attack") || type.equals("Defense")) {
            try {
                if (type.equals("Attack")) atk = Integer.parseInt(text);
                else defense = Integer.parseInt(text);
                price = (int) getNumber(atk) + (int) getNumber(defense) + priceEffect;
            } catch (Exception e) {
                alert("Enter a valid number for Attack and Defense");
            }
        } else if (type.equals("one")) {
            price += Integer.parseInt(text);
        } else {
            price -= priceEffect;
            priceEffect = Integer.parseInt(text);
            price += priceEffect;
            price = Math.max(0, price);
        }
        ((Label) ProgramController.currentScene.lookup("#priceLabel")).setText(String.valueOf(price));
    }

    private double getNumber(int number) {
        return Math.max(0, ((number - 500) * Math.pow(2.5, ((number - 500) / 2000.0) - 1 / 4.0)));
    }


    public void setSpellAndTrap(ActionEvent actionEvent) {
        cardType.setText(((MenuItem) actionEvent.getSource()).getText());
        if (atkSet != null) {
            cardCreator.getChildren().remove(atkSet);
            cardCreator.getChildren().remove(defenseSet);
            cardCreator.getChildren().remove(monsterTypes);
            cardCreator.getChildren().remove(attribute);
        }
    }

    private boolean errorNotChosen() {
        if (cardType.getText().equals("card type")) {
            alert("Please choose the card type first");
            return true;
        }
        return false;
    }

    private void alert(String context) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.initStyle(StageStyle.UNDECORATED);
        errorAlert.getDialogPane().getStylesheets().add(getClass().getResource("/CSS/CSS.css").toExternalForm());
        errorAlert.setContentText(context);
        errorAlert.showAndWait();
    }


    private ArrayList<String> getEffectsList() {
        if (cardType.getText().equals("Monster")) {
            return new ArrayList<>(Monster.getEffectsList());
        } else if (cardType.getText().equals("Spell")) {
            return new ArrayList<>(Spell.getEffectsList());
        }
        return new ArrayList<>(Trap.getEffectsList());
    }

    public void saveEffects() {
        for (int i = 0; i < CHOSEN_EFFECTS.keySet().size(); i++) {
            finalEffect.append(CHOSEN_EFFECTS.get((String) CHOSEN_EFFECTS.keySet().toArray()[i]));
            if (i != CHOSEN_EFFECTS.size() - 1) finalEffect.append("*");
        }
    }

    public void create() {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        if (name.getText().equals("") || cardType.getText().equals("card type") || image == null ||
                description.getText().equals("") || (cardType.getText().equals("Monster") &&
                (atkSet.getText().equals("") || defenseSet.getText().equals("") ||
                        monsterTypes.getValue().equals("Monster type") || attribute.getValue().equals("Attribute")))
                || ((!cardType.getText().equals("Monster")) &&
                finalEffect.length() == 0)) {
            alert("you have not set all the attributes");
            return;
        }
        if (ProgramController.userInGame.getCoins() < price) {
            alert("You do not have enough coins to create this card");
            return;
        }
        if (Card.getCardByName(name.getText()) != null) {
            alert("There is card with this name in the game");
            return;
        }
        if (!cardType.getText().equals("Monster")) {
            DataController.addSpellAndTrap(cardTypeString, name.getText(), effectsGroup, description.getText(),
                    price, finalEffect.toString(), image);
        } else {
            if (finalEffect.length() == 0) effectsGroup = "Normal";
            getBooleans(finalEffect);
            DataController.addMonster(name.getText(), getLevel(atkSet, defenseSet), attribute.getValue(),
                    monsterTypes.getValue(), effectsGroup, atk, defense, description.getText(), price,
                    getBooleans(finalEffect), finalEffect, image);
        }
        ProgramController.userInGame.setCoins(ProgramController.userInGame.getCoins() - price);
        ProgramController.allCards = DataController.getAllCards();
    }

    private String getBooleans(StringBuilder finalEffect) {
        String[] effects = finalEffect.toString().split("\\*");
        ArrayList<String> booleanMap = new ArrayList<>();
        for (String effect : effects) {
            String nameEffect = effect.replaceAll("=-?\\d+_-?\\d+", "");
            booleanMap.addAll(Monster.getBoolean(nameEffect));
        }
        StringBuilder booleanCSV = new StringBuilder();
        Set<String> set = new HashSet<>(booleanMap);
        booleanMap.clear();
        booleanMap.addAll(set);
        for (int i = 0; i < booleanMap.size(); i++) {
            booleanCSV.append(booleanMap.get(i));
            if (i != booleanMap.size() - 1) booleanCSV.append("-");
        }
        return booleanCSV.toString();
    }

    private String getLevel(TextField atkSet, TextField defenseSet) {
        int atk = Integer.parseInt(atkSet.getText()), def = Integer.parseInt(defenseSet.getText());
        return String.valueOf(((atk + def) / 750) + 1);
    }

    public void back() throws IOException {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        ProgramController.currentMenu = new MainMenu();
        ProgramController.currentMenu.showMenu();
    }

    private void showDetails(String effectName, MouseEvent mouseEvent) {
        Animation delay = new PauseTransition(Duration.seconds(1));
        int notToBeDuplicate = 150;
        Stage stage = new Stage();
        stage.setX(mouseEvent.getScreenX());
        stage.setY(mouseEvent.getScreenY());
        stage.initStyle(StageStyle.UNDECORATED);
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane, notToBeDuplicate, 100);
        delay.setOnFinished(e -> {
            delays.forEach(Animation::stop);
            stages.forEach(Stage::close);
            Label description = new Label(effectsHolder.getEffect().get(effectName));
            borderPane.setCenter(description);
            stage.setScene(scene);
            stage.show();
            stages.add(stage);
            delays.add(delay);
        });
        delay.play();
        ((VBox)mouseEvent.getSource()).setOnMouseExited(event -> delay.stop());
    }

    public void closeAllStages() {
        stages.forEach(Stage::close);
    }

}
