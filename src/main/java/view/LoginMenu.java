package view;

import controller.ProgramController;
import javafx.animation.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.User;

import java.io.IOException;
import java.util.regex.Matcher;
public class LoginMenu implements Menuable {

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
            ((Label) ProgramController.currentScene.lookup("#errorLogin")).setText("Username and password didn't match!");
            ProgramController.currentScene.lookup("#errorLogin").setStyle("-fx-border-color: red; -fx-background-color: white;");
            ProgramController.stage.show();
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
