package com.example.pam_project.utils;

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

}
