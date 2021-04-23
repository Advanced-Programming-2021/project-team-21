package controller.menu;
import controller.ProgramController;
import view.*;
public class LoginMenu implements Menuable{
    @Override
    public void run(String command) {

    }

    @Override
    public void showCurrentMenu() {
        PrintResponses.printLoginMenuShow();
    }

    @Override
    public void exitMenu() {
        ProgramController.currentMenu = Menus.EXIT;
    }
}
