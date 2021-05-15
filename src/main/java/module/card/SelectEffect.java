package module.card;

import module.Duel;
import module.User;

import java.util.ArrayList;

public class SelectEffect {
    public static void run(Monster selected , User rival , User player , Duel duel){
        if (selected.getCanScan().hasEffect()){
            ArrayList<Card> cards = rival.getBoard().getGraveyard();
        }
    }
}
