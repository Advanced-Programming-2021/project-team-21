package view;

import controller.*;
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
import model.message.Message;
import model.message.MessageInstruction;
import model.message.MessageLabel;
import model.message.MessageTag;

import java.io.IOException;
import java.util.Objects;

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

    private void createNewUser(Message message) throws IOException {
        AppController.sendMessageToServer(message);
        Object result = AppController.receiveMessageFromServer();
        if (result instanceof String && result.equals(Responses.userExists)) {
            clearPreviousErrorsInSignup();
            ((Label) ProgramController.currentScene.lookup("#errorSignUp")).setText("* : This username already exists!");
            ProgramController.currentScene.lookup("#errorSignUp").setStyle("-fx-border-color: red; -fx-background-color: white;");
            ((Label) ProgramController.currentScene.lookup("#errorUsernameSignUp")).setText("*");
            ProgramController.stage.show();
            return;
        }
        if (result instanceof String && result.equals(Responses.NICKNAME_EXISTS)) {
            clearPreviousErrorsInSignup();
            ((Label) ProgramController.currentScene.lookup("#errorSignUp")).setText("* : This nickname already exists!");
            ProgramController.currentScene.lookup("#errorSignUp").setStyle("-fx-border-color: red; -fx-background-color: white;");
            ((Label) ProgramController.currentScene.lookup("#errorNicknameSignUp")).setText("*");
            ProgramController.stage.show();
            return;
        }
        backToEntrance();
        showPopUpSuccessfulSignUp();
    }

    private void showPopUpSuccessfulSignUp() throws IOException {
        Parent pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXMLs/successfulSignup.fxml")));
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

    private void loginNewUser(Message message) throws IOException {
        AppController.sendMessageToServer(message);
        Object result = AppController.receiveMessageFromServer();
        if (result != null && result.equals(Responses.LOGIN_ERROR)) {
            ((Label) ProgramController.currentScene.lookup("#errorLogin")).setText("Username and password didn't match!");
            ProgramController.currentScene.lookup("#errorLogin").setStyle("-fx-border-color: red; -fx-background-color: white;");
            ProgramController.stage.show();
            return;
        } if (result != null && result.equals(Responses.ALREADY_LOGGED_IN)){
            ((Label) ProgramController.currentScene.lookup("#errorLogin")).setText((String) result);
            ProgramController.currentScene.lookup("#errorLogin").setStyle("-fx-border-color: red; -fx-background-color: white;");
            ProgramController.stage.show();
            return;
        }
        ProgramController.currentToken = (String) result;
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
        Message message = new Message(MessageInstruction.USER, MessageLabel.CREATE, MessageTag.USERNAME, MessageTag.NICKNAME, MessageTag.PASSWORD);
        message.setTagsInOrder(usernameSignUp.getText(), nicknameSignUp.getText(), passwordSignUp.getText());
        createNewUser(message);
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
        String commandLogin = "user --login --username " + usernameLogin.getText() + " --password " + passwordLogin.getText();
        Message message = new Message(MessageInstruction.USER, MessageLabel.LOGIN, MessageTag.USERNAME, MessageTag.PASSWORD);
        message.setTagsInOrder(usernameLogin.getText(), passwordLogin.getText());
        loginNewUser(message);
    }
}
