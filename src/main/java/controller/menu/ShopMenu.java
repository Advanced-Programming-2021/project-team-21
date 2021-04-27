package controller.menu;

import controller.DataController;
import controller.ProgramController;
import module.User;
import module.card.Card;
import view.PrintResponses;
import view.Regex;

import java.util.regex.Matcher;

public class ShopMenu implements Menuable{
    @Override
    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getMatcher(command , Regex.buyACard)).find()){
            buyCard(matcher);
        }
    }

    private void buyCard(Matcher matcher) {
    String cardName = matcher.group("name");
        Card card = DataController.getCard(cardName);
        User user = ProgramController.userInGame;
        if (card == null){
            PrintResponses.printNoCardExistToBuy();
            return;
        }
        if (user.getCoins() < card.getPrice()){
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
