package com.example.bank_app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

public class ThemeManager {

    private static final String PREFS_NAME = "AppSettings";
    private static final String KEY_DARK_MODE = "DarkMode";
    private static final String KEY_THEME_CHANGED = "ThemeChanged";

    private SharedPreferences sharedPreferences;
    private Context context;

    public ThemeManager(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void applyTheme() {
        boolean isDarkMode = sharedPreferences.getBoolean(KEY_DARK_MODE, false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            context.setTheme(R.style.Theme_BankApp_Dark);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            context.setTheme(R.style.Theme_BankApp_Light);
        }
    }

    public void setDarkMode(boolean isDarkMode) {
        boolean currentMode = sharedPreferences.getBoolean(KEY_DARK_MODE, false);
        if (currentMode != isDarkMode) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(KEY_DARK_MODE, isDarkMode);
            editor.putBoolean(KEY_THEME_CHANGED, true);
            editor.apply();
            applyTheme();
            if (context instanceof Activity) {
                ((Activity) context).recreate();
            }
        }
    }

    public void onActivityCreate() {
        applyTheme();
        boolean themeChanged = sharedPreferences.getBoolean(KEY_THEME_CHANGED, false);
        if (themeChanged) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(KEY_THEME_CHANGED, false);
            editor.apply();
        }
    }

    public boolean isDarkMode() {
        return sharedPreferences.getBoolean(KEY_DARK_MODE, false);
    }
}