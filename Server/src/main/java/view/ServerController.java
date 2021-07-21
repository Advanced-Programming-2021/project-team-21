package view;

import controller.DataController;
import controller.ProgramController;
import javafx.util.Pair;
import model.Deck;
import model.User;
import model.card.Card;
import model.message.Message;
import view.annotation.Instruction;
import view.annotation.Label;
import view.annotation.Tag;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;


public class ServerController {
    private static final ServerController SERVER_CONTROLLER = new ServerController();
    // this class meant to call different methods of classes from the command receives
    private HashMap<LocalDateTime, Pair<User, String>> messages = new HashMap<>();

    private ServerController() {
    }

    public static ServerController getInstance() {
        return SERVER_CONTROLLER;
    }

    public Object run(Message message) {
        return CommandParser.getInstance().parseCommand(message, SERVER_CONTROLLER);
    }

    @Instruction("user")
    @Label("login")
    private String loginUser(@Tag("username") String username,
                             @Tag("password") String password) {
        User user = User.getUserByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            return Responses.LOGIN_ERROR;
        } else if (ProgramController.isUserLoggedIn(user))
            return Responses.ALREADY_LOGGED_IN;
        return ProgramController.generateNewToken(user);
    }

    @Instruction("user")
    @Label("create")
    private String createUser(@Tag("username") String username,
                              @Tag("nickname") String nickname,
                              @Tag("password") String password) {
        User user = User.getUserByUsername(username);
        if (user != null) {
            return Responses.USER_EXISTS;
        } else if (User.getUserByNickname(nickname) != null)
            return Responses.NICKNAME_EXISTS;
        DataController.updateUserInformation(new User(username, password, nickname));
        return Responses.USER_CREATED;
    }

    @Instruction("user")
    @Label("logout")
    private String logout(@Tag("token") String token) {
        ProgramController.invalidateToken(token);
        return Responses.LOGOUT + token;
    }

    @Instruction("deck")
    @Label("create")
    private String createDeck(@Tag("token") String token,
                              @Tag("name") String name) {
        User user = ProgramController.getUserWithToken(token);
        if (user == null)
            return Responses.INVALID_TOKEN;
        for (Deck deck : user.getDecks()) {
            if (deck.getName().equals(name))
                return Responses.DECK_ALREADY_EXISTS;
        }
        Deck deck = new Deck(name);
        user.addDeck(deck);
        DataController.updateUserInformation(user);
        return Responses.DECK_CREATED;
    }

    @Instruction("deck")
    @Label("all")
    private ArrayList<Deck> getAllDecks(@Tag("token") String token) {
        User user = ProgramController.getUserWithToken(token);
        if (user == null)
            return null;
        return user.getDecks();
    }


    @Instruction("deck")
    @Label("delete")
    private String deleteDeck(@Tag("token") String token,
                              @Tag("name") String name) {
        User user = ProgramController.getUserWithToken(token);
        if (user == null)
            return null;
        boolean found = user.getDecks().stream().anyMatch(deck -> deck.getName().equals(name));
        if (found) {
            DataController.deleteDeck(name);
            return Responses.DECK_DELETED;
        } else
            return Responses.ERROR;
    }

    @Instruction("deck")
    @Label("activate")
    private String activateDeck(@Tag("token") String token,
                                @Tag("name") String name) {
        User user = ProgramController.getUserWithToken(token);
        if (user == null)
            return null;
        for (Deck deck : user.getDecks()) {
            deck.setActive(deck.getName().equals(name));
            DataController.updateDeck(deck);
        }
        return Responses.DECK_ACTIVATED;
    }

    @Instruction("deck")
    @Label("available-cards")
    private ArrayList<Card> getAllUserCards(@Tag("token") String token) {
        User user = ProgramController.getUserWithToken(token);
        if (user == null)
            return null;
        return user.getCards();
    }

    @Instruction("user")
    @Label("all")
    private LinkedHashMap<User, Boolean> getAllUsers(@Tag("token") String token) {
        LinkedHashMap<User, Boolean> usersWithOnlineStatus = new LinkedHashMap<>();
        ArrayList<User> users = getSortedUsers(DataController.getAllUsers());
        for (User user : users) {
            Boolean isOnline = false;
            for (String token1 : ProgramController.getTokenUserHashMap().keySet()) {
                if (ProgramController.getTokenUserHashMap().get(token1).getUsername().equals(user.getUsername()))
                    isOnline = true;
            }
            usersWithOnlineStatus.put(user, isOnline);
        }
        return usersWithOnlineStatus;
    }

    private ArrayList<User> getSortedUsers(ArrayList<User> users) {
        if (users == null)
            return null;
        Comparator<User> comparator = Comparator.comparing(User::getScore, Comparator.reverseOrder())
                .thenComparing(User::getUsername);
        users.sort(comparator);
        return users.stream().limit(20).collect(Collectors.toCollection(ArrayList::new));
    }

    @Instruction("user")
    @Label("get")
    private User getUserByToken(@Tag("token") String token) {
        return ProgramController.getUserWithToken(token);
    }

    @Instruction("shop")
    @Label("all")
    private HashMap<String, Card> sendAllCards(@Tag("token") String token) {
        return DataController.getAllCards();
    }

    @Instruction("shop")
    @Label("buy")
    private String buyCard(@Tag("card") String cardName, @Tag("token") String token) {
        User user = ProgramController.getUserWithToken(token);
        Card card = Card.getCardByName(cardName);
        if (card != null && (card.getAmountInShop() == 0 || !card.isCanBuyCard()))
            return Responses.ERROR;
        DataController.addCardToUser(card, user);
        user.setCoins(user.getCoins() - card.getPrice());
        card.setAmountInShop(card.getAmountInShop() - 1);
        user.addCard(card);
        DataController.updateUserInformation(user);
        return Responses.SUCCESS;
    }

    @Instruction("card")
    @Label("get")
    private Card getCard(@Tag("card") String cardName, @Tag("token") String token) {
        User user = ProgramController.getUserWithToken(token);
        if (user == null)
            return null;
        return Card.getCardByName(cardName);
    }

    @Instruction("chat")
    @Label("all")
    private synchronized HashMap<LocalDateTime, Pair<User, String>> getMessages(@Tag("token") String token) {
        return messages;
    }

    @Instruction("chat")
    @Label("get")
    private synchronized String sendMessage(@Tag("token") String token,
                                          @Tag("message") String message,
                                          @Tag("time") String time) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        messages.put(LocalDateTime.parse(time, dtf) , new Pair<>(ProgramController.getUserWithToken(token) , message));
        return "Success";
    }

}
