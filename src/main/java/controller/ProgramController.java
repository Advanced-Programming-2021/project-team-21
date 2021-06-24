package controller;

import controller.menu.LoginMenu;
import controller.menu.Menuable;
import module.User;
import module.card.Card;
import module.card.Monster;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class ProgramController {
    public static Scanner scanner = new Scanner(System.in);
    public static boolean gameOn = true;
    public static Menuable currentMenu = new LoginMenu();
    public static User userInGame;
    public static HashMap<String, Card> allCards;

    public void run() {
        DataController.createDirectories();
        allCards = DataController.getAllCards();
        while (gameOn) {
            String command = getCommand();
            currentMenu.run(command);
        }
    }

    private String getCommand() {
        return scanner.nextLine();
    }


}
