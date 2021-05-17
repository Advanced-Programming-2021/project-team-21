package controller.menu;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginMenuTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    public void showCurrentMenuTest() {
        new LoginMenu().showCurrentMenu();
        assertEquals("Login Menu\n", outputStreamCaptor.toString());
    }


    @Test
    void userWithThisNameExistsLoginMenuTest() {
        new LoginMenu().run("user create --username UserExistsLoginMenuTest --nickname UserExistsLoginMenuTest --password ali");
        new LoginMenu().run("user create --username UserExistsLoginMenuTest --nickname UserExistsLoginMenuTest2 --password ali");
        File file = new File("src/main/resources/users/UserExistsLoginMenuTest.user.json");
        file.delete();
        assertEquals("user created successfully!\nuser with username UserExistsLoginMenuTest already exists\n", outputStreamCaptor.toString());
    }

    @Test
    void userWithThisNameExistsAbbreviationLoginMenuTest() {
        new LoginMenu().run("user create -u UserExistsLoginMenuTest -n UserExistsLoginMenuTest -p ali");
        new LoginMenu().run("user create -u UserExistsLoginMenuTest -n UserExistsLoginMenuTest2 -p ali");
        File file = new File("src/main/resources/users/UserExistsLoginMenuTest.user.json");
        file.delete();
        assertEquals("user created successfully!\nuser with username UserExistsLoginMenuTest already exists\n", outputStreamCaptor.toString());
    }

    @Test
    void userWithThisNicknameExistsLoginMenuTest() {
        new LoginMenu().run("user create --username NicknameExistsLoginMenuTest --nickname NicknameExistsLoginMenuTest --password ali");
        new LoginMenu().run("user create --username NicknameExistsLoginMenuTest2 --nickname NicknameExistsLoginMenuTest --password ali");
        File file = new File("src/main/resources/users/NicknameExistsLoginMenuTest.user.json");
        file.delete();
        assertEquals("user created successfully!\nuser with nickname NicknameExistsLoginMenuTest already exists\n", outputStreamCaptor.toString());
    }

    @Test
    void userWithThisNicknameExistsAbbreviationLoginMenuTest() {
        new LoginMenu().run("user create -u NicknameExistsLoginMenuTest -n NicknameExistsLoginMenuTest -p ali");
        new LoginMenu().run("user create -u NicknameExistsLoginMenuTest2 -n NicknameExistsLoginMenuTest -p ali");
        File file = new File("src/main/resources/users/NicknameExistsLoginMenuTest.user.json");
        file.delete();
        assertEquals("user created successfully!\nuser with nickname NicknameExistsLoginMenuTest already exists\n", outputStreamCaptor.toString());
    }

    // Begin test 6 combination of username, nickname and password for successful user creation
    @Test
    void successfulUserCreationLoginMenuTest() {
        new LoginMenu().run("user create --username SuccessfulUserCreationLoginMenuTest --nickname SuccessfulUserCreationLoginMenuTest --password ali");
        assertEquals("user created successfully!\n", outputStreamCaptor.toString());
        File file = new File("src/main/resources/users/SuccessfulUserCreationLoginMenuTest.user.json");
        file.delete();
    }

    @Test
    void successfulUserCreationLoginMenuTest2() {
        new LoginMenu().run("user create --username SuccessfulUserCreationLoginMenuTest --password ali --nickname SuccessfulUserCreationLoginMenuTest");
        assertEquals("user created successfully!\n", outputStreamCaptor.toString());
        File file = new File("src/main/resources/users/SuccessfulUserCreationLoginMenuTest.user.json");
        file.delete();
    }

    @Test
    void successfulUserCreationLoginMenuTest3() {
        new LoginMenu().run("user create --nickname SuccessfulUserCreationLoginMenuTest --username SuccessfulUserCreationLoginMenuTest --password ali");
        assertEquals("user created successfully!\n", outputStreamCaptor.toString());
        File file = new File("src/main/resources/users/SuccessfulUserCreationLoginMenuTest.user.json");
        file.delete();
    }

    @Test
    void successfulUserCreationLoginMenuTest4() {
        new LoginMenu().run("user create --nickname SuccessfulUserCreationLoginMenuTest --password ali --username SuccessfulUserCreationLoginMenuTest");
        assertEquals("user created successfully!\n", outputStreamCaptor.toString());
        File file = new File("src/main/resources/users/SuccessfulUserCreationLoginMenuTest.user.json");
        file.delete();
    }

    @Test
    void successfulUserCreationLoginMenuTest5() {
        new LoginMenu().run("user create --password ali --nickname SuccessfulUserCreationLoginMenuTest --username SuccessfulUserCreationLoginMenuTest");
        assertEquals("user created successfully!\n", outputStreamCaptor.toString());
        File file = new File("src/main/resources/users/SuccessfulUserCreationLoginMenuTest.user.json");
        file.delete();
    }

    @Test
    void successfulUserCreationLoginMenuTest6() {
        new LoginMenu().run("user create --password ali --username SuccessfulUserCreationLoginMenuTest --nickname SuccessfulUserCreationLoginMenuTest");
        assertEquals("user created successfully!\n", outputStreamCaptor.toString());
        File file = new File("src/main/resources/users/SuccessfulUserCreationLoginMenuTest.user.json");
        file.delete();
    }
    // End test 6 combination of username, nickname and password for successful user creation

    // Begin test 6 combination of abbreviation of username, nickname and password for successful user creation
    @Test
    void successfulUserCreationAbbreviationLoginMenuTest() {
        new LoginMenu().run("user create -u SuccessfulUserCreationAbbreviationLoginMenuTest -n SuccessfulUserCreationAbbreviationLoginMenuTest -p ali");
        assertEquals("user created successfully!\n", outputStreamCaptor.toString());
        File file = new File("src/main/resources/users/SuccessfulUserCreationAbbreviationLoginMenuTest.user.json");
        file.delete();
    }

    @Test
    void successfulUserCreationAbbreviationLoginMenuTest2() {
        new LoginMenu().run("user create -u SuccessfulUserCreationLoginMenuTest -p ali -n SuccessfulUserCreationLoginMenuTest");
        assertEquals("user created successfully!\n", outputStreamCaptor.toString());
        File file = new File("src/main/resources/users/SuccessfulUserCreationAbbreviationLoginMenuTest.user.json");
        file.delete();
    }

    @Test
    void successfulUserCreationAbbreviationLoginMenuTest3() {
        new LoginMenu().run("user create -n SuccessfulUserCreationLoginMenuTest -u SuccessfulUserCreationLoginMenuTest -p ali");
        assertEquals("user created successfully!\n", outputStreamCaptor.toString());
        File file = new File("src/main/resources/users/SuccessfulUserCreationAbbreviationLoginMenuTest.user.json");
        file.delete();
    }

    @Test
    void successfulUserCreationAbbreviationLoginMenuTest4() {
        new LoginMenu().run("user create -n SuccessfulUserCreationLoginMenuTest -p ali -u SuccessfulUserCreationLoginMenuTest");
        assertEquals("user created successfully!\n", outputStreamCaptor.toString());
        File file = new File("src/main/resources/users/SuccessfulUserCreationAbbreviationLoginMenuTest.user.json");
        file.delete();
    }

    @Test
    void successfulUserCreationAbbreviationLoginMenuTest5() {
        new LoginMenu().run("user create -p ali --nickname SuccessfulUserCreationLoginMenuTest -u SuccessfulUserCreationLoginMenuTest");
        assertEquals("user created successfully!\n", outputStreamCaptor.toString());
        File file = new File("src/main/resources/users/SuccessfulUserCreationAbbreviationLoginMenuTest.user.json");
        file.delete();
    }

    @Test
    void successfulUserCreationAbbreviationLoginMenuTest6() {
        new LoginMenu().run("user create -p ali -u SuccessfulUserCreationLoginMenuTest -n SuccessfulUserCreationLoginMenuTest");
        assertEquals("user created successfully!\n", outputStreamCaptor.toString());
        File file = new File("src/main/resources/users/SuccessfulUserCreationAbbreviationLoginMenuTest.user.json");
        file.delete();
    }
    // End test 6 combination of abbreviation of username, nickname and password for successful user creation

    @Test
    void successfulLoginNewUserLoginMenuTest() {
        new LoginMenu().run("user create --username SuccessfulLoginNewUserLoginMenuTest --nickname SuccessfulLoginNewUserLoginMenuTest --password ali");
        new LoginMenu().run("user login --username SuccessfulLoginNewUserLoginMenuTest --nickname SuccessfulLoginNewUserLoginMenuTest --password ali");
        File file = new File("src/main/resources/users/SuccessfulLoginNewUserLoginMenuTest.user.json");
        file.delete();
        assertEquals("user created successfully!\nuser logged in successfully!\n", outputStreamCaptor.toString());
    }

    @Test
    void successfulLoginNewUserAbbreviationLoginMenuTest() {
        new LoginMenu().run("user create -u SuccessfulLoginNewUserLoginMenuTest -n SuccessfulLoginNewUserLoginMenuTest -p ali");
        new LoginMenu().run("user login -u SuccessfulLoginNewUserLoginMenuTest -n SuccessfulLoginNewUserLoginMenuTest -p ali");
        File file = new File("src/main/resources/users/SuccessfulLoginNewUserLoginMenuTest.user.json");
        file.delete();
        assertEquals("user created successfully!\nuser logged in successfully!\n", outputStreamCaptor.toString());
    }

    @Test
    void UserDoesNotExistToLoginLoginMenuTest() {
        new LoginMenu().run("user login --username UserDoesNotExistToLoginLoginMenuTest --nickname UserDoesNotExistToLoginLoginMenuTest --password ali");
        assertEquals("Username and password didn’t match!\n", outputStreamCaptor.toString());
    }

    @Test
    void UserDoesNotExistToLoginAbbreviationLoginMenuTest() {
        new LoginMenu().run("user login -u UserDoesNotExistToLoginLoginMenuTest -n UserDoesNotExistToLoginLoginMenuTest -p ali");
        assertEquals("Username and password didn’t match!\n", outputStreamCaptor.toString());
    }

    @Test
    void invalidPasswordToLoginLoginMenuTest() {
        new LoginMenu().run("user create --username InvalidPasswordToLoginLoginMenuTest --nickname InvalidPasswordToLoginLoginMenuTest --password ali");
        new LoginMenu().run("user login --username InvalidPasswordToLoginLoginMenuTest --nickname InvalidPasswordToLoginLoginMenuTest --password ali2");
        File file = new File("src/main/resources/users/InvalidPasswordToLoginLoginMenuTest.user.json");
        file.delete();
        assertEquals("Username and password didn’t match!\n", outputStreamCaptor.toString());
    }


    @Test
    void invalidPasswordToLoginAbbreviationLoginMenuTest() {
        new LoginMenu().run("user create -u InvalidPasswordToLoginLoginMenuTest -n InvalidPasswordToLoginLoginMenuTest -p ali");
        new LoginMenu().run("user login -u InvalidPasswordToLoginLoginMenuTest -p ali2");
        File file = new File("src/main/resources/users/InvalidPasswordToLoginLoginMenuTest.user.json");
        file.delete();
        assertEquals("user created successfully!\nUsername and password didn’t match!\n", outputStreamCaptor.toString());
    }

    @Test
    public void repetitiousUsernameUserCreation() {
        new LoginMenu().run("user create --username repetitiousUsernameUserCreation --nickname repetitiousUsernameUserCreation --password 1234");
        new LoginMenu().run("user create --username repetitiousUsernameUserCreation --nickname repetitiousUsernameUserCreation2 --password 1234");
        File file = new File("src/main/resources/users/repetitiousUsernameUserCreation.user.json");
        file.delete();
        assertEquals("user created successfully!" + System.lineSeparator() + "user with username repetitiousUsernameUserCreation already exists" + System.lineSeparator(), outputStreamCaptor.toString());
    }
}