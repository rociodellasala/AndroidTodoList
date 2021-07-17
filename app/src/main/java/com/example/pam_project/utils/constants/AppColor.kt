package com.example.pam_project.utils.constants

import android.graphics.Color

enum class AppColor(val hexValue: String) {
    RED("#30DC1316"), ORANGE("#50FFBA5C"), YELLOW("#40FBBC04"), BLUE("#AAAECBFA"), LIGHT_BLUE("#2500A6FF"), GREEN("#3077D353");

    val aRGBValue: Int
        get() = Color.parseColor(hexValue)

    companion object {
        fun fromARGBValue(value: Int): AppColor? {
            for (color in values()) {
                if (color.aRGBValue == value) return color
            }
            return null
        }

        fun fromName(colorName: String?): AppColor? {
            if (colorName == null) return null
            for (color in values()) {
                if (color.toString() == colorName.uppercase()) {
                    return color
                }
            }
            return null
        }
    }
}