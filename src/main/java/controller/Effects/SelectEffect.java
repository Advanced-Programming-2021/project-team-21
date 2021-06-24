package controller.Effects;

import controller.menu.DuelMenu;
import module.Duel;
import module.User;
import module.card.Card;
import module.card.Monster;
import view.PrintResponses;

import java.util.ArrayList;

public class SelectEffect {
    public static int scannerPlace;
    public static Monster scannerHolder;

    public static void run(Monster selected, User rival, User player, Duel duel, int selectedPlace) {
        if (selected.getCanScan().hasEffect()) {
            DuelMenu.specialSummonsedCards = selectMonstersFromGY(rival);
            if (DuelMenu.specialSummonsedCards.size() == 0){
                PrintResponses.printNoSpecialEffect();
                DuelMenu.specialSummonsedCards = null;
                return;
            }
            PrintResponses.printSpecialSummonCards(DuelMenu.specialSummonsedCards);
            DuelMenu.isForScan = true;
            scannerPlace = selectedPlace;
            scannerHolder = Monster.copy(selected);
            scannerHolder.setIsATKPosition(selected.isATKPosition());
        } else if (selected.getCanGetFromGYByLevelToHand().hasEffect()) {
            Card card = player.getHand().selectARandomCardFromHand();
            int i;
            for (i = 0; i < player.getHand().getCardsInHand().length; i++) {
                if (card == player.getHand().getCardsInHand()[i])
                    break;
            }
            player.getHand().discardACard(i);
            DuelMenu.specialSummonsedCards = player.getBoard().getCardsFromGYByLevel(selected.getCanGetFromGYByLevelToHand().getEffectNumber());
            DuelMenu.addToHand = true;
        }
    }

    private static ArrayList<Monster> selectMonstersFromGY(User rival) {
        ArrayList<Monster> monsters = new ArrayList<>();
        for (Card card : rival.getGraveyard()) {
            if (!(card instanceof Monster)) continue;
            Monster monster = (Monster) card;
            monsters.add(monster);
        }
        return monsters;
    }
}
