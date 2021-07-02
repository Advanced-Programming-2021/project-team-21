package controller;

import controller.menu.LoginMenu;
import controller.menu.Menuable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import module.User;
import module.card.Card;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

public class ProgramController {
    public static Scanner scanner = new Scanner(System.in);
    public static boolean gameOn = true;
    public static Menuable currentMenu = new LoginMenu();
    public static User userInGame;
    public static HashMap<String, Card> allCards;
    public static Stage stage;
    public static Scene currentScene;

    public static Scene createNewScene(URL url) throws IOException {
        Parent pane = FXMLLoader.load(url);
        Scene scene = new Scene(pane);
        ProgramController.stage.setScene(scene);
        ProgramController.currentScene = scene;
        return scene;
    }

    public void run() throws IOException {
        DataController.createDirectories();
        allCards = DataController.getAllCards();
        ProgramController.createNewScene(getClass().getResource("/fxmls/entrance.fxml"));
        ProgramController.stage.show();
    }

    private String getCommand() {
        return scanner.nextLine();
    }

}
