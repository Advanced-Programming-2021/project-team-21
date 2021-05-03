package controller.menu;

import module.User;

import java.util.Comparator;


public class ScoreSorter implements Comparator<User> {
    public int compare(User user1, User user2) {
        return user2.getScore() - user1.getScore();
    }
}