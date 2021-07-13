package view;

import controller.ProgramController;

import model.User;

import java.util.regex.Matcher;

import static view.Regex.changePassword;

public class ProfileMenu implements Menuable {


    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getMatcher(command, Regex.userChangeNickname)).find()) {
            changeNickname(matcher);
        } else if ((matcher = Regex.getMatcher(command, changePassword)).find() ||
                (matcher = Regex.getMatcher(command, Regex.changePasswordShort)).find()) {
            newPassword(matcher);
        }

    }

    private void newPassword(Matcher matcher) {
        String oldPassword = matcher.group("password"), newPassword = matcher.group("newPassword");
        if (!ProgramController.userInGame.getPassword().equals(oldPassword)) {
            //
            return;
        }
        if (newPassword.equals(oldPassword)) {
            //
        }
        ProgramController.userInGame.setPassword(newPassword);
    }

    private void changeNickname(Matcher matcher) {
        String nickname = matcher.group("nickname");
        //parse command
        if (User.getUserByNickname(nickname) != null) {

        }
        ProgramController.userInGame.setNickname(nickname);
    }



    public void  passInformationToChangeNickname() {
       //
    }

    public void passInformationToChangePassword() {
        // parse command

       //
    }

    public void changeAvatar() {


    }
}
