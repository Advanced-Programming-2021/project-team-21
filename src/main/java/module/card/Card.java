package module.card;

import java.io.File;

public abstract class Card {
    protected String name;
    private String description;
    private String cardType;
    private boolean isFaceUp;
    private int price;

    public Card getCardByName(String name) {
        File file;
        if (new File(name + ".monster.json").exists()) {
            return new Monster(name);
        } else if (new File(name + ".spell.json").exists()) {
            return new Spell(name);
        } else if (new File(name + ".trap.json").exists()) {
            return new Trap(name);
        }
        return null;
    }

    protected String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setCardType(String cardType) {
        this.cardType = cardType;
    }

    protected String getCardType() {
        return cardType;
    }

    protected boolean isFaceUp() {
        return isFaceUp;
    }

    protected void setFaceUp(boolean faceUp) {
        isFaceUp = faceUp;
    }

    protected int getPrice() {
        return price;
    }

    protected void setPrice(int price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public abstract void destroyWithoutLosingLifePoints();
}
