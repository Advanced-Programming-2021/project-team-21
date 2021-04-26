package controller.menu;

import controller.ProgramController;
import view.PrintResponses;

public class ShopMenu implements Menuable{
    @Override
    public void run(String command) {

    }

    @Override
    public void showCurrentMenu() {
        PrintResponses.printShopMenuShow();
    }

    @Override
    public void exitMenu() {
        ProgramController.currentMenu = new MainMenu();
    }
}
