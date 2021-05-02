package com.example.pam_project.utils;

import java.util.Arrays;
import java.util.List;

public class ColorFinder {
    public static AppColor findColor(String color) {
        final List<AppColor> colors = Arrays.asList(AppColor.values());

        for (int i = 0; i < colors.size(); i++) {
            if (color.equals(colors.get(i).toString())) {
                return colors.get(i);
            }
        }

        return null;
    }
}
