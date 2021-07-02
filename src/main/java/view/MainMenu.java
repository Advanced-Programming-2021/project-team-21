package view;

import controller.ProgramController;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class MainMenu implements Menuable {
    @Override
    public void showMenu() throws IOException {
        ProgramController.createNewScene(getClass().getResource("/FXMLs/mainMenu.fxml"));
        ProgramController.stage.show();
    }

    public void logout() throws IOException {
        ProgramController.currentMenu = new LoginMenu();
        ProgramController.userInGame = null;
        ((LoginMenu) ProgramController.currentMenu).backToEntrance();
    }

    public void goToShopMenu() throws IOException {
        ProgramController.currentMenu = new ShopMenu();
        ProgramController.currentMenu.showMenu();
    }

    public void goToScoreboard() throws IOException {
        ProgramController.currentMenu = new ScoreBoard();
        ProgramController.currentMenu.showMenu();
    }

    public void goToProfileMenu() throws IOException {
        ProgramController.currentMenu = new ProfileMenu();
        ProgramController.currentMenu.showMenu();
    }

    public void goToImportAndExport(MouseEvent mouseEvent) throws IOException {
        ProgramController.currentMenu = new ImportAndExport();
        ProgramController.currentMenu.showMenu();
    }
}
