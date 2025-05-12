package com.example.bank_app;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ThemeManager themeManager;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            themeManager = new ThemeManager(this);
            themeManager.applyTheme();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                try {
                    if (itemId == R.id.nav_my_account) {
                        selectedFragment = new MyAccountFragment();
                    } else if (itemId == R.id.nav_transfers) {
                        selectedFragment = new TransfersFragment();
                    } else if (itemId == R.id.nav_loans) {
                        selectedFragment = new LoansFragment();
                    } else if (itemId == R.id.nav_settings) {
                        selectedFragment = new SettingsFragment();
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
                        .replace(R.id.fragment_container, new MyAccountFragment())
                        .commit();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: ", e);
            Toast.makeText(this, "Помилка при завантаженні сторінки", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}