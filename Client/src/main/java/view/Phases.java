package view;

public enum Phases {
    DRAW_PHASE("draw phase"),
    STANDBY_PHASE("standby phase"),
    MAIN_PHASE1("main phase 1"),
    BATTLE_PHASE("battle phase"),
    MAIN_PHASE2("main phase 2"),
    END_PHASE("end phase");
    private final String name;

    Phases(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
