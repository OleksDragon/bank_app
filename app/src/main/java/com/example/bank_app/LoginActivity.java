package com.example.bank_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEditText, passwordEditText;
    private DatabaseHelper dbHelper;
    private ThemeManager themeManager;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "UserSession";
    private static final String KEY_LOGIN = "login";
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        themeManager = new ThemeManager(this);
        themeManager.onActivityCreate();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEditText = findViewById(R.id.loginEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);
        Button createAccountButton = findViewById(R.id.createAccountButton);
        TextView forgotPasswordText = findViewById(R.id.forgotPasswordText);

        dbHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Перевіряємо, чи є активна сесія
        String savedLogin = sharedPreferences.getString(KEY_LOGIN, null);
        if (savedLogin != null) {
            Log.d(TAG, "Знайдено активну сесію для користувача: " + savedLogin);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                Log.d(TAG, "Спроба авторизації: email=" + email + ", password=" + password);

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Заповніть усі поля", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dbHelper.checkUser(email, password)) {
                    Log.d(TAG, "Успішна авторизація для користувача: " + email);
                    // Зберігаємо логін у сесії
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY_LOGIN, email);
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("login", email);
                    startActivity(intent);
                    finish();
                } else {
                    Log.w(TAG, "Невдала спроба авторизації для користувача: " + email);
                    Toast.makeText(LoginActivity.this, "Неправильний логін або пароль", Toast.LENGTH_SHORT).show();
                }
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Функція скидання пароля ще не реалізована", Toast.LENGTH_SHORT).show();
            }
        });
    }
}