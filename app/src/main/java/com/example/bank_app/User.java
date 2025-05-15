package com.example.bank_app;

public class User {
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