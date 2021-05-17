package module;

import module.card.Card;
import module.card.CardType;
import module.card.Monster;

import java.util.ArrayList;

public class Board {
    private final Card[] monsters;
    private final String[] showMonsters;
    private final Card[] spellsAndTraps;
    private final String[] showSpellsAndTraps;
    private final ArrayList<Card> graveyard;
    private final ArrayList<Integer> order;
    private User boardOwner;
    private Card fieldZone;
    private String showFieldZone;

    {
        this.monsters = new Card[5];
        this.showMonsters = new String[]{"E", "E", "E", "E", "E"};
        this.spellsAndTraps = new Card[5];
        this.showSpellsAndTraps = new String[]{"E", "E", "E", "E", "E"};
        this.showFieldZone = "E";
        this.graveyard = new ArrayList<>();
        this.order = new ArrayList<>();
        order.add(5);
        order.add(3);
        order.add(1);
        order.add(2);
        order.add(4);
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
        ((Monster) selectedMonsterCard).setIsATKPosition(true);
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

    public void changeFacePositionToAttackForSpells(int placeOnBoard) {
        showSpellsAndTraps[placeOnBoard - 1] = "O";
        monsters[placeOnBoard - 1].setATK(true);
        monsters[placeOnBoard - 1].setFaceUp(false);
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
        if (monsterOrSpell == 'M')
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
        for (int i = 0; i < order.size(); i++) {
            if (monsters[i] == null)
                return i + 1;
        }
        return 0;
    }

    public int getAddressToPutSpell() {
        for (int i = 0; i < order.size(); i++) {
            if (spellsAndTraps[i] == null)
                return i + 1;
        }
        return 0;
    }

    public Boolean isCardOnBoard(Card card) {
        for (int i = 0; i < spellsAndTraps.length; i++) {
            if (spellsAndTraps[i] == card || monsters[i] == card)
                return true;
        }
        return false;
    }

    public boolean isCardOnMonsterZone(Card card) {
        for (Card monster : monsters)
            if (monster == card)
                return true;
        return false;
    }

    public String showMonstersToString() {
        StringBuilder stringShowMonsters = new StringBuilder("    ");
        for (Integer integer : order) {
            if (showMonsters[integer - 1].length() == 2)
                stringShowMonsters.append(showMonsters[integer - 1]).append("   ");
            else
                stringShowMonsters.append(showMonsters[integer - 1]).append("    ");
        }
        return stringShowMonsters.toString();
    }

    public String showSpellsAndTrapsToString() {
        StringBuilder stringShowSpellsAndTraps = new StringBuilder("    ");
        for (Integer integer : order) stringShowSpellsAndTraps.append(showSpellsAndTraps[integer - 1]).append("    ");
        return stringShowSpellsAndTraps.toString();
    }

    public String showMonstersToStringReverse() {
        StringBuilder stringShowMonstersReverse = new StringBuilder("    ");
        for (int i = order.size() - 1; i >= 0; i--) {
            if (showMonsters[order.get(i) - 1].length() == 2)
                stringShowMonstersReverse.append(showMonsters[order.get(i) - 1]).append("   ");
            else
                stringShowMonstersReverse.append(showMonsters[order.get(i) - 1]).append("    ");
        }

        return stringShowMonstersReverse.toString();
    }

    public String showSpellsAndTrapsToStringReverse() {
        StringBuilder reverseString = new StringBuilder("    ");
        for (int i = order.size() - 1; i >= 0; i--)
            reverseString.append(showSpellsAndTraps[order.get(i) - 1]).append("    ");
        return reverseString.toString();
    }

    public boolean isThereAnyCardWithGivenTypeInMonsters(CardType cardType) {
        for (int i = 0; i < order.size(); i++)
            if (monsters[i].getCardType().getName().equals(cardType.getName()) || spellsAndTraps[i].getCardType().getName().equals(cardType.getName()))
                return true;
        return false;
    }

    public boolean isThereASubsetOfMonstersWithSumOfLevelsGreaterThanGivenLevel(int levelOfRitualEffectSpell) {
        int level = 0;
        for (Card card : monsters)
            if (((Monster) card).getLevel() < level)
                level = ((Monster) card).getLevel();
        int maximumLevelInHand = 0;
        for (Card card : boardOwner.getHand().getCardsInHand())
            if (card != null)
                maximumLevelInHand += ((Monster) card).getLevel();
        return maximumLevelInHand >= level;
    }

    public boolean areGivenCardsEnoughForRitualSummon(int[] cardAddresses, Card selectedCard) {
        int sumOfLevels = 0;
        for (Integer integer : cardAddresses)
            sumOfLevels += ((Monster) monsters[integer - 1]).getLevel();
        return sumOfLevels >= ((Monster) selectedCard).getLevel();
    }

    public int getAddressByCard(Card card) {
        if(card instanceof Monster){
            for (int i = 0; i < order.size(); i++)
                if (monsters[i] == card)
                    return i + 1;
        }
        else {
            for (int i = 0; i < order.size(); i++)
                if (spellsAndTraps[i] == card)
                    return i + 1;
        }
        return 0;
    }

}
