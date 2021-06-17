package module.card;

import java.util.ArrayList;

public class Chain {
    private Card card;
    private Effect cancelEffect;
    private ArrayList<Card> destroySpellOrTrap;
    private String cardName;

    public Chain(Card card, ArrayList<Card> destroySpellOrTrap, String cardName) {
        this.card = card;
        this.destroySpellOrTrap = destroySpellOrTrap;
        this.cardName = cardName;
    }

    public Card getCard() {
        return card;
    }

    public ArrayList<Card> getDestroySpellOrTrap() {
        return destroySpellOrTrap;
    }

    public void setDestroySpellOrTrap(ArrayList<Card> destroySpellOrTrap) {
        this.destroySpellOrTrap = destroySpellOrTrap;
    }

    public void setCancelEffect(Effect cancelEffect) {
        this.cancelEffect = cancelEffect;
    }

    public Effect getCancelEffect() {
        return cancelEffect;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
