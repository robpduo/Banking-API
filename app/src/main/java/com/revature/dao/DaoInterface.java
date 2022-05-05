package com.revature.dao;

import com.revature.models.Account;
import com.revature.models.Transactions;

import java.sql.SQLException;
import java.util.List;

public interface DaoInterface {


    /*--------------------CREATE---------------------------------*/
    //Create a new account
    public void CreateUserAccount(Account newAcc,String database) ;

    /*-----------------------READ--------------------------------*/

    /*Determine if a value exists from a given column in a given table(directory OR Transactions
    Used to set a unique account id, transfer money, delete account*/
    public boolean checkDatabase (String checkValue, int columnIndex, String table);
    public boolean checkDatabase (String email, String password);
    public boolean checkDatabase (String email, double id);

    /*Display*/
    public String displayAll (String requestType) throws SQLException;
    public String displayTransactions ();

    /*retrieve account balance given account ID*/
    public double getBalance (double accountID);

    public boolean isManager(String email) throws SQLException;


    /*----------------------------DELETE----------------------------*/
    public void deleteTransaction (int accountId);

    /*removes an account from the Db, no questions asked*/
    public void deleteAccountDb (int accountId) throws SQLException;

    /*---------------------------UPDATE----------------------------*/

    /*should only be possible if user is logged in, determines if accountID matches with the username
    of the current session*/
    public void withdrawDb (double amt, double accountId) throws SQLException;
    public void depositDb (double amt, double accountId) throws SQLException;

    /*Persist changes onto the transaction database*/
    public void updateTransactionDB (Transactions addTransaction) throws SQLException;

    public Account approveAccountDb (int accountId);
}
