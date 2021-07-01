package module.card;

import java.util.ArrayList;

public class Chain {
    private final String cardName;
    private Card card;
    private ArrayList<Card> destroySpellOrTrap;

    public Chain(Card card, ArrayList<Card> destroySpellOrTrap, String cardName) {
        this.card = card;
        this.destroySpellOrTrap = destroySpellOrTrap;
        this.cardName = cardName;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public ArrayList<Card> getDestroySpellOrTrap() {
        return destroySpellOrTrap;
    }

    public void setDestroySpellOrTrap(ArrayList<Card> destroySpellOrTrap) {
        this.destroySpellOrTrap = destroySpellOrTrap;
    }

    public String getCardName() {
        return cardName;
    }
}
