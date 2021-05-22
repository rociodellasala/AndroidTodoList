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

    public static AppColor fromARGBValue(final int value) {
        for (AppColor color : values()) {
            if (color.getARGBValue() == value)
                return color;
        }
        return null;
    }

    public static AppColor fromName(final String colorName) {
        if (colorName == null)
            return null;

        for (AppColor color : values()) {
            if (color.toString().equals(colorName.toUpperCase())) {
                return color;
            }
        }
        return null;
    }

    public String getHexValue() {
        return hexValue;
    }

    public int getARGBValue() {
        return Color.parseColor(getHexValue());
    }

}
