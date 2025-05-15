package com.example.bank_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import java.util.List;

public class TransfersFragment extends Fragment {

    private static final String ARG_LOGIN = "login";

    public static TransfersFragment newInstance(String login) {
        TransfersFragment fragment = new TransfersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOGIN, login);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfers, container, false);
        TextView textView = view.findViewById(R.id.text_transfers);

        // Отримуємо логін із аргументів
        String login = getArguments() != null ? getArguments().getString(ARG_LOGIN) : "user";
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        User user = dbHelper.getUser(login);

        if (user != null) {
            String cardNumber = user.getCardNumber();
            List<Transaction> transactions = dbHelper.getTransactionsByCardNumber(cardNumber);

            if (transactions.isEmpty()) {
                textView.setText("Немає транзакцій для картки\n\n" + cardNumber);
            } else {
                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                for (Transaction transaction : transactions) {
                    String status = transaction.isSuccess() ? "Успішно" : "Неуспішно";
                    String text = String.format("Від: %s, \nДо: %s, \nСума: %.2f UAH, \nДата: %s, \nСтатус: %s",
                            transaction.getSenderCard(),
                            transaction.getReceiverCard(),
                            transaction.getAmount(),
                            transaction.getDateTime(),
                            status);
                    TextView transactionText = new TextView(getContext());
                    transactionText.setText(text);
                    transactionText.setTextSize(16);
                    transactionText.setPadding(0, 8, 0, 8);
                    layout.addView(transactionText);
                }

                ((ViewGroup) view).addView(layout, 1); // Додаємо після заголовка
                textView.setText("Історія транзакцій для картки\n" + cardNumber);
            }
        } else {
            textView.setText("Користувач не знайден");
        }

        return view;
    }
}