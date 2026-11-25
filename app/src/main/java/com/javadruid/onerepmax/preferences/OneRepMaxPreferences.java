package com.javadruid.onerepmax.preferences;

import android.content.SharedPreferences;

public class OneRepMaxPreferences {
    static final String KEY_SELECTED_ALGORITHM = "selected_algorithm";
    private final SharedPreferences prefs;

    public OneRepMaxPreferences(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    public String getSavedAlgorithmName() {
        return prefs.getString(KEY_SELECTED_ALGORITHM, "");
    }
    public void setSavedAlgorithmName(String name) {
        prefs.edit()
                .putString(KEY_SELECTED_ALGORITHM, name)
                .apply();
    }
}
