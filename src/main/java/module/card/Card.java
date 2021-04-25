package module.card;


public abstract class Card {
    protected String name;
    CardType cardType;
    private String description;
    private boolean isFaceUp;
    private int price;

    public Card getCardByName(String name) {

        return null;
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected CardType getCardType() {
        return cardType;
    }

    protected void setCardType(CardType cardType) {
        this.cardType = cardType;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public abstract void destroyWithoutLosingLifePoints();
}
