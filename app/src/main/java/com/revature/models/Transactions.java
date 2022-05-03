package com.revature.models;

import java.sql.Date;

public class Transactions {
    private int transactionId;
    private String transactionType = new String();
    private int recipientId;
    private int accountHolder;
    private double amount;
    private Date curDate;

    public Transactions() {
    }

    public Transactions(int transactionId, String transactionType, int recipientId, int accountHolder, double amount, Date curDate) {
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.recipientId = recipientId;
        this.accountHolder = accountHolder;
        this.amount = amount;
        this.curDate = curDate;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }

    public int getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(int accountHolder) {
        this.accountHolder = accountHolder;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getCurDate() {
        return curDate;
    }

    public void setCurDate(Date curDate) {
        this.curDate = curDate;
    }
}
