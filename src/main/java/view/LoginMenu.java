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

    public TextField usernameSignUp = new TextField();
    public TextField nicknameSignUp = new TextField();
    public PasswordField passwordSignUp = new PasswordField();
    public CheckBox agreeToPolicies = new CheckBox();
    public TextField usernameLogin = new TextField();
    public PasswordField passwordLogin = new PasswordField();

    @Override
    public void showMenu() {

    }

    private void createNewUser(String command) throws IOException {
        AppController.dataOutputStream.writeUTF(command);
        AppController.dataOutputStream.flush();
        String result = AppController.dataInputStream.readUTF();
        if (result.equals(Responses.userExists)) {
            clearPreviousErrorsInSignup();
            ((Label) ProgramController.currentScene.lookup("#errorSignUp")).setText("* : This username already exists!");
            ProgramController.currentScene.lookup("#errorSignUp").setStyle("-fx-border-color: red; -fx-background-color: white;");
            ((Label) ProgramController.currentScene.lookup("#errorUsernameSignUp")).setText("*");
            ProgramController.stage.show();
            return;
        }
        if (result.equals(Responses.nicknameExists)) {
            clearPreviousErrorsInSignup();
            ((Label) ProgramController.currentScene.lookup("#errorSignUp")).setText("* : This nickname already exists!");
            ProgramController.currentScene.lookup("#errorSignUp").setStyle("-fx-border-color: red; -fx-background-color: white;");
            ((Label) ProgramController.currentScene.lookup("#errorNicknameSignUp")).setText("*");
            ProgramController.stage.show();
            return;
        }
        ProgramController.currentToken = result.substring(0, 31);
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

    private void loginNewUser(String command) throws IOException {
        AppController.dataOutputStream.writeUTF(command);
        AppController.dataOutputStream.flush();
        String result = AppController.dataInputStream.readUTF();
        if (result.equals(Responses.errorLogin)) {
            ((Label) ProgramController.currentScene.lookup("#errorLogin")).setText("Username and password didn't match!");
            ProgramController.currentScene.lookup("#errorLogin").setStyle("-fx-border-color: red; -fx-background-color: white;");
            ProgramController.stage.show();
            return;
        }
        ProgramController.currentMenu = new MainMenu();
        ProgramController.currentMenu.showMenu();
    }


    public void goToLogin() throws IOException {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        ProgramController.createNewScene(getClass().getResource("/FXMLs/login.fxml"));
        ProgramController.stage.show();
    }

    public void goToSignup() throws IOException {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        ProgramController.createNewScene(getClass().getResource("/FXMLs/signup.fxml"));
        ProgramController.stage.show();
    }

    public void exitProgram() { System.exit(0); }

    public void passSignupInformationToCheck() throws IOException {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        if (!agreeToPolicies.isSelected()) {
            clearPreviousErrorsInSignup();
            ((Label) ProgramController.currentScene.lookup("#errorSignUp")).setText("* : You must agree to policies!");
            ProgramController.currentScene.lookup("#errorSignUp").setStyle("-fx-border-color: red; -fx-background-color: white;");
            ((Label) ProgramController.currentScene.lookup("#errorPoliciesSignUp")).setText("*");
            ProgramController.stage.show();
            return;
        }
        String commandSignup = "user create --username " + usernameSignUp.getText() + " --nickname " + nicknameSignUp.getText() + " --password " + passwordSignUp.getText();
        createNewUser(commandSignup);
    }

    public void clearPreviousErrorsInSignup() {
        ((Label) ProgramController.currentScene.lookup("#errorSignUp")).setText("");
        ((Label) ProgramController.currentScene.lookup("#errorUsernameSignUp")).setText("");
        ((Label) ProgramController.currentScene.lookup("#errorNicknameSignUp")).setText("");
        ((Label) ProgramController.currentScene.lookup("#errorPasswordSignUp")).setText("");
        ((Label) ProgramController.currentScene.lookup("#errorPoliciesSignUp")).setText("");
    }

    public void backToEntrance() throws IOException {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        ProgramController.createNewScene(getClass().getResource("/FXMLs/entrance.fxml"));
        ProgramController.stage.show();
    }

    public void policies() throws IOException {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        Parent pane = FXMLLoader.load(getClass().getResource("/FXMLs/policies.fxml"));
        Scene scene = new Scene(pane);
        Stage stagePopUp = new Stage();
        stagePopUp.setScene(scene);
        stagePopUp.setTitle("Policies");
        stagePopUp.setResizable(false);
        stagePopUp.show();
    }

    public void passLoginInformationToCheck() throws IOException {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        String commandLogin = "user login --username " + usernameLogin.getText() + " --password " + passwordLogin.getText();
        loginNewUser(commandLogin);
    }
}
