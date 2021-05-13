package module;

import module.card.Card;

import java.util.ArrayList;

public class Board {
    private User boardOwner;
    private Card[] monsters;
    private String[] showMonsters;
    private Card[] spellsAndTraps;
    private String[] showSpellsAndTraps;
    private Card fieldZone;
    private String showFieldZone;
    private ArrayList<Card> graveyard;
    private ArrayList<Integer> orderToSummon;

    {
        this.monsters = new Card[5];
        this.showMonsters = new String[]{"E", "E", "E", "E", "E"};
        this.spellsAndTraps = new Card[5];
        this.showSpellsAndTraps = new String[]{"E", "E", "E", "E", "E"};
        this.showFieldZone = "E";
        this.graveyard = new ArrayList<>();
        this.orderToSummon = new ArrayList<>();
        orderToSummon.add(5);
        orderToSummon.add(3);
        orderToSummon.add(1);
        orderToSummon.add(2);
        orderToSummon.add(4);
    }

    public Board(User boardOwner) {
        boardOwner.setBoard(this);
    }

    public Card[] getMonsters() {
        return monsters;
    }

    public Card[] getSpellsAndTraps() {
        return spellsAndTraps;
    }

    public Card getFieldZone() {
        return fieldZone;
    }

    public ArrayList<Card> getGraveyard() {
        return graveyard;
    }

    public String getShowFieldZone() {
        return showFieldZone;
    }

    public String[] getShowMonsters() {
        return showMonsters;
    }

    public String[] getShowSpellsAndTraps() {
        return showSpellsAndTraps;
    }

    public void addMonsterFaceUp(int placeInBoard, Card selectedMonsterCard) {
        monsters[placeInBoard - 1] = selectedMonsterCard;
        showMonsters[placeInBoard - 1] = "OO";
        selectedMonsterCard.setATK(true);
        selectedMonsterCard.setFaceUp(false);
    }

    public void addMonsterFaceDown(int placeInBoard, Card selectedMonsterCard) {
        monsters[placeInBoard - 1] = selectedMonsterCard;
        if (selectedMonsterCard.isFaceUp()) {
            showMonsters[placeInBoard - 1] = "DH";
            selectedMonsterCard.setFaceUp(true);
        } else {
            showMonsters[placeInBoard - 1] = "DO";
            selectedMonsterCard.setFaceUp(false);
        }
        selectedMonsterCard.setFaceUp(false);
    }

    public void addSpellAndTrap(int placeInBoard, Card selectedSpellAndTrapCard) {
        spellsAndTraps[placeInBoard - 1] = selectedSpellAndTrapCard;
        showSpellsAndTraps[placeInBoard - 1] = "H";
        selectedSpellAndTrapCard.setFaceUp(true);
        selectedSpellAndTrapCard.setATK(false);
    }

    public void changeFacePositionToAttackForMonsters(int placeInBoard) {
        showMonsters[placeInBoard - 1] = "OO";
        monsters[placeInBoard - 1].setATK(true);
        monsters[placeInBoard - 1].setFaceUp(false);
    }

    public void changeFacePositionToDefenceForMonsters(int placeInBoard) {
        showMonsters[placeInBoard - 1] = "DO";
        monsters[placeInBoard - 1].setATK(false);
        monsters[placeInBoard - 1].setFaceUp(false);
    }
    //todo implement the body of this
    public void changeFacePositionToAttackForSpells(int placeOnBoard){

    }

    public void putCardToFieldZone(Card card) {
        fieldZone = card;
        showFieldZone = "O";
    }

    public void removeMonster(int placeInBoard) {
        monsters[placeInBoard - 1] = null;
        showMonsters[placeInBoard - 1] = "E";
    }

    public void removeSpellAndTrap(int placeInBoard) {
        spellsAndTraps[placeInBoard - 1] = null;
        showSpellsAndTraps[placeInBoard - 1] = "E";
    }

    public void removeFieldZone() {
        fieldZone = null;
        showFieldZone = "E";
    }

    public void disableSpellAndTrap(int placeInBoard) {
        graveyard.add(spellsAndTraps[placeInBoard - 1]);
        spellsAndTraps[placeInBoard - 1] = null;
        showSpellsAndTraps[placeInBoard - 1] = "E";
    }

    // monsterOrSpell takes either 'S' as Spell or 'M' as Monster
    public Card getCard(int placeInBoard, char monsterOrSpell) {
        if (monsterOrSpell == 'm')
            return monsters[placeInBoard - 1];
        else
            return spellsAndTraps[placeInBoard - 1];
    }

    public Card selectOwnMonster(int placeInBoard) {
        return monsters[placeInBoard - 1];
    }

    public Card selectOpponentMonster(User opponent, int placeInBoard) {
        return opponent.getBoard().getMonsters()[placeInBoard - 1];
    }

    public Card selectOwnSpellAndTrap(int placeInBoard) {
        return spellsAndTraps[placeInBoard - 1];
    }

    public Card selectOpponentSpellAndTrap(User opponent, int placeInBoard) {
        return opponent.getBoard().getSpellsAndTraps()[placeInBoard - 1];
    }

    public Card selectOwnFieldZone() {
        return fieldZone;
    }

    public Card selectOpponentFieldZone(User opponent) {
        return opponent.getBoard().getFieldZone();
    }

    public int getAddressToSummon() {
        for (int i = 0; i < orderToSummon.size(); i++) {
            if (monsters[orderToSummon.get(i)] == null)
                return i;
        }
        return 0;
    }

    //TODO implement this like getAddressToSummon()
    public int getAddressToPutSpell() {
        return 0;
    }

    public Boolean isCardOnBoard(Card card) {
        for (int i = 0; i < spellsAndTraps.length; i++) {
            if (spellsAndTraps[i] == card || monsters[i] == card)
                return true;
        }
        return false;
    }

    //checks if the given card is in monster zone
    public boolean isCardOnMonsterZone(Card card) {
        return true;
    }


}
