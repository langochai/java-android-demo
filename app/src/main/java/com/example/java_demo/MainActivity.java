package com.example.java_demo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private static final String PREFS_NAME = "theme_prefs";
    private static final String KEY_THEME = "isDarkMode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        applySavedTheme();
        SwitchCompat themeSwitch = findViewById(R.id.theme_switch);
        themeSwitch.setChecked(isDarkModeEnabled());
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                new Handler(Looper.getMainLooper()).postDelayed(() -> toggleTheme(isChecked), 100)
        );
    }

    private void applySavedTheme() {
        boolean isDarkMode = isDarkModeEnabled();
        AppCompatDelegate.setDefaultNightMode(isDarkMode ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }

    private void toggleTheme(boolean enableDarkMode) {
        preferences.edit().putBoolean(KEY_THEME, enableDarkMode).apply();
        getWindow().setWindowAnimations(android.R.style.Animation_Activity);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            AppCompatDelegate.setDefaultNightMode(enableDarkMode ?
                    AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
            recreate();
        }, 100);
    }

    private boolean isDarkModeEnabled() {
        return preferences.getBoolean(KEY_THEME, false);
    }
}