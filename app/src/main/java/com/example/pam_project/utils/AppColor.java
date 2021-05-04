package com.example.pam_project.utils;

import android.graphics.Color;

public enum AppColor {
    RED("#22DC1316"),
    BLUE("#2200A6FF"),
    GREEN("#2277D353"),
    YELLOW("#22FFBA5C");

    private final String hexValue;

    AppColor(final String hexValue) {
        this.hexValue = hexValue;
    }

    public String getHexValue() {
        return hexValue;
    }

    public int getARGBValue() {
        return Color.parseColor(getHexValue());
    }

    public static AppColor fromARGBValue(int value) {
        for (AppColor color : values()) {
            if (color.getARGBValue() == value)
                return color;
        }
        return null;
    }

}
