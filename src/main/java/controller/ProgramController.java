package controller;
import model.User;
import model.card.Card;
import view.LoginMenu;
import view.Menuable;

import java.util.HashMap;

public class ProgramController {
    public static User userInGame;
    public static HashMap<String, Card> allCards;
    public static Menuable currentMenu = new LoginMenu();
}
