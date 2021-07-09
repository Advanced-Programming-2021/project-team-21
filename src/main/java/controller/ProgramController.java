package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import view.LoginMenu;
import view.Menuable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;
import model.card.Card;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class ProgramController {
    public static Scanner scanner = new Scanner(System.in);
    public static boolean gameOn = true;
    public static Menuable currentMenu = new LoginMenu();
    public static User userInGame;
    public static HashMap<String, Card> allCards;
    public static Stage stage;
    public static Scene currentScene;
    public static MediaPlayer mediaPlayer;
    public static MediaPlayer mediaPlayerBackground;
    public static double volume = 0.5;

    public static Scene createNewScene(URL url) throws IOException {
        Parent pane = FXMLLoader.load(url);
        Scene scene = new Scene(pane);
        ProgramController.stage.setScene(scene);
        ProgramController.currentScene = scene;
        return scene;
    }

    public void run() throws IOException {
        startNewAudioBackground("src/main/resources/audios/menuSound.mp3");
        DataController.createDirectories();
        allCards = DataController.getAllCards();
        DataController.initializeEffectHolders();
        ProgramController.createNewScene(getClass().getResource("/FXMLs/entrance.fxml"));
        ProgramController.stage.show();
    }

    public static void startNewAudioBackground(String path) {
        try {
            mediaPlayerBackground.stop();
        }
        catch (Exception e) {
        }
        Media media = new Media(new File(path).toURI().toString());
        mediaPlayerBackground = new MediaPlayer(media);
        mediaPlayerBackground.setVolume(volume);
        mediaPlayerBackground.setAutoPlay(true);
    }

    public static void startNewAudio(String path) {
        Media media = new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(volume);
        mediaPlayer.setAutoPlay(true);
    }

}
