package controller.menu;

import controller.ProgramController;
import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import module.User;
import view.PrintResponses;
import view.Regex;

import java.awt.*;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
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
        if (User.getUserByUsername(username) != null) {
            clearPreviousErrorsInSignup();
            ((Label) ProgramController.currentScene.lookup("#errorSignUp")).setText("This username already exists!");
            ((Label) ProgramController.currentScene.lookup("#errorUsernameSignUp")).setText("*");
            ProgramController.stage.show();
            return;
        }
        if (User.getUserByNickname(nickname) != null) {
            clearPreviousErrorsInSignup();
            ((Label) ProgramController.currentScene.lookup("#errorSignUp")).setText("This nickname already exists!");
            ((Label) ProgramController.currentScene.lookup("#errorNicknameSignUp")).setText("*");
            return;
        }
        new User(username, password, nickname);
        Parent pane = FXMLLoader.load(getClass().getResource("/fxmls/succussfulSignup.fxml"));
        Scene scene = new Scene(pane);
        Stage stagePopUp = new Stage();
        stagePopUp.setScene(scene);
        stagePopUp.setTitle("Successful sign up");
        stagePopUp.setResizable(false);
        stagePopUp.show();
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished( event -> stagePopUp.close() );
        delay.play();
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
            clearPreviousErrorsInSignup();
            ((Label) ProgramController.currentScene.lookup("#errorSignUp")).setText("You must agree to policies!");
            ((Label) ProgramController.currentScene.lookup("#errorPoliciesSignUp")).setText("*");
            ProgramController.stage.show();
            return;
        }
        String commandSignup = "user create --username " + usernameSignUp.getText() + " --nickname " + nicknameSignUp.getText() + " --password " + passwordSignUp.getText();
        run(commandSignup);
    }

    public void clearPreviousErrorsInSignup() {
        ((Label) ProgramController.currentScene.lookup("#errorSignUp")).setText("");
        ((Label) ProgramController.currentScene.lookup("#errorUsernameSignUp")).setText("");
        ((Label) ProgramController.currentScene.lookup("#errorNicknameSignUp")).setText("");
        ((Label) ProgramController.currentScene.lookup("#errorPasswordSignUp")).setText("");
        ((Label) ProgramController.currentScene.lookup("#errorPoliciesSignUp")).setText("");
    }

    public void backToEntrance(MouseEvent mouseEvent) throws IOException {
        ProgramController.createNewScene(getClass().getResource("/fxmls/entrance.fxml"));
        ProgramController.stage.show();
    }

    public void policies(MouseEvent mouseEvent) throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource("/fxmls/policies.fxml"));
        Scene scene = new Scene(pane);
        Stage stagePopUp = new Stage();
        stagePopUp.setScene(scene);
        stagePopUp.setTitle("Policies");
        stagePopUp.setResizable(false);
        stagePopUp.show();
    }
}
