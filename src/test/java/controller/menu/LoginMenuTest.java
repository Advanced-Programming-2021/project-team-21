package controller.menu;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class LoginMenuTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    public void showCurrentMenu() {
        new LoginMenu().showCurrentMenu();
        assertEquals("Login Menu\n", outputStreamCaptor.toString());
    }


    @Test
    void testUserWithThisNameExists() {
        new LoginMenu().run("user create --username UsernameTestLoginMenu --nickname UsernameTestLoginMenu --password ali");
        new LoginMenu().run("user create --username UsernameTestLoginMenu --nickname UsernameTestLoginMenu --password ali");
        File file = new File("src/main/resources/users/UsernameTestLoginMenu.user.json");
        file.delete();
        assertEquals("user created successfully!\nuser with username UsernameTestLoginMenu already exists\n", outputStreamCaptor.toString());
    }

}