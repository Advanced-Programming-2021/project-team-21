package controller.menu;

import controller.ProgramController;
import module.User;
import view.*;

import java.util.regex.Matcher;

public class LoginMenu implements Menuable {
    @Override
    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getMatcher(command, Regex.userLogin)).matches() ||
                (matcher = Regex.getMatcher(command, Regex.userLoginShort)).matches()) {
            loginNewUser(matcher);
        } else if (Regex.getMatcher(command, Regex.menuEnter).matches()) {
            PrintResponses.printNoLoginYet();
        } else if (Regex.getMatcher(command, Regex.menuExit).matches()) {
            exitMenu();
        } else if (Regex.getMatcher(command, Regex.menuShow).matches()) {
            showCurrentMenu();
        } else if ((matcher = Regex.getMatcher(command, Regex.userCreate)).matches() ||
                (matcher = Regex.getMatcher(command, Regex.userCreateShort)).matches()) {
            createNewUser(matcher);
        } else {
            PrintResponses.printInvalidFormat();
        }
    }

    private void createNewUser(Matcher matcher) {
        String username = matcher.group("username") , password = matcher.group("password") ,
                nickname = matcher.group("nickname");
        if (User.getUserByUsername(username) != null){
            PrintResponses.printUserExists(username);
            return;
        }

        PrintResponses.printSuccessfulUserCreation();
    }

    private void loginNewUser(Matcher matcher) {
        PrintResponses.printSuccessfulLogout();
    }


    @Override
    public void showCurrentMenu() {
        PrintResponses.printLoginMenuShow();
    }

    @Override
    public void exitMenu() {
        ProgramController.currentMenu = Menus.EXIT;
    }


}
