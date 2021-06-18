package module;

public class AI extends User {

    private static final AI ai = new AI("AI", "AI", "AI");

    private AI(String username, String password, String nickname) {
        super(username, password, nickname);
    }

    public static AI getInstance() {
        return ai;
    }

    /*
     * There is going to be a while (aiTurn) in this function.
     * It will call DuelMenu's run() with a string as a parameter.
     * The parameter will be the command we want to execute.
     */
    public void run() {

    }
}
