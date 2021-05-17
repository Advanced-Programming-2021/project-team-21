package controller;

import com.google.gson.Gson;
import module.Deck;
import module.User;
import module.card.Card;
import module.card.Monster;
import module.card.Spell;
import module.card.Trap;
import tech.tablesaw.api.Table;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

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
        String[] directoryNames = {"src/main/resources/users", "src/main/resources/decks"
                , "src/main/resources/cards"};
        for (String directoryName : directoryNames) {
            File directory = new File(directoryName);
            //noinspection ResultOfMethodCallIgnored
            directory.mkdir();
        }
    }

    //is called for saving User and Deck objects as json
    public static void saveData(Object object) {
        String dataToWrite = new Gson().toJson(object);
        if (object instanceof User) {
            try {
                FileWriter fileWriter = new FileWriter("src/main/resources/users/" + ((User) object).getUsername()
                        + ".user.json");
                fileWriter.write(dataToWrite);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (object instanceof Deck) {
            try {
                FileWriter fileWriter = new FileWriter("src/main/resources/decks/" + ((Deck) object).getName() + ".json");
                fileWriter.write(dataToWrite);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String[] getAllUserFileNames() {
        File file = new File(USER_PATH);
        return file.list();
    }
}
