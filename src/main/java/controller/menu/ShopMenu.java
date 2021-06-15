package controller.menu;

import controller.DataController;
import controller.ProgramController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import module.User;
import module.card.Card;
import view.PrintResponses;
import view.Regex;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;

public class ShopMenu implements Menuable {
    public static int currentPage = 1;
    public static int cardsShowed = 5;
    public static ArrayList<String> cardsName;
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
        ProgramController.userInGame.addCard(card);
        user.setCoins(user.getCoins() - card.getPrice());
        ((Label) ProgramController.currentScene.lookup("#money")).setText(String.valueOf(ProgramController.userInGame.getCoins()));
        ProgramController.stage.show();
    }

    @Override
    public void showCurrentMenu() {
        PrintResponses.printShopMenuShow();
    }

    @Override
    public void exitMenu() {
        ProgramController.currentMenu = new MainMenu();
    }

    public void showShopMenu() throws IOException {
        ProgramController.createNewScene(getClass().getResource("/fxmls/shopMenu.fxml"));
        ((Label) ProgramController.currentScene.lookup("#page")).setText(String.valueOf(currentPage));
        ((Label) ProgramController.currentScene.lookup("#money")).setText(String.valueOf(ProgramController.userInGame.getCoins()));
        cardsName = new ArrayList<>(DataController.getAllCards().keySet());
        Collections.sort(cardsName);
        ((ImageView) ProgramController.currentScene.lookup("#card1")).setImage(new Image("/images/cards/" + cardsName.get(0) + ".jpg"));
        ((ImageView) ProgramController.currentScene.lookup("#card2")).setImage(new Image("/images/cards/" + cardsName.get(1) + ".jpg"));
        ((ImageView) ProgramController.currentScene.lookup("#card3")).setImage(new Image("/images/cards/" + cardsName.get(2) + ".jpg"));
        ((ImageView) ProgramController.currentScene.lookup("#card4")).setImage(new Image("/images/cards/" + cardsName.get(3) + ".jpg"));
        ((ImageView) ProgramController.currentScene.lookup("#card5")).setImage(new Image("/images/cards/" + cardsName.get(4) + ".jpg"));
        ProgramController.stage.show();
    }

    public void back(MouseEvent mouseEvent) throws IOException {
        ProgramController.currentMenu = new MainMenu();
        ((MainMenu) ProgramController.currentMenu).showMainMenu();
    }

    public void nextPage(MouseEvent mouseEvent) { // bugs should be fixed
            currentPage++;
            ((Label) ProgramController.currentScene.lookup("#page")).setText(String.valueOf(currentPage));
            for (int i = cardsShowed, j = 1; j <= 5 || cardsShowed <= 76; j++,cardsShowed++)
                ((ImageView) ProgramController.currentScene.lookup("#card" + j)).setImage(new Image("/images/cards/" + cardsName.get(cardsShowed) + ".jpg"));
        ProgramController.stage.show();
    }

    public void previousPage(MouseEvent mouseEvent) { // bugs should be fixed
            currentPage--;
            ((Label) ProgramController.currentScene.lookup("#page")).setText(String.valueOf(currentPage));
            for (int i = cardsShowed, j = 5; j >= 1 || cardsShowed >= 1; j--,cardsShowed--)
                ((ImageView) ProgramController.currentScene.lookup("#card" + j)).setImage(new Image("/images/cards/" + cardsName.get(cardsShowed) + ".jpg"));
        ProgramController.stage.show();
    }

    public void buyFirstCard(MouseEvent mouseEvent) {
        String cardName = null;
        if (cardsShowed < 16) {
            cardName = cardsName.get(currentPage * 5 - 5);
        }
        else
            cardName = cardsName.get(75);
        String commandBuyCard = "shop buy " + cardName;
        run(commandBuyCard);
    }

    public void buySecondCard(MouseEvent mouseEvent) { // bugs should be fixed
        String commandBuyCard = "shop buy " + cardsName.get(currentPage * 5 - 4);;
        run(commandBuyCard);
    }

    public void buyThirdCard(MouseEvent mouseEvent) { // bugs should be fixed
        String commandBuyCard = "shop buy " + cardsName.get(currentPage * 5 - 3);;
        run(commandBuyCard);
    }

    public void buyFourthCard(MouseEvent mouseEvent) { // bugs should be fixed
        String commandBuyCard = "shop buy " + cardsName.get(currentPage * 5 - 2);;
        run(commandBuyCard);
    }

    public void buyFifthCard(MouseEvent mouseEvent) { // bugs should be fixed
        String commandBuyCard = "shop buy " + cardsName.get(currentPage * 5 - 1);;
        System.out.println(commandBuyCard);
        run(commandBuyCard);
    }
}
