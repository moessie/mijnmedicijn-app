package com.example.mustafa.mijnmedicijn;

import java.util.ArrayList;
import java.util.List;

// Class used to hold list of medicine, only for temporary use until API is not set up
public class DataHelper {
    public static ArrayList<String> getTestMedicines(){
        ArrayList<String>medicines = new ArrayList<>();
        medicines.add("panadol");
        medicines.add("isozam 20 mg");
        medicines.add("cosamin 15mg");
        medicines.add("amoxiclav 200g");
        medicines.add("flagyl 250");
        medicines.add("entamizole DS");
        medicines.add("loplate Plus");
        medicines.add("rizek Insta Sashe 20");
        medicines.add("tenormin 25");
        medicines.add("abacol cal Tabs");
        return medicines;
    }

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

    public static List<String> getIntervalDays(){
        List<String>units = new ArrayList<>();
        for(int i=0;i<30;i++){units.add(String.valueOf(i));}
        return units;
    }
}
