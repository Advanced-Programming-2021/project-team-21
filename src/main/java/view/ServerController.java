package view;

import controller.ProgramController;

import java.io.IOException;
import java.util.regex.Matcher;

public class ServerController {
    // this class meant to call different methods of classes from the command receives

    public String run(String command) {
        return ProgramController.currentMenu.run(command);
    }

}
