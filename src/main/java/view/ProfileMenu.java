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

    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getMatcher(command, Regex.userChangeNickname)).find()) {
            changeNickname(matcher);
        } else if ((matcher = Regex.getMatcher(command, changePassword)).find() ||
                (matcher = Regex.getMatcher(command, Regex.changePasswordShort)).find()) {
            newPassword(matcher);
        }

    }

    private void newPassword(Matcher matcher) {
        String oldPassword = matcher.group("password"), newPassword = matcher.group("newPassword");
        if (!ProgramController.userInGame.getPassword().equals(oldPassword)) {
            clearPreviousErrorsInProfileMenu();
            ((Label) ProgramController.currentScene.lookup("#errorProfile")).setText("* : Old password is wrong!");
            ProgramController.currentScene.lookup("#errorProfile").setStyle("-fx-border-color: red; -fx-background-color: white;");
            ((Label) ProgramController.currentScene.lookup("#errorOldPassword")).setText("*");
            ProgramController.currentScene.lookup("#buttonPassword").setStyle("-fx-background-color: red;");
            ProgramController.stage.show();
            return;
        }
        if (newPassword.equals(oldPassword)) {
            errorNewAndOldPassword("* : Old and new passwords are equal!");
            ProgramController.stage.show();
            return;
        }
        ProgramController.userInGame.setPassword(newPassword);
        clearPreviousErrorsInProfileMenu();
        ProgramController.currentScene.lookup("#buttonPassword").setStyle("-fx-background-color: green;");
        ((Label) ProgramController.currentScene.lookup("#errorProfile")).setText("Password changed successfully!");
        ProgramController.currentScene.lookup("#errorProfile").setStyle("-fx-border-color: green; -fx-background-color: white; -fx-text-fill: green;");
        ProgramController.stage.show();
    }

    private void changeNickname(Matcher matcher) {
        String nickname = matcher.group("nickname");
        if (User.getUserByNickname(nickname) != null) {
            clearPreviousErrorsInProfileMenu();
            ((Label) ProgramController.currentScene.lookup("#errorProfile")).setText("* : This nickname already exists!");
            ProgramController.currentScene.lookup("#errorProfile").setStyle("-fx-border-color: red; -fx-background-color: white;");
            ((Label) ProgramController.currentScene.lookup("#errorNickname")).setText("*");
            ProgramController.currentScene.lookup("#buttonNickname").setStyle("-fx-background-color: red;");
            ProgramController.stage.show();
            return;
        }
        ProgramController.userInGame.setNickname(nickname);
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

        }
        catch (Exception e) {
            ((ImageView) ProgramController.currentScene.lookup("#imageProfile")).setImage(new Image(ProgramController.userInGame.getAvatar()));

        }
        ProgramController.stage.show();
    }

    public void passInformationToChangeNickname() {
        ProgramController.startNewAudioWithoutStopPrevious("src/main/resources/audios/click.mp3");
        if (((TextField) ProgramController.currentScene.lookup("#nicknameProfile")).getText().trim().isEmpty()) {
            ((Label) ProgramController.currentScene.lookup("#errorProfile")).setText("* : Nickname field is empty!");
            ProgramController.currentScene.lookup("#errorProfile").setStyle("-fx-border-color: red; -fx-background-color: white;");
            ((Label) ProgramController.currentScene.lookup("#errorNickname")).setText("*");
            ProgramController.currentScene.lookup("#buttonNickname").setStyle("-fx-background-color: red;");
            ProgramController.stage.show();
            return;
        }
        if (!((TextField) ProgramController.currentScene.lookup("#nicknameProfile")).getText().equals(ProgramController.userInGame.getNickname())) {
            String commandChangeNickname = "profile change --nickname " + ((TextField) ProgramController.currentScene.lookup("#nicknameProfile")).getText();
            run(commandChangeNickname);
        }
        else {
            clearPreviousErrorsInProfileMenu();
            ((Label) ProgramController.currentScene.lookup("#errorProfile")).setText("* : This is your nickname!");
            ProgramController.currentScene.lookup("#errorProfile").setStyle("-fx-border-color: red; -fx-background-color: white;");
            ((Label) ProgramController.currentScene.lookup("#errorNickname")).setText("*");
            ProgramController.currentScene.lookup("#buttonNickname").setStyle("-fx-background-color: red;");
            ProgramController.stage.show();
        }
    }

    public void passInformationToChangePassword() {
        ProgramController.startNewAudioWithoutStopPrevious("src/main/resources/audios/click.mp3");
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
       run(commandChangePassword);
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
        ProgramController.startNewAudioWithoutStopPrevious("src/main/resources/audios/click.mp3");
        ProgramController.currentMenu = new MainMenu();
        ProgramController.currentMenu.showMenu();
    }

    public void changeAvatar(MouseEvent mouseEvent) {
        ProgramController.startNewAudioWithoutStopPrevious("src/main/resources/audios/click.mp3");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("JPG Files (*.jpg)",
                "*.jpg");
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setSelectedExtensionFilter(extensionFilter);
        Stage stage = new Stage();
        File image;
        image = fileChooser.showOpenDialog(stage);
        ProgramController.userInGame.setAvatar(image.toURI().toString());
        ((ImageView) ProgramController.currentScene.lookup("#imageProfile")).setImage(new Image(image.toURI().toString()));
        clearPreviousErrorsInProfileMenu();
        ((Label) ProgramController.currentScene.lookup("#errorProfile")).setText("Profile image successfully changed!");
        ProgramController.currentScene.lookup("#errorProfile").setStyle("-fx-border-color: green; -fx-background-color: white; -fx-text-fill: green;");


    }
}
