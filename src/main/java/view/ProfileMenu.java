package view;

import controller.ProgramController;

import model.User;

import java.util.regex.Matcher;

import static view.Regex.changeAvatar;
import static view.Regex.changePassword;

public class ProfileMenu implements Menuable {

    @Override
    public String run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getMatcher(command, Regex.userChangeNickname)).find())
            return changeNickname(matcher);
        else if ((matcher = Regex.getMatcher(command, changePassword)).find() ||
                (matcher = Regex.getMatcher(command, Regex.changePasswordShort)).find())
            return newPassword(matcher);
        else if ((matcher = Regex.getMatcher(command, changeAvatar)).find())
            return changeAvatar(matcher);
        return Responses.invalidFormat;
    }

    private String newPassword(Matcher matcher) {
        String oldPassword = matcher.group("password"), newPassword = matcher.group("newPassword");
        if (!ProgramController.userInGame.getPassword().equals(oldPassword))
            return Responses.invalidPassword;
        if (newPassword.equals(oldPassword))
            return Responses.equalityOfNewAndOldPassword;
        ProgramController.userInGame.setPassword(newPassword);
        return Responses.successful;
    }

    private String changeNickname(Matcher matcher) {
        String nickname = matcher.group("nickname");
        if (nickname.equals(""))
            return Responses.emptyNicknameField;
        if (nickname.equals(ProgramController.userInGame.getNickname()))
            return Responses.changeToCurrentNickname;
        if (User.getUserByNickname(nickname) != null)
            return Responses.nicknameExists;
        ProgramController.userInGame.setNickname(nickname);
        return Responses.successful;
    }


    public String changeAvatar(Matcher matcher) {
        String url = matcher.group(1);
        ProgramController.userInGame.setAvatar(url);
        return Responses.successful;
    }
}
