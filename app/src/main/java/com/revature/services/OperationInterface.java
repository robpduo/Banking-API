package com.revature.services;
import com.revature.models.Account;

import java.sql.SQLClientInfoException;
import java.sql.SQLData;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.List;

/*generates transaction (Transaction Models) data automatically for user in withdraw, transfer, and deposit*/
public interface OperationInterface {
    /*Verify that the user is trying to withdraw from their own account*/
    public boolean verifyAccountWithSession (double accountId, String email);

    public String listTransactions();

    /*updates balances in an account*/
    public double deposit(double depositAmt, int accountId) throws SQLException;

    /*delete an entry*/
    public double withdraw(double withdrawAmt, int accountId) throws SQLException, RuntimeException;

    /*creates an account*/
    public void createAccount(Account nAccount);

    public double transfer(double trnsAmt, int sender, int recipient) throws SQLException;

    public void updateTransaction(double amt, int sender, int recipient, String type) throws SQLException;

    public boolean checkId (int accountId, int columnIndex, String table);

    public double getBalance(int accountId);
    /*login the user*/
    public String loginUser(String email, String password) throws SQLException, SQLDataException;

    public boolean deleteAccount(int accountId) throws SQLException;

    /*Returns true if there is a manager account associated with the email*/
    public boolean verifyManagerStatus (String email) throws SQLException;

    public String displayAll (String request) throws SQLException;

    public void approveAccount(int accountId) throws SQLDataException;
}
