package view;

import controller.ProgramController;
import javafx.animation.PauseTransition;
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

    public TextField usernameSignUp = new TextField();
    public TextField nicknameSignUp = new TextField();
    public PasswordField passwordSignUp = new PasswordField();
    public CheckBox agreeToPolicies = new CheckBox();
    public TextField usernameLogin = new TextField();
    public PasswordField passwordLogin = new PasswordField();

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

    @Override
    public void showMenu() {

    }


    private void createNewUser(Matcher matcher) throws IOException {
        String username = matcher.group("username"), password = matcher.group("password"),
                nickname = matcher.group("nickname");
        if (User.getUserByUsername(username) != null) {
            clearPreviousErrorsInSignup();
            ((Label) ProgramController.currentScene.lookup("#errorSignUp")).setText("* : This username already exists!");
            ProgramController.currentScene.lookup("#errorSignUp").setStyle("-fx-border-color: red; -fx-background-color: white;");
            ((Label) ProgramController.currentScene.lookup("#errorUsernameSignUp")).setText("*");
            ProgramController.stage.show();
            return;
        }
        if (User.getUserByNickname(nickname) != null) {
            clearPreviousErrorsInSignup();
            ((Label) ProgramController.currentScene.lookup("#errorSignUp")).setText("* : This nickname already exists!");
            ProgramController.currentScene.lookup("#errorSignUp").setStyle("-fx-border-color: red; -fx-background-color: white;");
            ((Label) ProgramController.currentScene.lookup("#errorNicknameSignUp")).setText("*");
            ProgramController.stage.show();
            return;
        }
        new User(username, password, nickname);
        backToEntrance();
        showPopUpSuccessfulSignUp();
    }

    private void showPopUpSuccessfulSignUp() throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource("/FXMLs/succussfulSignup.fxml"));
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
        ProgramController.currentMenu = new MainMenu();
        ProgramController.currentMenu.showMenu();
    }


    public void goToLogin() throws IOException {
        ProgramController.createNewScene(getClass().getResource("/FXMLs/login.fxml"));
        ProgramController.stage.show();
    }

    public void goToSignup() throws IOException {
        ProgramController.createNewScene(getClass().getResource("/FXMLs/signup.fxml"));
        ProgramController.stage.show();
    }

    public void exitProgram() {
        System.exit(0);
    }

    public void passSignupInformationToCheck() throws IOException {
        if (!agreeToPolicies.isSelected()) {
            clearPreviousErrorsInSignup();
            ((Label) ProgramController.currentScene.lookup("#errorSignUp")).setText("* : You must agree to policies!");
            ProgramController.currentScene.lookup("#errorSignUp").setStyle("-fx-border-color: red; -fx-background-color: white;");
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

    public void backToEntrance() throws IOException {
        ProgramController.createNewScene(getClass().getResource("/FXMLs/entrance.fxml"));
        ProgramController.stage.show();
    }

    public void policies() throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource("/FXMLs/policies.fxml"));
        Scene scene = new Scene(pane);
        Stage stagePopUp = new Stage();
        stagePopUp.setScene(scene);
        stagePopUp.setTitle("Policies");
        stagePopUp.setResizable(false);
        stagePopUp.show();
    }

    public void passLoginInformationToCheck() throws IOException {
        String commandLogin = "user login --username " + usernameLogin.getText() + " --password " + passwordLogin.getText();
        run(commandLogin);
    }
}
