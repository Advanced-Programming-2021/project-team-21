package model.card.enums;

import java.util.ArrayList;

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
    public  static ArrayList<String>monsterTypes(){
        ArrayList<String> types = new ArrayList<>();
        types.add("Warrior");
        types.add("Beast-Warrior");
        types.add("Fiend");
        types.add("Aqua");
        types.add("Beast");
        types.add("Pyro");
        types.add("SpellCaster");
        types.add("Thunder");
        types.add("Dragon");
        types.add("Machine");
        types.add("Rock");
        types.add("Insect");
        types.add("Cyberse");
        types.add("Fairy");
        types.add("Sea Serpent");
        return types;
    }
}
