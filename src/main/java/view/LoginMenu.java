package view;

import controller.DataController;
import controller.ProgramController;
import javafx.scene.control.Label;
import model.User;

import java.io.IOException;
import java.util.regex.Matcher;
public class LoginMenu implements Menuable{
    private Matcher matcher;

    public String run(String command){
        if ((matcher = Regex.getMatcher(command, Regex.userCreate)).find())
            return LoginMenu.createNewUser(matcher);
        else if ((matcher = Regex.getMatcher(command, Regex.userLogin)).find())
            return LoginMenu.loginNewUser(matcher);
        return Responses.invalidFormat;
    }

    private static String createNewUser(Matcher matcher){
        String username = matcher.group("username"), password = matcher.group("password"),
                nickname = matcher.group("nickname");
        if (User.getUserByUsername(username) != null)
            return Responses.userExists;
        if (User.getUserByNickname(nickname) != null)
            return Responses.nicknameExists;
        new User(username, password, nickname);
        return Responses.successful;
    }

    private static String loginNewUser(Matcher matcher) {
        String username = matcher.group("username"), password = matcher.group("password");
        User user = User.getUserByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            return Responses.errorLogin;
        }
        ProgramController.userInGame = user;
        return Responses.successful;
    }



    public void exitProgram() { System.exit(0); }

}
