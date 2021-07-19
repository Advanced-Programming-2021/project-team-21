package controller;

import model.User;
import model.card.Card;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;

public class ProgramController {
    //    public static Menuable currentMenu = new LoginMenu();
    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe
    private static final HashMap<String, User> tokenUserHashMap = new HashMap<>();
    public static User userInGame;
    public static HashMap<String, Card> allCards;

    public static String generateNewToken(User user) {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        tokenUserHashMap.put(base64Encoder.encodeToString(randomBytes), user);
        return base64Encoder.encodeToString(randomBytes);
    }

    public static String getCurrentTimeAndDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd | HH:mm:ss >> ");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public static User getUserWithToken(String token) {
        return User.getUserByUsername(tokenUserHashMap.get(token).getUsername());
    }

    public static void invalidateToken(String token){
        tokenUserHashMap.remove(token);
    }

    public static boolean isUserLoggedIn(User user) {
        return tokenUserHashMap.values().stream().anyMatch(loggedInUser -> loggedInUser.getUsername().equals(user.getUsername()));
    }
}
