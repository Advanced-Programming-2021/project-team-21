package view;

import controller.DataController;
import model.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ScoreBoard {


    private ArrayList<User> getSortedUsers() {
        ArrayList<User> users = DataController.getAllUsers();
        if (users == null)
            return null;
        Comparator<User> comparator = Comparator.comparing(User::getScore, Comparator.reverseOrder())
                .thenComparing(User::getUsername);
        users.sort(comparator);
        return users.stream().limit(20).collect(Collectors.toCollection(ArrayList::new));
    }

    private String[] getRanks(ArrayList<User> users) {
        String[] ranks = new String[users.size()];
        ranks[0] = "1";
        for (int i = 1; i < users.size(); i++) {
            if (users.get(i).getScore() == users.get(i - 1).getScore()) {
                ranks[i] = ranks[i - 1];
            } else {
                ranks[i] = String.valueOf(i + 1);
            }
        }
        return ranks;
    }


}
