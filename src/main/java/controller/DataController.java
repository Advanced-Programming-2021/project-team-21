package controller;

import com.google.gson.Gson;
import module.Deck;
import module.User;
import module.card.Card;
import module.card.Monster;
import tech.tablesaw.api.Table;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DataController {

    public static Card getCardFromDataBase(String cardName) {
        Table t;
        try {
            t = Table.read().csv("src/main/resources/Monster.csv");
            for (int i = 0; i < t.rowCount(); i++) {
                if (t.column("name").get(i).equals(cardName)) {
                    String[] columnNames = t.columnNames().toArray(new String[0]);
                    Object[] parameters = new Object[columnNames.length];
                    for (int j = 0; j < columnNames.length; j++) {
                        parameters[j] = t.column(columnNames[j]).get(i);
                    }
                    Monster monsterCard = new Monster(parameters);
                    break;
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
                , "src/main/resources/cards", "src/main/resources/cards/monsters",
                "src/main/resources/cards/spellsAndTraps"};
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
