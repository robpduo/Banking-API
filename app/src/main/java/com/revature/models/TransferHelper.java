package com.revature.models;

public class TransferHelper {
    public double amount;   //amount to exchange
    public int recipient; //account to transfer to
    public int sender;  //account to withdraw from
    public int delete;  //account to be deleted
    public int approve; //account to be approved

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