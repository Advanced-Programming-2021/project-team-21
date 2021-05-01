package controller.menu;

import controller.ProgramController;
import view.PrintResponses;
import view.Regex;

import java.util.regex.Matcher;

public class MainMenu implements Menuable{
    @Override
    public void run(String command) {
        Matcher matcher;
        if (Regex.getMatcher(command , Regex.menuExit).find())exitMenu();
        else if (Regex.getMatcher(command , Regex.menuShow).find())showCurrentMenu();
        else if ((matcher = Regex.getMatcher(command , Regex.menuEnter)).find())menuEnter(matcher);
        else PrintResponses.printInvalidFormat();
    }

    private void menuEnter(Matcher matcher) {
        String name = matcher.group("menuName");
        switch (name) {
            case "Duel":
                ProgramController.currentMenu = new DuelMenu();
                break;
            case "Deck":
                ProgramController.currentMenu = new DeckMenu();
                break;
            case "Scoreboard":
                ProgramController.currentMenu = new ScoreBoard();
                break;
            case "Profile":
                ProgramController.currentMenu = new ProfileMenu();
                break;
            case "Shop":
                ProgramController.currentMenu = new ShopMenu();
                break;
            case "Import":
                ProgramController.currentMenu = new ImportMenu();
                break;
            case "Export":
                ProgramController.currentMenu = new ExportMenu();
                break;
            default:
                PrintResponses.printInvalidFormat();
                break;
        }
    }

    @Override
    public void exitMenu() {
        ProgramController.currentMenu = new LoginMenu();
    }

    @Override
    public void showCurrentMenu() {
        PrintResponses.printMainMenuShow();
    }

}
