package com.example.bank_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class LoansFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loans, container, false);
        TextView textView = view.findViewById(R.id.text_loans);
        textView.setText("Сторінка: Кредити");
        return view;
    }
}