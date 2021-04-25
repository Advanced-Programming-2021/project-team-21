package controller.menu;

import controller.ProgramController;
import view.PrintResponses;

public class DuelMenu implements Menuable{
    @Override
    public void run(String command) {

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
