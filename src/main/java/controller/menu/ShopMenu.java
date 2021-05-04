package controller.menu;

import controller.ProgramController;
import module.User;
import module.card.Card;
import view.PrintResponses;
import view.Regex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;

public class ShopMenu implements Menuable {
    @Override
    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getMatcher(command, Regex.buyACard)).find()) {
            buyCard(matcher);
        } else if (Regex.getMatcher(command, Regex.showCardShop).matches()) {
            printCardsShop();
        } else if (Regex.getMatcher(command, Regex.menuShow).matches()) {
            showCurrentMenu();
        } else if (Regex.getMatcher(command, Regex.menuExit).matches()) {
            exitMenu();
        } else if ((matcher = Regex.getMatcher(command, Regex.showACard)).find()) {
            String cardName = matcher.group("cardName");
            PrintResponses.printACard(Card.getCardByName(cardName));
        } else PrintResponses.printInvalidFormat();
    }

    private void printCardsShop() {
        HashMap<String, Card> allCards = ProgramController.allCards;
        ArrayList<String> names = new ArrayList<>(allCards.keySet());
        Collections.sort(names);
        for (String name : names) {
            PrintResponses.printCardsInShop(name, allCards.get(name).getPrice());
        }
    }

    private void buyCard(Matcher matcher) {
        String cardName = matcher.group("name");
        Card card = Card.getCardByName(cardName);
        User user = ProgramController.userInGame;
        if (card == null) {
            PrintResponses.printNoCardExistToBuy();
            return;
        }
        if (user.getCoins() < card.getPrice()) {
            PrintResponses.printLackOfMoney();
            return;
        }
        ProgramController.userInGame.addCard(card);
        user.setCoins(user.getCoins() - card.getPrice());
    }

    @Override
    public void showCurrentMenu() {
        PrintResponses.printShopMenuShow();
    }

    @Override
    public void exitMenu() {
        ProgramController.currentMenu = new MainMenu();
    }
}
