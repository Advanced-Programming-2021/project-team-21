package controller.menu;

import controller.ProgramController;
import module.User;
import view.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginMenu implements Menuable {
    @Override
    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getMatcher(command, Regex.userLogin)).find() ||
                (matcher = Regex.getMatcher(command, Regex.userLoginShort)).find()) {
            loginNewUser(matcher);
        } else if (Regex.getMatcher(command, Regex.menuEnter).find()) {
            PrintResponses.printNoLoginYet();
        } else if (Regex.getMatcher(command, Regex.menuExit).find()) {
            exitMenu();
        } else if (Regex.getMatcher(command, Regex.menuShow).find()) {
            showCurrentMenu();
        } else if ((matcher = Regex.getMatcher(command, Regex.userCreate)).find() ||
                (matcher = Regex.getMatcher(command, Regex.userCreateShort)).find()) {
            createNewUser(matcher);
        } else {
            PrintResponses.printInvalidFormat();
        }
    }

    private void createNewUser(Matcher matcher) {
        String username = matcher.group("username") , password = matcher.group("password") ,
                nickname = matcher.group("nickname");
        if (User.getUserByUsername(username) != null){
            PrintResponses.printUserExistsWithUsername(username);
            return;
        }
        if (User.getUserByNickname(nickname) != null){
            PrintResponses.printUserExistsWithNickname(nickname);
            return;
        }
        new User(username , password , nickname);
        PrintResponses.printSuccessfulUserCreation();
    }

    private void loginNewUser(Matcher matcher) {
        String username = matcher.group("username") , password = matcher.group("password");
        User user = User.getUserByUsername(username);
        if (user == null){
            PrintResponses.printNoUserExists();
            return;
        }
        if (!user.getPassword().equals(password)){
            PrintResponses.printWrongPasswordInLogin();
            return;
        }
        PrintResponses.printSuccessfulLogout();
        ProgramController.userInGame = user;
        ProgramController.currentMenu = new MainMenu();
    }


    @Override
    public void showCurrentMenu() {
        PrintResponses.printLoginMenuShow();
    }

    @Override
    public void exitMenu() {
        ProgramController.gameOn = false;
    }


}
