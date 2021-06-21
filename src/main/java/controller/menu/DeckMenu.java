package controller.menu;

import controller.DataController;
import controller.ProgramController;
import module.Deck;
import module.User;
import module.card.Card;
import view.PrintResponses;
import view.Regex;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class DeckMenu implements Menuable {
    User user = ProgramController.userInGame;

    @Override
    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getMatcher(command, Regex.deckCreate)).find()) {
            createDeck(matcher);
        } else if ((matcher = Regex.getMatcher(command, Regex.deckDelete)).find()) {
            deleteDeck(matcher);
        } else if (Regex.getMatcher(command, Regex.menuShow).find()) {
            showCurrentMenu();
        } else if ((matcher = Regex.getMatcher(command, Regex.ActiveDeck)).find()) {
            activateDeck(matcher);
        } else if ((matcher = Regex.getMatcher(command, Regex.addCardSide)).find() ||
                (matcher = Regex.getMatcher(command, Regex.addCardMain)).find()) {
            addCard(matcher);
        } else if ((matcher = Regex.getMatcher(command, Regex.removeCardMain)).find() ||
                (matcher = Regex.getMatcher(command, Regex.removeCardSide)).find()) {
            removeCard(matcher);
        } else if (Regex.getMatcher(command, Regex.menuExit).find()) {
            exitMenu();
        } else if (Regex.getMatcher(command, Regex.showAllDeck).find()) {
            showAllDeck();
        } else if ((matcher = Regex.getMatcher(command, Regex.showDeckSide)).find() ||
                (matcher = Regex.getMatcher(command, Regex.showDeckMain)).find()) {
            showADeck(matcher);
        } else if ((matcher = Regex.getMatcher(command, Regex.showACard)).find()) {
            String cardName = matcher.group("cardName").trim();
            PrintResponses.printACard(Card.getCardByName(cardName));
        } else if (Regex.getMatcher(command, Regex.deckShowCard).find()) {
            showAllCards();
        } else PrintResponses.printInvalidFormat();
    }

    private void showAllCards() {
        ArrayList<Card> allCards = user.getCards();
        Card.sort(allCards);
        for (Card card : allCards) {
            PrintResponses.printAllCard(card);
        }
    }

    private void showADeck(Matcher matcher) {
        String deckName = matcher.group("deckName");
        Deck deck = user.getDeckByName(deckName);
        if (deck == null) {
            PrintResponses.printDeckNotExist(deckName);
            return;
        }
        String type;
        try {
            type = matcher.group("side");
        } catch (Exception e) {
            type = "main";
        }
        PrintResponses.printADeck(deck, type);
    }

    private void showAllDeck() {
        PrintResponses.printActive();
        ArrayList<Deck> show = Deck.deckSort(user.getDecks());
        int index = 0;
        if (show.size() > 0 && show.get(index).isActive()) {
            PrintResponses.printDeckShow(show.get(index));
            index++;
        }
        PrintResponses.printOther();
        for (int i = index; i < show.size(); i++) {
            PrintResponses.printDeckShow(show.get(i));
        }
    }

    private void removeCard(Matcher matcher) {
        String cardName = matcher.group("cardName").trim(), deckName = matcher.group("deckName");
        Deck deck = user.getDeckByName(deckName);
        if (deck == null) {
            PrintResponses.printDeckNotExist(deckName);
            return;
        }
        Card card;
        try {
            String side = matcher.group("side");
            card = deck.checkCardInDeck(cardName, side);
            if (card == null) {
                PrintResponses.printNoCardTodDelete(cardName, side);
                return;
            }
            deck.removeCardFromSideDeck(card);
        } catch (Exception e) {
            String type = "main";
            card = deck.checkCardInDeck(cardName, type);
            if (card == null) {
                PrintResponses.printNoCardTodDelete(cardName, type);
                return;
            }
            deck.removeCardFromMainDeck(card);
        }
        PrintResponses.printSuccessfulCardRemoval();
    }

    private void addCard(Matcher matcher) {
        String cardName = matcher.group("cardName").trim(), deckName = matcher.group("deckName");
        Card card = Card.getCardByName(cardName);
        Deck deck = user.getDeckByName(deckName);
        if (card == null) {
            PrintResponses.printCardNotExist(cardName);
            return;
        }
        if (deck == null) {
            PrintResponses.printDeckNotExist(deckName);
            return;
        }
        try {
            String side = matcher.group("side");
            if (deck.getNumberOfSideDeckCards() == 15) {
                PrintResponses.printDeckFull(side);
                return;
            }
            if (invalidAdd(cardName, deckName, deck)) return;
            deck.addCardToSideDeck(card);
        } catch (Exception e) {
            String type = "main";
            if (deck.getNumberOfMainDeckCards() == 60) {
                PrintResponses.printDeckFull(type);
                return;
            }
            if (invalidAdd(cardName, deckName, deck)) return;
            deck.addCardToMainDeck(card);
        }
        PrintResponses.printSuccessfulCardAddition();
    }

    private boolean invalidAdd(String cardName, String deckName, Deck deck) {
        if (deck.getCardNumber(cardName) >= 3) {
            PrintResponses.printInvalidAdd(cardName, deckName);
            return true;
        }
        return false;
    }

    private void activateDeck(Matcher matcher) {
        String name = matcher.group("deckName");
        Deck deck = getDeck(name);
        if (deck == null) return;
        deck.setActive(true);
        user.deactivateDecks(name);
        PrintResponses.printSuccessfulDeckActivation();
    }

    private Deck getDeck(String name) {
        Deck deck = user.getDeckByName(name);
        if (deck == null) {
            PrintResponses.printDeckNotExist(name);
            return null;
        }
        return deck;
    }

    private void deleteDeck(Matcher matcher) {
        String name = matcher.group("deckName");
        Deck deck = getDeck(name);
        if (deck == null) return;
        user.removeDeck(deck);
        PrintResponses.printSuccessfulDeckDeletion();
    }

    private void createDeck(Matcher matcher) {
        String name = matcher.group("deckName");
        if (user.getDeckByName(name) != null) {
            PrintResponses.printDeckExist(name);
            return;
        }
        Deck deck = new Deck(name);
        user.addDeck(deck);
        PrintResponses.printSuccessfulDeckCreation();
    }

    @Override
    public void exitMenu() {
        ProgramController.currentMenu = new MainMenu();
        DataController.saveData(user);
    }

    @Override
    public void showCurrentMenu() {
        PrintResponses.printDeckMenuShow();
    }
}
