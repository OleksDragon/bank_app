package com.example.bank_app;

public class Transaction {
    private final int transId;
    private final String senderCard;
    private final String receiverCard;
    private final double amount;
    private final String dateTime;
    private final boolean success;

    public Transaction(int transId, String senderCard, String receiverCard, double amount, String dateTime, boolean success) {
        this.transId = transId;
        this.senderCard = senderCard;
        this.receiverCard = receiverCard;
        this.amount = amount;
        this.dateTime = dateTime;
        this.success = success;
    }

    public int getTransId() {
        return transId;
    }

    public String getSenderCard() {
        return senderCard;
    }

    public String getReceiverCard() {
        return receiverCard;
    }

    public double getAmount() {
        return amount;
    }

    public String getDateTime() {
        return dateTime;
    }

    public boolean isSuccess() {
        return success;
    }
}