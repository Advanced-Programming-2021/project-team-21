package view;

import controller.ProgramController;
import model.User;
import model.card.Card;


public class ShopMenu {
    private void buy(String command) {
        // parse regex of command
        Card card = Card.getCardByName(command);
        // check card is valid
        User user = ProgramController.userInGame;
        ProgramController.userInGame.addCard(card);
        user.setCoins(user.getCoins() - (card != null ? card.getPrice() : 0));
    }

    public void increaseMoney(String command) {
        // parse regex of command
        //ProgramController.userInGame.setCoins(ProgramController.userInGame.getCoins() + amount);
    }
}
