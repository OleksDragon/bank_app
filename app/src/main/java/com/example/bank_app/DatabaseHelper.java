package com.example.bank_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Random;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BankApp.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_LOGIN = "login";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_CARD_NUMBER = "card_number";
    private static final String COLUMN_BALANCE = "balance";
    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LOGIN + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_CARD_NUMBER + " TEXT, " +
                COLUMN_BALANCE + " REAL)";
        db.execSQL(createTable);

        // Додаємо тестового користувача
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOGIN, "user@gmail.com".toLowerCase().trim());
        values.put(COLUMN_PASSWORD, "pass123");
        values.put(COLUMN_CARD_NUMBER, "1234 5678 9012 3456");
        values.put(COLUMN_BALANCE, 5000.00);
        db.insert(TABLE_USERS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public boolean addUser(String login, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Нормалізація логіна та пароля
        String normalizedLogin = login.toLowerCase().trim();
        String normalizedPassword = password.trim();

        // Генеруємо унікальний номер картки
        String cardNumber = generateCardNumber();
        values.put(COLUMN_LOGIN, normalizedLogin);
        values.put(COLUMN_PASSWORD, normalizedPassword);
        values.put(COLUMN_CARD_NUMBER, cardNumber);
        values.put(COLUMN_BALANCE, 0.00);

        try {
            long result = db.insert(TABLE_USERS, null, values);
            Log.d(TAG, "Користувач доданий: login=" + normalizedLogin + ", cardNumber=" + cardNumber);
            return result != -1;
        } catch (Exception e) {
            Log.e(TAG, "Помилка додавання користувача: " + e.getMessage());
            return false;
        }
    }

    private String generateCardNumber() {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            int digit = random.nextInt(10);
            cardNumber.append(digit);
            if (i % 4 == 3 && i < 15) {
                cardNumber.append(" "); // Додаємо пробіл кожні 4 цифри
            }
        }
        return cardNumber.toString();
    }

    public boolean checkUser(String login, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Нормалізація введених даних
        String normalizedLogin = login.toLowerCase().trim();
        String normalizedPassword = password.trim();
        Log.d(TAG, "Перевірка користувача: login=" + normalizedLogin + ", password=" + normalizedPassword);
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID},
                COLUMN_LOGIN + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{normalizedLogin, normalizedPassword}, null, null, null);
        int count = cursor.getCount();
        Log.d(TAG, "Знайдено записів: " + count);
        boolean exists = count > 0;
        cursor.close();
        return exists;
    }

    public User getUser(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_CARD_NUMBER, COLUMN_BALANCE},
                COLUMN_LOGIN + "=?",
                new String[]{login.toLowerCase().trim()}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String cardNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CARD_NUMBER));
            double balance = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_BALANCE));
            cursor.close();
            return new User(cardNumber, balance);
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    // Клас для збереження даних користувача
    public static class User {
        private final String cardNumber;
        private final double balance;

        public User(String cardNumber, double balance) {
            this.cardNumber = cardNumber;
            this.balance = balance;
        }

        public String getCardNumber() {
            return cardNumber;
        }

        public double getBalance() {
            return balance;
        }
    }
}