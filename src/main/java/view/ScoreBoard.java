package view;

import controller.DataController;
import controller.ProgramController;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

public class ScoreBoard implements Menuable {


    private ArrayList<User> getSortedUsers() {
        ArrayList<User> users = DataController.getAllUsers();
        if (users == null)
            return null;
        Comparator<User> comparator = Comparator.comparing(User::getScore, Comparator.reverseOrder())
                .thenComparing(User::getUsername);
        users.sort(comparator);
        return users.stream().limit(20).collect(Collectors.toCollection(ArrayList::new));
    }

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
        ArrayList<User> users = getSortedUsers();
        String[] ranks = getRanks(Objects.requireNonNull(users));
        for (int i = 0; i < ranks.length; i++) {
            mainVBox.getChildren().add(getHBoxForUser(users.get(i), ranks[i]));
        }

    }

    private HBox getHBoxForUser(User user, String rank) {
        HBox hBox = new HBox();
        if (user.getUsername().equals(ProgramController.userInGame.getUsername()))
            hBox.getStyleClass().add("scoreboard-user-in-game");
        else
            hBox.getStyleClass().add("scoreboard-users");
        hBox.setAlignment(Pos.CENTER);
        hBox.setMaxWidth(350);
        ArrayList<Node> labelsForUser = getLabelsForUser(user, rank);
        ImageView avatar = new ImageView(new Image(String.valueOf(getClass().getResource(user.getAvatar()))));
        avatar.setFitHeight(50);
        avatar.setFitWidth(50);
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
        ProgramController.createNewScene(getClass().getResource("/FXMLs/mainMenu.fxml"));
        ProgramController.stage.show();
    }
}
