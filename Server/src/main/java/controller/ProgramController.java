package controller;
import model.User;
import model.card.Card;
import view.LoginMenu;
import view.Menuable;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;

public class ProgramController {
    public static User userInGame;
    public static HashMap<String, Card> allCards;
    public static Menuable currentMenu = new LoginMenu();
    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe
    public static String currentToken;

    public static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
