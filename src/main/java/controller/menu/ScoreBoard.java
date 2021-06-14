package controller.menu;

import controller.DataController;
import controller.ProgramController;
import module.User;
import view.PrintResponses;
import view.Regex;

import java.io.IOException;
import java.util.ArrayList;

public class ScoreBoard implements Menuable {
    @Override
    public void run(String command) {
        if (Regex.getMatcher(command, Regex.menuExit).find()) exitMenu();
        else if (Regex.getMatcher(command, Regex.menuShow).find()) showCurrentMenu();
        else if (Regex.getMatcher(command, Regex.scoreBoard).matches()) scoreBoardShow();
        else PrintResponses.printInvalidFormat();
    }

    private void scoreBoardShow() {
        ArrayList<User> users = DataController.getAllUsers();
        if (users == null)
            return;
        users.sort(new ScoreSorter());
        int[] ranks = getRanks(users);
        PrintResponses.printScoreboard(ranks, users);
    }

    private int[] getRanks(ArrayList<User> users) {
        int[] ranks = new int[users.size()];
        ranks[0] = 1;
        for (int i = 1; i < users.size(); i++) {
            if (users.get(i).getScore() == users.get(i - 1).getScore()) {
                ranks[i] = ranks[i - 1];
            } else {
                ranks[i] = i;
            }
        }
        return ranks;
    }

    @Override
    public void exitMenu() {
        ProgramController.currentMenu = new MainMenu();
    }

    @Override
    public void showCurrentMenu() {
        PrintResponses.printScoreboardShow();
    }

    public void showScoreBoard() throws IOException {
        ProgramController.createNewScene(getClass().getResource("/fxmls/scoreboard.fxml"));
        ProgramController.stage.show();
    }
}
