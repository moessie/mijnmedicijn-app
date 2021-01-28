package com.example.mustafa.mijnmedicijn;

import java.util.ArrayList;
import java.util.List;

public class DataHelper {

    public static List<String> getUnits(){
        List<String>units = new ArrayList<>();
        units.add("Tablet(en)");
        units.add("Capsules(s)");
        units.add("Gram(s)");
        units.add("Milligram(s)");
        units.add("Milliliter(s)");
        return units;
    }

    public static List<String> getIntakeFrequency(){
        List<String>units = new ArrayList<>();
        units.add("Elkedag");
        units.add("Elke X Dagen");
        units.add("Specifieke dagen");
        return units;
    }

}
