package com.example.bank_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ThemeManager themeManager;
    private static final String TAG = "MainActivity";
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "UserSession";
    private static final String KEY_LOGIN = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            themeManager = new ThemeManager(this);
            themeManager.applyTheme();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // Ініціалізуємо SharedPreferences
            sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

            // Отримуємо логін із сесії
            String login = sharedPreferences.getString(KEY_LOGIN, null);

            // Перевіряємо авторизацію
            if (login == null) {
                Log.w(TAG, "Немає активної сесії");
                Toast.makeText(this, "Будь ласка, увійдіть у систему", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return;
            }

            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                try {
                    if (itemId == R.id.nav_my_account) {
                        selectedFragment = MyAccountFragment.newInstance(login);
                    } else if (itemId == R.id.nav_transfers) {
                        selectedFragment = TransfersFragment.newInstance(login);
                    } else if (itemId == R.id.nav_loans) {
                        selectedFragment = LoansFragment.newInstance(login);
                    } else if (itemId == R.id.nav_settings) {
                        selectedFragment = SettingsFragment.newInstance(login);
                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, selectedFragment)
                                .commit();
                    }
                    return true;
                } catch (Exception e) {
                    Log.e(TAG, "Error switching fragment: ", e);
                    return false;
                }
            });

            // Завантажуємо фрагмент за замовчуванням
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, MyAccountFragment.newInstance(login))
                        .commit();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: ", e);
            Toast.makeText(this, "Помилка при завантаженні сторінки", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    // Метод для виходу з акаунта
    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_LOGIN);
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}