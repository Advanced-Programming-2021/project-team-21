package controller.menu;


import controller.ProgramController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class LoginMenuTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    public void successfulUserCreation1() {
        new LoginMenu().run("user create --username successfulUserCreation1 --nickname successfulUserCreation1 --password 1234");
        new File("src/main/resources/users/successfulUserCreation1.user.json").delete();
        assertEquals("user created successfully!" + System.lineSeparator(), outputStreamCaptor.toString());
    }


    @Test
    public void successfulUserCreation2() {
        new LoginMenu().run("user create --username successfulUserCreation2 --password 1234 --nickname successfulUserCreation2");
        new File("src/main/resources/users/successfulUserCreation2.user.json").delete();
        assertEquals("user created successfully!" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void successfulUserCreation3() {
        new LoginMenu().run("user create --nickname successfulUserCreation3 --username successfulUserCreation3 --password 1234");
        new File("src/main/resources/users/successfulUserCreation3.user.json").delete();
        assertEquals("user created successfully!" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void successfulUserCreation4() {
        new LoginMenu().run("user create --nickname successfulUserCreation4 --password 1234 --username successfulUserCreation4");
        new File("src/main/resources/users/successfulUserCreation4.user.json").delete();
        assertEquals("user created successfully!" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void successfulUserCreation5() {
        new LoginMenu().run("user create --password 1234 --nickname successfulUserCreation5 --username successfulUserCreation5");
        new File("src/main/resources/users/successfulUserCreation5.user.json").delete();
        assertEquals("user created successfully!" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void successfulUserCreation6() {
        new LoginMenu().run("user create --password 1234 --username successfulUserCreation6 --nickname successfulUserCreation6");
        new File("src/main/resources/users/successfulUserCreation6.user.json").delete();
        assertEquals("user created successfully!" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void successfulUserCreationAbbreviation1() {
        new LoginMenu().run("user create -u successfulUserCreationAbbreviation1 -n successfulUserCreationAbbreviation1 -p 1234");
        new File("src/main/resources/users/successfulUserCreationAbbreviation1.user.json").delete();
        assertEquals("user created successfully!" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void successfulUserCreationAbbreviation2() {
        new LoginMenu().run("user create -u successfulUserCreationAbbreviation2 -p 1234 -n successfulUserCreationAbbreviation2");
        new File("src/main/resources/users/successfulUserCreationAbbreviation2.user.json").delete();
        assertEquals("user created successfully!" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void successfulUserCreationAbbreviation3() {
        new LoginMenu().run("user create -n successfulUserCreationAbbreviation3 -u successfulUserCreationAbbreviation3 -p 1234");
        new File("src/main/resources/users/successfulUserCreationAbbreviation3.user.json").delete();
        assertEquals("user created successfully!" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void successfulUserCreationAbbreviation4() {
        new LoginMenu().run("user create -n successfulUserCreationAbbreviation4 -p 1234 -u successfulUserCreationAbbreviation4");
        new File("src/main/resources/users/successfulUserCreationAbbreviation4.user.json").delete();
        assertEquals("user created successfully!" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void successfulUserCreationAbbreviation5() {
        new LoginMenu().run("user create -p 1234 -n successfulUserCreationAbbreviation5 -u successfulUserCreationAbbreviation5");
        new File("src/main/resources/users/successfulUserCreationAbbreviation5.user.json").delete();
        assertEquals("user created successfully!" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void successfulUserCreationAbbreviation6() {
        new LoginMenu().run("user create -p 1234 -u successfulUserCreationAbbreviation6 -n successfulUserCreationAbbreviation6");
        new File("src/main/resources/users/successfulUserCreationAbbreviation6.user.json").delete();
        assertEquals("user created successfully!" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void repetitiousUsernameUserCreation() {
        new LoginMenu().run("user create --username repetitiousUsernameUserCreation --nickname repetitiousUsernameUserCreation --password 1234");
        new LoginMenu().run("user create --username repetitiousUsernameUserCreation --nickname repetitiousUsernameUserCreation2 --password 1234");
        File file = new File("src/main/resources/users/repetitiousUsernameUserCreation.user.json");
        file.delete();
        assertEquals("user created successfully!" + System.lineSeparator() + "user with username repetitiousUsernameUserCreation already exists" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void repetitiousNicknameUserCreation() {
        new LoginMenu().run("user create --username repetitiousUsernameUserCreation --nickname repetitiousUsernameUserCreation --password 1234");
        new LoginMenu().run("user create --username repetitiousUsernameUserCreation2 --nickname repetitiousUsernameUserCreation --password 1234");
        new File("src/main/resources/users/repetitiousUsernameUserCreation.user.json").delete();
        assertEquals("user created successfully!" + System.lineSeparator() + "user with nickname repetitiousUsernameUserCreation already exists" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void successfulLogin() {
        new LoginMenu().run("user create --username successfulLogin --nickname successfulLogin --password 1234");
        new LoginMenu().run("user login --username successfulLogin --nickname successfulLogin --password 1234");
        new File("src/main/resources/users/successfulLogin.user.json").delete();
        assertEquals("user created successfully!" + System.lineSeparator() + "user logged in successfully!" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void noUserExistsToLogin() {
        new LoginMenu().run("user login --username noUserExistsToLogin --nickname noUserExistsToLogin --password 1234");
        assertEquals("Username and password didn’t match!" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void wrongPasswordToLogin() {
        new LoginMenu().run("user create --username wrongPasswordToLogin --nickname wrongPasswordToLogin --password 1234");
        new LoginMenu().run("user login --username wrongPasswordToLogin --nickname wrongPasswordToLogin --password 12345");
        new File("src/main/resources/users/wrongPasswordToLogin.user.json").delete();
        assertEquals("user created successfully!" + System.lineSeparator() + "Username and password didn’t match!" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void invalidCommand() {
        new LoginMenu().run("user craaaaaaaaaaate --username invalidCommand --nickname invalidCommand --password 1234");
        assertEquals("invalid command" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void showCurrentMenu() {
        new LoginMenu().run("menu show-current");
        assertEquals("Login Menu" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void noLoginYet() {
        new LoginMenu().run("menu enter Main Menu");
        assertEquals("please login first" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void menuExit() {
        new LoginMenu().run("menu exit");
        assertFalse(ProgramController.gameOn);
    }

}