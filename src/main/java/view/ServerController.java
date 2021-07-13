package view;

import controller.ProgramController;


public class ServerController {
    // this class meant to call different methods of classes from the command receives

    public String run(String command) {
        if (!command.startsWith("user create") ) {
            if (!command.substring(0, 31).equals(ProgramController.currentToken))
                return Responses.invalidToken;
            else command = command.substring(32);
        }
        return ProgramController.currentMenu.run(command);
    }

}
