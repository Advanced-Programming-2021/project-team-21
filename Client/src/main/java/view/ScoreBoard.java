package view;

import controller.ProgramController;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.User;
import model.message.Message;
import model.message.MessageInstruction;
import model.message.MessageLabel;
import model.message.MessageTag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.stream.Collectors;

public class ScoreBoard implements Menuable {

    private String[] getRanks(ArrayList<User> users) {
        String[] ranks = new String[users.size()];
        ranks[0] = "1";
        for (int i = 1; i < users.size(); i++) {
            if (users.get(i).getScore() == users.get(i - 1).getScore()) {
                ranks[i] = ranks[i - 1];
            } else {
                ranks[i] = String.valueOf(i + 1);
            }
        }
        return ranks;
    }

    @Override
    public void showMenu() throws IOException {

        ProgramController.createNewScene(getClass().getResource("/FXMLs/ScoreboardMenu.fxml"));
        ProgramController.stage.show();
        VBox mainVBox = (VBox) ProgramController.currentScene.lookup("#mainVBox");
        Message message = new Message(MessageInstruction.USER, MessageLabel.ALL , MessageTag.TOKEN);
        message.setTagsInOrder(ProgramController.currentToken);
        AppController.sendMessageToServer(message);
        LinkedHashMap<User , Boolean> users =  (LinkedHashMap<User , Boolean>) AppController.receiveMessageFromServer();
        String[] ranks = getRanks(new ArrayList<>(Objects.requireNonNull(users).keySet()));
        for (int i = 0; i < ranks.length; i++) {
            mainVBox.getChildren().add(getHBoxForUser(users, ranks , i));
        }

    }

    private HBox getHBoxForUser(LinkedHashMap<User , Boolean> users, String[] ranks , int i) {
        String rank = ranks[i];
        User user =( new ArrayList<>(users.keySet())).get(i);
        HBox hBox = new HBox();
        Message message = new Message(MessageInstruction.USER , MessageLabel.GET);
        message.setTagsInOrder(ProgramController.currentToken);
        AppController.sendMessageToServer(message);
        User userInGame = (User) AppController.receiveMessageFromServer();
        if (user.getUsername().equals(Objects.requireNonNull(userInGame).getUsername()))
            hBox.getStyleClass().add("scoreboard-user-in-game");
        else
            hBox.getStyleClass().add("scoreboard-users");
        Circle circle = new Circle( 10);
        for (User user1 : users.keySet()) {
            if (user1.getUsername().equals(user.getUsername())){
                if (users.get(user1))circle.setFill(Color.FIREBRICK);
                else circle.setFill(Color.WHITE);
            }
        }
        hBox.setAlignment(Pos.CENTER);
        hBox.setMaxWidth(350);
        ArrayList<Node> labelsForUser = getLabelsForUser(user, rank);
        ImageView avatar = new ImageView(new Image(String.valueOf(getClass().getResource(user.getAvatar()))));
        avatar.setFitHeight(50);
        avatar.setFitWidth(50);
        hBox.getChildren().add(circle);
        hBox.getChildren().add(avatar);
        ((Label) labelsForUser.get(0)).setMinWidth(30);
        ((Label) labelsForUser.get(1)).setMinWidth(200);
        ((Label) labelsForUser.get(2)).setMinWidth(60);
        hBox.getChildren().addAll(labelsForUser);
        hBox.setSpacing(20);
        return hBox;
    }

    private ArrayList<Node> getLabelsForUser(User user, String rank) {
        ArrayList<Node> labels = new ArrayList<>();
        labels.add(new Label(rank + "."));
        labels.add(new Label(user.getNickname()));
        labels.add(new Label(String.valueOf(user.getScore())));
        return labels;
    }

    public void back() throws IOException {
        ProgramController.currentMenu = new MainMenu();
        ProgramController.currentMenu.showMenu();
    }

    public void handleBackToMenu() throws IOException {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        ProgramController.createNewScene(getClass().getResource("/FXMLs/mainMenu.fxml"));
        ProgramController.stage.show();
    }
}
