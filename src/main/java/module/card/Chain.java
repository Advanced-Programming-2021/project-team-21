package module.card;

import java.util.ArrayList;

public class Chain {
    private Card card;
    private Monster cancelSummon;
    private ArrayList<Card> destroySpellOrTrap;
    private String cardName;

    public Chain(Card card, Monster cancelSummon, ArrayList<Card> destroySpellOrTrap, String cardName) {
        this.card = card;
        this.cancelSummon = cancelSummon;
        this.destroySpellOrTrap = destroySpellOrTrap;
        this.cardName = cardName;
    }

    public Card getCard() {
        return card;
    }
}
