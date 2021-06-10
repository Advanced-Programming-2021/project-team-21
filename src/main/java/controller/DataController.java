package controller;

import com.google.gson.Gson;
import module.Deck;
import module.User;
import module.card.Card;
import module.card.Monster;
import module.card.Spell;
import module.card.Trap;
import module.card.effects.Effect;
import tech.tablesaw.api.Table;
import view.Regex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;

public class DataController {


    private static final String[] CARD_PATHS = {"src/main/resources/cards/Monster.csv",
            "src/main/resources/cards/Spell.csv",
            "src/main/resources/cards/Trap.csv"};
    private static final String USER_PATH = "src/main/resources/users";


    public static ArrayList<User> getAllUsers() {
        String[] fileNames = getAllUserFileNames();
        ArrayList<User> allUsers = new ArrayList<>();
        if (fileNames == null)
            return null;
        for (String fileName : fileNames) {
            allUsers.add(getUserByUsername(fileName.replaceAll("src/main/resources/users", "")
                    .replaceAll(".user.json", "")));
        }
        return allUsers;
    }

    public static HashMap<String, Card> getAllCards() {
        HashMap<String, Card> allCards = new HashMap<>();
        for (String path : CARD_PATHS) {
            allCards.putAll(getCardsFromTable(path));
        }
        return allCards;
    }

    private static HashMap<String, Card> getCardsFromTable(String path) {
        HashMap<String, Card> cards = new HashMap<>();
        try {
            Table table = Table.read().csv(path);
            for (int i = 0; i < table.rowCount(); i++) {
                String[] columnNames = table.columnNames().toArray(new String[0]);
                Object[] parameters = new Object[columnNames.length];
                for (int j = 0; j < columnNames.length; j++) {
                    parameters[j] = table.column(columnNames[j]).get(i);
                }//checking which constructor to call
                if (path.equals(CARD_PATHS[0])) {
                    Monster monster = new Monster(parameters);
                    cards.put(monster.getName(), monster);
                } else if (path.equals(CARD_PATHS[1])) {
                    Spell spell = new Spell(parameters);
                    cards.put(spell.getName(), spell);
                } else {
                    Trap trap = new Trap(parameters);
                    cards.put(trap.getName(), trap);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cards;
    }

    //returns what is in the file as an User object
    public static User getUserByUsername(String username) {
        String[] fileNames = getAllUserFileNames();
        if (fileNames == null)
            return null;
        String givenUserFileName = username + ".user.json";
        for (String fileName : fileNames) {
            if (givenUserFileName.equals(fileName)) {
                String filePath = "src/main/resources/users/" + givenUserFileName;
                File file = new File(filePath);
                try {
                    StringBuilder data = new StringBuilder();
                    Scanner scanner = new Scanner(file);
                    while (scanner.hasNextLine())
                        data.append(scanner.nextLine());
                    User user = new Gson().fromJson(data.toString(), User.class);
                    addCardsToDeck(user);
                    return user;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static User getUserByNickname(String nickname) {
        String[] fileNames = getAllUserFileNames();
        if (fileNames == null)
            return null;
        for (String fileName : fileNames) {
            User user = getUserByUsername(fileName.replaceAll(".user.json", ""));
            if (user != null && user.getNickname().equals(nickname))
                return user;
        }
        return null;
    }

    //creates necessary directories for storing data
    public static void createDirectories() {
        String[] directoryNames = {"src/main/resources/users", "src/main/resources/exported cards"
                , "src/main/resources/cards"};
        for (String directoryName : directoryNames) {
            File directory = new File(directoryName);
            //noinspection ResultOfMethodCallIgnored
            directory.mkdir();
        }
    }

    //is called for saving User and Card objects as json
    public static void saveData(Object object) {
        String dataToWrite = new Gson().toJson(object);
        String fileName = "";
        if (object instanceof User)
            fileName = "src/main/resources/users/" + ((User) object).getUsername() + ".user.json";
        else if (object instanceof Card)
            fileName = "src/main/resources/exported cards/" + ((Card) object).getName() + "." + object.getClass()
                    .toString().replaceAll("class module\\.card\\.", "") + ".json";
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(dataToWrite);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Card importCardFromJson(String cardName) {
        File file = new File("src/main/resources/exported cards");
        for (String fileName : Objects.requireNonNull(file.list())) {
            String[] fileInfo = fileName.split("\\.");
            if (fileInfo[0].equals(cardName)) {
                try {
                    file = new File("src/main/resources/exported cards/" + fileName);
                    StringBuilder data = new StringBuilder();
                    Scanner scanner = new Scanner(file);
                    while (scanner.hasNextLine())
                        data.append(scanner.nextLine());
                    if (fileInfo[1].equals("Monster"))
                        return new Gson().fromJson(data.toString(), Monster.class);
                    else if (fileInfo[1].equals("Spell"))
                        return new Gson().fromJson(data.toString(), Spell.class);
                    else
                        new Gson().fromJson(data.toString(), Trap.class);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static String[] getAllUserFileNames() {
        File file = new File(USER_PATH);
        return file.list();
    }


    private static void addCardsToDeck(User user) {
        for (int i = 0; i < user.getDecks().size(); i++) {
            Deck deck = user.getDecks().get(i);
            ArrayList<Card> newMainDeckCards = new ArrayList<>();
            for (int i1 = 0; i1 < deck.getMainDeckCards().size(); i1++) {
                String cardName = deck.getMainDeckCards().get(i1).getName();
                newMainDeckCards.add(Card.getCardByName(cardName));
            }
            ArrayList<Card> newSideDeckCards = new ArrayList<>();
            for (int i1 = 0; i1 < user.getDecks().get(i).getSideDeckCards().size(); i1++) {
                String cardName = deck.getSideDeckCards().get(i1).getName();
                newSideDeckCards.add(Card.getCardByName(cardName));
            }
            deck.setMainDeckCards(newMainDeckCards);
            deck.setSideDeckCards(newSideDeckCards);
        }
    }

    public static void monsterEffectParser(String information, Monster monster) {
        if (information.isEmpty())
            return;
        String[] effects = information.split("-");
        Arrays.stream(effects).forEach(effect -> monster.getBooleanMap().get(effect).accept(true));
    }

    public static void monsterPairsParser(String information, Monster monster) {
        if (information.isEmpty())
            return;
        String[] pairs = information.split("\\*");
        String[] parserRegexes = {Regex.parseTwoNumberEffects, Regex.parseOneNumberTwoStrings};
        Arrays.stream(pairs).forEach(pair -> Arrays.stream(parserRegexes).forEach(parserRegex -> {
            Matcher matcher = Regex.getMatcher(pair, parserRegex);
            if (matcher.find()) {
                if (parserRegex.equals(Regex.parseTwoNumberEffects))
                    monster.getEffectsMap().get(pair.replaceAll("=.*", ""))
                            .accept(getEffectForTwoNumberPairs(matcher));
                else
                    monster.getEffectsMap().get(pair.replaceAll("=.*", ""))
                            .accept(getEffectForOneNumberTwoStringPairs(matcher));
            }
        }));
    }


    private static Effect getEffectForTwoNumberPairs(Matcher matcher) {
        int firstNumber = Integer.parseInt(matcher.group("firstNumber")),
                secondNumber = Integer.parseInt(matcher.group("secondNumber"));
        return new Effect(firstNumber, secondNumber);
    }

    private static Effect getEffectForOneNumberTwoStringPairs(Matcher matcher) {
        int firstNumber = Integer.parseInt(matcher.group("firstNumber"));
        String secondNumber = matcher.group("stringNumber"),
                string = matcher.group("string");
        return new Effect(firstNumber, secondNumber, string);
    }


}
