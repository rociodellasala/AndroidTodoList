package com.example.pam_project.landing

import android.content.SharedPreferences

class SharedPreferencesFtuStorage(private val sharedPref: SharedPreferences) : FtuStorage {
    override val isActive: Boolean
        get() = sharedPref.getBoolean(FTU_KEY, true)

    override fun deactivate() {
        sharedPref.edit().putBoolean(FTU_KEY, false).apply()
    }

    companion object {
        private const val FTU_KEY = "is_ftu"
    }
}