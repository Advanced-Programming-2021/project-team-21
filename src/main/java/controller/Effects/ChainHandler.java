package controller.Effects;

import controller.ProgramController;
import module.Duel;
import module.Hand;
import module.User;
import module.card.*;
import view.PrintResponses;

import java.util.ArrayList;

public class ChainHandler {
    public static Duel currentDuel;

    public static boolean run(Duel duel, String choose, User user, User rival
            , Chain card, WhereToChain where) {
        ArrayList<Chain> chainCards = getChain(duel, choose, user, rival, card, where);
        for (int i = chainCards.size() - 1; i >= 0; i--) {
            Chain chain = chainCards.get(i);
            if (chain.getCard() instanceof Spell) {
                Spell spell = (Spell) chain.getCard();
                checkForSpellNegate(spell, chainCards, i);
                SpellActivation.run(spell, getUserNow(user, rival, i, card),
                        getRival(user, rival, i, card), duel,
                        getUserNow(user, rival, i, card).getBoard().getAddressByCard(spell),
                        false, null, chain);
            }
        }
        return false;
    }

    private static void checkForSpellNegate(Spell spell, ArrayList<Chain> chainCards, int i) {
        if (spell.getNegateTrap().hasEffect()) {
            Trap trap = (Trap) chainCards.get(i - 1).getCard();
            Effect effect = trap.getCanAttackLP();
            effect.resetEffect();
            trap.setCanAttackLP(effect);
            Effect effect1 = trap.getCanNegateWholeAttack();
            effect1.resetEffect();
            trap.setCanNegateWholeAttack(effect1);
            chainCards.get(i - 1).setCard(trap);
        }
    }

    private static User getRival(User user, User rival, int i, Chain card) {
        if (getUserNow(user, rival, i, card) == user) return rival;
        else return user;
    }

    private static User getUserNow(User user, User rival, int i, Chain card) {
        if (card.getCard() instanceof Spell || card.getCard() instanceof Trap) i++;
        if (i % 2 == 1) return rival;
        else return user;
    }

    public static String getChainCommand(User user) {
        PrintResponses.printAskToChain(user, currentDuel);
        String input = ProgramController.scanner.nextLine();
        while (true) {
            if (input.equals("no")) return input;
            else if (input.equals("yes")) return ProgramController.scanner.nextLine();
            PrintResponses.printInvalidFormat();
        }
    }

    public static ArrayList<module.card.Chain> getChain(Duel duel, String choose, User user, User rival
            , Chain firstCard, WhereToChain where) {
        currentDuel = duel;
        int chainCount = 0;
        int speed = 2;
        ArrayList<module.card.Chain> chainCards = new ArrayList<>();
        if (firstCard.getCard() instanceof Spell || firstCard.getCard() instanceof Trap) {
            chainCards.add(firstCard);
        }
        while (!choose.equals("no")) {
            chainCount++;
            Card spellOrTrap;
            User userNow = user;
            User otherUser = rival;
            if (chainCount % 2 == 1) {
                userNow = rival;
                otherUser = user;
            }
            choose = getNumber(choose, userNow);
            spellOrTrap = getSpellOrTrap(choose, userNow);
            while (spellOrTrap == null || checkSpell(speed, spellOrTrap)
                    || !checkIsRightPlace(chainCards, where, spellOrTrap, firstCard.getCard(), userNow)) {
                PrintResponses.printWrongSpell();
                choose = getNumber(choose, userNow);
                spellOrTrap = getSpellOrTrap(choose, userNow);
            }
            if ((spellOrTrap instanceof Trap) && ((Trap) spellOrTrap).getSpellTrapIcon().getName().equals("Counter"))
                speed = 3;
            PrintResponses.printChainComplete(chainCount);
            module.card.Chain chain = getOtherInputs(spellOrTrap, userNow, otherUser, firstCard);
            chainCards.add(chain);
            choose = getChainCommand(otherUser);
        }
        return chainCards;
    }

    private static Chain getOtherInputs(Card spellOrTrap, User userNow, User rival, Chain firstCard) {
        getDiscardCards(spellOrTrap, userNow);
        return new Chain(spellOrTrap,
                getDestroyedCards(rival, spellOrTrap, firstCard.getCard()), getCardName(spellOrTrap));
    }

    private static String getCardName(Card spellOrTrap) {
        if (spellOrTrap instanceof Trap && ((Trap) spellOrTrap).getCanDestroyFromDeckAndHand().hasEffect()) {
            PrintResponses.printAskTpGetCardName();
            Card card;
            while ((card = Card.getCardByName(ProgramController.scanner.nextLine())) == null) {
                PrintResponses.printWrongCardName();
            }
            return card.getName();
        }
        return null;
    }


    private static ArrayList<Card> getDestroyedCards(User rival, Card spellOrTrap, Card spellBefore) {
        if (spellOrTrap instanceof Spell) {
            Spell spell = (Spell) spellOrTrap;
            if (spell.getCanDestroyOpponentSpellAndTrap().hasEffect()) {
                return destroySpellAndTraps(spell.getCanDestroyOpponentSpellAndTrap().getEffectNumber(), rival);
            }
        } else if (spellOrTrap instanceof Trap) {
            Trap trap = (Trap) spellOrTrap;
            if (trap.getNegateSpellActivation().hasEffect()) {
                ArrayList<Card> cards = new ArrayList<>();
                cards.add(spellBefore);
                return cards;
            }
        }
        return null;
    }

    private static ArrayList<Card> destroySpellAndTraps(int effectNumber, User rival) {
        ArrayList<Card> spells = new ArrayList<>();
        Card[] cards = rival.getBoard().getSpellsAndTraps();
        int place = -1;
        for (int i = 0; i < effectNumber; i++) {
            while (place == -1) {
                place = SpellActivation.getPlace(cards);
            }
            spells.add(cards[place]);
            place = -1;
        }
        return spells;
    }

    public static void getDiscardCards(Card spellOrTrap, User userNow) {
        if (spellOrTrap instanceof Spell) {
            Spell spell = (Spell) spellOrTrap;
            if (spell.getDiscardACardToActivate().hasEffect()) {
                discard(userNow, spell.getDiscardACardToActivate().getEffectNumber());
            }
        } else if (spellOrTrap instanceof Trap) {
            Trap trap = (Trap) spellOrTrap;
            if (trap.getDiscardACard().hasEffect()) {
                discard(userNow, trap.getDiscardACard().getEffectNumber());
            }
        }
    }

    private static void discard(User userNow, int effectNumber) {
        for (int i = 0; i < effectNumber; i++) {
            Hand hand = userNow.getHand();
            PrintResponses.printAskToDiscard();
            int place;
            while (true) {
                try {
                    place = Integer.parseInt(ProgramController.scanner.nextLine());
                    if (place < 5 && place >= 0 && hand.getCardsInHand()[place] != null)
                        break;
                } catch (Exception e) {
                    PrintResponses.printWrongSpellFormat();
                }
            }
            Card card = hand.getCardsInHand()[place];
            currentDuel.addCardToGraveyard(card, 10, userNow);
            hand.removeCardFromHand(place);
        }
    }


    private static boolean checkIsRightPlace(ArrayList<module.card.Chain> chainCards, WhereToChain where,
                                             Card spellOrTrap, Card card, User userNow) {
        if (chainCards.size() != 0) card = chainCards.get(chainCards.size() - 1).getCard();
        if (spellOrTrap instanceof Spell) {
            Spell spell = (Spell) spellOrTrap;
            if (spell.getDiscardACardToActivate().hasEffect() &&
                    userNow.getHand().getCardsInHand().length > spell.getDiscardACardToActivate().getEffectNumber())
                return true;
            return spell.getNegateTrap().hasEffect() &&
                    where.getPlace().equals("effect") &&
                    (card instanceof Trap &&
                            (((Trap) card).getCanAttackLP().hasEffect()));
        } else if (spellOrTrap instanceof Trap) {
            Trap trap = (Trap) spellOrTrap;
            if (trap.getCostLP().hasEffect() && userNow.getLifePoints() > trap.getCostLP().getEffectNumber())
                return true;
            if ((trap.getCanNegateWholeAttack().hasEffect() || trap.getDestroyAttackMonsters().hasEffect())
                    && where.getPlace().equals("attack")) return true;
            else if ((trap.getCanDestroyAll().hasEffect() ||
                    (trap.getCanDestroyMonsterSummonWithATK().hasEffect() && card instanceof Monster
                            && ((Monster) card).getAtk() >= trap.getCanDestroyMonsterSummonWithATK().getEffectNumber()) ||
                    trap.getNegateASummon().hasEffect())
                    && where.getPlace().equals("summon")) return true;
            else return trap.getNegateSpellActivation().hasEffect() && where.getPlace().equals("effect");
        }
        return false;
    }

    private static boolean checkSpell(int speed, Card spellOrTrap) {
        Spell spell = null;
        Trap trap = null;
        if (spellOrTrap instanceof Spell) spell = (Spell) spellOrTrap;
        else trap = (Trap) spellOrTrap;
        if (speed == 3 && trap != null && !trap.getSpellTrapIcon().getName().equals("Counter")) return true;
        return spell != null && !spell.getSpellTrapIcon().getName().equals("Quick play");
    }

    private static String getNumber(String which, User user) {
        while (!which.matches("\\d+")) {
            PrintResponses.printWrongSpellFormat();
            which = getChainCommand(user);
        }
        return which;
    }

    private static Card getSpellOrTrap(String which, User userNow) {
        int place = Integer.parseInt(which);
        return userNow.getBoard().getCard(place, 'S');
    }
}
