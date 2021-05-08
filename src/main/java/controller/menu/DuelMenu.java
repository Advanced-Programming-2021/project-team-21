package controller.menu;

import controller.ProgramController;
import module.Duel;
import module.User;
import module.card.Card;
import view.PrintResponses;
import view.Regex;

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
        } else if (ProgramController.userInGame.getActiveDeck().isValid()) {
            PrintResponses.printInvalidDeck(ProgramController.userInGame);
        } else if (secondPlayer.getActiveDeck().isValid()) {
            PrintResponses.printInvalidDeck(secondPlayer);
        } else if (!(rounds == 1 || rounds == 3)) {
            PrintResponses.printNonSupportiveRound();
        } else {
            currentDuel = new Duel(ProgramController.userInGame, secondPlayer);
            phase = Phases.DRAW_PHASE;
            Card card = currentDuel.drawACard();
            if (card == null){
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
                || whereToSelectFrom.equals("hand"))){
            PrintResponses.printInvalidSelection();
        } else {
            currentDuel.selectCard(cardAddress, whereToSelectFrom, "own");
            if (currentDuel.getSelectedCard() == null){
                PrintResponses.printNoCardInPosition();
            }else {
                PrintResponses.printSuccessfulCardSelection();
            }
        }
    }

    private void selectCardFromOpponent(Matcher matcher) {
        String whereToSelectFrom = matcher.group("where");
        int cardAddress = Integer.parseInt(matcher.group("number"));
        if (!(whereToSelectFrom.equals("monster") || whereToSelectFrom.equals("spell")
                || whereToSelectFrom.equals("field"))){
            PrintResponses.printInvalidSelection();
        } else {
            currentDuel.selectCard(cardAddress, whereToSelectFrom, "opponent");
            if (currentDuel.getSelectedCard() == null){
                PrintResponses.printNoCardInPosition();
            }else {
                PrintResponses.printSuccessfulCardSelection();
            }
        }
    }

    private void deselectCard(Matcher matcher){
        if (currentDuel.getSelectedCard() == null){
            PrintResponses.printNoCardSelected();
        } else{
            currentDuel.deselectACard();
            PrintResponses.printSuccessfulCardDeselection();
        }
    }

    private void goToNextPhase(Matcher matcher){
        if (phase.equals(Phases.DRAW_PHASE)){
            phase = Phases.STANDBY_PHASE;
        } else if (phase.equals(Phases.STANDBY_PHASE)){
            phase = Phases.MAIN_PHASE1;
        } else if (phase.equals(Phases.MAIN_PHASE1)){
            phase = Phases.BATTLE_PHASE;
        }else if (phase.equals(Phases.BATTLE_PHASE)){
            phase = Phases.MAIN_PHASE2;
        } else if (phase.equals(Phases.MAIN_PHASE2)){
            phase = Phases.END_PHASE;
            PrintResponses.printPhaseName(phase);
            currentDuel.changeTurn();
            PrintResponses.showTurn(currentDuel.getUserWhoPlaysNow());
        }
        PrintResponses.printPhaseName(phase);
    }

    private void endTheGame(){

    }


}
