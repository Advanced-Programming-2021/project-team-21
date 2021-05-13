package module.card;

import module.Duel;
import module.User;

import java.util.ArrayList;
import java.util.Arrays;

public class ChangeTurnEffects {
    //texchanger
    public static void run(User user , User user1 , Duel duel){
        ArrayList<Card> monsters = new ArrayList<>();
        monsters.addAll(Arrays.asList(user.getBoard().getMonsters()));
        monsters.addAll(Arrays.asList(user1.getBoard().getMonsters()));
        for (Card card : monsters) {
            if (!(card instanceof Monster))continue;
            Monster monster = (Monster) card;
            if (monster.getUndefeatable().isNeedsToBeReset())monster.getUndefeatable().resetEffect();
        }
    }
}
