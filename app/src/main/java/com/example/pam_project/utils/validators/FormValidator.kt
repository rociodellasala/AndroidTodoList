package com.example.pam_project.utils.validators

import android.content.Context
import android.content.res.Resources
import android.widget.EditText
import com.example.pam_project.R

object FormValidator {
    fun validate(context: Context, map: Map<EditText, String?>): Boolean {
        var valid = true
        for ((key, value) in map) {
            valid = valid && checkEmptyTextInput(context.resources, value, key)
        }
        return valid
    }

    private fun checkEmptyTextInput(resources: Resources, textInput: String?, input: EditText): Boolean {
        if (textInput == null || textInput.trim { it <= ' ' }.isEmpty()) {
            input.error = resources.getString(R.string.error_empty_input)
            return false
        }
        return true
    }
}