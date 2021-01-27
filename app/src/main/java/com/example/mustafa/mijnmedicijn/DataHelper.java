package com.example.mustafa.mijnmedicijn;

import java.util.ArrayList;
import java.util.List;

// Class used to hold list of medicine, only for temporary use until API is not set up
public class DataHelper {

    public static List<String> getUnits(){
        List<String>units = new ArrayList<>();
        units.add("pill(s)");
        units.add("capsules(s)");
        units.add("gram(s)");
        units.add("milligram(s)");
        units.add("milliliter(s)");
        units.add("drop(s)");
        units.add("spoon(s)");
        units.add("table spoon(s)");
        units.add("puff(s)");
        return units;
    }

    public static List<String> getIntakeFrequency(){
        List<String>units = new ArrayList<>();
        units.add("Everyday");
        units.add("Every X days");
        units.add("Specific days of Week");
        return units;
    }

}
