package view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    public static String userLogin = "user login (?=.*--username (?<username>\\S+))(?=.*--password (?<password>\\S+))";
    public static String userLoginShort = "user login (?=.*-u (?<username>\\S+))(?=.*-p (?<password>\\S+))";
    public static String menuEnter = "menu enter (?<menuName>\\S+)";
    public static String menuExit = "menu exit";
    public static String menuShow = "menu show-current";
    public static String userCreate = "user create (?=.*--username (?<username>\\S+))(?=.*--password (?<password>\\S+))" +
            "(?=.*--nickname (?<nickname>\\S+))";
    public static String userCreateShort = "user create (?=.*-u (?<username>\\S+))(?=.*-p (?<password>\\S+))" +
            "(?=.*-n (?<nickname>\\S+))";
    public static String userChangeNickname = "profile change --nickname (?<nickname>\\S+)";
    public static String changePassword = "profile change (?=.*--password (?<password>\\S+))(?=.*--new (?<newPassword>\\S+))";
    public static String changePasswordShort = "profile change (?=.*-p (?<password>\\S+))(?=.*-n (?<newPassword>\\S+))";
    public static String buyACard = "shop buy (?<name>.+)";
    public static String showCardShop = "shop show --all";
    public static String showACard = "card show (?<cardName>[a-z A-Z]+)";
    public static String deckCreate = "deck create (?<deckName>\\S+)";
    public static String deckDelete = "deck delete (?<deckName>\\S+)";
    public static String ActiveDeck = "deck set-activate (?<deckName>\\S+)";
    public static String addCardMain = "deck add-card (?=.*--card (?<cardName>[a-z A-Z]+))(?=.*--deck (?<deckName>\\S+))";
    public static String addCardSide = addCardMain + "(?=.*--(?<side>side))";
    public static String removeCardMain = "deck rm-card (?=.*--card (?<cardName>[a-z A-Z]+))(?=.*--deck (?<deckName>\\S+))";
    public static String removeCardSide = removeCardMain + "(?=.*--(?<side>side))";
    public static String showAllDeck = "deck show --all";
    public static String showDeckMain = "deck show --deck-name (?<deckName>\\S+)";
    public static String showDeckSide = "deck show (?=.*--deck-name (?<deckName>\\S+)) (?=.*--(?<side>side))";
    public static String deckShowCard = "deck show --cards";
    public static String scoreBoard = "scoreboard show";

    public static Matcher getMatcher(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input);
    }
}
