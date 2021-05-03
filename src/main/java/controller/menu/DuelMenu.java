package controller.menu;

import controller.ProgramController;
import view.PrintResponses;

import java.util.regex.Matcher;

public class DuelMenu implements Menuable {
    @Override
    public void run(String command) {
        Matcher matcher;
    }

    @Override
    public void showCurrentMenu() {
        PrintResponses.printDuelMenuShow();
    }

    @Override
    public void exitMenu() {
        ProgramController.currentMenu = new MainMenu();
    }
}
