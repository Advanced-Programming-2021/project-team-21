package controller.menu;

import controller.ProgramController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.Regex;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class MainMenuTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    public void menuExit() {
        new LoginMenu().run("user create -u menuExitTest -n menuExitTest -p 1234");
        new LoginMenu().run("user login -u menuExitTest -n menuExitTest -p 1234");
        ProgramController.currentMenu.run("menu exit");
        ProgramController.currentMenu.showCurrentMenu();
        File file = new File("src/main/resources/users/menuExitTest.user.json");
        file.deleteOnExit();
        assertEquals("user created successfully!" + System.lineSeparator() + "user logged in successfully!" + System.lineSeparator() + "Login Menu" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void menuShow() {
        new LoginMenu().run("user create -u ali -n ali -p 1234");
        new LoginMenu().run("user login -u ali -n ali -p 1234");
        ProgramController.currentMenu.run("menu show-current");
        new File("src/main/resources/users/ali.user.json").delete();
        assertEquals("user created successfully!" + System.lineSeparator() + "user logged in successfully!" + System.lineSeparator() + "Main menu" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void menuEnterDuel() {
        new LoginMenu().run("user create -u ali -n ali -p 1234");
        new LoginMenu().run("user login -u ali -n ali -p 1234");
        ProgramController.currentMenu.run("menu enter Duel Menu");
        ProgramController.currentMenu.showCurrentMenu();
        new File("src/main/resources/users/ali.user.json").delete();
        assertEquals("user created successfully!" + System.lineSeparator() + "user logged in successfully!" + System.lineSeparator() + "Duel Menu" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void menuEnterDeck() {
        new LoginMenu().run("user create -u ali -n ali -p 1234");
        new LoginMenu().run("user login -u ali -n ali -p 1234");
        ProgramController.currentMenu.run("menu enter Deck Menu");
        ProgramController.currentMenu.showCurrentMenu();
        new File("src/main/resources/users/ali.user.json").delete();
        assertEquals("user created successfully!" + System.lineSeparator() + "user logged in successfully!" + System.lineSeparator() + "Deck Menu" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void menuEnterProfile() {
        new LoginMenu().run("user create -u ali -n ali -p 1234");
        new LoginMenu().run("user login -u ali -n ali -p 1234");
        ProgramController.currentMenu.run("menu enter Profile Menu");
        ProgramController.currentMenu.showCurrentMenu();
        new File("src/main/resources/users/ali.user.json").delete();
        assertEquals("user created successfully!" + System.lineSeparator() + "user logged in successfully!" + System.lineSeparator() + "Profile Menu" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void menuEnterShop() {
        new LoginMenu().run("user create -u ali -n ali -p 1234");
        new LoginMenu().run("user login -u ali -n ali -p 1234");
        ProgramController.currentMenu.run("menu enter Shop Menu");
        ProgramController.currentMenu.showCurrentMenu();
        new File("src/main/resources/users/ali.user.json").delete();
        assertEquals("user created successfully!" + System.lineSeparator() + "user logged in successfully!" + System.lineSeparator() + "Shop Menu" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void menuEnterImport() {
        new LoginMenu().run("user create -u ali -n ali -p 1234");
        new LoginMenu().run("user login -u ali -n ali -p 1234");
        ProgramController.currentMenu.run("menu enter Import Menu");
        ProgramController.currentMenu.showCurrentMenu();
        File file = new File("src/main/resources/users/ali.user.json");
        file.deleteOnExit();
        assertEquals("user created successfully!" + System.lineSeparator() + "user logged in successfully!" + System.lineSeparator() + "Export/Import Menu" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void menuEnterExport() {
        LoginMenu loginMenu = new LoginMenu();
        loginMenu.run("user create -u ali -n ali -p 1234");
        loginMenu.run("user login -u ali -n ali -p 1234");
        ProgramController.currentMenu.run("menu enter Export Menu");
        ProgramController.currentMenu.showCurrentMenu();
        File file = new File("src/main/resources/users/ali.user.json");
        file.deleteOnExit();
        assertEquals("user created successfully!" + System.lineSeparator() + "user logged in successfully!" + System.lineSeparator() + "Export/Import Menu" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void menuEnterInvalid() {
        new LoginMenu().run("user create -u ali -n ali -p 1234");
        new LoginMenu().run("user login -u ali -n ali -p 1234");
        ProgramController.currentMenu.run("menu enter Invalid Menu");
        new File("src/main/resources/users/ali.user.json").delete();
        assertEquals("user created successfully!" + System.lineSeparator() + "user logged in successfully!" + System.lineSeparator() + "invalid command" + System.lineSeparator(), outputStreamCaptor.toString());
    }



}