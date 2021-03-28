package com.example.pam_project.utils;

public enum AppColor {
    RED("#DC1316"),
    BLUE("#00A6FF"),
    GREEN("#77D353"),
    YELLOW("#FFBA5C");

    private final String hexValue;

    private AppColor(final String hexValue) {
        this.hexValue = hexValue;
    }

    public String getHexValue() {
        return hexValue;
    }

//    public static void setColorAsUsed(String color) {
//        unusedColors.remove(color);
//    }

//    public static List<String> getUnusedColors() {
//        return unusedColors;
//    }
}
