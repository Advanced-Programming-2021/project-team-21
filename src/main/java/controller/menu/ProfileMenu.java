package controller.menu;

import controller.ProgramController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import module.User;
import view.PrintResponses;
import view.Regex;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.regex.Matcher;

import static view.Regex.changePassword;

public class ProfileMenu implements Menuable {
    public PasswordField oldPassword = new PasswordField();
    public PasswordField newPassword = new PasswordField();

    @Override
    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getMatcher(command, Regex.userChangeNickname)).find()) {
            changeNickname(matcher);
        } else if (Regex.getMatcher(command, Regex.menuEnter).find()) {
            PrintResponses.printMenuNavigationError();
        } else if (Regex.getMatcher(command, Regex.menuExit).find()) {
            exitMenu();
        } else if (Regex.getMatcher(command, Regex.menuShow).find()) {
            showCurrentMenu();
        } else if ((matcher = Regex.getMatcher(command, changePassword)).find() ||
                (matcher = Regex.getMatcher(command, Regex.changePasswordShort)).find()) {
            newPassword(matcher);
        } else PrintResponses.printInvalidFormat();

    }

    private void newPassword(Matcher matcher) {
        String oldPassword = matcher.group("password"), newPassword = matcher.group("newPassword");
        if (!ProgramController.userInGame.getPassword().equals(oldPassword)) {
            clearPreviousErrorsInProfileMenu();
            ((Label) ProgramController.currentScene.lookup("#errorProfile")).setText("* : Old password is wrong!");
            ((Label) ProgramController.currentScene.lookup("#errorProfile")).setStyle("-fx-border-color: red; -fx-background-color: white;");
            ((Label) ProgramController.currentScene.lookup("#errorOldPassword")).setText("*");
            ((Button) ProgramController.currentScene.lookup("#buttonPassword")).setStyle("-fx-background-color: red;");
            ProgramController.stage.show();
            return;
        }
        if (newPassword.equals(oldPassword)) {
            clearPreviousErrorsInProfileMenu();
            ((Label) ProgramController.currentScene.lookup("#errorProfile")).setText("* : Old and new passwords are equal!");
            ((Label) ProgramController.currentScene.lookup("#errorProfile")).setStyle("-fx-border-color: red; -fx-background-color: white;");
            ((Label) ProgramController.currentScene.lookup("#errorNewPassword")).setText("*");
            ((Label) ProgramController.currentScene.lookup("#errorOldPassword")).setText("*");
            ((Button) ProgramController.currentScene.lookup("#buttonPassword")).setStyle("-fx-background-color: red;");
            ProgramController.stage.show();
            return;
        }
        ProgramController.userInGame.setPassword(newPassword);
        clearPreviousErrorsInProfileMenu();
        ((Button) ProgramController.currentScene.lookup("#buttonPassword")).setStyle("-fx-background-color: green;");
        ((Label) ProgramController.currentScene.lookup("#errorProfile")).setText("Password changed successfully!");
        ((Label) ProgramController.currentScene.lookup("#errorProfile")).setStyle("-fx-border-color: green; -fx-background-color: white; -fx-text-fill: green;");
        ProgramController.stage.show();
    }

    private void changeNickname(Matcher matcher) {
        String nickname = matcher.group("nickname");
        if (User.getUserByNickname(nickname) != null) {
            clearPreviousErrorsInProfileMenu();
            ((Label) ProgramController.currentScene.lookup("#errorProfile")).setText("* : This nickname already exists!");
            ((Label) ProgramController.currentScene.lookup("#errorProfile")).setStyle("-fx-border-color: red; -fx-background-color: white;");
            ((Label) ProgramController.currentScene.lookup("#errorNickname")).setText("*");
            ((Button) ProgramController.currentScene.lookup("#buttonNickname")).setStyle("-fx-background-color: red;");
            ProgramController.stage.show();
            return;
        }
        ProgramController.userInGame.setNickname(nickname);
        clearPreviousErrorsInProfileMenu();
        ((Button) ProgramController.currentScene.lookup("#buttonNickname")).setStyle("-fx-background-color: green;");
        ((Label) ProgramController.currentScene.lookup("#errorProfile")).setText("Nickname changed successfully!");
        ((Label) ProgramController.currentScene.lookup("#errorProfile")).setStyle("-fx-border-color: green; -fx-background-color: white; -fx-text-fill: green;");
        ProgramController.stage.show();
    }

    public void clearPreviousErrorsInProfileMenu() {
        ((Label) ProgramController.currentScene.lookup("#errorProfile")).setStyle("");
        ((Label) ProgramController.currentScene.lookup("#errorProfile")).setText("");
        ((Label) ProgramController.currentScene.lookup("#errorNickname")).setText("");
        ((Button) ProgramController.currentScene.lookup("#buttonNickname")).setStyle("");
        ((Label) ProgramController.currentScene.lookup("#errorNewPassword")).setText("");
        ((Label) ProgramController.currentScene.lookup("#errorOldPassword")).setText("");
    }

    @Override
    public void exitMenu() {
        ProgramController.currentMenu = new MainMenu();
    }

    @Override
    public void showCurrentMenu() {
        PrintResponses.printProfileMenuShow();
    }

    public void showProfileMenu() throws IOException {
        ProgramController.createNewScene(getClass().getResource("/fxmls/profileMenu.fxml"));
        ((Label) ProgramController.currentScene.lookup("#usernameProfile")).setText(ProgramController.userInGame.getUsername());
        ((Label) ProgramController.currentScene.lookup("#usernameProfile")).setStyle("-fx-border-color: black; -fx-background-color: white;");
        ((TextField) ProgramController.currentScene.lookup("#nicknameProfile")).setText(ProgramController.userInGame.getNickname());
        ((ImageView) ProgramController.currentScene.lookup("#imageProfile")).setImage(new Image((ProgramController.userInGame.getAvatar())));
        ProgramController.stage.show();
    }

    public void passInformationToChangeNickname(MouseEvent mouseEvent) {
        if (((TextField) ProgramController.currentScene.lookup("#nicknameProfile")).getText().trim().isEmpty()) {
            ((Label) ProgramController.currentScene.lookup("#errorProfile")).setText("* : Nickname field is empty!");
            ((Label) ProgramController.currentScene.lookup("#errorProfile")).setStyle("-fx-border-color: red; -fx-background-color: white;");
            ((Label) ProgramController.currentScene.lookup("#errorNickname")).setText("*");
            ((Button) ProgramController.currentScene.lookup("#buttonNickname")).setStyle("-fx-background-color: red;");
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
            ((Label) ProgramController.currentScene.lookup("#errorProfile")).setStyle("-fx-border-color: red; -fx-background-color: white;");
            ((Label) ProgramController.currentScene.lookup("#errorNickname")).setText("*");
            ((Button) ProgramController.currentScene.lookup("#buttonNickname")).setStyle("-fx-background-color: red;");
            ProgramController.stage.show();
        }
    }

    public void passInformationToChangePassword(MouseEvent mouseEvent) {
        if (oldPassword.getText().trim().isEmpty()) {
            if (newPassword.getText().trim().isEmpty()) {
                clearPreviousErrorsInProfileMenu();
                ((Label) ProgramController.currentScene.lookup("#errorProfile")).setText("* : You must fill all password fields!");
                ((Label) ProgramController.currentScene.lookup("#errorProfile")).setStyle("-fx-border-color: red; -fx-background-color: white;");
                ((Label) ProgramController.currentScene.lookup("#errorNewPassword")).setText("*");
                ((Label) ProgramController.currentScene.lookup("#errorOldPassword")).setText("*");
                ((Button) ProgramController.currentScene.lookup("#buttonPassword")).setStyle("-fx-background-color: red;");
                return;
            } else {
                clearPreviousErrorsInProfileMenu();
                ((Label) ProgramController.currentScene.lookup("#errorProfile")).setText("* : You must fill old password field!");
                ((Label) ProgramController.currentScene.lookup("#errorProfile")).setStyle("-fx-border-color: red; -fx-background-color: white;");
                ((Label) ProgramController.currentScene.lookup("#errorOldPassword")).setText("*");
                ((Button) ProgramController.currentScene.lookup("#buttonPassword")).setStyle("-fx-background-color: red;");
                return;
            }
        }
       if (newPassword.getText().trim().isEmpty()) {
           if (oldPassword.getText().trim().isEmpty()) {
               clearPreviousErrorsInProfileMenu();
               ((Label) ProgramController.currentScene.lookup("#errorProfile")).setText("* : You must fill all password fields!");
               ((Label) ProgramController.currentScene.lookup("#errorProfile")).setStyle("-fx-border-color: red; -fx-background-color: white;");
               ((Label) ProgramController.currentScene.lookup("#errorNewPassword")).setText("*");
               ((Label) ProgramController.currentScene.lookup("#errorOldPassword")).setText("*");
               ((Button) ProgramController.currentScene.lookup("#buttonPassword")).setStyle("-fx-background-color: red;");
               return;
           }
           else {
               clearPreviousErrorsInProfileMenu();
               ((Label) ProgramController.currentScene.lookup("#errorProfile")).setText("* : You must fill new password field!");
               ((Label) ProgramController.currentScene.lookup("#errorProfile")).setStyle("-fx-border-color: red; -fx-background-color: white;");
               ((Label) ProgramController.currentScene.lookup("#errorNewPassword")).setText("*");
               ((Button) ProgramController.currentScene.lookup("#buttonPassword")).setStyle("-fx-background-color: red;");
               return;
           }
       }
       String commandChangePassword = "profile change --password --current " + oldPassword.getText() + " --new " + newPassword.getText();
       run(commandChangePassword);
    }

    public void backToMainMenu(MouseEvent mouseEvent) throws IOException {
        ProgramController.currentMenu = new MainMenu();
        ((MainMenu) ProgramController.currentMenu).showMainMenu();
    }
}
