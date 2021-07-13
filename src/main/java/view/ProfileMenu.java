package view;

import controller.ProgramController;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.User;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;

import static view.Regex.changePassword;

public class ProfileMenu implements Menuable {
    public PasswordField oldPassword = new PasswordField();
    public PasswordField newPassword = new PasswordField();

    private void newPassword(String command) throws IOException {
        AppController.dataOutputStream.writeUTF(ProgramController.currentToken + " " + command);
        AppController.dataOutputStream.flush();
        String result = AppController.dataInputStream.readUTF();
        if (result.equals(Responses.invalidPassword)) {
            clearPreviousErrorsInProfileMenu();
            ((Label) ProgramController.currentScene.lookup("#errorProfile")).setText("* : Old password is wrong!");
            ProgramController.currentScene.lookup("#errorProfile").setStyle("-fx-border-color: red; -fx-background-color: white;");
            ((Label) ProgramController.currentScene.lookup("#errorOldPassword")).setText("*");
            ProgramController.currentScene.lookup("#buttonPassword").setStyle("-fx-background-color: red;");
            ProgramController.stage.show();
            return;
        }
        if (result.equals(Responses.equalityOfNewAndOldPassword)) {
            errorNewAndOldPassword("* : Old and new passwords are equal!");
            ProgramController.stage.show();
            return;
        }
        clearPreviousErrorsInProfileMenu();
        ProgramController.currentScene.lookup("#buttonPassword").setStyle("-fx-background-color: green;");
        ((Label) ProgramController.currentScene.lookup("#errorProfile")).setText("Password changed successfully!");
        ProgramController.currentScene.lookup("#errorProfile").setStyle("-fx-border-color: green; -fx-background-color: white; -fx-text-fill: green;");
        ProgramController.stage.show();
    }

    private void changeNickname(String command) throws IOException {
        AppController.dataOutputStream.writeUTF(ProgramController.currentToken + " " + command);
        AppController.dataOutputStream.flush();
        String result = AppController.dataInputStream.readUTF();
        if (result.equals(Responses.changeToCurrentNickname)) {
            clearPreviousErrorsInProfileMenu();
            ((Label) ProgramController.currentScene.lookup("#errorProfile")).setText("* : This is your nickname!");
            ProgramController.currentScene.lookup("#errorProfile").setStyle("-fx-border-color: red; -fx-background-color: white;");
            ((Label) ProgramController.currentScene.lookup("#errorNickname")).setText("*");
            ProgramController.currentScene.lookup("#buttonNickname").setStyle("-fx-background-color: red;");
            ProgramController.stage.show();
        }
        if (result.equals(Responses.nicknameExists)) {
            clearPreviousErrorsInProfileMenu();
            ((Label) ProgramController.currentScene.lookup("#errorProfile")).setText("* : This nickname already exists!");
            ProgramController.currentScene.lookup("#errorProfile").setStyle("-fx-border-color: red; -fx-background-color: white;");
            ((Label) ProgramController.currentScene.lookup("#errorNickname")).setText("*");
            ProgramController.currentScene.lookup("#buttonNickname").setStyle("-fx-background-color: red;");
            ProgramController.stage.show();
            return;
        }
        clearPreviousErrorsInProfileMenu();
        ProgramController.currentScene.lookup("#buttonNickname").setStyle("-fx-background-color: green;");
        ((Label) ProgramController.currentScene.lookup("#errorProfile")).setText("Nickname changed successfully!");
        ProgramController.currentScene.lookup("#errorProfile").setStyle("-fx-border-color: green; -fx-background-color: white; -fx-text-fill: green;");
        ProgramController.stage.show();
    }

    public void clearPreviousErrorsInProfileMenu() {
        ProgramController.currentScene.lookup("#errorProfile").setStyle("");
        ((Label) ProgramController.currentScene.lookup("#errorProfile")).setText("");
        ((Label) ProgramController.currentScene.lookup("#errorNickname")).setText("");
        ProgramController.currentScene.lookup("#buttonNickname").setStyle("");
        ((Label) ProgramController.currentScene.lookup("#errorNewPassword")).setText("");
        ((Label) ProgramController.currentScene.lookup("#errorOldPassword")).setText("");
    }

    @Override
    public void showMenu() throws IOException {
        ProgramController.createNewScene(getClass().getResource("/FXMLs/profileMenu.fxml"));
        ((Label) ProgramController.currentScene.lookup("#usernameProfile")).setText(" username : " + ProgramController.userInGame.getUsername());
        ProgramController.currentScene.lookup("#usernameProfile").setStyle("-fx-border-color: black; -fx-background-color: white;");
        ((TextField) ProgramController.currentScene.lookup("#nicknameProfile")).setText(ProgramController.userInGame.getNickname());
        try {
            ((ImageView) ProgramController.currentScene.lookup("#imageProfile")).setImage(new Image((getClass().getResource(ProgramController.userInGame.getAvatar()).toExternalForm())));

        } catch (Exception e) {
            ((ImageView) ProgramController.currentScene.lookup("#imageProfile")).setImage(new Image(ProgramController.userInGame.getAvatar()));

        }
        ProgramController.stage.show();
    }

    public void passInformationToChangeNickname() throws IOException {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        if (((TextField) ProgramController.currentScene.lookup("#nicknameProfile")).getText().trim().isEmpty()) {
            ((Label) ProgramController.currentScene.lookup("#errorProfile")).setText("* : Nickname field is empty!");
            ProgramController.currentScene.lookup("#errorProfile").setStyle("-fx-border-color: red; -fx-background-color: white;");
            ((Label) ProgramController.currentScene.lookup("#errorNickname")).setText("*");
            ProgramController.currentScene.lookup("#buttonNickname").setStyle("-fx-background-color: red;");
            ProgramController.stage.show();
            return;
        }
        changeNickname("profile change --nickname " + ((TextField) ProgramController.currentScene.lookup("#nicknameProfile")).getText());
    }

    public void passInformationToChangePassword() throws IOException {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        if (oldPassword.getText().trim().isEmpty()) {
            if (newPassword.getText().trim().isEmpty()) {
                errorNewAndOldPassword("* : You must fill all password fields!");
            } else {
                clearPreviousErrorsInProfileMenu();
                ((Label) ProgramController.currentScene.lookup("#errorProfile")).setText("* : You must fill old password field!");
                ProgramController.currentScene.lookup("#errorProfile").setStyle("-fx-border-color: red; -fx-background-color: white;");
                ((Label) ProgramController.currentScene.lookup("#errorOldPassword")).setText("*");
                ProgramController.currentScene.lookup("#buttonPassword").setStyle("-fx-background-color: red;");
            }
            return;
        }
        if (newPassword.getText().trim().isEmpty()) {
            if (oldPassword.getText().trim().isEmpty()) {
                errorNewAndOldPassword("* : You must fill all password fields!");
            } else {
                clearPreviousErrorsInProfileMenu();
                ((Label) ProgramController.currentScene.lookup("#errorProfile")).setText("* : You must fill new password field!");
                ProgramController.currentScene.lookup("#errorProfile").setStyle("-fx-border-color: red; -fx-background-color: white;");
                ((Label) ProgramController.currentScene.lookup("#errorNewPassword")).setText("*");
                ProgramController.currentScene.lookup("#buttonPassword").setStyle("-fx-background-color: red;");
            }
            return;
        }
        String commandChangePassword = "profile change --password --current " + oldPassword.getText() + " --new " + newPassword.getText();
        newPassword(commandChangePassword);
    }

    private void errorNewAndOldPassword(String error) {
        clearPreviousErrorsInProfileMenu();
        ((Label) ProgramController.currentScene.lookup("#errorProfile")).setText(error);
        ProgramController.currentScene.lookup("#errorProfile").setStyle("-fx-border-color: red; -fx-background-color: white;");
        ((Label) ProgramController.currentScene.lookup("#errorNewPassword")).setText("*");
        ((Label) ProgramController.currentScene.lookup("#errorOldPassword")).setText("*");
        ProgramController.currentScene.lookup("#buttonPassword").setStyle("-fx-background-color: red;");
    }

    public void backToMainMenu() throws IOException {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        ProgramController.currentMenu = new MainMenu();
        ProgramController.currentMenu.showMenu();
    }

    public void changeAvatar() {
        ProgramController.startNewAudio("src/main/resources/audios/click.mp3");
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("JPG Files (*.jpg)",
                    "*.jpg");
            fileChooser.getExtensionFilters().add(extensionFilter);
            fileChooser.setSelectedExtensionFilter(extensionFilter);
            Stage stage = new Stage();
            File image;
            image = fileChooser.showOpenDialog(stage);
            AppController.dataOutputStream.writeUTF(ProgramController.currentToken + " change avatar " + image.toURI().toString());
            AppController.dataOutputStream.flush();
            String result = AppController.dataInputStream.readUTF();
            ((ImageView) ProgramController.currentScene.lookup("#imageProfile")).setImage(new Image(image.toURI().toString()));
            clearPreviousErrorsInProfileMenu();
            ((Label) ProgramController.currentScene.lookup("#errorProfile")).setText("Profile image successfully changed!");
            ProgramController.currentScene.lookup("#errorProfile").setStyle("-fx-border-color: green; -fx-background-color: white; -fx-text-fill: green;");
        } catch (Exception e) {

        }
    }
}
