package com.example.bank_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    private EditText nameEditText, addressEditText, phoneEditText;
    private Switch darkThemeSwitch;
    private Button saveButton, logoutButton;
    private ThemeManager themeManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        themeManager = new ThemeManager(requireContext());

        nameEditText = view.findViewById(R.id.nameEditText);
        addressEditText = view.findViewById(R.id.addressEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
        darkThemeSwitch = view.findViewById(R.id.darkThemeSwitch);
        saveButton = view.findViewById(R.id.saveButton);
        logoutButton = view.findViewById(R.id.logoutButton);

        // Завантажуємо збережені дані
        loadSavedData();

        // Налаштовуємо перемикач теми
        darkThemeSwitch.setChecked(themeManager.isDarkMode());
        darkThemeSwitch.setText(themeManager.isDarkMode() ? "Темна тема" : "Світла тема");
        darkThemeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                themeManager.setDarkMode(isChecked);
                darkThemeSwitch.setText(isChecked ? "Темна тема" : "Світла тема");
                requireActivity().recreate(); // Перезапускаємо активність для оновлення теми
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileData();
                Toast.makeText(requireContext(), "Дані збережено", Toast.LENGTH_SHORT).show();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), LoginActivity.class);
                startActivity(intent);
                requireActivity().finishAffinity();
            }
        });

        return view;
    }

    private void loadSavedData() {
        SharedPreferences prefs = requireContext().getSharedPreferences("AppSettings", requireContext().MODE_PRIVATE);
        nameEditText.setText(prefs.getString("Name", ""));
        addressEditText.setText(prefs.getString("Address", ""));
        phoneEditText.setText(prefs.getString("Phone", ""));
    }

    private void saveProfileData() {
        SharedPreferences prefs = requireContext().getSharedPreferences("AppSettings", requireContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Name", nameEditText.getText().toString().trim());
        editor.putString("Address", addressEditText.getText().toString().trim());
        editor.putString("Phone", phoneEditText.getText().toString().trim());
        editor.apply();
    }
}
