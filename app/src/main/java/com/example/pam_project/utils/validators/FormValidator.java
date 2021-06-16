package com.example.pam_project.utils.validators;

import android.content.Context;
import android.content.res.Resources;
import android.widget.EditText;

import com.example.pam_project.R;

import java.util.Map;

public class FormValidator {
    public static boolean validate(Context context, Map<EditText, String> map) {
        boolean valid = true;

        for (Map.Entry<EditText, String> entry : map.entrySet()) {
            valid = valid && checkEmptyTextInput(context.getResources(), entry.getValue(), entry.getKey());
        }

        return valid;
    }

    private static boolean checkEmptyTextInput(Resources resources, String textInput, EditText input) {
        if (textInput == null || textInput.trim().isEmpty()) {
            input.setError(resources.getString(R.string.error_empty_input));
            return false;
        }

        return true;
    }
}
