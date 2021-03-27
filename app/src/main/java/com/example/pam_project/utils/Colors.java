package com.example.pam_project.utils;

import java.util.LinkedList;
import java.util.List;

public class Colors { // TODO: Podriamos hacer algo con enums para que quede mas lindo
    public static final String RED = "#DC1316";
    public static final String BLUE = "#00A6FF";
    public static final String GREEN = "#77D353";
    public static final String YELLOW = "#FFBA5C";

//    private static List<String> unusedColors;
    public static List<String> allColors;

    public Colors() {
        allColors = new LinkedList<String>();
        this.fillColors(allColors);
//        unusedColors = new LinkedList<String>(allColors);
    }

    private void fillColors(List<String> allColors) {
        allColors.add(RED);
        allColors.add(GREEN);
        allColors.add(BLUE);
        allColors.add(YELLOW);
    }

//    public static void setColorAsUsed(String color) {
//        unusedColors.remove(color);
//    }

//    public static List<String> getUnusedColors() {
//        return unusedColors;
//    }

    public static List<String> getAllColors() {
        return allColors;
    }
}
