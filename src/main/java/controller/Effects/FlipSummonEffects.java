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
            DestroyAMonster(rival, duel);
            flipSummoned.getCanDestroyMonster().finishEffect();
        }
    }

    private static void DestroyAMonster(User rival, Duel duel) {
        Board board = rival.getBoard();
        Card[] monsters = board.getMonsters();
        Monster monster = findTheDead(monsters , board);
        if (monster == null) return;
        duel.addCardToGraveyard(monster, index, rival);
    }

    private static Monster findTheDead(Card[] monsters , Board board) {
        Monster monster;
        if (monsters == null || (monster = board.getNotNullMonster()) == null) return null;
        index = board.getAddressByCard(monster);
        for (int i = 1; i < monsters.length; i++) {
            if (!(monsters[i] instanceof Monster)) continue;
            Monster search = (Monster) monsters[i];
            if (monster.getAtk() + monster.getDef() < search.getAtk() + search.getDef()) {
                monster = search;
                index = i + 1;
            }
        }
        return monster;
    }
}
