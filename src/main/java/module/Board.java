package module;

public class Board {
    private String nicknameFirstPlayer;
    private String nicknameSecondPlayer;
    private int lifePointFirstPlayer;
    private int lifePointSecondPlayer;
    private Deck deckFirstPlayer;
    private Deck deckSecondPlayer;
    private int cardsInHandCountFirstPlayer;
    private int cardsInHandCountSecondPlayer;
    private int graveyardCardsCountFirstPlayer;
    private int graveyardCardsCountSecondPlayer;
    private int remainingCardsInDeckCountFirstPlayer;
    private int remainingCardsInDeckCountSecondPlayer;
    private String[] monsterCardsZoneFirstPlayer;
    private String[] monsterCardsZoneSecondPlayer;
    private String[] spellCardsZoneFirstPlayer;
    private String[] spellCardsZoneSecondPlayer;
    private char FZOccupationFirstPlayer;
    private char FZOccupationSecondPlayer;
    private int turn;


    {
        this.lifePointFirstPlayer = 8000;
        this.lifePointSecondPlayer = 8000;
        this.monsterCardsZoneFirstPlayer = new String[5];
        this.monsterCardsZoneSecondPlayer = new String[5];
        this.spellCardsZoneFirstPlayer = new String[5];
        this.spellCardsZoneSecondPlayer = new String[5];
        this.FZOccupationFirstPlayer = 'E';
        this.FZOccupationSecondPlayer = 'E';
    }

    public Board(String nicknameFirstPlayer, String nicknameSecondPlayer, Deck deckFirstPlayer, Deck deckSecondPlayer) {
        this.nicknameFirstPlayer = nicknameFirstPlayer;
        this.nicknameSecondPlayer = nicknameSecondPlayer;
        this.deckFirstPlayer = deckFirstPlayer;
        this.deckSecondPlayer = deckSecondPlayer;
    }


    public String getNicknameFirstPlayer() {
        return nicknameFirstPlayer;
    }

    public String getNicknameSecondPlayer() {
        return nicknameSecondPlayer;
    }

    public int getLifePointFirstPlayer() {
        return lifePointFirstPlayer;
    }

    public int getLifePointSecondPlayer() {
        return lifePointSecondPlayer;
    }

    public int getCardsInHandCountFirstPlayer() {
        return cardsInHandCountFirstPlayer;
    }

    public int getCardsInHandCountSecondPlayer() {
        return cardsInHandCountSecondPlayer;
    }

    public int getGraveyardCardsCountFirstPlayer() {
        return graveyardCardsCountFirstPlayer;
    }

    public int getGraveyardCardsCountSecondPlayer() {
        return graveyardCardsCountSecondPlayer;
    }

    public int getRemainingCardsInDeckCountFirstPlayer() {
        return remainingCardsInDeckCountFirstPlayer;
    }

    public int getRemainingCardsInDeckCountSecondPlayer() {
        return remainingCardsInDeckCountSecondPlayer;
    }

    public String[] getMonsterCardsZoneFirstPlayer() {
        return monsterCardsZoneFirstPlayer;
    }

    public String[] getMonsterCardsZoneSecondPlayer() {
        return monsterCardsZoneSecondPlayer;
    }

    public String[] getSpellCardsZoneFirstPlayer() {
        return spellCardsZoneFirstPlayer;
    }

    public String[] getSpellCardsZoneSecondPlayer() {
        return spellCardsZoneSecondPlayer;
    }

    public char getFZOccupationFirstPlayer() {
        return FZOccupationFirstPlayer;
    }

    public char getFZOccupationSecondPlayer() {
        return FZOccupationSecondPlayer;
    }

    public int getTurn() {
        return turn;
    }


    public void setTurn(int turn) {
        this.turn = turn;
    }


    void printBoard() {
        if (getTurn() == 1)
            printBoardFirstPLayer();
        else if (getTurn() == 2)
            printBoardSecondPlayer();
    }


    void changeTurn() {
        if (getTurn() == 1)
            setTurn(2);
        else if (getTurn() == 2)
            setTurn(1);
    }


    void printBoardFirstPLayer() {
        System.out.println(getNicknameSecondPlayer() + ":" + getLifePointSecondPlayer());
        for (int i = 1; i <= getCardsInHandCountSecondPlayer(); i++)
            System.out.print("    C");
        System.out.println();
        System.out.println(getRemainingCardsInDeckCountSecondPlayer());
        for (int i = getSpellCardsZoneSecondPlayer().length - 1; i >= 0; i--)
            System.out.print("    " + getSpellCardsZoneSecondPlayer()[i]);
        System.out.println();
        for (int i = getMonsterCardsZoneSecondPlayer().length - 1; i >= 0; i--)
            System.out.print("    " + getMonsterCardsZoneSecondPlayer()[i]);
        System.out.println();
        System.out.println(getGraveyardCardsCountSecondPlayer() + "                             " + getFZOccupationSecondPlayer());
        System.out.println("\n");
        System.out.println("--------------------------");
        System.out.println("\n");
        System.out.println(getGraveyardCardsCountFirstPlayer() + "                             " + getFZOccupationFirstPlayer());
        for (int i = 0; i < getMonsterCardsZoneFirstPlayer().length; i++)
            System.out.print("    " + getMonsterCardsZoneFirstPlayer()[i]);
        System.out.println();
        for (int i = 0; i < getSpellCardsZoneFirstPlayer().length; i++)
            System.out.print("    " + getSpellCardsZoneFirstPlayer()[i]);
        System.out.println();
        System.out.println("                        " + getRemainingCardsInDeckCountFirstPlayer());
        for (int i = 1; i <= getCardsInHandCountFirstPlayer(); i++)
            System.out.print("C   ");
        System.out.println();
        System.out.println(getNicknameFirstPlayer() + ":" + getLifePointFirstPlayer());
    }

    void printBoardSecondPlayer() {
        System.out.println(getNicknameFirstPlayer() + ":" + getLifePointFirstPlayer());
        for (int i = 1; i <= getCardsInHandCountFirstPlayer(); i++)
            System.out.print("    C");
        System.out.println();
        System.out.println(getRemainingCardsInDeckCountFirstPlayer());
        for (int i = getSpellCardsZoneFirstPlayer().length - 1; i >= 0; i--)
            System.out.print("    " + getSpellCardsZoneFirstPlayer()[i]);
        System.out.println();
        for (int i = getMonsterCardsZoneFirstPlayer().length - 1; i >= 0; i--)
            System.out.print("    " + getMonsterCardsZoneFirstPlayer()[i]);
        System.out.println();
        System.out.println(getGraveyardCardsCountFirstPlayer() + "                             " + getFZOccupationFirstPlayer());
        System.out.println("\n");
        System.out.println("--------------------------");
        System.out.println("\n");
        System.out.println(getGraveyardCardsCountSecondPlayer() + "                             " + getFZOccupationSecondPlayer());
        for (int i = 0; i < getMonsterCardsZoneSecondPlayer().length; i++)
            System.out.print("    " + getMonsterCardsZoneSecondPlayer()[i]);
        System.out.println();
        for (int i = 0; i < getSpellCardsZoneSecondPlayer().length; i++)
            System.out.print("    " + getSpellCardsZoneSecondPlayer()[i]);
        System.out.println();
        System.out.println("                        " + getRemainingCardsInDeckCountSecondPlayer());
        for (int i = 1; i <= getCardsInHandCountSecondPlayer(); i++)
            System.out.print("C   ");
        System.out.println();
        System.out.println(getNicknameSecondPlayer() + ":" + getLifePointSecondPlayer());
    }
}
