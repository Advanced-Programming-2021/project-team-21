package controller.menu;

import controller.ProgramController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.Regex;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        new LoginMenu().run("user create -u menuExitT1yl0rSw1ftW4$B0rnIn1989 -n menuExitT1yl0rSw1ftW4$B0rnIn1989 -p 1989");
        new LoginMenu().run("user login -u menuExitT1yl0rSw1ftW4$B0rnIn1989 -n menuExitT1yl0rSw1ftW4$B0rnIn1989 -p 1989");
        ProgramController.currentMenu.run("menu exit");
        ProgramController.currentMenu.showCurrentMenu();
        new File("src/main/resources/users/menuExitT1yl0rSw1ftW4$B0rnIn1989.user.json").delete();
        assertEquals("user created successfully!" + System.lineSeparator() + "user logged in successfully!" + System.lineSeparator() + "Login Menu" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void menuShow() {
        new LoginMenu().run("user create -u menuShowT1yl0rSw1ftW4$B0rnIn1989 -n menuShowT1yl0rSw1ftW4$B0rnIn1989 -p 1989");
        new LoginMenu().run("user login -u menuShowT1yl0rSw1ftW4$B0rnIn1989 -n menuShowT1yl0rSw1ftW4$B0rnIn1989 -p 1989");
        ProgramController.currentMenu.run("menu show-current");
        new File("src/main/resources/users/menuShowT1yl0rSw1ftW4$B0rnIn1989.user.json").delete();
        assertEquals("user created successfully!" + System.lineSeparator() + "user logged in successfully!" + System.lineSeparator() + "Main menu" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void menuEnterDuel() {
        new LoginMenu().run("user create -u menuEnterDuelT1yl0rSw1ftW4$B0rnIn1989 -n menuEnterDuelT1yl0rSw1ftW4$B0rnIn1989 -p 1989");
        new LoginMenu().run("user login -u menuEnterDuelT1yl0rSw1ftW4$B0rnIn1989 -n menuEnterDuelT1yl0rSw1ftW4$B0rnIn1989 -p 1989");
        ProgramController.currentMenu.run("menu enter Duel Menu");
        ProgramController.currentMenu.showCurrentMenu();
        new File("src/main/resources/users/menuEnterDuelT1yl0rSw1ftW4$B0rnIn1989.user.json").delete();
        assertEquals("user created successfully!" + System.lineSeparator() + "user logged in successfully!" + System.lineSeparator() + "Duel Menu" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void menuEnterDeck() {
        new LoginMenu().run("user create -u menuEnterDeckT1yl0rSw1ftW4$B0rnIn1989 -n menuEnterDeckT1yl0rSw1ftW4$B0rnIn1989 -p 1989");
        new LoginMenu().run("user login -u menuEnterDeckT1yl0rSw1ftW4$B0rnIn1989 -n menuEnterDeckT1yl0rSw1ftW4$B0rnIn1989 -p 1989");
        ProgramController.currentMenu.run("menu enter Deck Menu");
        ProgramController.currentMenu.showCurrentMenu();
        new File("src/main/resources/users/menuEnterDeckT1yl0rSw1ftW4$B0rnIn1989.user.json").delete();
        assertEquals("user created successfully!" + System.lineSeparator() + "user logged in successfully!" + System.lineSeparator() + "Deck Menu" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void menuEnterProfile() {
        new LoginMenu().run("user create -u menuEnterProfileT1yl0rSw1ftW4$B0rnIn1989 -n menuEnterProfileT1yl0rSw1ftW4$B0rnIn1989 -p 1989");
        new LoginMenu().run("user login -u menuEnterProfileT1yl0rSw1ftW4$B0rnIn1989 -n menuEnterProfileT1yl0rSw1ftW4$B0rnIn1989 -p 1989");
        ProgramController.currentMenu.run("menu enter Profile Menu");
        ProgramController.currentMenu.showCurrentMenu();
        new File("src/main/resources/users/menuEnterProfileT1yl0rSw1ftW4$B0rnIn1989.user.json").delete();
        assertEquals("user created successfully!" + System.lineSeparator() + "user logged in successfully!" + System.lineSeparator() + "Profile Menu" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void menuEnterShop() {
        new LoginMenu().run("user create -u menuEnterShopT1yl0rSw1ftW4$B0rnIn1989 -n menuEnterShopT1yl0rSw1ftW4$B0rnIn1989 -p 1989");
        new LoginMenu().run("user login -u menuEnterShopT1yl0rSw1ftW4$B0rnIn1989 -n menuEnterShopT1yl0rSw1ftW4$B0rnIn1989 -p 1989");
        ProgramController.currentMenu.run("menu enter Shop Menu");
        ProgramController.currentMenu.showCurrentMenu();
        new File("src/main/resources/users/menuEnterShopT1yl0rSw1ftW4$B0rnIn1989.user.json").delete();
        assertEquals("user created successfully!" + System.lineSeparator() + "user logged in successfully!" + System.lineSeparator() + "Shop Menu" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void menuEnterImport() {
        new LoginMenu().run("user create -u menuEnterImportT1yl0rSw1ftW4$B0rnIn1989 -n menuEnterImportT1yl0rSw1ftW4$B0rnIn1989 -p 1989");
        new LoginMenu().run("user login -u menuEnterImportT1yl0rSw1ftW4$B0rnIn1989 -n menuEnterImportT1yl0rSw1ftW4$B0rnIn1989 -p 1989");
        ProgramController.currentMenu.run("menu enter Import Menu");
        ProgramController.currentMenu.showCurrentMenu();
        File file = new File("src/main/resources/users/menuEnterImportT1yl0rSw1ftW4$B0rnIn1989.user.json");
        file.deleteOnExit();
        assertEquals("user created successfully!" + System.lineSeparator() + "user logged in successfully!" + System.lineSeparator() + "Export/Import Menu" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void menuEnterExport() {
        LoginMenu loginMenu = new LoginMenu();
        loginMenu.run("user create -u menuEnterExportT1yl0rSw1ftW4$B0rnIn1989 -n menuEnterExportT1yl0rSw1ftW4$B0rnIn1989 -p 1989");
        loginMenu.run("user login -u menuEnterExportT1yl0rSw1ftW4$B0rnIn1989 -n menuEnterExportT1yl0rSw1ftW4$B0rnIn1989 -p 1989");
        ProgramController.currentMenu.run("menu enter Export Menu");
        ProgramController.currentMenu.showCurrentMenu();
        File file = new File("src/main/resources/users/menuEnterExportT1yl0rSw1ftW4$B0rnIn1989.user.json");
        file.deleteOnExit();
        assertEquals("user created successfully!" + System.lineSeparator() + "user logged in successfully!" + System.lineSeparator() + "Export/Import Menu" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void menuEnterScoreboard() {
        LoginMenu loginMenu = new LoginMenu();
        loginMenu.run("user create -u menuEnterScoreboardT1yl0rSw1ftW4$B0rnIn1989 -n menuEnterScoreboardT1yl0rSw1ftW4$B0rnIn1989 -p 1989");
        loginMenu.run("user login -u menuEnterScoreboardT1yl0rSw1ftW4$B0rnIn1989 -n menuEnterScoreboardT1yl0rSw1ftW4$B0rnIn1989 -p 1989");
        ProgramController.currentMenu.run("menu enter Scoreboard Menu");
        ProgramController.currentMenu.showCurrentMenu();
        File file = new File("src/main/resources/users/menuEnterScoreboardT1yl0rSw1ftW4$B0rnIn1989.user.json");
        file.deleteOnExit();
        assertEquals("user created successfully!" + System.lineSeparator() + "user logged in successfully!" + System.lineSeparator() + "Scoreboard Menu" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void menuEnterInvalid() {
        new LoginMenu().run("user create -u menuEnterInvalidT1yl0rSw1ftW4$B0rnIn1989 -n menuEnterInvalidT1yl0rSw1ftW4$B0rnIn1989 -p 1989");
        new LoginMenu().run("user login -u menuEnterInvalidT1yl0rSw1ftW4$B0rnIn1989 -n menuEnterInvalidT1yl0rSw1ftW4$B0rnIn1989 -p 1989");
        ProgramController.currentMenu.run("menu enter Invalid Menu");
        new File("src/main/resources/users/menuEnterInvalidT1yl0rSw1ftW4$B0rnIn1989.user.json").delete();
        assertEquals("user created successfully!" + System.lineSeparator() + "user logged in successfully!" + System.lineSeparator() + "invalid command" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void invalidCommand() {
        new LoginMenu().run("user create -u invalidCommandT1yl0rSw1ftW4$B0rnIn1989 -n invalidCommandT1yl0rSw1ftW4$B0rnIn1989 -p 1989");
        new LoginMenu().run("user login -u invalidCommandT1yl0rSw1ftW4$B0rnIn1989 -n invalidCommandT1yl0rSw1ftW4$B0rnIn1989 -p 1989");
        ProgramController.currentMenu.run("menu Taylorjoon Duel Menu");
        new File("src/main/resources/users/invalidCommandT1yl0rSw1ftW4$B0rnIn1989.user.json").delete();
        assertEquals("user created successfully!" + System.lineSeparator() + "user logged in successfully!" + System.lineSeparator() + "invalid command" + System.lineSeparator(), outputStreamCaptor.toString());
    }



}