package module.card;

public enum MonsterTypes {
    WARRIOR("Warrior"),
    BEAST_WARRIOR("Beast-Warrior"),
    FIEND("Fiend"),
    AQUA("Aqua"),
    BEAST("Beast"),
    PYRO("Pyro"),
    SPELLCASTER("SpellCaster"),
    THUNDER("Thunder"),
    DRAGON("Dragon"),
    MACHINE("Machine"),
    ROCK("Rock"),
    INSECT("Insect"),
    CYBERSE("Cyberse"),
    FAIRY("Fairy"),
    SEA_SERPENT("Sea Serpent");
    private final String name;

    MonsterTypes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
