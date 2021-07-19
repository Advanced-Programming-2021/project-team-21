package view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    public static final String logout = "logout";
    public static final String menuEnter = "menu enter (?<menuName>\\S+)";
    public static final String menuExit = "menu exit";
    public static final String changeAvatar = "change avatar (.+)";
    public static final String userLogin = "user login --username (?<username>\\S+) --password (?<password>\\S+)";
    public static final String userCreate = "user create --username (?<username>\\S+) --password (?<password>\\S+) --nickname (?<nickname>\\S+)";
    public static final String userChangeNickname = "profile change --nickname (?<nickname>\\S+)";
    public static final String changePassword = "profile change (?=.*--password)(?=.*--current (?<password>\\S+))(?=.*--new (?<newPassword>\\S+))";
    public static final String changePasswordShort = "profile change (?=.*-p (?<password>\\S+))(?=.*-n (?<newPassword>\\S+))";
    public static final String selectFromOwn = "^select\\s--(?<where>(?!field)\\S+)\\s*(?<number>\\d+)$";
    public static final String summon = "^summon$";
    public static final String set = "^set$";
    public static final String attack = "attack (?<number>\\d+)";
    public static final String specialSummon = "Special card (?<cardNumber>\\d+)";
    //These are for extracting data for card effects.
    public static final String parseTwoNumberEffects = "^(?<effectName>\\w+)=(?<firstNumber>-?\\d+)_(?<secondNumber>-?\\d+)$";
    public static final String parseOneNumberTwoStrings = "(?<effectName>\\w+)=(?<firstNumber>-?\\d+)_\"(?<stringNumber>-?\\d+)\"_\"(?<string>\\w*)\"";
    public static final String parseTwoNumberOneString = "(?<effectName>\\w+)=(?<firstNumber>-?\\d+)_(?<secondNumber>-?\\d+)_\"(?<string>\\w*)\"";
    //These are some commands that are not in the doc, including cheat-commands.
    public static final String increaseLP = "increase --LP (?<amount>-?\\d+)";
    public static final String setWinner = "duel set-winner (?<nickname>\\S+)";
    public static final String forceSelectHand = "select (?=.*--hand (?<cardName>.[^-]+))(?=.*--force)";
    public static final String addCardToHand = "add-to-hand (?<cardName>.+)";

    // TODO
    // return the 3 limit to the cards in a deck
    // add a feature that you can not add cards less than you have bought
    // add you can not do any thing in draw phase and standby phase
    // add a feature that is not necessary to type next phase and you can go directly from phases to another
    // add a feature to print something to ask for tribute
    //decks are not saved if you exit the program exactly after adding card
    // add a feature that you can activate traps in your turn too without chain
    // add tribute set
    // add if there are not enough cards to tribute you can not summon
    // add a feature to check for spells required
    public static Matcher getMatcher(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input);
    }
}