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
    public static String buyACard = "shop buy (?<name>\\S+)";
    public static  String showCardShop = "shop show --all";
    public static String showACard = "card show (?<cardName>\\S+)";
    public static Matcher getMatcher(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input);
    }
}
