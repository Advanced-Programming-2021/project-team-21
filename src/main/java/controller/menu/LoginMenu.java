package controller.menu;

import controller.ProgramController;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import module.User;
import view.PrintResponses;
import view.Regex;

import java.awt.*;
import java.io.IOException;
import java.util.regex.Matcher;

public class LoginMenu implements Menuable {

    public TextField usernameSignUp = new TextField();
    public TextField nicknameSignUp = new TextField();
    public PasswordField passwordSignUp = new PasswordField();
    public CheckBox agreeToPolicies = new CheckBox();

    @Override
    public void run(String command) throws IOException {
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

    private void createNewUser(Matcher matcher) throws IOException {
        String username = matcher.group("username"), password = matcher.group("password"),
                nickname = matcher.group("nickname");
        System.out.println(username);
        if (User.getUserByUsername(username) != null) {
            ProgramController.createNewScene(getClass().getResource("/fxmls/signup.fxml"));
            ((Label) ProgramController.currentScene.lookup("#errorSignUp")).setText("This username already exists!");
            ((Label) ProgramController.currentScene.lookup("#errorUsernameSignUp")).setText("*");
            ProgramController.stage.show();
            return;
        }
        if (User.getUserByNickname(nickname) != null) {
            PrintResponses.printUserExistsWithNickname(nickname);
            return;
        }
        new User(username, password, nickname);
        PrintResponses.printSuccessfulUserCreation();
        ProgramController.stage.show();
    }

    private void loginNewUser(Matcher matcher) {
        String username = matcher.group("username"), password = matcher.group("password");
        User user = User.getUserByUsername(username);
        if (user == null) {
            PrintResponses.printNoUserExists();
            return;
        }
        if (!user.getPassword().equals(password)) {
            PrintResponses.printWrongPasswordInLogin();
            return;
        }
        PrintResponses.printSuccessfulLogin();
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


    public void goToLogin() throws IOException {
        ProgramController.createNewScene(getClass().getResource("/fxmls/login.fxml"));
        ProgramController.stage.show();
    }

    public void goToSignup() throws IOException {
        ProgramController.createNewScene(getClass().getResource("/fxmls/signup.fxml"));
        ProgramController.stage.show();
    }

    public void exitProgram() {
        System.exit(0);
    }

    public void passSignupInformationToCheck() throws IOException {
        if (!agreeToPolicies.isSelected()) {
            ((Label) ProgramController.currentScene.lookup("#errorSignUp")).setText("You must agree to policies!");
            ((Label) ProgramController.currentScene.lookup("#errorPoliciesSignUp")).setText("*");
            ProgramController.stage.show();
            return;
        }
        String commandSignup = "user create --username " + usernameSignUp.getText() + " --nickname " + nicknameSignUp.getText() + " --password " + passwordSignUp.getText();
        run(commandSignup);
    }
}
