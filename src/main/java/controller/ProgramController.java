package controller;
import controller.menu.*;
import module.User;

import java.util.*;
public class ProgramController {
    public static Scanner scanner = new Scanner(System.in);
    public static  boolean gameOn = true;
    public static Menuable currentMenu = new LoginMenu();
    public static User userInGame;
    public void run (){
    while (gameOn){
        String command = getCommand();
        currentMenu.run(command);
    }
    }

    private String getCommand() {
        return scanner.nextLine();
    }

    public static void saveData(Object object){

    }
}
