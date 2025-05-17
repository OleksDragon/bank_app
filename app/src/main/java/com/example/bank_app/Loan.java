package com.example.bank_app;

public class Loan {
    private final int loanId;
    private final double loanAmount;
    private final int loanTerm;
    private final double remainingAmount;
    private final String issueDate;

    public Loan(int loanId, double loanAmount, int loanTerm, double remainingAmount, String issueDate) {
        this.loanId = loanId;
        this.loanAmount = loanAmount;
        this.loanTerm = loanTerm;
        this.remainingAmount = remainingAmount;
        this.issueDate = issueDate;
    }

    public int getLoanId() { return loanId; }
    public double getLoanAmount() { return loanAmount; }
    public int getLoanTerm() { return loanTerm; }
    public double getRemainingAmount() { return remainingAmount; }
    public String getIssueDate() { return issueDate; }
}
