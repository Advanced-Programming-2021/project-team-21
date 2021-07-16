package view;

import controller.ProgramController;
import model.User;

import java.util.regex.Matcher;
public class LoginMenu implements Menuable{

    @Override
    public String run(String command){
        Matcher  matcher;
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
        ProgramController.currentToken = ProgramController.generateNewToken();
        return ProgramController.currentToken + " " + Responses.successful;
    }

}
