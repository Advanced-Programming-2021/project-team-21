package controller.menu;

import controller.DataController;
import controller.ProgramController;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import module.User;
import module.card.Card;
import view.Regex;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;

public class ShopMenu implements Menuable {
    public static int currentPage = 1;
    public static int cardsShowed = 6;
    public static ArrayList<String> cardsName;

    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getMatcher(command, Regex.buyACard)).find())
            buyCard(matcher);
    }


    private void buyCard(Matcher matcher) {
        String cardName = matcher.group("name");
        Card card = Card.getCardByName(cardName);
        User user = ProgramController.userInGame;
        ProgramController.userInGame.addCard(card);
        user.setCoins(user.getCoins() - (card != null ? card.getPrice() : 0));
        ((Label) ProgramController.currentScene.lookup("#money")).setText(String.valueOf(ProgramController.userInGame.getCoins()));
        ProgramController.stage.show();
    }



    public void showMenu() throws IOException {
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
        ProgramController.currentScene.lookup("#previousPage").setDisable(true);
        ProgramController.currentScene.lookup("#previousPage").setStyle("-fx-background-color: gray;");
        ProgramController.stage.show();
    }

    public void back() throws IOException {
        ProgramController.currentMenu = new MainMenu();
        ProgramController.currentMenu.showMenu();
    }

    public void nextPage() { // bugs should be fixed
        if (ProgramController.currentScene.lookup("#previousPage").isDisable()) {
            ProgramController.currentScene.lookup("#previousPage").setDisable(false);
            ProgramController.currentScene.lookup("#previousPage").setStyle("");
        }
        currentPage++;
        if (currentPage == 16) {
            ProgramController.currentScene.lookup("#nextPage").setDisable(false);
            ProgramController.currentScene.lookup("#nextPage").setStyle("-fx-background-color: gray;");
            ((Label) ProgramController.currentScene.lookup("#page")).setText(String.valueOf(currentPage));
            ((ImageView) ProgramController.currentScene.lookup("#card1")).setImage(new Image("/images/cards/" + cardsName.get(75) + ".jpg"));
            ProgramController.stage.show();
            return;
        }
        ((Label) ProgramController.currentScene.lookup("#page")).setText(String.valueOf(currentPage));
        ((ImageView) ProgramController.currentScene.lookup("#card1")).setImage(new Image("/images/cards/" + cardsName.get(cardsShowed - 1) + ".jpg"));
        cardsShowed++;
        ((ImageView) ProgramController.currentScene.lookup("#card2")).setImage(new Image("/images/cards/" + cardsName.get(cardsShowed - 1) + ".jpg"));
        cardsShowed++;
        ((ImageView) ProgramController.currentScene.lookup("#card3")).setImage(new Image("/images/cards/" + cardsName.get(cardsShowed - 1) + ".jpg"));
        cardsShowed++;
        ((ImageView) ProgramController.currentScene.lookup("#card4")).setImage(new Image("/images/cards/" + cardsName.get(cardsShowed - 1) + ".jpg"));
        cardsShowed++;
        ((ImageView) ProgramController.currentScene.lookup("#card5")).setImage(new Image("/images/cards/" + cardsName.get(cardsShowed - 1) + ".jpg"));
        cardsShowed++;
        ProgramController.stage.show();
    }

    public void previousPage() { // bugs should be fixed
        if (ProgramController.currentScene.lookup("#nextPage").isDisable()) {
            ProgramController.currentScene.lookup("#nextPage").setDisable(false);
            ProgramController.currentScene.lookup("#nextPage").setStyle("-fx-background-color: white;");
        }
        currentPage--;
        if (currentPage == 1) {
            ProgramController.currentScene.lookup("#previousPage").setDisable(true);
            ProgramController.currentScene.lookup("#previousPage").setStyle("-fx-background-color: gray;");
        }
        if (currentPage != 15)
            cardsShowed -= 6;
        ((Label) ProgramController.currentScene.lookup("#page")).setText(String.valueOf(currentPage));
        ((ImageView) ProgramController.currentScene.lookup("#card5")).setImage(new Image("/images/cards/" + cardsName.get(cardsShowed - 1) + ".jpg"));
        cardsShowed--;
        ((ImageView) ProgramController.currentScene.lookup("#card4")).setImage(new Image("/images/cards/" + cardsName.get(cardsShowed - 1) + ".jpg"));
        cardsShowed--;
        ((ImageView) ProgramController.currentScene.lookup("#card3")).setImage(new Image("/images/cards/" + cardsName.get(cardsShowed - 1) + ".jpg"));
        cardsShowed--;
        ((ImageView) ProgramController.currentScene.lookup("#card2")).setImage(new Image("/images/cards/" + cardsName.get(cardsShowed - 1) + ".jpg"));
        cardsShowed--;
        ((ImageView) ProgramController.currentScene.lookup("#card1")).setImage(new Image("/images/cards/" + cardsName.get(cardsShowed - 1) + ".jpg"));
        cardsShowed += 5;
        ProgramController.stage.show();
    }

    public void buyFirstCard() {
        String cardName;
        if (cardsShowed < 16) {
            cardName = cardsName.get(currentPage * 5 - 5);
        }
        else
            cardName = cardsName.get(75);
        String commandBuyCard = "shop buy " + cardName;
        run(commandBuyCard);
    }

    public void buySecondCard() { // bugs should be fixed
        String commandBuyCard = "shop buy " + cardsName.get(currentPage * 5 - 4);
        run(commandBuyCard);
    }

    public void buyThirdCard() { // bugs should be fixed
        String commandBuyCard = "shop buy " + cardsName.get(currentPage * 5 - 3);
        run(commandBuyCard);
    }

    public void buyFourthCard() { // bugs should be fixed
        String commandBuyCard = "shop buy " + cardsName.get(currentPage * 5 - 2);
        run(commandBuyCard);
    }

    public void buyFifthCard() { // bugs should be fixed
        String commandBuyCard = "shop buy " + cardsName.get(currentPage * 5 - 1);
        System.out.println(commandBuyCard);
        run(commandBuyCard);
    }
}
