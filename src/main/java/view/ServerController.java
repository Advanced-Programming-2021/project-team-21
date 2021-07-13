package view;

import java.io.IOException;
import java.util.regex.Matcher;

public class ServerController {
    // this class meant to call different methods of classes from the command receives
    private static Matcher matcher;

    public String run(String command) throws IOException {
        if ((matcher = Regex.getMatcher(command, Regex.userCreate)).find())
            return LoginMenu.createNewUser(matcher);
        else if ((matcher = Regex.getMatcher(command, Regex.userLogin)).find())
            return LoginMenu.loginNewUser(matcher);
        return Responses.invalidFormat;
    }
}
