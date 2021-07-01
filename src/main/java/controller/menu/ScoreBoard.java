package controller.menu;

import controller.DataController;
import controller.ProgramController;
import javafx.scene.control.Label;
import module.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class ScoreBoard implements Menuable {

    private ArrayList<User> scoreBoardShow() {
        ArrayList<User> users = DataController.getAllUsers();
        if (users == null)
            return null;
        Comparator<User> comparator = Comparator.comparing(User::getScore, Comparator.reverseOrder())
                .thenComparing(User::getUsername);
        users.sort(comparator);
        int[] ranks = getRanks(users);
        return users;
    }

    private int[] getRanks(ArrayList<User> users) {
        int[] ranks = new int[users.size()];
        ranks[0] = 1;
        for (int i = 1; i < users.size(); i++) {
            if (users.get(i).getScore() == users.get(i - 1).getScore()) {
                ranks[i] = ranks[i - 1];
            } else {
                ranks[i] = i + 1;
            }
        }
        return ranks;
    }

    @Override
    public void showMenu() throws IOException {
        ProgramController.createNewScene(getClass().getResource("/fxmls/scoreboard.fxml"));
        ArrayList<ArrayList<Label>> columnInformation = getLabels();
        ArrayList<User> users = scoreBoardShow();
        int[] ranks = getRanks(users);
        for (int i = 0; i < Math.min(20, users.size()); i++) {
            columnInformation.get(i).get(0).setText(String.valueOf(ranks[i]));
            columnInformation.get(i).get(1).setText(users.get(i).getNickname());
            columnInformation.get(i).get(2).setText(String.valueOf(users.get(i).getScore()));
            if (users.get(i).getNickname().equals(ProgramController.userInGame.getNickname())) {
                columnInformation.get(i).get(0).setStyle("-fx-text-fill: red; -fx-border-color: black;");
                columnInformation.get(i).get(1).setStyle("-fx-text-fill: red; -fx-border-color: black;");
                columnInformation.get(i).get(2).setStyle("-fx-text-fill: red; -fx-border-color: black;");
            }
        }
        ProgramController.stage.show();
    }

    private ArrayList<ArrayList<Label>> getLabels() {
        ArrayList<ArrayList<Label>> columnInformation = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            ArrayList<Label> column = new ArrayList<>();
            column.add((Label) ProgramController.currentScene.lookup("#rank" + i));
            column.add((Label) ProgramController.currentScene.lookup("#nickname" + i));
            column.add((Label) ProgramController.currentScene.lookup("#score" + i));
            columnInformation.add(column);
        }
        return columnInformation;
    }

    public void back() throws IOException {
        ProgramController.currentMenu = new MainMenu();
        ProgramController.currentMenu.showMenu();
    }
}
