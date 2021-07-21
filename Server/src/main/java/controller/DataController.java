package controller;

import com.google.gson.Gson;
import controller.Effects.EffectsHolder;
import model.Deck;
import model.User;
import model.card.Card;
import model.card.Monster;
import model.card.Spell;
import model.card.Trap;
import model.card.effects.Effect;
import view.Regex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class DataController {


    private static final String[] CARD_TABLE_NAMES = {"Monsters", "Spells", "Traps"};
    public static EffectsHolder monster;
    public static EffectsHolder spell;
    public static EffectsHolder trap;


    //is called when we want to update user information or we want to add a deck
    public static void updateUserInformation(User user) {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/YuGiOh", "root", "YuGiOh212121%");

            updateOrCreateUser(user, connection);
            Statement stmt;
            String query;

            query = "SELECT user_id FROM users WHERE username = '" + user.getUsername() + "';";
            stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);
            ArrayList<Deck> decks = user.getDecks();
            int user_id = 0;
            if (resultSet.next())
                user_id = resultSet.getInt(1);
            for (Deck deck : decks) {
                if (!doesDeckExist(connection, deck)) {
                    addDeck(connection, user_id, deck);
                }
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateOrCreateUser(User user, Connection connection) throws SQLException {
        String query = "SELECT * FROM users WHERE username = '" + user.getUsername() + "';";
        ResultSet resultSet = getResultSet(connection, query);
        if (!resultSet.next()) {
            query = "INSERT INTO users (username, nickname, password, score, coins, avatar)"
                    + " VALUES (?, ?, ?, ?, ?, ?)";
        } else {
            query = "UPDATE users SET username = ?, nickname = ?, password = ?, score = ?, coins = ?, avatar = ? WHERE user_id = '" + resultSet.getInt(1) + "'";
        }


        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getNickname());
        preparedStatement.setString(3, user.getPassword());
        preparedStatement.setInt(4, user.getScore());
        preparedStatement.setInt(5, user.getCoins());
        preparedStatement.setString(6, user.getAvatar());
        preparedStatement.execute();

    }

    private static void addDeck(Connection connection, int user_id, Deck deck) throws SQLException {
        String query;
        PreparedStatement preparedStatement;
        query = "INSERT INTO decks (owner_id, name, is_active)" +
                "VALUES (?, ?, ?)";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, user_id);
        preparedStatement.setString(2, deck.getName());
        preparedStatement.setBoolean(3, deck.isActive());
        preparedStatement.execute();
    }

    private static boolean doesDeckExist(Connection connection, Deck deck) throws SQLException {
        ResultSet resultSet;
        String query;
        Statement stmt;
        query = "SELECT * FROM decks WHERE name = '" + deck.getName() + "'";
        stmt = connection.createStatement();
        resultSet = stmt.executeQuery(query);
        return resultSet.next();
    }


    //is called when user toggles deck-activation
    public static void updateDeck(Deck deck) {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/YuGiOh", "root", "YuGiOh212121%");
            int is_active = (deck.isActive()) ? 1 : 0;
            String query = "UPDATE decks SET is_active = '" + is_active + "' WHERE name = '" + deck.getName() + "';";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
            connection.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    //is called when user buys a new card from shop
    public static void addCardToUser(Card card, User user) {
        try {
            if (card.getAmountInShop() == 0)
                return;
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/YuGiOh", "root", "YuGiOh212121%");
            String query = "SELECT user_id FROM users WHERE username = '" + user.getUsername() + "'";
            Statement statement = connection.createStatement();
            String cardClassName = card.getClass().getSimpleName().toLowerCase();
            ResultSet resultSet = statement.executeQuery(query);
            int user_id = 0;
            if (resultSet.next())
                user_id = resultSet.getInt(1);
            query = "SELECT " + cardClassName + "_id FROM " + cardClassName + "s WHERE Name = '" + card.getName() + "'";
            resultSet = statement.executeQuery(query);
            int card_id = 0;
            if (resultSet.next())
                 card_id = resultSet.getInt(1);
            query = "INSERT INTO users_has_" + cardClassName + "s (users_user_id, " + card.getClass().getSimpleName() + "s_" + card.getClass().getSimpleName() + "_id) " +
                    "VALUES(?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, user_id);
            preparedStatement.setInt(2, card_id);
            preparedStatement.execute();
            query = "UPDATE " + cardClassName + "s SET Amount = '" + (card.getAmountInShop() - 1) + "' WHERE Name = '" + card.getName() + "'";
            connection.prepareStatement(query).execute();
            connection.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    //is called when admin wants to ban users from buying a card
    public static void toggleCanBuyCard(Card card, boolean canBuy) {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/YuGiOh", "root", "YuGiOh212121%");
            int canBuyInt = (canBuy) ? 1 : 0;
            String query = "UPDATE " + card.getClass().getSimpleName().toLowerCase() + "s SET can_buy = '" + canBuyInt + "' WHERE Name = '" + card.getName() + "'";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
            connection.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    // is called when admin wants to change the amount of a card in shop
    public static void setAmountForCard(Card card, int amount) {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/YuGiOh", "root", "YuGiOh212121%");
            String query = "UPDATE " + card.getClass().getSimpleName().toLowerCase() + "s SET Amount = '" + amount + "' WHERE Name = '" + card.getName() + "'";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
            connection.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    // is called when we want to get all users (i.e. in scoreboard)
    public static ArrayList<User> getAllUsers() {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/YuGiOh", "root", "YuGiOh212121%");
            String query = "SELECT username FROM users";
            ResultSet resultSet = getResultSet(connection, query);
            ArrayList<String> usernames = new ArrayList<>();
            while (resultSet.next()) {
                usernames.add(resultSet.getString(1));
            }
            ArrayList<User> allUsers;
            if (usernames.isEmpty())
                return null;
            allUsers = usernames.stream().map(DataController::getUserByUsername).collect(Collectors.toCollection(ArrayList::new));
            connection.close();
            return allUsers;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return null;
        }
    }

    // is called when we want to get all cards (i.e. in shop)
    public static HashMap<String, Card> getAllCards() {
        HashMap<String, Card> allCards = new HashMap<>();
        for (String tableName : CARD_TABLE_NAMES) {
            allCards.putAll(getCardsFromTable(tableName));
        }
        return allCards;
    }

    private static HashMap<String, Card> getCardsFromTable(String tableName) {
        HashMap<String, Card> cards = new HashMap<>();
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/YuGiOh", "root", "YuGiOh212121%");
            String query = "SELECT * FROM " + tableName.toLowerCase();
            ResultSet resultSet = getResultSet(connection, query);
            if (resultSet == null)
                return null;
            int size = resultSet.getMetaData().getColumnCount() - 1;
            while (resultSet.next()) {
                Object[] parameters = new Object[size];
                for (int i = 0; i < parameters.length; i++) {
                    parameters[i] = resultSet.getString(i + 2);
                }
                //checking which constructor to call
                if (tableName.equals(CARD_TABLE_NAMES[0])) {
                    Monster monster = new Monster(parameters);
                    cards.put(monster.getName(), monster);
                } else if (tableName.equals(CARD_TABLE_NAMES[1])) {
                    Spell spell = new Spell(parameters);
                    cards.put(spell.getName(), spell);
                } else {
                    Trap trap = new Trap(parameters);
                    cards.put(trap.getName(), trap);
                }
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cards;
    }

    //is called when we want to get a user with it's username
    public static User getUserByUsername(String username) {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/YuGiOh", "root", "YuGiOh212121%");
            String query = "SELECT * FROM users WHERE username = '" + username + "';";
            ResultSet resultSet = getResultSet(connection, query);
            Statement stmt;
            int user_id = 0;
            User user = null;
            if (resultSet.next()) {
                user = new User(resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("nickname"));
                user.setScore(resultSet.getInt("score"));
                user.setCoins(resultSet.getInt("coins"));
                user.setAvatar(resultSet.getString("avatar"));
                user_id = resultSet.getInt("user_id");
            }
            if (user == null)
                return null;
            query = "SELECT * FROM decks WHERE owner_id = '" + user_id + "';";
            stmt = connection.createStatement();
            resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                Deck deck = new Deck(resultSet.getString("name"));
                deck.setActive(resultSet.getBoolean("is_active"));
                user.addDeck(deck);
            }
            addCardsToUserObject(connection, user_id, user);
            return user;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return null;
        }
    }

    private static void addCardsToUserObject(Connection connection, int user_id, User user) throws SQLException {
        String query;
        Statement stmt;
        ResultSet resultSet;
        String[] cardTypes = {"Monster", "Spell", "Trap"};
        for (String cardType : cardTypes) {
            query = "SELECT " + cardType + "s_" + cardType + "_id FROM users_has_" + cardType.toLowerCase() + "s WHERE users_user_id = '" + user_id + "'";
            stmt = connection.createStatement();
            resultSet = stmt.executeQuery(query);
            ArrayList<Integer> cardIds = new ArrayList<>();
            while (resultSet.next()) {
                cardIds.add(resultSet.getInt(1));
            }
            int i = 1;
            for (Integer cardId : cardIds) {
                query = "SELECT Name FROM " + cardType.toLowerCase() + "s WHERE " + cardType + "_id = '" + cardId + "'";
                resultSet = stmt.executeQuery(query);
                if (resultSet.next()) {
                    user.addCard(Card.getCardByName(getAllCards().get(resultSet.getString(1)).getName()));
                }
            }
        }
    }

    // is called when we want to delete a deck
    public static void deleteDeck(String deckName) {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/YuGiOh", "root", "YuGiOh212121%");
            String query = "DELETE FROM decks WHERE name = '" + deckName + "';";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

    }

    // is called when we want to get a user with it's username
    public static User getUserByNickname(String nickname) {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/YuGiOh", "root", "YuGiOh212121%");
            String query = "SELECT username FROM users WHERE nickname = '" + nickname + "'";
            ResultSet resultSet = getResultSet(connection, query);
            if (resultSet.next())
                return getUserByUsername(resultSet.getString("username"));
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }

    private static ResultSet getResultSet(Connection connection, String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    //creates necessary directories for storing data
    public static void createDirectories() {
        String[] directoryNames = {"src/main/resources/exported cards"};
        for (String directoryName : directoryNames) {
            File directory = new File(directoryName);
            //noinspection ResultOfMethodCallIgnored
            directory.mkdir();
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

    public static void monsterEffectParser(String information, Monster monster) {
        if (information == null || information.isEmpty())
            return;
        String[] effects = information.split("-");
        Arrays.stream(effects).forEach(effect -> monster.getBooleanMap().get(effect).accept(true));
    }

    public static void cardPairsParser(String information, Card card) {
        if (information == null || information.isEmpty())
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


    private static void setImage(String name, File image) {
        Path oldFile
                = image.toPath();
        Path newFile = Paths.get("src/main/resources/images/cards/" + name + ".jpg");
        try {
            Files.copy(oldFile, newFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ignored) {
        }
//        ShopMenu.paths.put(name , newFile.toString());
//        DeckMenu.paths.put(name , newFile.toString());
    }

    private static StringBuilder getStringBuilder(String where) throws FileNotFoundException {
        File file = new File("src/main/resources/cards/" + where + ".json");
        StringBuilder data = new StringBuilder();
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine())
            data.append(scanner.nextLine());
        return data;
    }

}