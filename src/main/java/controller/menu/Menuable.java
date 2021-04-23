package controller.menu;

public interface Menuable {
    void run(String command);
    void showCurrentMenu();
    void exitMenu();
}
