package controller.menu;

import controller.ProgramController;
import view.PrintResponses;

public class ScoreBoard implements Menuable{
    @Override
    public void run(String command) {

    }

    @Override
    public void exitMenu() {
        ProgramController.currentMenu = new MainMenu();
    }

    @Override
    public void showCurrentMenu() {
        PrintResponses.printScoreboardShow();
    }
}
