package controller.menu;

import controller.ProgramController;
import module.User;
import view.PrintResponses;
import view.Regex;

import java.util.regex.Matcher;

import static view.Regex.changePassword;

public class ProfileMenu implements Menuable{
    @Override
    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getMatcher(command, Regex.userChangeNickname)).find()) {
            changeNickname(matcher);
        } else if (Regex.getMatcher(command, Regex.menuEnter).find()) {
            PrintResponses.printMenuNavigationError();
        } else if (Regex.getMatcher(command, Regex.menuExit).find()) {
            exitMenu();
        } else if (Regex.getMatcher(command, Regex.menuShow).find()) {
            showCurrentMenu();
        } else if ((matcher = Regex.getMatcher(command, changePassword)).matches() ||
                (matcher = Regex.getMatcher(command, Regex.changePasswordShort)).matches()) {
            newPassword(matcher);
        } else {
            PrintResponses.printInvalidFormat();
        }
    }

    private void newPassword(Matcher matcher) {
        String oldPassword = matcher.group("password") , newPassword = matcher.group("newPassword");
        if (!ProgramController.userInGame.getPassword().equals(oldPassword)){
            PrintResponses.printWrongPasswordInChange();
            return;
        }
        if (newPassword.equals(oldPassword)){
            PrintResponses.printEqualityOfCurrentAndNewPassword();
            return;
        }
        ProgramController.userInGame.setPassword(newPassword);
        PrintResponses.printSuccessfulPasswordChange();
    }

    private void changeNickname(Matcher matcher) {
        String nickname = matcher.group("nickname");
        if (User.getUserByNickname(nickname) != null){
            PrintResponses.printUserExistsWithNickname(nickname);
            return;
        }
        ProgramController.userInGame.setNickname(nickname);
        PrintResponses.printSuccessfulNicknameChange();
    }

    @Override
    public void exitMenu() {
        ProgramController.currentMenu = Menus.MAIN_MENU;
    }

    @Override
    public void showCurrentMenu() {
        PrintResponses.printProfileMenuShow();
    }
}
