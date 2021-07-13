package view;

import controller.ProgramController;
import javafx.scene.control.Label;
import model.User;

import java.io.IOException;
import java.util.regex.Matcher;
public class LoginMenu {

    public void run(String command) throws IOException {
        Matcher matcher;
        if ((matcher = Regex.getMatcher(command, Regex.userLogin)).find() ||
                (matcher = Regex.getMatcher(command, Regex.userLoginShort)).find()) {
            loginNewUser(matcher);
        }
        else if ((matcher = Regex.getMatcher(command, Regex.userCreate)).find() ||
                (matcher = Regex.getMatcher(command, Regex.userCreateShort)).find()) {
            createNewUser(matcher);
        }
    }


    private void createNewUser(Matcher matcher) throws IOException {
        String username = matcher.group("username"), password = matcher.group("password"),
                nickname = matcher.group("nickname");
        if (User.getUserByUsername(username) != null) {
 //
            return;
        }
        if (User.getUserByNickname(nickname) != null) {
   //
        }
        new User(username, password, nickname);
    }

    private void loginNewUser(Matcher matcher) throws IOException {
        String username = matcher.group("username"), password = matcher.group("password");
        User user = User.getUserByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {

            return;
        }
        ProgramController.userInGame = user;
    }



    public void exitProgram() { System.exit(0); }

    public void passSignupInformationToCheck() throws IOException {
       //
    }


    public void passLoginInformationToCheck() throws IOException {
      //
    }
}
