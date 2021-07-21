package view;

import controller.ProgramController;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Pair;
import model.User;
import model.message.Message;
import model.message.MessageInstruction;
import model.message.MessageLabel;
import model.message.MessageTag;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ChatBox implements Menuable {
    @Override
    public void showMenu() throws IOException {

        Message message = new Message(MessageInstruction.CHAT, MessageLabel.ALL, MessageTag.TOKEN);
        message.setTagsInOrder(ProgramController.currentToken);
        AppController.sendMessageToServer(message);
        HashMap<LocalDateTime, Pair<User, String>> messages = (HashMap<LocalDateTime, Pair<User, String>>)AppController.receiveMessageFromServer();
        ProgramController.createNewScene(getClass().getResource("/FXMLs/ChatBox.fxml"));
        ProgramController.stage.show();
        VBox mainVBox = (VBox) ProgramController.currentScene.lookup("#mainVBox");
        for (int i = 0; i < messages.size(); i++) {
            mainVBox.getChildren().add(getHBoxForUser(messages, i));
        }
        HBox hBox = new HBox();
        TextField textField = new TextField();
        textField.setPromptText("Message");
        Button button = new Button();
        button.setText("send");
        button.setOnMouseClicked(event -> {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            Message message1 = new Message(MessageInstruction.CHAT, MessageLabel.GET,
                    MessageTag.TOKEN , MessageTag.MESSAGE , MessageTag.TIME);
            message1.setTagsInOrder(ProgramController.currentToken , textField.getText(), dtf.format(now));
            AppController.sendMessageToServer(message1);
            try {
                showMenu();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        hBox.getChildren().addAll(textField , button);
        mainVBox.getChildren().add(hBox);
    }

    private HBox getHBoxForUser(HashMap<LocalDateTime, Pair<User, String>> messages, int i) {
        User user = messages.get((new ArrayList<>(messages.keySet())).get(i)).getKey();
        HBox hBox = new HBox();
        Message message = new Message(MessageInstruction.USER, MessageLabel.ALL, MessageTag.TOKEN);
        message.setTagsInOrder(ProgramController.currentToken);
        AppController.sendMessageToServer(message);
        AppController.receiveMessageFromServer();
        LinkedHashMap<User, Boolean> users = (LinkedHashMap<User, Boolean>) AppController.receiveMessageFromServer();
        Circle circle = new Circle(5);
        for (User user1 : users.keySet()) {
            if (user1.getUsername().equals(user.getUsername())) {
                if (users.get(user1)) circle.setFill(Color.FIREBRICK);
                else circle.setFill(Color.WHITE);
            }
        }
        hBox.setAlignment(Pos.CENTER);
        hBox.setMaxWidth(400);
        ArrayList<Node> labelsForUser = getLabelsForUser(user, ((new ArrayList<>(messages.keySet())).get(i)),
                messages.get((new ArrayList<>(messages.keySet())).get(i)).getValue());
        ImageView avatar = new ImageView(new Image(String.valueOf(getClass().getResource(user.getAvatar()))));
        avatar.setFitHeight(30);
        avatar.setFitWidth(30);
        hBox.getChildren().add(circle);
        hBox.getChildren().add(avatar);
        ((Label) labelsForUser.get(0)).setMinWidth(30);
        ((Label) labelsForUser.get(1)).setMinWidth(200);
        ((Label) labelsForUser.get(2)).setMinWidth(40);
        hBox.getChildren().addAll(labelsForUser);
        hBox.setSpacing(20);
        return hBox;
    }

    private ArrayList<Node> getLabelsForUser(User user, LocalDateTime localDateTime, String message) {
        ArrayList<Node> labels = new ArrayList<>();
        labels.add(new Label(user.getNickname()));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        labels.add(new Label(dtf.format(localDateTime)));
        labels.add(new Label(message));
        return labels;
    }

    public void handleBackToMenu() throws IOException {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        ProgramController.createNewScene(getClass().getResource("/FXMLs/ScoreboardMenu.fxml"));
        ProgramController.stage.show();
    }

    public void refresh(MouseEvent event) throws IOException {
        showMenu();
    }
}
