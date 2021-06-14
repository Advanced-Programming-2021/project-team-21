package controller.menu;

import java.io.IOException;

public interface Menuable {
    void run(String command) throws IOException;

    void showCurrentMenu();

    void exitMenu();
}
