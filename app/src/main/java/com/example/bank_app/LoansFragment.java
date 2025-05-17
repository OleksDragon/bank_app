package com.example.bank_app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class LoansFragment extends Fragment {

    private static final String ARG_LOGIN = "login";
    private DatabaseHelper dbHelper;
    private String login;

    public static LoansFragment newInstance(String login) {
        LoansFragment fragment = new LoansFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOGIN, login);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loans, container, false);

        dbHelper = new DatabaseHelper(requireContext());
        login = getArguments() != null ? getArguments().getString(ARG_LOGIN) : "user";

        displayExistingLoans(view);
        setupCalculator(view);
        Button takeLoanButton = view.findViewById(R.id.button_take_loan);
        takeLoanButton.setOnClickListener(v -> showTakeLoanDialog());

        return view;
    }

    private void displayExistingLoans(View view) {
        LinearLayout loansContainer = view.findViewById(R.id.loans_container);
        loansContainer.removeAllViews();

        User user = dbHelper.getUser(login);
        if (user != null) {
            List<Loan> loans = dbHelper.getLoansByCardNumber(user.getCardNumber());
            if (loans.isEmpty()) {
                TextView noLoansText = new TextView(requireContext());
                noLoansText.setText("Немає діючих кредитів");
                loansContainer.addView(noLoansText);
            } else {
                for (Loan loan : loans) {
                    LinearLayout loanLayout = new LinearLayout(requireContext());
                    loanLayout.setOrientation(LinearLayout.VERTICAL);
                    loanLayout.setPadding(0, 8, 0, 8);

                    TextView loanText = new TextView(requireContext());
                    loanText.setText(String.format("Сума: %.2f UAH,\nТермін: %d місяців,\nЗалишок: %.2f UAH,\nДата: %s",
                            loan.getLoanAmount(), loan.getLoanTerm(), loan.getRemainingAmount(), loan.getIssueDate()));

                    Button payButton = new Button(requireContext());
                    payButton.setText("Погасити кредит");
                    payButton.setOnClickListener(v -> showPayLoanDialog(loan.getLoanId(), loan.getRemainingAmount()));

                    loanLayout.addView(loanText);
                    loanLayout.addView(payButton);
                    loansContainer.addView(loanLayout);
                }
            }
        }
    }

    private void setupCalculator(View view) {
        EditText editLoanAmount = view.findViewById(R.id.edit_loan_amount);
        EditText editLoanTerm = view.findViewById(R.id.edit_loan_term);
        EditText editInterestRate = view.findViewById(R.id.edit_interest_rate);
        Button calculateButton = view.findViewById(R.id.button_calculate);
        TextView monthlyPaymentText = view.findViewById(R.id.text_monthly_payment);

        calculateButton.setOnClickListener(v -> calculateMonthlyPayment(editLoanAmount, editLoanTerm, editInterestRate, monthlyPaymentText));
    }

    private void calculateMonthlyPayment(EditText amountEdit, EditText termEdit, EditText rateEdit, TextView resultText) {
        String amountStr = amountEdit.getText().toString().trim();
        String termStr = termEdit.getText().toString().trim();
        String rateStr = rateEdit.getText().toString().trim();

        if (amountStr.isEmpty() || termStr.isEmpty() || rateStr.isEmpty()) {
            resultText.setText("Введіть усі дані");
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            int term = Integer.parseInt(termStr);
            double rate = Double.parseDouble(rateStr) / 100 / 12;

            if (amount <= 0 || term <= 0 || rate < 0) {
                resultText.setText("Невірні дані");
                return;
            }

            double monthlyPayment = (amount * rate * Math.pow(1 + rate, term)) / (Math.pow(1 + rate, term) - 1);
            DecimalFormat df = new DecimalFormat("#.##");
            resultText.setText("Щомісячний платіж: " + df.format(monthlyPayment) + " UAH");
        } catch (NumberFormatException e) {
            resultText.setText("Невірний формат даних");
        }
    }

    private void showTakeLoanDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_loans, null);
        builder.setView(dialogView);

        EditText editLoanAmount = dialogView.findViewById(R.id.edit_loan_amount);
        EditText editLoanTerm = dialogView.findViewById(R.id.edit_loan_term);
        Button cancelButton = dialogView.findViewById(R.id.button_cancel);
        Button confirmButton = dialogView.findViewById(R.id.button_confirm);

        AlertDialog dialog = builder.create();

        cancelButton.setOnClickListener(v -> dialog.dismiss());
        confirmButton.setOnClickListener(v -> {
            String amountStr = editLoanAmount.getText().toString().trim();
            String termStr = editLoanTerm.getText().toString().trim();

            if (amountStr.isEmpty() || termStr.isEmpty()) {
                Toast.makeText(requireContext(), "Введіть суму та термін", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double amount = Double.parseDouble(amountStr);
                int term = Integer.parseInt(termStr);

                if (amount <= 0 || term <= 0) {
                    Toast.makeText(requireContext(), "Сума та термін повинні бути додатними", Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = dbHelper.getUser(login);
                if (user != null) {
                    if (dbHelper.rechargeBalance(login, amount) && dbHelper.addLoan(user.getCardNumber(), amount, term)) {
                        Toast.makeText(requireContext(), "Кредит на " + amount + " UAH видано на " + term + " місяців", Toast.LENGTH_SHORT).show();
                        displayExistingLoans(getView());
                    } else {
                        Toast.makeText(requireContext(), "Помилка видачі кредиту", Toast.LENGTH_SHORT).show();
                    }
                }
                dialog.dismiss();
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Невірний формат даних", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void showPayLoanDialog(int loanId, double remainingAmount) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_loan_payment, null);
        builder.setView(dialogView);

        Spinner cardSpinner = dialogView.findViewById(R.id.spinner_card);
        EditText editPaymentAmount = dialogView.findViewById(R.id.edit_payment_amount);
        Button cancelButton = dialogView.findViewById(R.id.button_cancel);
        Button confirmButton = dialogView.findViewById(R.id.button_confirm);

        // Налаштування Spinner із списком карток
        List<String> cards = new ArrayList<>();
        User user = dbHelper.getUser(login);
        if (user != null) {
            cards.add(user.getCardNumber());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, cards);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cardSpinner.setAdapter(adapter);

        AlertDialog dialog = builder.create();

        cancelButton.setOnClickListener(v -> dialog.dismiss());
        confirmButton.setOnClickListener(v -> {
            String paymentAmountStr = editPaymentAmount.getText().toString().trim();

            if (paymentAmountStr.isEmpty()) {
                Toast.makeText(requireContext(), "Введіть суму погашення", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double paymentAmount = Double.parseDouble(paymentAmountStr);

                if (paymentAmount <= 0) {
                    Toast.makeText(requireContext(), "Сума погашення повинна бути додатною", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (paymentAmount > remainingAmount) {
                    Toast.makeText(requireContext(), "Сума погашення не може перевищувати залишок: " + remainingAmount + " UAH", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dbHelper.payLoan(login, loanId, paymentAmount)) {
                    Toast.makeText(requireContext(), "Погашено " + paymentAmount + " UAH", Toast.LENGTH_SHORT).show();
                    displayExistingLoans(getView());
                } else {
                    Toast.makeText(requireContext(), "Помилка погашення кредиту", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Невірний формат суми", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}