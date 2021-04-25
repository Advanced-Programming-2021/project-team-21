package controller;

import com.google.gson.Gson;
import module.Deck;
import module.User;
import module.card.Card;
import module.card.Monster;
import module.card.Spell;
import module.card.Trap;
import tech.tablesaw.api.Table;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DataController {

    private static final String[] paths = {"src/main/resources/cards/Monster.csv",
            "src/main/resources/cards/Spell.csv",
            "src/main/resources/cards/Trap.csv"};

    public static Card getCard(String cardName) {
        for (String path : paths) {
            Card card = getCardFromTable(cardName, path);
            if (card != null)
                return card;
        }
        return null;
    }

    private static Card getCardFromTable(String cardName, String path) {
        Table table;
        try {
            table = Table.read().csv(path);
            for (int i = 0; i < table.rowCount(); i++) {
                if (table.column("name").get(i).equals(cardName)) {
                    String[] columnNames = table.columnNames().toArray(new String[0]);
                    Object[] parameters = new Object[columnNames.length];
                    for (int j = 0; j < columnNames.length; j++) {
                        parameters[j] = table.column(columnNames[j]).get(i);
                    }//checking which constructor to call
                    if (path.equals(paths[0]))
                        return new Monster(parameters);
                    else if (path.equals(paths[1]))
                        return new Spell(parameters);
                    else
                        return new Trap(parameters);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //creates necessary directories for storing data
    public static void createDirectories() {
        String[] directoryNames = {"src/main/resources/users", "src/main/resources/decks"
                , "src/main/resources/cards"};
        for (String directoryName : directoryNames) {
            File directory = new File(directoryName);
            boolean isCreated = directory.mkdir();
        }
    }

    //is called for saving User and Deck objects as json
    public static void saveData(Object object) {
        String dataToWrite = new Gson().toJson(object);
        if (object instanceof User) {
            try {
                FileWriter fileWriter = new FileWriter("src/main/resources/users/" + ((User) object).getUsername()
                        + ".user.json");
                fileWriter.write(dataToWrite);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (object instanceof Deck) {
            try {
                FileWriter fileWriter = new FileWriter("src/main/resources/decks/" + ((Deck) object).getName() + "." +
                        ((Deck) object).getUserWhoOwns().getUsername() + ".json");
                fileWriter.write(dataToWrite);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
