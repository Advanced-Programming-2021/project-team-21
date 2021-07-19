package controller.Effects;

import model.Duel;
import model.User;
import model.card.Card;
import model.card.Monster;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class SelectEffect {
    public static int scannerPlace;
    public static Monster scannerHolder;

    public static void run(Monster selected, User rival, User player, Duel duel, int selectedPlace) throws FileNotFoundException {
        if (selected.getCanScan().hasEffect()) {
//            DuelMenu.specialSummonsedCards = selectMonstersFromGY(rival);
//            if (DuelMenu.specialSummonsedCards.size() == 0) {
//                PrintResponses.printNoSpecialEffect();
//                DuelMenu.specialSummonsedCards = null;
//                return;
//            }
//            PrintResponses.printSpecialSummonCards(DuelMenu.specialSummonsedCards);
//            DuelMenu.isForScan = true;
            scannerPlace = selectedPlace;
            scannerHolder = Monster.copy(selected);
            scannerHolder.setATK(selected.isATK());
//            while (DuelMenu.specialSummonsedCards != null)
//                DuelMenu.checkSpecialSummon(duel, false);
        } else if (selected.getCanGetFromGYByLevelToHand().hasEffect()) {
            Card card = player.getHand().selectARandomCardFromHand();
            int i;
            for (i = 0; i < player.getHand().getCardsInHand().length; i++) {
                if (card == player.getHand().getCardsInHand()[i])
                    break;
            }
            player.getHand().discardACard(i);
//            DuelMenu.specialSummonsedCards = getCardsFromGYByLevel(
//                    selected.getCanGetFromGYByLevelToHand().getEffectNumber(), player);
//            DuelMenu.addToHand = true;
//            if (DuelMenu.specialSummonsedCards.size() > 0)
//                PrintResponses.printSpecialSummonCards(DuelMenu.specialSummonsedCards);
//            while (DuelMenu.specialSummonsedCards != null)
//                DuelMenu.checkSpecialSummon(duel, false);
            selected.getCanGetFromGYByLevelToHand().finishEffect();
            selected.getCanGetFromGYByLevelToHand().setNeedsToBeReset(true);
            selected.setSelectEffect(false);
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

    public static ArrayList<Monster> getCardsFromGYByLevel(int minimum, User boardOwner) {
        ArrayList<Monster> cards = new ArrayList<>();
        for (Card card : boardOwner.getGraveyard()) {
            if (!(card instanceof Monster)) continue;
            Monster monster = (Monster) card;
            if (monster.getLevel() >= minimum) cards.add(monster);
        }
        return cards;
    }
}
