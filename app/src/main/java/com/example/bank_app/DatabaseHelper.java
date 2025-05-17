package com.example.bank_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BankApp.db";
    private static final int DATABASE_VERSION = 3;
    private static final String TABLE_USERS = "users";
    private static final String TABLE_TRANSACTIONS = "transactions";
    private static final String TABLE_LOANS = "loans";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_LOGIN = "login";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_CARD_NUMBER = "card_number";
    private static final String COLUMN_BALANCE = "balance";
    private static final String COLUMN_TRANS_ID = "trans_id";
    private static final String COLUMN_SENDER_CARD = "sender_card";
    private static final String COLUMN_RECEIVER_CARD = "receiver_card";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_DATE_TIME = "date_time";
    private static final String COLUMN_SUCCESS = "success";
    private static final String COLUMN_LOAN_ID = "loan_id";
    private static final String COLUMN_LOAN_AMOUNT = "loan_amount";
    private static final String COLUMN_LOAN_TERM = "loan_term";
    private static final String COLUMN_REMAINING_AMOUNT = "remaining_amount";
    private static final String COLUMN_ISSUE_DATE = "issue_date";
    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LOGIN + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_CARD_NUMBER + " TEXT, " +
                COLUMN_BALANCE + " REAL)";
        db.execSQL(createUsersTable);

        String createTransactionsTable = "CREATE TABLE IF NOT EXISTS " + TABLE_TRANSACTIONS + " (" +
                COLUMN_TRANS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SENDER_CARD + " TEXT, " +
                COLUMN_RECEIVER_CARD + " TEXT, " +
                COLUMN_AMOUNT + " REAL, " +
                COLUMN_DATE_TIME + " TEXT, " +
                COLUMN_SUCCESS + " INTEGER)";
        db.execSQL(createTransactionsTable);

        String createLoansTable = "CREATE TABLE IF NOT EXISTS " + TABLE_LOANS + " (" +
                COLUMN_LOAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CARD_NUMBER + " TEXT, " +
                COLUMN_LOAN_AMOUNT + " REAL, " +
                COLUMN_LOAN_TERM + " INTEGER, " +
                COLUMN_REMAINING_AMOUNT + " REAL, " +
                COLUMN_ISSUE_DATE + " TEXT)";
        db.execSQL(createLoansTable);

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
        if (oldVersion < 3) {
            // Перевіряємо, чи існує таблиця users
            Cursor usersCursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{TABLE_USERS});
            boolean usersTableExists = usersCursor != null && usersCursor.moveToFirst();
            if (usersCursor != null) {
                usersCursor.close();
            }

            if (!usersTableExists) {
                String createUsersTable = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_LOGIN + " TEXT UNIQUE, " +
                        COLUMN_PASSWORD + " TEXT, " +
                        COLUMN_CARD_NUMBER + " TEXT, " +
                        COLUMN_BALANCE + " REAL)";
                db.execSQL(createUsersTable);
                Log.d(TAG, "Таблицю users створено під час оновлення до версії 3");

                // Додаємо тестового користувача, якщо таблиця щойно створена
                ContentValues userValues = new ContentValues();
                userValues.put(COLUMN_LOGIN, "user@gmail.com".toLowerCase().trim());
                userValues.put(COLUMN_PASSWORD, "pass123");
                userValues.put(COLUMN_CARD_NUMBER, "1234 5678 9012 3456");
                userValues.put(COLUMN_BALANCE, 5000.00);
                db.insert(TABLE_USERS, null, userValues);
            }

            // Перевіряємо, чи існує таблиця transactions
            Cursor transactionsCursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{TABLE_TRANSACTIONS});
            boolean transactionsTableExists = transactionsCursor != null && transactionsCursor.moveToFirst();
            if (transactionsCursor != null) {
                transactionsCursor.close();
            }

            if (!transactionsTableExists) {
                String createTransactionsTable = "CREATE TABLE IF NOT EXISTS " + TABLE_TRANSACTIONS + " (" +
                        COLUMN_TRANS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_SENDER_CARD + " TEXT, " +
                        COLUMN_RECEIVER_CARD + " TEXT, " +
                        COLUMN_AMOUNT + " REAL, " +
                        COLUMN_DATE_TIME + " TEXT, " +
                        COLUMN_SUCCESS + " INTEGER)";
                db.execSQL(createTransactionsTable);
                Log.d(TAG, "Таблицю transactions створено під час оновлення до версії 3");
            }

            // Перевіряємо, чи існує таблиця loans
            Cursor loansCursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{TABLE_LOANS});
            boolean loansTableExists = loansCursor != null && loansCursor.moveToFirst();
            if (loansCursor != null) {
                loansCursor.close();
            }

            if (!loansTableExists) {
                String createLoansTable = "CREATE TABLE IF NOT EXISTS " + TABLE_LOANS + " (" +
                        COLUMN_LOAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_CARD_NUMBER + " TEXT, " +
                        COLUMN_LOAN_AMOUNT + " REAL, " +
                        COLUMN_LOAN_TERM + " INTEGER, " +
                        COLUMN_REMAINING_AMOUNT + " REAL, " +
                        COLUMN_ISSUE_DATE + " TEXT)";
                db.execSQL(createLoansTable);
                Log.d(TAG, "Таблицю loans створено під час оновлення до версії 3");
            }
        }
    }


    public boolean addUser(String login, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String normalizedLogin = login.toLowerCase().trim();
        String normalizedPassword = password.trim();

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
                cardNumber.append(" ");
            }
        }
        return cardNumber.toString();
    }

    public boolean checkUser(String login, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
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

    public User getUserByCardNumber(String cardNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_CARD_NUMBER, COLUMN_BALANCE, COLUMN_LOGIN},
                COLUMN_CARD_NUMBER + "=?",
                new String[]{cardNumber.trim()}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String foundCardNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CARD_NUMBER));
            double balance = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_BALANCE));
            String userLogin = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOGIN));
            cursor.close();
            return new User(foundCardNumber, balance);
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    public boolean rechargeBalance(String login, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_BALANCE, COLUMN_CARD_NUMBER},
                COLUMN_LOGIN + "=?", new String[]{login.toLowerCase().trim()}, null, null, null);
        double currentBalance = 0.0;
        String cardNumber = "";
        if (cursor != null && cursor.moveToFirst()) {
            currentBalance = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_BALANCE));
            cardNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CARD_NUMBER));
            cursor.close();
        } else {
            if (cursor != null) cursor.close();
            return false;
        }

        double newBalance = currentBalance + amount;
        ContentValues values = new ContentValues();
        values.put(COLUMN_BALANCE, newBalance);

        try {
            int rowsAffected = db.update(TABLE_USERS, values, COLUMN_LOGIN + "=?", new String[]{login.toLowerCase().trim()});
            if (rowsAffected > 0) {
                Log.d(TAG, "Баланс поповнено для користувача: " + login + ", сума: " + amount + ", новий баланс: " + newBalance);

                // Фіксація поповнення як транзакції
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
                String dateTime = sdf.format(new Date());
                ContentValues transactionValues = new ContentValues();
                transactionValues.put(COLUMN_SENDER_CARD, "SYSTEM"); // Позначка, що це поповнення
                transactionValues.put(COLUMN_RECEIVER_CARD, cardNumber);
                transactionValues.put(COLUMN_AMOUNT, amount);
                transactionValues.put(COLUMN_DATE_TIME, dateTime);
                transactionValues.put(COLUMN_SUCCESS, 1); // Успішне поповнення
                db.insert(TABLE_TRANSACTIONS, null, transactionValues);

                return true;
            } else {
                Log.w(TAG, "Користувач не знайден для поповнення: " + login);
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "Помилка поповнення балансу: " + e.getMessage());
            return false;
        }
    }

    public boolean transferBalance(String senderLogin, String receiverCardNumber, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            // Перевіряємо відправника
            Cursor senderCursor = db.query(TABLE_USERS, new String[]{COLUMN_BALANCE, COLUMN_LOGIN, COLUMN_CARD_NUMBER},
                    COLUMN_LOGIN + "=?", new String[]{senderLogin.toLowerCase().trim()}, null, null, null);
            double senderBalance = 0.0;
            String senderCardNumber = "";
            if (senderCursor != null && senderCursor.moveToFirst()) {
                senderBalance = senderCursor.getDouble(senderCursor.getColumnIndexOrThrow(COLUMN_BALANCE));
                senderCardNumber = senderCursor.getString(senderCursor.getColumnIndexOrThrow(COLUMN_CARD_NUMBER));
                senderCursor.close();
            } else {
                if (senderCursor != null) senderCursor.close();
                return false;
            }

            if (senderBalance < amount) {
                Log.w(TAG, "Недостатньо коштів для переказу: " + senderLogin);
                return false;
            }

            // Знаходимо отримувача
            User receiver = getUserByCardNumber(receiverCardNumber);
            if (receiver == null) {
                Log.w(TAG, "Отримувач не знайден: " + receiverCardNumber);
                return false;
            }

            // Оновлюємо баланс відправника
            double newSenderBalance = senderBalance - amount;
            ContentValues senderValues = new ContentValues();
            senderValues.put(COLUMN_BALANCE, newSenderBalance);
            int senderRowsAffected = db.update(TABLE_USERS, senderValues, COLUMN_LOGIN + "=?", new String[]{senderLogin.toLowerCase().trim()});
            if (senderRowsAffected <= 0) {
                Log.w(TAG, "Помилка оновлення балансу відправника: " + senderLogin);
                db.endTransaction();
                return false;
            }

            // Оновлюємо баланс отримувача
            Cursor receiverCursor = db.query(TABLE_USERS, new String[]{COLUMN_BALANCE},
                    COLUMN_CARD_NUMBER + "=?", new String[]{receiverCardNumber.trim()}, null, null, null);
            double receiverBalance = 0.0;
            if (receiverCursor != null && receiverCursor.moveToFirst()) {
                receiverBalance = receiverCursor.getDouble(receiverCursor.getColumnIndexOrThrow(COLUMN_BALANCE));
                receiverCursor.close();
            }
            double newReceiverBalance = receiverBalance + amount;
            ContentValues receiverValues = new ContentValues();
            receiverValues.put(COLUMN_BALANCE, newReceiverBalance);
            int receiverRowsAffected = db.update(TABLE_USERS, receiverValues, COLUMN_CARD_NUMBER + "=?", new String[]{receiverCardNumber.trim()});
            if (receiverRowsAffected <= 0) {
                Log.w(TAG, "Помилка оновлення балансу отримувача: " + receiverCardNumber);
                db.endTransaction();
                return false;
            }

            // Фіксуємо транзакцію
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
            String dateTime = sdf.format(new Date());
            ContentValues transactionValues = new ContentValues();
            transactionValues.put(COLUMN_SENDER_CARD, senderCardNumber);
            transactionValues.put(COLUMN_RECEIVER_CARD, receiverCardNumber);
            transactionValues.put(COLUMN_AMOUNT, amount);
            transactionValues.put(COLUMN_DATE_TIME, dateTime);
            transactionValues.put(COLUMN_SUCCESS, 1); // 1 - успіх, 0 - невдача
            db.insert(TABLE_TRANSACTIONS, null, transactionValues);

            db.setTransactionSuccessful();
            Log.d(TAG, "Переказ виконано: від " + senderLogin + " до " + receiverCardNumber + ", сума: " + amount);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Помилка переказу: " + e.getMessage());
            return false;
        } finally {
            db.endTransaction();
        }
    }

    public List<Transaction> getTransactionsByCardNumber(String cardNumber) {
        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TRANSACTIONS,
                new String[]{COLUMN_TRANS_ID, COLUMN_SENDER_CARD, COLUMN_RECEIVER_CARD, COLUMN_AMOUNT, COLUMN_DATE_TIME, COLUMN_SUCCESS},
                COLUMN_SENDER_CARD + "=? OR " + COLUMN_RECEIVER_CARD + "=?",
                new String[]{cardNumber.trim(), cardNumber.trim()}, null, null, COLUMN_DATE_TIME + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int transId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TRANS_ID));
                String senderCard = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SENDER_CARD));
                String receiverCard = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECEIVER_CARD));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT));
                String dateTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE_TIME));
                int success = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SUCCESS));
                transactions.add(new Transaction(transId, senderCard, receiverCard, amount, dateTime, success == 1));
            } while (cursor.moveToNext());
            cursor.close();
        } else if (cursor != null) {
            cursor.close();
        }
        return transactions;
    }

    public boolean addLoan(String cardNumber, double loanAmount, int loanTerm) {
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String issueDate = sdf.format(new Date());

        ContentValues values = new ContentValues();
        values.put(COLUMN_CARD_NUMBER, cardNumber.trim());
        values.put(COLUMN_LOAN_AMOUNT, loanAmount);
        values.put(COLUMN_LOAN_TERM, loanTerm);
        values.put(COLUMN_REMAINING_AMOUNT, loanAmount);
        values.put(COLUMN_ISSUE_DATE, issueDate);

        try {
            long result = db.insert(TABLE_LOANS, null, values);
            Log.d(TAG, "Кредит додано: cardNumber=" + cardNumber + ", amount=" + loanAmount);
            return result != -1;
        } catch (Exception e) {
            Log.e(TAG, "Помилка додавання кредиту: " + e.getMessage());
            return false;
        }
    }

    public List<Loan> getLoansByCardNumber(String cardNumber) {
        List<Loan> loans = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LOANS,
                new String[]{COLUMN_LOAN_ID, COLUMN_LOAN_AMOUNT, COLUMN_LOAN_TERM, COLUMN_REMAINING_AMOUNT, COLUMN_ISSUE_DATE},
                COLUMN_CARD_NUMBER + "=?",
                new String[]{cardNumber.trim()}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int loanId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LOAN_ID));
                double loanAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LOAN_AMOUNT));
                int loanTerm = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LOAN_TERM));
                double remainingAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_REMAINING_AMOUNT));
                String issueDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ISSUE_DATE));
                loans.add(new Loan(loanId, loanAmount, loanTerm, remainingAmount, issueDate));
            } while (cursor.moveToNext());
            cursor.close();
        } else if (cursor != null) {
            cursor.close();
        }
        return loans;
    }

    public boolean payLoan(String login, int loanId, double paymentAmount) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            // Перевіряємо баланс користувача
            Cursor userCursor = db.query(TABLE_USERS, new String[]{COLUMN_BALANCE, COLUMN_CARD_NUMBER},
                    COLUMN_LOGIN + "=?", new String[]{login.toLowerCase().trim()}, null, null, null);
            double currentBalance = 0.0;
            String cardNumber = "";
            if (userCursor != null && userCursor.moveToFirst()) {
                currentBalance = userCursor.getDouble(userCursor.getColumnIndexOrThrow(COLUMN_BALANCE));
                cardNumber = userCursor.getString(userCursor.getColumnIndexOrThrow(COLUMN_CARD_NUMBER));
                userCursor.close();
            } else {
                if (userCursor != null) userCursor.close();
                return false;
            }

            if (currentBalance < paymentAmount) {
                Log.w(TAG, "Недостатньо коштів для погашення кредиту: " + login);
                return false;
            }

            // Отримуємо залишок кредиту
            Cursor loanCursor = db.query(TABLE_LOANS, new String[]{COLUMN_REMAINING_AMOUNT},
                    COLUMN_LOAN_ID + "=?", new String[]{String.valueOf(loanId)}, null, null, null);
            double remainingAmount = 0.0;
            if (loanCursor != null && loanCursor.moveToFirst()) {
                remainingAmount = loanCursor.getDouble(loanCursor.getColumnIndexOrThrow(COLUMN_REMAINING_AMOUNT));
                loanCursor.close();
            } else {
                if (loanCursor != null) loanCursor.close();
                return false;
            }

            if (paymentAmount > remainingAmount) {
                Log.w(TAG, "Сума погашення більша за залишок кредиту: " + paymentAmount + " > " + remainingAmount);
                return false;
            }

            // Оновлюємо баланс користувача
            double newBalance = currentBalance - paymentAmount;
            ContentValues userValues = new ContentValues();
            userValues.put(COLUMN_BALANCE, newBalance);
            int userRowsAffected = db.update(TABLE_USERS, userValues, COLUMN_LOGIN + "=?", new String[]{login.toLowerCase().trim()});
            if (userRowsAffected <= 0) {
                Log.w(TAG, "Помилка оновлення балансу користувача: " + login);
                db.endTransaction();
                return false;
            }

            // Оновлюємо залишок кредиту
            double newRemainingAmount = remainingAmount - paymentAmount;
            ContentValues loanValues = new ContentValues();
            loanValues.put(COLUMN_REMAINING_AMOUNT, newRemainingAmount);
            int loanRowsAffected = db.update(TABLE_LOANS, loanValues, COLUMN_LOAN_ID + "=?", new String[]{String.valueOf(loanId)});
            if (loanRowsAffected <= 0) {
                Log.w(TAG, "Помилка оновлення залишку кредиту: loanId=" + loanId);
                db.endTransaction();
                return false;
            }

            // 5. Фіксуємо транзакцію погашення
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
            String dateTime = sdf.format(new Date());
            ContentValues transactionValues = new ContentValues();
            transactionValues.put(COLUMN_SENDER_CARD, cardNumber);
            transactionValues.put(COLUMN_RECEIVER_CARD, "LOAN_PAYMENT");
            transactionValues.put(COLUMN_AMOUNT, paymentAmount);
            transactionValues.put(COLUMN_DATE_TIME, dateTime);
            transactionValues.put(COLUMN_SUCCESS, 1);
            db.insert(TABLE_TRANSACTIONS, null, transactionValues);

            // 6. Якщо залишок = 0, видаляємо кредит
            if (newRemainingAmount == 0) {
                db.delete(TABLE_LOANS, COLUMN_LOAN_ID + "=?", new String[]{String.valueOf(loanId)});
                Log.d(TAG, "Кредит повністю погашено та видалено: loanId=" + loanId);
            }

            db.setTransactionSuccessful();
            Log.d(TAG, "Погашення кредиту виконано: login=" + login + ", loanId=" + loanId + ", сума: " + paymentAmount);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Помилка погашення кредиту: " + e.getMessage());
            return false;
        } finally {
            db.endTransaction();
        }
    }
}