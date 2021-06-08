package controller.menu;

import controller.ProgramController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.Regex;
import view.Responses;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ProfileMenuTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    public void changeNickname() {
        new LoginMenu().run("user create -u changeNicknameR3dw4$Publish3dOn2012 -n changeNicknameR3dw4$Publish3dOn2012 -p 1989");
        new LoginMenu().run("user login -u changeNicknameR3dw4$Publish3dOn2012 -p 1989");
        ProgramController.currentMenu.run("menu enter Profile Menu");
        ProgramController.currentMenu.run("profile change --nickname changeNicknameR3dw4$Publish3dOn201222October)");
        new File("src/main/resources/users/changeNicknameR3dw4$Publish3dOn2012.user.json").delete();
        assertEquals(Responses.successfulUserCreation + System.lineSeparator() + Responses.successfulLogin + System.lineSeparator() + "nickname changed successfully!" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void repetitiousNicknameToChange() {
        new LoginMenu().run("user create -u repetitiousNicknameToChangeR3dw4$Publish3dOn201222October -n repetitiousNicknameToChangeR3dw4$Publish3dOn201222October -p 1989");
        new LoginMenu().run("user create -u repetitiousNicknameToChangeR3dw4$Publish3dOn2012 -n repetitiousNicknameToChangeR3dw4$Publish3dOn2012 -p 1989");
        new LoginMenu().run("user login -u repetitiousNicknameToChangeR3dw4$Publish3dOn2012 -p 1989");
        ProgramController.currentMenu.run("menu enter Profile Menu");
        ProgramController.currentMenu.run("profile change --nickname repetitiousNicknameToChangeR3dw4$Publish3dOn201222October");
        new File("src/main/resources/users/repetitiousNicknameToChangeR3dw4$Publish3dOn201222October.user.json").delete();
        new File("src/main/resources/users/repetitiousNicknameToChangeR3dw4$Publish3dOn2012.user.json").delete();
        assertEquals(Responses.successfulUserCreation + System.lineSeparator() + Responses.successfulUserCreation + System.lineSeparator() + Responses.successfulLogin + System.lineSeparator() + "user with nickname repetitiousNicknameToChangeR3dw4$Publish3dOn201222October already exists" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void menuEnter() {
        new LoginMenu().run("user create -u menuEnterR3dw4$Publish3dOn2012 -n menuEnterR3dw4$Publish3dOn2012 -p 1989");
        new LoginMenu().run("user login -u menuEnterR3dw4$Publish3dOn2012 -p 1989");
        ProgramController.currentMenu.run("menu enter Profile Menu");
        ProgramController.currentMenu.run("menu enter taylor");
        new File("src/main/resources/users/menuEnterR3dw4$Publish3dOn2012.user.json").delete();
        assertEquals(Responses.successfulUserCreation + System.lineSeparator() + Responses.successfulLogin + System.lineSeparator() + Responses.menuNavigationError + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void menuExit() {
        new LoginMenu().run("user create -u menuExitR3dw4$Publish3dOn2012 -n menuExitR3dw4$Publish3dOn2012 -p 1989");
        new LoginMenu().run("user login -u menuExitR3dw4$Publish3dOn2012 -p 1989");
        ProgramController.currentMenu.run("menu enter Profile Menu");
        ProgramController.currentMenu.run("menu exit");
        ProgramController.currentMenu.showCurrentMenu();
        new File("src/main/resources/users/menuExitR3dw4$Publish3dOn2012.user.json").delete();
        assertEquals(Responses.successfulUserCreation + System.lineSeparator() + Responses.successfulLogin + System.lineSeparator() + "Main menu" + System.lineSeparator(), outputStreamCaptor.toString());

    }

    @Test
    public void menuShow() {
        new LoginMenu().run("user create -u menuShowR3dw4$Publish3dOn2012 -n menuShowR3dw4$Publish3dOn2012 -p 1989");
        new LoginMenu().run("user login -u menuShowR3dw4$Publish3dOn2012 -p 1989");
        ProgramController.currentMenu.run("menu enter Profile Menu");
        ProgramController.currentMenu.run("menu show-current");
        new File("src/main/resources/users/menuShowR3dw4$Publish3dOn2012.user.json").delete();
        assertEquals(Responses.successfulUserCreation + System.lineSeparator() + Responses.successfulLogin + System.lineSeparator() + "Profile Menu" + System.lineSeparator(), outputStreamCaptor.toString());

    }

    @Test
    public void changePassword() {
        new LoginMenu().run("user create -u changePasswordR3dw4$Publish3dOn2012 -n changePasswordR3dw4$Publish3dOn2012 -p 1989");
        new LoginMenu().run("user login -u changePasswordR3dw4$Publish3dOn2012 -p 1989");
        ProgramController.currentMenu.run("menu enter Profile Menu");
        ProgramController.currentMenu.run("profile change --password --current 1989 --new 19899");
        new File("src/main/resources/users/changePasswordR3dw4$Publish3dOn2012.user.json").delete();
        assertEquals(Responses.successfulUserCreation + System.lineSeparator() + Responses.successfulLogin + System.lineSeparator() + Responses.successfulPasswordChange + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void changePasswordWithTheSameNew() {
        new LoginMenu().run("user create -u changePasswordWithTheSameNewR3dw4$Publish3dOn2012 -n changePasswordWithTheSameNewR3dw4$Publish3dOn2012 -p 1989");
        new LoginMenu().run("user login -u changePasswordWithTheSameNewR3dw4$Publish3dOn2012 -p 1989");
        ProgramController.currentMenu.run("menu enter Profile Menu");
        ProgramController.currentMenu.run("profile change --password --current 1989 --new 1989");
        new File("src/main/resources/users/changePasswordWithTheSameNewR3dw4$Publish3dOn2012.user.json").delete();
        assertEquals(Responses.successfulUserCreation + System.lineSeparator() + Responses.successfulLogin + System.lineSeparator() + Responses.equalityOfCurrentAndNewPassword + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void changePasswordWithWrongOne() {
        new LoginMenu().run("user create -u changePasswordWithWrongOneR3dw4$Publish3dOn2012 -n changePasswordWithWrongOneR3dw4$Publish3dOn2012 -p 1989");
        new LoginMenu().run("user login -u changePasswordWithWrongOneR3dw4$Publish3dOn2012 -p 1989");
        ProgramController.currentMenu.run("menu enter Profile Menu");
        ProgramController.currentMenu.run("profile change --password --current 19899 --new 1989");
        new File("src/main/resources/users/changePasswordWithWrongOneR3dw4$Publish3dOn2012.user.json").delete();
        assertEquals(Responses.successfulUserCreation + System.lineSeparator() + Responses.successfulLogin + System.lineSeparator() + Responses.wrongPasswordInChange + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void shortChangePassword() {
        new LoginMenu().run("user create -u shortChangePasswordR3dw4$Publish3dOn2012 -n shortChangePasswordR3dw4$Publish3dOn2012 -p 1989");
        new LoginMenu().run("user login -u shortChangePasswordR3dw4$Publish3dOn2012 -p 1989");
        ProgramController.currentMenu.run("menu enter Profile Menu");
        ProgramController.currentMenu.run("profile change -p 1989 -n 19899");
        new File("src/main/resources/users/shortChangePasswordR3dw4$Publish3dOn2012.user.json").delete();
        assertEquals(Responses.successfulUserCreation + System.lineSeparator() + Responses.successfulLogin + System.lineSeparator() + Responses.successfulPasswordChange + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void invalidCommand() {
        new LoginMenu().run("user create -u invalidCommandR3dw4$Publish3dOn2012 -n invalidCommandR3dw4$Publish3dOn2012 -p 1989");
        new LoginMenu().run("user login -u invalidCommandR3dw4$Publish3dOn2012 -p 1989");
        ProgramController.currentMenu.run("menu enter Profile Menu");
        ProgramController.currentMenu.run("profile changeeeeeeeeee --password --current 19899 --new 1989");
        new File("src/main/resources/users/invalidCommandR3dw4$Publish3dOn2012.user.json").delete();
        assertEquals(Responses.successfulUserCreation + System.lineSeparator() + Responses.successfulLogin + System.lineSeparator() + Responses.invalidFormat + System.lineSeparator(), outputStreamCaptor.toString());
    }

}