package controller.Effects;

import controller.ProgramController;
import controller.menu.DuelMenu;
import module.Board;
import module.Duel;
import module.User;
import module.card.*;
import module.card.effects.Effect;
import view.PrintResponses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SpellActivation {
    public static String changedHeartName;
    public static int oldPlace = -1;
    public static int newPlaceHolder = -1;
    public static int changeSpellPlace;

    public static boolean run(Spell spell, User userNow, User rival, Duel duel, int place, boolean fieldAndEquipSpellAdd, Monster equipped, Chain chain) {
        ArrayList<Card> deckCards = userNow.getHand().getDeckToDraw().getMainDeckCards();
        if (spell.getCanSummonFromGY().hasEffect()) {
            handleSummonFromGY(spell, userNow, rival, duel, place);
        }
        if (spell.getDiscardACardToActivate().hasEffect()) {
            ChainHandler.getDiscardCards(spell, userNow);
        }
        if (spell.getCanAddFieldSpellFromDeck().hasEffect()) {
            handleAddFieldSpell(spell, userNow, duel, place, deckCards);
        }
        if (spell.getCanAddFromDeckToHand().hasEffect()) {
            handleDraw(spell, userNow, deckCards, duel, place);
        }
        if (spell.getCanDestroyOpponentMonster().hasEffect()) {
            destroyMonsters(spell, rival, duel, place);
        }
        if (spell.getCanDestroyMyMonster().hasEffect()) {
            destroyMonsters(spell, userNow, duel, place);
        }
        if (spell.getCanControlOpponentMonster().hasEffect()) {
            handleControl(spell, userNow, rival, duel, place);
        }
        if (spell.getCanChangeFaceOFOpponent().hasEffect()) {
            handleFaceChange(spell, userNow, rival, duel);
        }
        if (spell.getCanMakeMonstersUndefeatable().hasEffect()) {
            handleMakeUndefeatable(rival, duel);
        }
        if (spell.getMonstersCanNotAttack().hasEffect()) {
            handleMonsterNotAttack(rival, spell);
        }
        if (spell.getFieldSpellType1().hasEffect()) {
            fieldSpell(spell, userNow, fieldAndEquipSpellAdd);
        }
        if (spell.getFieldATKIncreaseGY1().hasEffect()) {
            secondFieldSpell(spell, userNow, fieldAndEquipSpellAdd);
        }
        if (spell.getEquipCardNormal1().hasEffect()) {
            equipSpell(equipped, spell, fieldAndEquipSpellAdd);
        }
        if (spell.getEquipBasedMyUpMonsters().hasEffect()) {
            secondEquipSpell(equipped, spell, fieldAndEquipSpellAdd, userNow);
        }
        if (spell.getEquipBasedOnPosition().hasEffect()) {
            thirdEquipSpell(equipped, spell, fieldAndEquipSpellAdd);
        }
        if (spell.getCanDestroyOpponentSpellAndTrap().hasEffect()) {
            handleDestroySpell(spell, rival, duel, userNow, place, chain);
            return true;
        }
        checkSpells(userNow, duel);
        checkSpells(rival, duel);
        return false;
    }

    private static void thirdEquipSpell(Monster equipped, Spell spell, boolean fieldAndEquipSpellAdd) {
        Effect effect = spell.getEquipBasedMyUpMonsters();
        if (equipped.getMonsterType().getName().equals(spell.getEquipBasedOnPosition().getType())) {
            if (equipped.isATK()) effect.setAttack(equipped.getDef());
            else effect.setDefense(equipped.getAtk());
        }
        setChanges(fieldAndEquipSpellAdd, effect, equipped);
    }

    private static void secondEquipSpell(Monster equipped, Spell spell, boolean fieldAndEquipSpellAdd, User userNow) {
        Effect effect = spell.getEquipBasedMyUpMonsters();
        effect.setAttack(effect.getAttack() * userNow.getBoard().getMonsters().length);
        effect.setDefense(effect.getDefense() * userNow.getBoard().getMonsters().length);
        setChanges(fieldAndEquipSpellAdd, effect, equipped);
    }

    private static void equipSpell(Monster equipped, Spell spell, boolean equipSpellAdd) {
        Effect effect = spell.getEquipCardNormal2();
        int type = 1;
        HashMap<Integer, Effect> checkNext = new HashMap<>();
        checkNext.put(1, spell.getEquipCardNormal1());
        checkNext.put(2, spell.getEquipCardNormal2());
        checkNext.put(3, spell.getEquipCardNormal3());
        while (effect.hasEffect()) {
            if (effect.getType().equals("") || equipped.getMonsterType().getName().equals(effect.getType())) {
                setChanges(equipSpellAdd, effect, equipped);
            }
            type++;
            effect = checkNext.get(type);
        }
    }

    private static void secondFieldSpell(Spell spell, User userNow, boolean fieldSpellAdd) {
        Effect effect = spell.getFieldATKIncreaseGY1();
        while (effect.hasEffect()) {
            ArrayList<Monster> monstersWithType = getMonsters(userNow, effect);
            for (Monster monster : monstersWithType) {
                effect.setAttack(effect.getAttack() * userNow.getGraveyard().size());
                effect.setDefense(effect.getDefense() * userNow.getGraveyard().size());
                setChanges(fieldSpellAdd, effect, monster);
            }
            effect = spell.getFieldATKIncreaseGY2();
        }
    }

    private static void fieldSpell(Spell spell, User userNow, boolean filedSpellAdd) {
        Effect effect = spell.getFieldSpellType1();
        int type = 1;
        HashMap<Integer, Effect> checkNext = new HashMap<>();
        checkNext.put(1, spell.getFieldSpellType1());
        checkNext.put(2, spell.getFieldSpellType2());
        checkNext.put(3, spell.getFieldSpellType3());
        checkNext.put(4, spell.getFieldSpellType4());
        checkNext.put(5, spell.getFieldSpellType5());
        while (effect.hasEffect()) {
            ArrayList<Monster> monstersWithType = getMonsters(userNow, effect);
            for (Monster monster : monstersWithType) {
                setChanges(filedSpellAdd, effect, monster);
            }
            type++;
            effect = checkNext.get(type);
        }
    }

    private static void setChanges(boolean filedSpellAdd, Effect effect, Monster monster) {
        if (filedSpellAdd) monster.setAtk(monster.getAtk() + effect.getAttack());
        else monster.setAtk(monster.getAtk() - effect.getAttack());
        if (filedSpellAdd) monster.setDef(monster.getDef() + effect.getDefense());
        else monster.setDef(monster.getDef() - effect.getDefense());
    }

    private static ArrayList<Monster> getMonsters(User userNow, Effect effect) {
        ArrayList<Monster> monstersWithType = new ArrayList<>();
        for (Card monster : userNow.getBoard().getMonsters()) {
            Monster monsterType = (Monster) monster;
            if (monster != null && monsterType.getMonsterType().getName().equals(effect.getType()))
                monstersWithType.add(monsterType);
        }
        monstersWithType.addAll(userNow.getHand().getCardsWithType(7, effect.getType()));
        return monstersWithType;
    }

    private static void handleMonsterNotAttack(User rival, Spell spell) {
        for (Card monsters : rival.getBoard().getMonsters()) {
            Monster monster = (Monster) monsters;
            if (monster.getAtk() >= spell.getMonstersCanNotAttack().getEffectNumber()) {
                monster.setCanAttack(false);
            }
        }
    }

    private static void checkSpells(User userNow, Duel duel) {
        for (Card spellsAndTrap : userNow.getBoard().getSpellsAndTraps()) {
            if (spellsAndTrap instanceof Spell) {
                Spell spell = (Spell) spellsAndTrap;
                if (spell.getGetLPForEverySpellActivation().hasEffect()) {
                    duel.changeLP(userNow, spell.getGetLPForEverySpellActivation().getEffectNumber());
                }
            }
        }
    }

    private static void handleMakeUndefeatable(User rival, Duel duel) {
        rival.setCanAttack(false);
    }

    private static void handleFaceChange(Spell spell, User userNow, User rival, Duel duel) {
        Board board = rival.getBoard();
        for (Card monster : board.getMonsters()) {
            if (monster != null && !monster.isFaceUp()) duel.flipSetForMonsters(board.getAddressByCard(monster));
        }
    }

    private static void handleDestroySpell(Spell spell, User rival, Duel duel, User userNow, int place, Chain chain) {
        int number = spell.getCanDestroyOpponentSpellAndTrap().getEffectNumber();
        Card[] spellsAndTraps = rival.getBoard().getSpellsAndTraps();
        if (spellsAndTraps.length <= number) {
            for (Card card : spellsAndTraps) {
                destroySpell(card, rival, duel);
            }
        } else {
            while (number > 0) {
                int selected;
                Card card;
                if (chain == null) {
                    selected = getPlace(spellsAndTraps);
                    if (selected == -1) continue;
                    card = spellsAndTraps[selected];
                } else {
                    ArrayList<Card> cards = chain.getDestroySpellOrTrap();
                    card = cards.get(0);
                    cards.remove(cards.get(0));
                    chain.setDestroySpellOrTrap(cards);
                }
                duel.addCardToGraveyard(card, rival.getBoard().getAddressByCard(card), rival);
                spellsAndTraps[rival.getBoard().getAddressByCard(card)] = null;
                number--;
            }
        }
        duel.addCardToGraveyard(spell, place, userNow);

    }

    public static int getPlace(Card[] spellsAndTraps) {
        PrintResponses.printSpellsToDestroy(Arrays.asList(spellsAndTraps));
        int selected = Integer.parseInt(ProgramController.scanner.nextLine());
        if (selected < 0 || selected > spellsAndTraps.length) {
            PrintResponses.printNoSpellFound();
            return -1;
        }
        return selected;
    }

    private static void destroySpell(Card card, User userNow, Duel duel) {
        if (card == null)
            return;
        duel.addCardToGraveyard(card, userNow.getBoard().getAddressByCard(card), userNow);
    }

    private static void handleControl(Spell spell, User userNow, User rival, Duel duel, int place) {
        PrintResponses.printChangeOfHeart();
        if (rival.getBoard().getMonsters().length == 0) {
            PrintResponses.printCanNotControl();
            duel.addCardToGraveyard(spell, place, userNow);
            return;
        }
        boolean isChosen = false;
        int monsterPlace = 0;
        while (!isChosen) {
            monsterPlace = Integer.parseInt(ProgramController.scanner.nextLine());
            if (monsterPlace > 5 || monsterPlace < 1 || userNow.getBoard().getCard(monsterPlace, 'M') == null) {
                PrintResponses.printWrongControl();
            } else isChosen = true;
        }
        Monster monster = (Monster) userNow.getBoard().getCard(monsterPlace, 'M');
        rival.getBoard().removeMonster(monsterPlace);
        int newPlace = userNow.getBoard().getAddressToSummon();
        monster.setAtk(userNow.getIncreaseATK() + monster.getAtk());
        monster.setDef(userNow.getIncreaseDEF() + monster.getDef());
        if (userNow.isHasSummonedAlteringATK()) {
            Monster altering = (Monster) userNow.getBoard().getCard(userNow.getAlteringATKPlace(), 'm');
            altering.setAtk(monster.getAtk() + 300 * monster.getLevel());
        }
        userNow.getBoard().addMonsterFaceUp(newPlace, monster);
        changedHeartName = monster.getName();
        oldPlace = monsterPlace;
        newPlaceHolder = newPlace;
        changeSpellPlace = place;
    }

    private static void handleDraw(Spell spell, User userNow, ArrayList<Card> deckCards, Duel duel, int place) {
        int number = spell.getCanAddFromDeckToHand().getEffectNumber();
        for (int i = 0; i < number; i++) {
            Card card = deckCards.get(0);
            userNow.getHand().addCardToHand(card);
            userNow.getHand().getDeckToDraw().removeCardFromMainDeck(card);
        }
        duel.addCardToGraveyard(spell, place, userNow);
    }

    private static void handleAddFieldSpell(Spell spell, User userNow, Duel duel, int place, ArrayList<Card> deckCards) {
        int number = spell.getCanAddFieldSpellFromDeck().getEffectNumber();
        for (Card card : deckCards) {
            if (!(card instanceof Spell)) continue;
            Spell field = (Spell) card;
            if (field.isFieldZone()) {
                userNow.getHand().addCardToHand(field);
                userNow.getHand().removeFromDeck(field.getName());
            }
            number--;
            if (number == 0) break;
        }
        duel.addCardToGraveyard(spell, place, userNow);
    }

    private static void handleSummonFromGY(Spell spell, User userNow, User rival, Duel duel, int place) {
        ArrayList<Monster> cards = new ArrayList<>();
        for (Card card : userNow.getGraveyard()) {
            if (card instanceof Monster) cards.add((Monster) card);
        }
        for (Card card : rival.getGraveyard()) {
            if (card instanceof Monster) cards.add((Monster) card);
        }
        DuelMenu.specialSummonsedCards = cards;
        DuelMenu.isGetFroOpponentGY = true;
        PrintResponses.printSpecialSummonCards(cards);
        duel.addCardToGraveyard(spell, place, userNow);
    }

    private static void destroyMonsters(Spell spell, User userNow, Duel duel, int place) {
        Card[] myCards = userNow.getBoard().getMonsters();
        for (Card rivalCard : myCards) {
            if (!(rivalCard instanceof Monster)) continue;
            Monster monster = (Monster) rivalCard;
            duel.addCardToGraveyard(monster, userNow.getBoard().getAddressByCard(monster), userNow);
        }
        if (!duel.getUserWhoPlaysNow().equals(userNow))
            duel.addCardToGraveyard(spell, place, duel.getUserWhoPlaysNow());
        else
            duel.addCardToGraveyard(spell, place, duel.getRival());
    }
}
