package com.example.pam_project.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Helper {

    public static AppColor findColor(String color) {
        final List<AppColor> colors = Arrays.asList(AppColor.values());

        for (int i = 0; i < colors.size(); i++) {
            if (color.equals(colors.get(i).toString())) {
                return colors.get(i);
            }
        }

        return null;
    }

    public static int getRandomFromArray(long[] array) {
        int rnd = new Random().nextInt(array.length);
        return (int) array[rnd];
    }

    public static int getRandom(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }
}
