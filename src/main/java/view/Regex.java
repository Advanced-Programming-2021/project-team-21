package view;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    public static final String userLogin = "user login (?=.*--username (?<username>\\S+))(?=.*--password (?<password>\\S+))";
    public static final String userLoginShort = "user login (?=.*-u (?<username>\\S+))(?=.*-p (?<password>\\S+))";
    public static final String logout = "logout";
    public static final String menuEnter = "menu enter (?<menuName>\\S+)";
    public static final String menuExit = "menu exit";
    public static final String menuShow = "menu show-current";
    public static final String userCreate = "user create (?=.*--username (?<username>\\S+))(?=.*--password (?<password>\\S+))" + "(?=.*--nickname (?<nickname>\\S+))";
    public static final String userCreateShort = "user create (?=.*-u (?<username>\\S+))(?=.*-p (?<password>\\S+))" + "(?=.*-n (?<nickname>\\S+))";
    public static final String userChangeNickname = "profile change --nickname (?<nickname>\\S+)";
    public static final String changePassword = "profile change (?=.*--password)(?=.*--current (?<password>\\S+))(?=.*--new (?<newPassword>\\S+))";
    public static final String changePasswordShort = "profile change (?=.*-p (?<password>\\S+))(?=.*-n (?<newPassword>\\S+))";
    public static final String buyACard = "shop buy (?<name>.+)";
    public static final String showCardShop = "shop show --all";
    public static final String showACard = "card show (?<cardName>[a-z A-Z]+)";
    public static final String deckCreate = "deck create (?<deckName>\\S+)";
    public static final String deckDelete = "deck delete (?<deckName>\\S+)";
    public static final String ActiveDeck = "deck set-activate (?<deckName>\\S+)";
    public static final String addCardMain = "deck add-card (?=.*--card (?<cardName>[a-z A-Z,]+-?[a-z A-Z]*))(?=.*--deck (?<deckName>\\S+))";
    public static final String addCardSide = addCardMain + "(?=.*--(?<side>side))";
    public static final String removeCardMain = "deck rm-card (?=.*--card (?<cardName>[a-z A-Z]+))(?=.*--deck (?<deckName>\\S+))";
    public static final String removeCardSide = removeCardMain + "(?=.*--(?<side>side))";
    public static final String showAllDeck = "deck show --all";
    public static final String showDeckMain = "deck show --deck-name (?<deckName>\\S+)";
    public static final String showDeckSide = "deck show (?=.*--deck-name (?<deckName>\\S+))(?=.*--(?<side>side))";
    public static final String deckShowCard = "deck show --cards";
    public static final String scoreBoard = "scoreboard show";
    public static final String createNewDuel = "duel\\s(?=.*--new)(?=.*--second-player\\s(?<player2Username>\\S+))(?=.*--rounds\\s(?<rounds>\\d+))";
    public static final String createNewDuelWithAI = "duel\\s(?=.*--new)(?=.*--ai)(?=.*--rounds\\s(?<rounds>\\d+))";
    public static final String selectFromOwn = "^select\\s--(?<where>(?!field)\\S+)\\s*(?<number>\\d+)$";
    public static final String selectFromOwnField = "^select\\s--field$";
    public static final String selectFromOpponent = "select\\s(?=.*--(?<where>(?!field)\\S+)\\s(?<number>\\d+))(?=.*--opponent)";
    public static final String selectFromOpponentField = "^select\\s--field\\s--opponent$";
    public static final String selectFromOpponentField2 = "^select\\s--opponent\\s--field$";
    public static final String deselectCard = "select -d";
    public static final String nextPhase = "^next phase$";
    public static final String summon = "^summon$";
    public static final String set = "^set$";
    public static final String setPosition = "set\\s.*--position\\s(?<position>attack|defense)";
    public static final String flipSummon = "^flip-summon$";
    public static final String attack = "attack (?<number>\\d+)";
    public static final String attackDirectly = "^attack direct$";
    public static final String activateSpell = "^activate effect$";
    public static final String showGraveyard = "^show graveyard$";
    public static final String showSelectedCard = "card show .*--selected";
    public static final String surrender = "^surrender$";
    public static final String specialSummon = "Special card (?<cardNumber>\\d+)";
    public static final String importCard = "import card (?<cardName>\\S+)";
    public static final String exportCard = "export card (?<cardName>\\S+)";
    //These are for extracting data for card effects.
    public static final String parseTwoNumberEffects = "(?<effectName>\\w+)=(?<firstNumber>-?\\d+)_(?<secondNumber>-?\\d+)";
    public static final String parseOneNumberTwoStrings = "(?<effectName>\\w+)=(?<firstNumber>-?\\d+)_\"(?<stringNumber>-?\\d+)\"_\"(?<string>\\w+)\"";
    public static final String parseTwoNumberOneString = "(?<effectName>\\w+)=(?<firstNumber>-?\\d+)_(?<secondNumber>-?\\d+)_\"(?<string>\\w+)\"";
    //These are some commands that are not in the doc, including cheat-commands.
    public static final String increaseMoney = "increase --money (?<amount>\\d+)";
    public static final String increaseLP = "increase --LP (?<amount>\\d+)";
    public static final String setWinner = "duel set-winner (?<nickname>\\S+)";
    public static final String forceSelectHand = "select (?=.*--hand (?<cardName>.[^-]+))(?=.*--force)";
    public static final String addCardToHand = "add-to-hand (?<cardName>.+)";
    public static final String showHand = "^hand-show$";
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