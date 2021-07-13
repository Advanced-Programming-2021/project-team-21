package controller;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Duration;
import view.DuelMenu;
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
    public static Menuable currentMenu = new LoginMenu();
    public static User userInGame;
    public static HashMap<String, Card> allCards;



    public void run() throws IOException {
    //
    }


}
