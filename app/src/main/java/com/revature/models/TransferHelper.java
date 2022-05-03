package com.revature.models;

public class TransferHelper {
    public double amount;
    public int recipient; //account holder is the recipient in a withdrawal
    public int sender;
    public int delete;

    public TransferHelper() {
    }

    public TransferHelper(double amount, int recipient, int sender) {
        this.amount = amount;
        this.recipient = recipient;
        this.sender = sender;
    }

    @Override
    public String toString() {
        return "TransferHelper{" +
                "amount=" + amount +
                ", recipient=" + recipient +
                ", sender=" + sender +
                ", delete=" + delete +
                '}';
    }
}