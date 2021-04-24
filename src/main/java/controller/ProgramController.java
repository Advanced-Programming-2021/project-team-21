package controller;
import controller.menu.*;
import module.User;

import java.util.*;
public class ProgramController {
    public static Scanner scanner;
    public static Menus currentMenu = Menus.LOGIN_MENU;
    public static User userInGame;
    LoginMenu  loginMenu = new LoginMenu();
    ProfileMenu profileMenu = new ProfileMenu();
    public void run (){
    while (currentMenu != Menus.EXIT){
        String command = getCommand();
        if (currentMenu == Menus.LOGIN_MENU){
            loginMenu.run(command);
        }
        if (currentMenu == Menus.PROFILE_MENU){
            profileMenu.run(command);
        }
    }
    }

    private String getCommand() {
        return scanner.nextLine();
    }

    public static void saveData(Object object){

    }
}
