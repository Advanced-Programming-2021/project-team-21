package view;

import controller.ProgramController;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class MainMenu implements Menuable {
    @Override
    public void showMenu() throws IOException {
        ProgramController.createNewScene(getClass().getResource("/FXMLs/mainMenu.fxml"));
        if (ProgramController.volume == 0)
            ((ImageView) ProgramController.currentScene.lookup("#soundIcon")).setImage(new Image(getClass().getResource("/images/soundIconMute.png").toExternalForm()));
        else ((ImageView) ProgramController.currentScene.lookup("#soundIcon")).setImage(new Image(getClass().getResource("/images/soundIcon.png").toExternalForm()));
        ProgramController.stage.show();
    }

    public void logout() throws IOException {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        ProgramController.currentMenu = new LoginMenu();
        ProgramController.userInGame = null;
        ((LoginMenu) ProgramController.currentMenu).backToEntrance();
    }

    public void goToShopMenu() throws IOException {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        ProgramController.currentMenu = new ShopMenu();
        ProgramController.currentMenu.showMenu();
    }

    public void goToScoreboard() throws IOException {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        ProgramController.currentMenu = new ScoreBoard();
        ProgramController.currentMenu.showMenu();
    }

    public void goToProfileMenu() throws IOException {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        ProgramController.currentMenu = new ProfileMenu();
        ProgramController.currentMenu.showMenu();
    }

    public void goToCreateCard() throws IOException {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        ProgramController.currentMenu = new CreateCard();
        ProgramController.currentMenu.showMenu();
    }

    public void goToDeckMenu() throws IOException {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        ProgramController.currentMenu = new DeckMenu();
        ProgramController.currentMenu.showMenu();
    }

    public void goToDuelMenu() throws IOException {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        ProgramController.currentMenu = new DuelMenu();
        ProgramController.currentMenu.showMenu();
    }

    public void goToImportAndExportMenu() throws IOException {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        ProgramController.currentMenu = new ImportAndExport();
        ProgramController.currentMenu.showMenu();
    }

    public void muteOrUnmute() {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        if (ProgramController.volume != 0) {
            ((ImageView) ProgramController.currentScene.lookup("#soundIcon")).setImage(new Image(getClass().getResource("/images/soundIconMute.png").toExternalForm()));
            try {
                ProgramController.mediaPlayer.setVolume(0);
            }
            catch (Exception e) {
            }
            ProgramController.volume = 0;
            ProgramController.mediaPlayerBackground.setVolume(ProgramController.volume);
        }
        else {
            ((ImageView) ProgramController.currentScene.lookup("#soundIcon")).setImage(new Image(getClass().getResource("/images/soundIcon.png").toExternalForm()));
            try {
                ProgramController.mediaPlayer.setVolume(0.5);
            }
            catch (Exception e) {
            }
            ProgramController.volume = 0.5;
            ProgramController.mediaPlayerBackground.setVolume(ProgramController.volume);
        }
    }
}
