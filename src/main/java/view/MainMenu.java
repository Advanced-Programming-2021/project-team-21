package view;

import controller.ProgramController;
import java.util.regex.Matcher;

public class MainMenu implements Menuable {
    @Override
    public String run(String command) {
        Matcher matcher;
        if (Regex.getMatcher(command, Regex.menuExit).find() || Regex.getMatcher(command, Regex.logout).find())
            return exitMenu();
        else if ((matcher = Regex.getMatcher(command, Regex.menuEnter)).find()) return menuEnter(matcher);
        else return Responses.invalidFormat;
    }

    private String menuEnter(Matcher matcher) {
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
            case "Export":
                ProgramController.currentMenu = new ImportAndExport();
                break;
            default:
                PrintResponses.printInvalidFormat();
                break;
        }
        return Responses.successful;
    }

    public String exitMenu() {
        ProgramController.currentMenu = new LoginMenu();
        return Responses.successful;
    }

}
