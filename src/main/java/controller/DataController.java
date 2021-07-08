package controller;

import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import controller.Effects.EffectsHolder;
import model.AI;
import model.Deck;
import model.User;
import model.card.Card;
import model.card.Monster;
import model.card.Spell;
import model.card.Trap;
import model.card.effects.Effect;
import view.Regex;
import view.ShopMenu;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.regex.Matcher;

public class DataController {


    private static final String[] CARD_PATHS = {"src/main/resources/cards/Monster.csv",
            "src/main/resources/cards/Spell.csv",
            "src/main/resources/cards/Trap.csv"};
    private static final String USER_PATH = "src/main/resources/users";
    public static EffectsHolder monster;
    public static EffectsHolder spell;
    public static EffectsHolder trap;
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
            File file = new File(path);
            FileReader filereader = new FileReader(file);
            CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
            List<String[]> allData = csvReader.readAll();
            for (String[] allDatum : allData) {
                Object[] parameters = new Object[allData.get(0).length];
                //checking which constructor to call
                System.arraycopy(allDatum, 0, parameters, 0, allData.get(0).length);
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
        } catch (Exception e) {
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
        if (object instanceof AI)
            return;
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

    public static Card importCardFromJson(String fileAddress) {
        File file;
        String[] fileInfo = fileAddress.split("\\.");
        try {
            file = new File(fileAddress);
            StringBuilder data = new StringBuilder();
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine())
                data.append(scanner.nextLine());
            if (fileInfo[3].equals("Monster"))
                return new Gson().fromJson(data.toString(), Monster.class);
            else if (fileInfo[3].equals("Spell"))
                return new Gson().fromJson(data.toString(), Spell.class);
            else
                new Gson().fromJson(data.toString(), Trap.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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

    public static void cardPairsParser(String information, Card card) {
        if (information.isEmpty())
            return;
        String[] pairs = information.split("\\*");
        String[] parserRegexes = {Regex.parseTwoNumberEffects, Regex.parseOneNumberTwoStrings, Regex.parseTwoNumberOneString};
        Arrays.stream(pairs).forEach(pair -> Arrays.stream(parserRegexes).forEach(parserRegex -> {
            Matcher matcher = Regex.getMatcher(pair, parserRegex);
            if (matcher.find()) {
                if (parserRegex.equals(Regex.parseTwoNumberEffects))
                    card.getEffectsMap().get(pair.replaceAll("=.*", ""))
                            .accept(getEffectForTwoNumberPairs(matcher));
                else if (parserRegex.equals(Regex.parseOneNumberTwoStrings))
                    card.getEffectsMap().get(pair.replaceAll("=.*", ""))
                            .accept(getEffectForOneNumberTwoStringPairs(matcher));
                else
                    card.getEffectsMap().get(pair.replaceAll("=.*", ""))
                            .accept(getEffectForTwoNumberOneStringPairs(matcher));
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

    private static Effect getEffectForTwoNumberOneStringPairs(Matcher matcher) {
        int firstNumber = Integer.parseInt(matcher.group("firstNumber")),
                secondNumber = Integer.parseInt(matcher.group("secondNumber"));
        String string = matcher.group("string");

        return new Effect(firstNumber, secondNumber, string);
    }


    public static void initializeEffectHolders() {
        extractMonsterJson();
        extractSpellJson();
        extractTrapJson();
    }

    private static void extractTrapJson() {
        try {
            StringBuilder data = getStringBuilder("TrapEffects");
            trap = new Gson().fromJson(data.toString(), EffectsHolder.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void extractSpellJson() {
        try {
            StringBuilder data = getStringBuilder("SpellEffects");
            spell = new Gson().fromJson(data.toString(), EffectsHolder.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void extractMonsterJson() {
        try {
            StringBuilder data = getStringBuilder("MonsterEffects");
            monster = new Gson().fromJson(data.toString(), EffectsHolder.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void addSpellAndTrap(String where , String name , String group , String Description
            , int price , String effects , File image){
        setImage(name, image);
        File file = new File("src/main/resources/cards/" + where + ".csv");
        try {
            FileWriter outfile = new FileWriter(file , true);
            CSVWriter writer = new CSVWriter(outfile);
            String[] data = { name, group, Description , "Unlimited" , String.valueOf(price) , effects };
            writer.writeNext(data);
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setImage(String name, File image) {
        Path oldFile
                = image.toPath();
        Path newFile = Paths.get("src/main/resources/images/cards/" + name + ".jpg");
        try {
            Files.copy(oldFile ,newFile  ,StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException ignored) {
        }
        ShopMenu.paths.put(name , newFile.toString());
    }

    private static StringBuilder getStringBuilder(String where) throws FileNotFoundException {
        File file = new File("src/main/resources/cards/" + where + ".json");
        StringBuilder data = new StringBuilder();
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine())
            data.append(scanner.nextLine());
        return data;
    }

    public static void addMonster(String name, String level, String attribute, String monsterType, String effectsGroup,
                                  int atk, int defense, String descriptions, int price, String booleans,
                                  StringBuilder finalEffect, File image) {
        setImage(name , image);
        File file = new File("src/main/resources/cards/Monster.csv");
        try {
            FileWriter outfile = new FileWriter(file , true);
            CSVWriter writer = new CSVWriter(outfile);
            String[] data = { name, level,attribute , monsterType , effectsGroup ,
                   String.valueOf(atk) , String.valueOf(defense),  descriptions, String.valueOf(price) ,booleans,
                    finalEffect.toString() };
            writer.writeNext(data);
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}