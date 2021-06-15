package controller.menu;

import controller.DataController;
import controller.ProgramController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import module.card.Card;
import view.PrintResponses;
import view.Regex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

public class MainMenu implements Menuable {
    @Override
    public void run(String command) {
        Matcher matcher;
        if (Regex.getMatcher(command, Regex.menuExit).find()) exitMenu();
        else if (Regex.getMatcher(command, Regex.menuShow).find()) showCurrentMenu();
        else if ((matcher = Regex.getMatcher(command, Regex.menuEnter)).find()) menuEnter(matcher);
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

                break;
            case "Profile":

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
    }

    @Override
    public void exitMenu() {

    }

    @Override
    public void showCurrentMenu() {
        PrintResponses.printMainMenuShow();
    }

    public void showMainMenu() throws IOException {
        ProgramController.createNewScene(getClass().getResource("/fxmls/mainMenu.fxml"));
        ProgramController.stage.show();
    }

    public void logout(MouseEvent mouseEvent) throws IOException {
        ProgramController.currentMenu = new LoginMenu();
        ProgramController.userInGame = null;
        ((LoginMenu) ProgramController.currentMenu).backToEntrance();
    }

    public void goToShopMenu(MouseEvent mouseEvent) throws IOException {
        ProgramController.currentMenu = new ShopMenu();
        ((ShopMenu) ProgramController.currentMenu).showShopMenu();
    }

    public void goToScoreboard(MouseEvent mouseEvent) throws IOException {
        ProgramController.currentMenu = new ScoreBoard();
        ((ScoreBoard) ProgramController.currentMenu).showScoreBoard();
    }

    public void goToProfileMenu(MouseEvent mouseEvent) throws IOException {
        ProgramController.currentMenu = new ProfileMenu();
        ((ProfileMenu) ProgramController.currentMenu).showProfileMenu();
    }
}
