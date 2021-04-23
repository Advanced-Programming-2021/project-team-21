package controller;
import controller.menu.*;

import java.util.*;
public class ProgramController {
    public static Scanner scanner;
    public static Menus currentMenu = Menus.LOGIN_MENU;
    LoginMenu  loginMenu = new LoginMenu();
    public void run (){
    while (currentMenu != Menus.EXIT){
        String command = getCommand();
        if (currentMenu == Menus.LOGIN_MENU){
            loginMenu.run(command);
        }
    }
    }

    private String getCommand() {
        return scanner.nextLine();
    }

    public static void saveData(Object object){

    }
}
