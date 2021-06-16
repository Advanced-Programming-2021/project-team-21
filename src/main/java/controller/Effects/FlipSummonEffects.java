package controller.Effects;

import module.Board;
import module.Duel;
import module.User;
import module.card.Card;
import module.card.Monster;

public class FlipSummonEffects {
    // man eater bug
    static int index;

    public static void run(Monster flipSummoned, User rival, Duel duel, User userNow) {
        if (flipSummoned.getCanDestroyMonster().hasEffect()) {
            DestroyAMonster(rival, duel, userNow);
            flipSummoned.getCanDestroyMonster().finishEffect();
        }
    }

    private static void DestroyAMonster(User rival, Duel duel, User userNow) {
        Board board = rival.getBoard();
        Card[] monsters = board.getMonsters();
        Monster monster = findTheDead(monsters);
        if (monster == null) return;
        index = board.getAddressByCard(monster);
        duel.addCardToGraveyard(monster, index, userNow);
    }

    private static Monster findTheDead(Card[] monsters) {
        if (monsters == null || monsters.length == 0) return null;
        Monster monster = (Monster) monsters[0];
        for (int i = 1; i < monsters.length; i++) {
            if (!(monsters[i] instanceof Monster)) continue;
            Monster search = (Monster) monsters[i];
            if (monster.getAtk() + monster.getDef() < search.getAtk() + search.getDef()) {
                monster = search;
            }
        }
        return monster;
    }
}
