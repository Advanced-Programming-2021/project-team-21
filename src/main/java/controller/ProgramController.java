package controller;

import controller.menu.LoginMenu;
import controller.menu.Menuable;
import module.User;

import java.util.Scanner;

public class ProgramController {
    public static Scanner scanner = new Scanner(System.in);
    public static boolean gameOn = true;
    public static Menuable currentMenu = new LoginMenu();
    public static User userInGame;

    public void run() {
        DataController.createDirectories();
        while (gameOn) {
            String command = getCommand();
            currentMenu.run(command);
        }
    }

    private String getCommand() {
        return scanner.nextLine();
    }


}
