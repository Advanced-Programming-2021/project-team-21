package controller.menu;

import controller.DataController;
import controller.ProgramController;
import module.card.Card;
import view.PrintResponses;
import view.Regex;
import view.Responses;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.regex.Matcher;

public class ImportAndExport implements Menuable {
    @Override
    public void run(String command) {
        HashMap<String, Consumer<Matcher>> commandMap = createCommandMap();
        boolean isValidCommand = false;
        for (String string : commandMap.keySet()) {
            if (command.equals(Regex.menuExit)) {
                exitMenu();
                return;
            }
            Matcher matcher = Regex.getMatcher(command, string);
            if (matcher.find()) {
                isValidCommand = true;
                commandMap.get(string).accept(matcher);
            }
        }
        if (!isValidCommand)
            PrintResponses.printInvalidFormat();
    }

    protected HashMap<String, Consumer<Matcher>> createCommandMap() {
        HashMap<String, Consumer<Matcher>> commandMap = new HashMap<>();
        commandMap.put(Regex.importCard, this::importCard);
        commandMap.put(Regex.exportCard, this::exportCard);
        return commandMap;
    }

    private void exportCard(Matcher matcher) {
        Card card = Card.getCardByName(matcher.group("cardName"));
        if (card == null) {
            PrintResponses.print(Responses.noCardExistToBuy);
            return;
        }
        DataController.saveData(card);
        PrintResponses.print(Responses.cardExportedSuccessfully);
    }

    private void importCard(Matcher matcher) {
        String cardName = matcher.group("cardName");
        Card card = DataController.importCardFromJson(cardName);
        if (card == null) {
            PrintResponses.print(Responses.noCardExistToBuy);
            return;
        }
        ProgramController.userInGame.addCard(card);
        PrintResponses.print(Responses.cardImportedSuccessfully);
    }

    @Override
    public void exitMenu() {
        ProgramController.currentMenu = new MainMenu();
    }

    @Override
    public void showCurrentMenu() {
        PrintResponses.printExportAndImportMenuShow();
    }

}
