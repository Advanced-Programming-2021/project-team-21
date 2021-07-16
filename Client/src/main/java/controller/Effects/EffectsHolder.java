package controller.Effects;

import java.util.ArrayList;
import java.util.LinkedHashMap;
public class EffectsHolder {
        LinkedHashMap<String , ArrayList<Integer>> price = new LinkedHashMap<>();
        LinkedHashMap<String , ArrayList<String>> description = new LinkedHashMap<>();
        LinkedHashMap<String , String>effect = new LinkedHashMap<>();

    public LinkedHashMap<String, ArrayList<Integer>> getPrice() {
        return price;
    }

    public LinkedHashMap<String, ArrayList<String>> getDescription() {
        return description;
    }

    public LinkedHashMap<String, String> getEffect() {
        return effect;
    }
}
