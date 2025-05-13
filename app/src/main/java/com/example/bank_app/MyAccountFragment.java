package com.example.bank_app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class MyAccountFragment extends Fragment {

    private static final String ARG_LOGIN = "login";

    public static MyAccountFragment newInstance(String login) {
        MyAccountFragment fragment = new MyAccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOGIN, login);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);

        // Отримуємо логін із аргументів
        String login = getArguments() != null ? getArguments().getString(ARG_LOGIN) : "user";

        // Елементи картки
        TextView cardNumber = view.findViewById(R.id.card_number);
        TextView cardBalance = view.findViewById(R.id.card_balance);

        // Отримуємо дані користувача з бази даних
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        DatabaseHelper.User user = dbHelper.getUser(login);

        if (user != null) {
            cardNumber.setText(user.getCardNumber());
            cardBalance.setText("Баланс: " + String.format("%.2f", user.getBalance()) + " UAH");
        } else {
            cardNumber.setText("Невідомий користувач");
            cardBalance.setText("Баланс: 0.00 UAH");
        }

        // Кнопки
        Button rechargeButton = view.findViewById(R.id.button_recharge);
        Button transferButton = view.findViewById(R.id.button_transfer);

        rechargeButton.setOnClickListener(v -> showRechargeDialog(login, dbHelper, cardBalance));

        transferButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Функція переказу коштів в розробці", Toast.LENGTH_SHORT).show();
        });

        // Курси валют (імітація, у реальному додатку потрібно брати з API)
        TextView exchangeRateUsd = view.findViewById(R.id.exchange_rate_usd);
        TextView exchangeRateEur = view.findViewById(R.id.exchange_rate_eur);

        // Імітація курсів валют (станом на 12 травня 2025 року, приблизні значення)
        double usdToUah = 41.50; // Приблизний курс USD/UAH
        double eurToUah = 45.20; // Приблизний курс EUR/UAH

        exchangeRateUsd.setText("USD/UAH: " + String.format("%.2f", usdToUah));
        exchangeRateEur.setText("EUR/UAH: " + String.format("%.2f", eurToUah));

        return view;
    }

    private void showRechargeDialog(String login, DatabaseHelper dbHelper, TextView cardBalance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_recharge, null);
        builder.setView(dialogView);

        EditText editAmount = dialogView.findViewById(R.id.edit_amount);
        Button cancelButton = dialogView.findViewById(R.id.button_cancel);
        Button confirmButton = dialogView.findViewById(R.id.button_confirm);

        AlertDialog dialog = builder.create();

        cancelButton.setOnClickListener(v -> dialog.dismiss());
        confirmButton.setOnClickListener(v -> {
            String amountStr = editAmount.getText().toString().trim();
            if (!amountStr.isEmpty()) {
                try {
                    double amount = Double.parseDouble(amountStr);
                    if (amount > 0) {
                        if (dbHelper.rechargeBalance(login, amount)) {
                            DatabaseHelper.User updatedUser = dbHelper.getUser(login);
                            if (updatedUser != null) {
                                cardBalance.setText("Баланс: " + String.format("%.2f", updatedUser.getBalance()) + " UAH");
                                Toast.makeText(getContext(), "Рахунок поповнено на " + amount + " UAH", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Помилка поповнення рахунку", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Сума повинна бути додатною", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Невірний формат суми", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Введіть суму", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}