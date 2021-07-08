package model.card.enums;

import java.util.ArrayList;

public enum Attributes {
    DARK("Dark"),
    EARTH("Earth"),
    FIRE("Fire"),
    LIGHT("Light"),
    WATER("Water"),
    WIND("Wind");
    private String name;
    Attributes(String name){
        this.name = name;
    }
    public static ArrayList<String> getAttributes(){
        ArrayList<String>arrayList = new ArrayList<>();
        arrayList.add("Dark");
        arrayList.add("Earth");
        arrayList.add("Fire");
        arrayList.add("Light");
        arrayList.add("Water");
        arrayList.add("Wind");
        return arrayList;
    }
}
