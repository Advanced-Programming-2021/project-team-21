package controller.menu;

import controller.ProgramController;
import module.Duel;
import module.User;
import module.card.Card;
import module.card.Monster;
import view.PrintResponses;
import view.Regex;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.regex.Matcher;

public class DuelMenu implements Menuable {
    private Duel currentDuel;
    private Phases phase;

    @Override
    public void run(String command) {
        HashMap<String, Consumer<Matcher>> commandMap = createCommandMap();
        for (String string : commandMap.keySet()) {
            Matcher matcher = Regex.getMatcher(command, string);
            if (matcher.find())
                commandMap.get(string).accept(matcher);
        }

    }

    @Override
    public void showCurrentMenu() {
        PrintResponses.printDuelMenuShow();
    }

    @Override
    public void exitMenu() {
        ProgramController.currentMenu = new MainMenu();
    }

    protected HashMap<String, Consumer<Matcher>> createCommandMap() {
        HashMap<String, Consumer<Matcher>> commandMap = new HashMap<>();
        commandMap.put(Regex.createNewDuel, this::createNewDuel);
        commandMap.put(Regex.createNewDuelWithAI, this::createNewDuelWithAI);
        commandMap.put(Regex.selectFromOwn, this::selectCardFromOwn);
        commandMap.put(Regex.selectFromOpponent, this::selectCardFromOpponent);
        commandMap.put(Regex.deselectCard, this::deselectCard);
        commandMap.put(Regex.nextPhase, this::goToNextPhase);
        commandMap.put(Regex.summon, this::summon);
        commandMap.put(Regex.set, this::set);
        commandMap.put(Regex.setPosition, this::setPosition);
        return commandMap;
    }

    private void createNewDuel(Matcher matcher) {
        User secondPlayer = User.getUserByUsername(matcher.group("player2Username"));
        int rounds = Integer.parseInt(matcher.group("rounds"));
        if (secondPlayer == null) {
            PrintResponses.printNoUserExistToPlayWith();
        } else if (ProgramController.userInGame.getActiveDeck() == null) {
            PrintResponses.printHasNoActiveDeck(ProgramController.userInGame);
        } else if (secondPlayer.getActiveDeck() == null) {
            PrintResponses.printHasNoActiveDeck(secondPlayer);
        } else if (!ProgramController.userInGame.getActiveDeck().isValid()) {
            PrintResponses.printInvalidDeck(ProgramController.userInGame);
        } else if (!secondPlayer.getActiveDeck().isValid()) {
            PrintResponses.printInvalidDeck(secondPlayer);
        } else if (!(rounds == 1 || rounds == 3)) {
            PrintResponses.printNonSupportiveRound();
        } else {
            currentDuel = new Duel(ProgramController.userInGame, secondPlayer);
            phase = Phases.DRAW_PHASE;
            Card card = currentDuel.drawACard();
            if (card == null) {
                endTheGame();
            } else {
                PrintResponses.printDrawnCard(card);
            }
        }
    }

    private void createNewDuelWithAI(Matcher matcher) {

    }

    private void selectCardFromOwn(Matcher matcher) {
        String whereToSelectFrom = matcher.group("where");
        int cardAddress = Integer.parseInt(matcher.group("number"));
        if (!(whereToSelectFrom.equals("monster") || whereToSelectFrom.equals("spell")
                || whereToSelectFrom.equals("field")
                || whereToSelectFrom.equals("hand"))) {
            PrintResponses.printInvalidSelection();
        } else {
            currentDuel.selectCard(cardAddress, whereToSelectFrom, "own");
            if (currentDuel.getSelectedCard() == null) {
                PrintResponses.printNoCardInPosition();
            } else {
                PrintResponses.printSuccessfulCardSelection();
            }
        }
    }

    private void selectCardFromOpponent(Matcher matcher) {
        String whereToSelectFrom = matcher.group("where");
        int cardAddress = Integer.parseInt(matcher.group("number"));
        if (!(whereToSelectFrom.equals("monster") || whereToSelectFrom.equals("spell")
                || whereToSelectFrom.equals("field"))) {
            PrintResponses.printInvalidSelection();
        } else {
            currentDuel.selectCard(cardAddress, whereToSelectFrom, "opponent");
            if (currentDuel.getSelectedCard() == null) {
                PrintResponses.printNoCardInPosition();
            } else {
                PrintResponses.printSuccessfulCardSelection();
            }
        }
    }

    private void deselectCard(Matcher matcher) {
        if (currentDuel.getSelectedCard() == null) {
            PrintResponses.printNoCardSelected();
        } else {
            currentDuel.deselectACard();
            PrintResponses.printSuccessfulCardDeselection();
        }
    }

    private void goToNextPhase(Matcher matcher) {
        if (phase.equals(Phases.DRAW_PHASE)) {
            phase = Phases.STANDBY_PHASE;
        } else if (phase.equals(Phases.STANDBY_PHASE)) {
            phase = Phases.MAIN_PHASE1;
        } else if (phase.equals(Phases.MAIN_PHASE1)) {
            phase = Phases.BATTLE_PHASE;
        } else if (phase.equals(Phases.BATTLE_PHASE)) {
            phase = Phases.MAIN_PHASE2;
        } else if (phase.equals(Phases.MAIN_PHASE2)) {
            phase = Phases.END_PHASE;
            PrintResponses.printPhaseName(phase);
            currentDuel.changeTurn();
            PrintResponses.showTurn(currentDuel.getUserWhoPlaysNow());
        }
        PrintResponses.printPhaseName(phase);
    }

    private void endTheGame() {
    }

    private void summon(Matcher matcher) {
        if (currentDuel.getSelectedCard() == null) {
            PrintResponses.printNoCardSelected();
        } else if (!currentDuel.canSummonSelectedCard()) {
            PrintResponses.printUnableToSummonCard();
        } else if (!(phase.equals(Phases.MAIN_PHASE1) || phase.equals(Phases.MAIN_PHASE2))) {
            PrintResponses.printSummonInWrongPhase();
        } else if (currentDuel.getUserWhoPlaysNow().getBoard().getAddressToSummon() == 0) {
            PrintResponses.printFullnessOfMonsterCardZone();
        } else if (currentDuel.isHasSummonedOrSetOnce()) {
            PrintResponses.printUnableToSummonInTurn();
        } else if (((Monster) currentDuel.getSelectedCard()).getLevel() > 4) {
            if (((Monster) currentDuel.getSelectedCard()).getLevel() < 7) {
                if (isNotEnoughCardsForTribute(1)) return;
            } else {
                if (isNotEnoughCardsForTribute(2)) return;
            }
            int[] cardToTributeAddress = Arrays.stream(ProgramController.scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            if (areCardAddressesEmpty(cardToTributeAddress)) return;
            currentDuel.tribute(cardToTributeAddress);
            PrintResponses.printSuccessfulSummon();
        } else {
            currentDuel.summonMonster();
            PrintResponses.printSuccessfulSummon();
        }

    }

    private void set(Matcher matcher) {
        if (currentDuel.getSelectedCard() == null) {
            PrintResponses.printNoCardSelected();
        } else if (!currentDuel.getUserWhoPlaysNow().getHand().isCardInHand(currentDuel.getSelectedCard())) {
            PrintResponses.printUnableToSetCard();
        } else if (currentDuel.getSelectedCard() instanceof Monster) {
            if (!(phase.equals(Phases.MAIN_PHASE1) || phase.equals(Phases.MAIN_PHASE2))) {
                PrintResponses.printSetCardInWrongPhase();
            } else if (currentDuel.getUserWhoPlaysNow().getBoard().getAddressToSummon() == 0) {
                PrintResponses.printFullnessOfMonsterCardZone();
            } else if (currentDuel.isHasSummonedOrSetOnce()) {
                PrintResponses.printUnableToSummonInTurn();
            } else {
                currentDuel.setMonster();
                PrintResponses.printSuccessfulCardSetting();
            }
        } //needs to be implemented for Spell and Trap

    }

    private void setPosition(Matcher matcher) {
        String position = matcher.group("position");
        if (currentDuel.getSelectedCard() == null) {
            PrintResponses.printNoCardSelected();
        } else if (!currentDuel.getUserWhoPlaysNow().getBoard().isCardOnMonsterZone(currentDuel.getSelectedCard())) {
            PrintResponses.printUnableToChangePositionOfCard();
        } else if (!(phase.equals(Phases.MAIN_PHASE1) || phase.equals(Phases.MAIN_PHASE2))) {
            PrintResponses.printSetCardInWrongPhase();
        } else if (isPositionTheSameAsBefore(position)){
            PrintResponses.printCardInWantedPosition();
        } else if (currentDuel.isHasChangedPositionOnce()){
            PrintResponses.printUnableToChangePositionInTurnTwice();
        } else {
            if (position.equals("attack")){
                currentDuel.changeToAttackPosition();
            } else{
                currentDuel.changeToDefensePosition();
            }
            PrintResponses.printSuccessfulMonsterCardPositionChange();
        }
    }


    private boolean isNotEnoughCardsForTribute(int requiredCardsAmount) {
        if (currentDuel.getUserWhoPlaysNow().getBoard().getMonsters().length == requiredCardsAmount) {
            PrintResponses.printNoCardToTribute();
            return true;
        }
        return false;
    }

    private boolean areCardAddressesEmpty(int[] cardsAddresses) {
        for (int toTributeAddress : cardsAddresses) {
            if (currentDuel.getUserWhoPlaysNow().getBoard().getCard(toTributeAddress, 'M') == null) {
                PrintResponses.printNoMonsterOnAddress();
                return true;
            }
        }
        return false;
    }

    private boolean isPositionTheSameAsBefore(String position){
        if (position.equals("attack")){
            return currentDuel.getSelectedCard().isFaceUp();
        } else{
            return !currentDuel.getSelectedCard().isFaceUp() && !currentDuel.getSelectedCard().isHidden();
        }
    }


}
