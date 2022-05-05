package com.revature.services;
import com.revature.dao.AccountDao;
import com.revature.dao.DaoInterface;
import com.revature.models.Account;
import com.revature.models.Transactions;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/*Data Structure Libraries*/

/*This class is used to perform validation checks account transfers and creation, and sends
to JDBC to be persisted onto the database */
public class Operations implements OperationInterface {

    /*get the current date*/
    private Date getCurDate (Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        format.format(date);

        return date;
    }

    public double getBalance(int accountId) {
        DaoInterface di = new AccountDao();
        return di.getBalance(accountId);
    }

    /*Withdraws trnsAmt from Sender to Deposits it in recipient*/
    public double transfer(double trnsAmt, int sender, int recipient) throws SQLException {
        DaoInterface di = new AccountDao();

        try {
            di.withdrawDb(trnsAmt, sender);
            di.depositDb(trnsAmt, recipient);
        } catch (SQLException e) {
            throw new SQLException();
        }

        return di.getBalance(sender);
    }

    public void updateTransaction(double amt, int sender, int recipient, String type) throws SQLException {
        DaoInterface di = new AccountDao();
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date curDate = new java.sql.Date(utilDate.getTime());

        //Update Transaction
        try {
            //new transaction entry: nTransaction
            System.out.println("x2");
            Transactions nTransaction = new Transactions(randNumGen(1, "transactions"), type, recipient, sender, amt, curDate);
            System.out.println("x3");
            di.updateTransactionDB(nTransaction);

        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    /*validates that the amount is valid >= 0 and adds it to the user account's balance
    * returns the final balance after the deposit*/
    public double deposit(double depositAmt, int accountId) throws SQLException {
        DaoInterface di = new AccountDao();

        try {
            di.depositDb(depositAmt, accountId);
            return di.getBalance(accountId);

        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    public boolean deleteAccount(int accountId) throws SQLException, RuntimeException {
        DaoInterface di = new AccountDao();

        try {
            di.deleteTransaction(accountId);    //Remove foreign key constraints
            di.deleteAccountDb(accountId);

        } catch (SQLException e) {
            throw new SQLException();

        } catch (RuntimeException e) {
            throw new RuntimeException();

        }
        

        return false;
    }

    /*Determine if the accountId exists in a given database(table) of a given column (columnIndex)*/
    public boolean checkId (int accountId, int columnIndex, String table) {
        DaoInterface di = new AccountDao();
        return di.checkDatabase(""+accountId, columnIndex, table);
    }

    public double withdraw(double withdrawAmt, int accountId) throws SQLException {
        DaoInterface di = new AccountDao();

        try {
            di.withdrawDb(withdrawAmt, accountId);
            return di.getBalance(accountId);
        } catch (SQLException e) {
            throw new SQLException();   //pass the exception to the controller to update context result
        }

    }

    /*generate a random integer and compares it with column number and table*/
    private int randNumGen(int column, String table) {
        DaoInterface di = new AccountDao();
        Random randomGen = new Random();
        int rand;
        System.out.println("x4");
        do {
            rand = ThreadLocalRandom.current().nextInt(100000, 999999);
        } while (di.checkDatabase("" + rand, column, table));

        return rand;
    }

    /*Verify that the user is trying to withdraw from their own account*/
    public boolean verifyAccountWithSession (double accountId, String email) {
        DaoInterface di = new AccountDao ();

        //Account exists -> match account ID with email
        return di.checkDatabase(email, accountId);
    }

    public boolean verifyManagerStatus (String email) throws SQLException {
        DaoInterface di = new AccountDao();

        try {
            return di.isManager(email);
        } catch (SQLException e) {
            throw new SQLException();
        }

    }

    public String displayAll (String request) throws SQLException {
        DaoInterface di = new AccountDao();

        try {
            return di.displayAll(request);
        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    public String listTransactions () {
        DaoInterface di = new AccountDao();

            return di.displayTransactions();
    }

    /*generates a random 6 digit unique number and set it as the account number*/
    public void createAccount (Account nAccount) {
        nAccount.setAccountId( randNumGen(1, "BankAccounts") );
        DaoInterface di = new AccountDao();

        /*Determine which database (Pending or BankAccounts) to create the new entry in*/
        if(nAccount.getAccountType().equals("Manager")) {
            di.CreateUserAccount(nAccount, "BankAccounts");
        } else {
            di.CreateUserAccount(nAccount, "pending");
        }
    }

    public String loginUser(String email, String password) throws SQLException, SQLDataException {
        DaoInterface di = new AccountDao();

        try {
            if (di.checkDatabase(email, password)) {
                return di.displayAll(email);
            } else {
                throw new SQLDataException();
            }
        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    public void approveAccount(int accountId) throws SQLDataException{

        DaoInterface di = new AccountDao();

        Account crAccount = di.approveAccountDb(accountId);

        if (crAccount.getEmail() != null) {
            di.CreateUserAccount(crAccount, "BankAccounts"); //Create an entry for BankAccounts Database
        } else {
            throw new SQLDataException();
        }

    }


}
