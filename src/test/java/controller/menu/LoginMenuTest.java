package controller.menu;


import controller.ProgramController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sun.rmi.runtime.Log;
import view.Regex;
import view.Responses;

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
        new LoginMenu().run("user create --username successfulUserCreation1T1yl0rSw1ftIs179C3nt1m3t3ers --nickname successfulUserCreation1T1yl0rSw1ftIs179C3nt1m3t3ers --password 1989");
        new File("src/main/resources/users/successfulUserCreation1T1yl0rSw1ftIs179C3nt1m3t3ers.user.json").delete();
        assertEquals(Responses.successfulUserCreation + System.lineSeparator(), outputStreamCaptor.toString());
    }


    @Test
    public void successfulUserCreation2() {
        new LoginMenu().run("user create --username successfulUserCreation2T1yl0rSw1ftIs179C3nt1m3t3ers --password 1989 --nickname successfulUserCreation2T1yl0rSw1ftIs179C3nt1m3t3ers");
        new File("src/main/resources/users/successfulUserCreation2T1yl0rSw1ftIs179C3nt1m3t3ers.user.json").delete();
        assertEquals(Responses.successfulUserCreation + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void successfulUserCreation3() {
        new LoginMenu().run("user create --nickname successfulUserCreation3T1yl0rSw1ftIs179C3nt1m3t3ers --username successfulUserCreation3T1yl0rSw1ftIs179C3nt1m3t3ers --password 1989");
        new File("src/main/resources/users/successfulUserCreation3T1yl0rSw1ftIs179C3nt1m3t3ers.user.json").delete();
        assertEquals(Responses.successfulUserCreation + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void successfulUserCreation4() {
        new LoginMenu().run("user create --nickname successfulUserCreation4T1yl0rSw1ftIs179C3nt1m3t3ers --password 1989 --username successfulUserCreation4T1yl0rSw1ftIs179C3nt1m3t3ers");
        new File("src/main/resources/users/successfulUserCreation4T1yl0rSw1ftIs179C3nt1m3t3ers.user.json").delete();
        assertEquals(Responses.successfulUserCreation + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void successfulUserCreation5() {
        new LoginMenu().run("user create --password 1989 --nickname successfulUserCreation5T1yl0rSw1ftIs179C3nt1m3t3ers --username successfulUserCreation5T1yl0rSw1ftIs179C3nt1m3t3ers");
        new File("src/main/resources/users/successfulUserCreation5T1yl0rSw1ftIs179C3nt1m3t3ers.user.json").delete();
        assertEquals(Responses.successfulUserCreation + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void successfulUserCreation6() {
        new LoginMenu().run("user create --password 1989 --username successfulUserCreation6T1yl0rSw1ftIs179C3nt1m3t3ers --nickname successfulUserCreation6T1yl0rSw1ftIs179C3nt1m3t3ers");
        new File("src/main/resources/users/successfulUserCreation6T1yl0rSw1ftIs179C3nt1m3t3ers.user.json").delete();
        assertEquals(Responses.successfulUserCreation + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void successfulUserCreationAbbreviation1() {
        new LoginMenu().run("user create -u successfulUserCreationAbbreviation1T1yl0rSw1ftIs179C3nt1m3t3ers -n successfulUserCreationAbbreviation1T1yl0rSw1ftIs179C3nt1m3t3ers -p 1989");
        new File("src/main/resources/users/successfulUserCreationAbbreviation1T1yl0rSw1ftIs179C3nt1m3t3ers.user.json").delete();
        assertEquals(Responses.successfulUserCreation + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void successfulUserCreationAbbreviation2() {
        new LoginMenu().run("user create -u successfulUserCreationAbbreviation2T1yl0rSw1ftIs179C3nt1m3t3ers -p 1989 -n successfulUserCreationAbbreviation2T1yl0rSw1ftIs179C3nt1m3t3ers");
        new File("src/main/resources/users/successfulUserCreationAbbreviation2T1yl0rSw1ftIs179C3nt1m3t3ers.user.json").delete();
        assertEquals(Responses.successfulUserCreation + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void successfulUserCreationAbbreviation3() {
        new LoginMenu().run("user create -n successfulUserCreationAbbreviation3T1yl0rSw1ftIs179C3nt1m3t3ers -u successfulUserCreationAbbreviation3T1yl0rSw1ftIs179C3nt1m3t3ers -p 1989");
        new File("src/main/resources/users/successfulUserCreationAbbreviation3T1yl0rSw1ftIs179C3nt1m3t3ers.user.json").delete();
        assertEquals(Responses.successfulUserCreation + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void successfulUserCreationAbbreviation4() {
        new LoginMenu().run("user create -n successfulUserCreationAbbreviation4T1yl0rSw1ftIs179C3nt1m3t3ers -p 1989 -u successfulUserCreationAbbreviation4T1yl0rSw1ftIs179C3nt1m3t3ers");
        new File("src/main/resources/users/successfulUserCreationAbbreviation4T1yl0rSw1ftIs179C3nt1m3t3ers.user.json").delete();
        assertEquals(Responses.successfulUserCreation + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void successfulUserCreationAbbreviation5() {
        new LoginMenu().run("user create -p 1989 -n successfulUserCreationAbbreviation5T1yl0rSw1ftIs179C3nt1m3t3ers -u successfulUserCreationAbbreviation5T1yl0rSw1ftIs179C3nt1m3t3ers");
        new File("src/main/resources/users/successfulUserCreationAbbreviation5T1yl0rSw1ftIs179C3nt1m3t3ers.user.json").delete();
        assertEquals(Responses.successfulUserCreation + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void successfulUserCreationAbbreviation6() {
        new LoginMenu().run("user create -p 1989 -u successfulUserCreationAbbreviation6T1yl0rSw1ftIs179C3nt1m3t3ers -n successfulUserCreationAbbreviation6T1yl0rSw1ftIs179C3nt1m3t3ers");
        new File("src/main/resources/users/successfulUserCreationAbbreviation6T1yl0rSw1ftIs179C3nt1m3t3ers.user.json").delete();
        assertEquals(Responses.successfulUserCreation + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void repetitiousUsernameUserCreation() {
        new LoginMenu().run("user create --username repetitiousUsernameUserCreationT1yl0rSw1ftIs179C3nt1m3t3ers --nickname repetitiousUsernameUserCreationT1yl0rSw1ftIs179C3nt1m3t3ers --password 1989");
        new LoginMenu().run("user create --username repetitiousUsernameUserCreationT1yl0rSw1ftIs179C3nt1m3t3ers --nickname repetitiousUsernameUserCreation2T1yl0rSw1ftIs179C3nt1m3t3ers --password 1989");
        File file = new File("src/main/resources/users/repetitiousUsernameUserCreationT1yl0rSw1ftIs179C3nt1m3t3ers.user.json");
        file.delete();
        assertEquals(Responses.successfulUserCreation + System.lineSeparator() + "user with username repetitiousUsernameUserCreation already exists" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void repetitiousNicknameUserCreation() {
        new LoginMenu().run("user create --username repetitiousUsernameUserCreationT1yl0rSw1ftIs179C3nt1m3t3ers --nickname repetitiousUsernameUserCreationT1yl0rSw1ftIs179C3nt1m3t3ers --password 1989");
        new LoginMenu().run("user create --username repetitiousUsernameUserCreation2T1yl0rSw1ftIs179C3nt1m3t3ers --nickname repetitiousUsernameUserCreationT1yl0rSw1ftIs179C3nt1m3t3ers --password 1989");
        new File("src/main/resources/users/repetitiousUsernameUserCreationT1yl0rSw1ftIs179C3nt1m3t3ers.user.json").delete();
        assertEquals(Responses.successfulUserCreation + System.lineSeparator() + "user with nickname repetitiousUsernameUserCreation already exists" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void successfulLogin() {
        new LoginMenu().run("user create --username successfulLoginT1yl0rSw1ftIs179C3nt1m3t3ers --nickname successfulLoginT1yl0rSw1ftIs179C3nt1m3t3ers --password 1989");
        new LoginMenu().run("user login --username successfulLoginT1yl0rSw1ftIs179C3nt1m3t3ers --password 1989");
        new File("src/main/resources/users/successfulLoginT1yl0rSw1ftIs179C3nt1m3t3ers.user.json").delete();
        assertEquals(Responses.successfulUserCreation + System.lineSeparator() + Responses.successfulLogin + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void noUserExistsToLogin() {
        new LoginMenu().run("user login --username noUserExistsToLoginT1yl0rSw1ftIs179C3nt1m3t3ers --password 1989");
        assertEquals(Responses.noUserExists + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void wrongPasswordToLogin() {
        new LoginMenu().run("user create --username wrongPasswordToLoginT1yl0rSw1ftIs179C3nt1m3t3ers --nickname wrongPasswordToLoginT1yl0rSw1ftIs179C3nt1m3t3ers --password 1989");
        new LoginMenu().run("user login --username wrongPasswordToLoginT1yl0rSw1ftIs179C3nt1m3t3ers --password 1989OldTaylorIsDead");
        new File("src/main/resources/users/wrongPasswordToLoginT1yl0rSw1ftIs179C3nt1m3t3ers.user.json").delete();
        assertEquals(Responses.successfulUserCreation + System.lineSeparator() + Responses.wrongPasswordInLogin + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void invalidCommand() {
        new LoginMenu().run("user craaaaaaaaaaate --username invalidCommandT1yl0rSw1ftIs179C3nt1m3t3ers --password 1989");
        assertEquals(Responses.invalidFormat + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void showCurrentMenu() {
        new LoginMenu().run("Regex.menuShow");
        assertEquals(Responses.loginMenuShow + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void noLoginYet() {
        new LoginMenu().run("menu enter Main Menu");
        assertEquals(Responses.noLoginYet + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void menuExit() {
        new LoginMenu().run(Regex.menuExit);
        assertFalse(ProgramController.gameOn);
    }

}