package com.example.pam_project.utils.constants;

import android.graphics.Color;

public enum AppColor {
    RED("#30DC1316"),
    ORANGE("#50FFBA5C"),
    YELLOW("#40FBBC04"),
    BLUE("#AAAECBFA"),
    LIGHT_BLUE("#2500A6FF"),
    GREEN("#3077D353");

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
