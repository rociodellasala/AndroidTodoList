package com.example.pam_project.landing;

import android.content.SharedPreferences;

public class SharedPreferencesFtuStorage implements FtuStorage {

    private static final String FTU_KEY = "is_ftu";
    private final SharedPreferences sharedPref;

    public SharedPreferencesFtuStorage(final SharedPreferences sharedPref) {
        this.sharedPref = sharedPref;
    }

    @Override
    public boolean isActive() {
        return sharedPref.getBoolean(FTU_KEY, true);
    }

    @Override
    public void deactivate() {
        sharedPref.edit().putBoolean(FTU_KEY, false).apply();
    }
}
