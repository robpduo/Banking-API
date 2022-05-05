package com.revature.dao;

import com.revature.models.Account;
import com.revature.models.Transactions;
import com.revature.utils.ConnectionSingleton;
import com.sun.corba.se.pept.transport.ConnectionCache;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDao implements DaoInterface {
    public ConnectionSingleton cs = ConnectionSingleton.getConnectionSingleton();

    /*open connection to database and create a new entry*/
    /*Determine which database the account is added to based on account type (Pending vs main directory)*/
    public void CreateUserAccount(Account newAcc,String database) {
        Connection c = cs.getConnection();

        String sql = "INSERT INTO " + database + " (account_id, email, password, account_type, balance) Values (?, ?, ?, ?, 0);";
        String sqlUsers = "INSERT INTO users (email, name_first, name_last, address, social_number, phone_number) Values (?, ?, ?, ?, ?, ?);";

        try {
            /* Only 1 entry of users into this database*/
            if (!checkDatabase(newAcc.getEmail(), 1, "users")) {
                PreparedStatement psu = c.prepareStatement(sqlUsers);
                psu.setString(1, newAcc.getEmail());
                psu.setString(2, newAcc.getFirstName());
                psu.setString(3, newAcc.getLastName());
                psu.setString(4, newAcc.getAddress());
                psu.setLong(5, newAcc.getSocialNum());
                psu.setLong(6, newAcc.getPhoneNum());
                psu.executeUpdate();
            }
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setDouble(1, newAcc.getAccountId());
            ps.setString(2, newAcc.getEmail());
            ps.setString(3, newAcc.getPassword());
            ps.setString(4, newAcc.getAccountType());
            ps.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*Inserts a new entry into the transaction database*/
    public void updateTransactionDB (Transactions addTransaction) throws SQLException {
        Connection c = cs.getConnection();
        String sql = "INSERT INTO transactions (account_id, transaction_id, transaction_type, recipient_id, transac_amt, cur_date) " +
                "VALUES (?, ?, ?, ?, ?, ?);";

        try {
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setInt(1, addTransaction.getAccountHolder());
            ps.setInt(2, addTransaction.getTransactionId());
            ps.setString(3, addTransaction.getTransactionType());
            ps.setInt(4, addTransaction.getRecipientId());
            ps.setDouble(5, addTransaction.getAmount());
            ps.setDate(6, addTransaction.getCurDate());
            ps.execute();

        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    /*Used by another method to set a unique account id, transfer money, delete account*/
    /*Returns True if it already exists*/
    public boolean checkDatabase (String checkValue, int columnIndex, String table) {
        Connection c = cs.getConnection();

        String sql = "SELECT * FROM " + table + ";";

        try {

            Statement s = c.createStatement();
            s.execute(sql);
            ResultSet rs = s.getResultSet();

            while (rs.next()) { //Loops through all accounts in the database and compare account_id with checkvalue
                if (rs.getString(columnIndex) == null && table.equals("transactions")) { //null value found on transaction ID
                    if(rs.getString(7).equals(checkValue)) {
                        return true;    //same accountId found but it is inactive
                    }
                } else if (rs.getString(columnIndex).equals(checkValue)) {
                    return true;    //same active accountId found
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }


        return false;   //The key already exists
    }

    /*removes an account from the Db, no questions asked*/
    public void deleteAccountDb (int accountId) throws SQLException {
        Connection c = cs.getConnection();
        //If all accounts have been remove, delete the user entry in users database
        String getEmailSql = "Select email FROM BankAccounts WHERE account_id = " + accountId + ";";
        String delSql = "DELETE FROM BankAccounts Where account_id = " + accountId + ";";
        try {
            Statement s = c.createStatement();
            s.execute(getEmailSql);
            ResultSet rs = s.getResultSet(); //Get the email

            rs.next();
            String email = rs.getString(1);

            Statement sDelete = c.createStatement();
            sDelete.execute(delSql); // delete the account

            //check if the user is still in the BankAccounts Database, if not, delete user from the account database
            if (checkDatabase(email, 2, "BankAccounts") == false) {
                s.execute("DELETE FROM users WHERE email = '" + email + "'");
            }

        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    /*searches through BankAccount Database for a matching email and password, overloaded method
    returns true if a matching pair is found*/
    public boolean checkDatabase (String email, String password) {
        Connection c = cs.getConnection();
        String sql = "SELECT * FROM BankAccounts;";

        try {

            Statement s = c.createStatement();
            s.execute(sql);
            ResultSet rs = s.getResultSet();

            while (rs.next()) { //Loops through all accounts in the database and compare account_id with checkvalue
                if (rs.getString(2).equals(email) && rs.getString(3).equals(password)) {
                    return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /*searches through BankAccount Database for a matching email and accountID, and determine if they
     exist. overloaded method, returns true if a matching pair is found*/
    public boolean checkDatabase (String email, double id) {
        Connection c = cs.getConnection();
        String sql = "SELECT * FROM BankAccounts;";

        try {

            Statement s = c.createStatement();
            s.execute(sql);
            ResultSet rs = s.getResultSet();

            while (rs.next()) { //Loops through all accounts in the database and compare account_id with checkvalue
                if (rs.getString(2).equals(email) && (rs.getInt(1) == id)) {
                    return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void deleteTransaction (int accountId) {
        Connection c = cs.getConnection();
        String nullTransSql = "update transactions set account_id = null, inactive = " + accountId + " where account_id = " + accountId + ";";

        try {
            Statement s = c.createStatement();
            s.execute(nullTransSql);
        } catch (SQLException e) {
            // do nothing with this exception -- this method is just to remove foreign key constraints if there are any
        }

    }
    /*return the account balance of a specified account ID*/
    public double getBalance (double accountID) {
        Connection c = cs.getConnection();
        String sql = "SELECT * FROM BankAccounts;";

        try {
            Statement s = c.createStatement();
            s.execute(sql);
            ResultSet rs = s.getResultSet();

            while(rs.next()) {
                if (rs.getDouble(1) == accountID) {
                    return rs.getDouble(5);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void depositDb (double amt, double accountId) throws SQLException {
        double accountBalance = getBalance(accountId);
        accountBalance += amt;

        String sql = "UPDATE BankAccounts SET balance = " + accountBalance + " WHERE account_id = " + accountId + ";";

        if (amt >= 0) {
            Connection c = cs.getConnection();
            Statement s = c.createStatement();
            s.execute(sql);

        } else {
            throw new SQLException();
        }
    }

    /*is the email is associated with a Manager's account, return true*/
    public boolean isManager(String email) throws SQLException {
        Connection c = cs.getConnection();
        String sql = "Select * From BankAccounts;";

        Statement s = c.createStatement();
        s.execute(sql);

        ResultSet rs = s.getResultSet();

        while(rs.next()) {
            if(rs.getString(2).equals(email) && rs.getString(4).equals("Manager")) {
                return true;
            }
        }
        return false;
    }

    /*Updates the database by subtracting the balance with a valid amount
    (this method does not check if it is valid)*/
    public void withdrawDb (double amt, double accountId) throws SQLException {
        /*Retrieve account balance*/
        double accountBalance = 0;
        Connection c = cs.getConnection();
        Connection cWithdraw = cs.getConnection();
        String sql = "SELECT * FROM BankAccounts;";


        accountBalance = getBalance(accountId);

        try {
            if (accountBalance >= amt && amt > 0) {
                accountBalance -= amt;  //new account balance
                String sqlUpdate = "UPDATE BankAccounts SET balance = " + accountBalance + " WHERE account_id = " + accountId + ";";

                Statement s = c.createStatement();
                s.execute(sql);

                ResultSet rs = s.getResultSet();
                while (rs.next()) {
                    if (rs.getDouble(1) == accountId) {
                        Statement s2 = c.createStatement();
                        s2.execute(sqlUpdate);
                    }
                }

            } else {
                throw new SQLException();
            }
        } catch (SQLException e ) {
            throw new SQLException();
        }
    }

    public String displayTransactions () {
        StringBuilder allTransactions = new StringBuilder();

        Connection c = cs.getConnection();
        String sqlTrans = "SELECT * FROM transactions;";

        try {
            Statement s = c.createStatement();
            s.execute(sqlTrans);
            ResultSet rs = s.getResultSet();

            while(rs.next()) {
                allTransactions.append(("{---------------------------------\n"));

                if (rs.getInt(1) < 1) {
                    allTransactions.append(("Inactive: " + rs.getInt(7)) + "\n");
                } else {
                    allTransactions.append("Account Id: " + rs.getInt(1) + "\n");
                }

                allTransactions.append("Transaction Type: " + rs.getString(3) + "\n");
                allTransactions.append("Recipient ID: " + rs.getInt(4) + "\n");
                allTransactions.append("Transaction AMT: " + rs.getDouble(5) + "\n");
                allTransactions.append("Transaction Date: " + rs.getDate(6) + "\n");
                allTransactions.append("---------------------------------}\n\n");


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allTransactions.toString();
    }
    /*displays all the accounts in the database*/
    public String displayAll (String requestType) throws SQLException {
        StringBuilder allAccounts = new StringBuilder();

        Connection c = cs.getConnection();
        String sql = "SELECT account_id, users.email, users.name_first, users.name_last, BankAccounts.balance, account_type FROM BankAccounts INNER JOIN users ON BankAccounts.email = users.email;";

        try {
            Statement s = c.createStatement();
            s.execute(sql);
            ResultSet rs = s.getResultSet();

            /*If the request type is from a manager then display all accounts in the directory, if the request type is
            a users email, then display only the user's accounts
            */

            if (requestType.equals("Manager")) {    //Manager's Pull Request
                while (rs.next()) {
                    allAccounts.append(("{---------------------------------\n"));
                    allAccounts.append("\tAccount Number: " + rs.getDouble(1) + "\n");
                    allAccounts.append("\tEmail: " + rs.getString(2) + "\n");
                    allAccounts.append("\tFirst Name: " + rs.getString(3) + "\n");
                    allAccounts.append("\tLast Name: " + rs.getString(4) + " \n");
                    allAccounts.append("\tBalance: " + rs.getDouble(5) + "\n");
                    allAccounts.append("\tType: " + rs.getString(6) + "\n");
                    allAccounts.append("---------------------------------}\n\n");
                }
            } else if (requestType.equals("pending")) {
                Statement sp = c.createStatement();
                sp.execute("SELECT account_id, users.email, users.name_first, users.name_last, pending.balance, account_type FROM pending INNER JOIN users ON pending.email = users.email;");
                ResultSet rsp = sp.getResultSet();

                while (rsp.next()) {
                    allAccounts.append(("{---------------------------------\n"));
                    allAccounts.append("PENDING\n");
                    allAccounts.append("\tAccount Number: " + rsp.getDouble(1) + "\n");
                    allAccounts.append("\tEmail: " + rsp.getString(2) + "\n");
                    allAccounts.append("\tFirst Name: " + rsp.getString(3) + "\n");
                    allAccounts.append("\tLast Name: " + rsp.getString(4) + " \n");
                    allAccounts.append("\tBalance: " + rsp.getDouble(5) + "\n");
                    allAccounts.append("\tType: " + rsp.getString(6) + "\n");
                    allAccounts.append("---------------------------------}\n\n");
                }
            }else {
                while (rs.next()) {
                    if (requestType.equals(rs.getString(2))) {  //User Email pull request
                        allAccounts.append(("{---------------------------------\n"));
                        allAccounts.append("\tAccount Number: " + rs.getDouble(1) + "\n");
                        allAccounts.append("\tEmail: " + rs.getString(2) + "\n");
                        allAccounts.append("\tFirst Name: " + rs.getString(3) + "\n");
                        allAccounts.append("\tLast Name: " + rs.getString(4) + " \n");
                        allAccounts.append("\tBalance: " + rs.getDouble(5) + "\n");
                        allAccounts.append("\tType: " + rs.getString(6) + "\n");
                        allAccounts.append("---------------------------------}\n\n");
                    }
                }
            }

        } catch (SQLException e) {
            throw new SQLException();
        }

        return allAccounts.toString();
    }

    public Account approveAccountDb (int accountId) {
        Account pendingAcc = new Account();

        Connection c = cs.getConnection();
        Connection c2 = cs.getConnection();

        String penSql = "SELECT * from pending INNER JOIN users on pending.email = users.email;";
        String delSql = "DELETE FROM pending WHERE account_id = " + accountId + ";";

        try {
            Statement s = c.createStatement();
            s.execute(penSql);
            ResultSet rs = s.getResultSet();

            while(rs.next()) {

                if ( rs.getInt(1) == accountId ) {
                    pendingAcc.setAccountId(rs.getInt(1));
                    pendingAcc.setEmail(rs.getString(2));
                    pendingAcc.setPassword(rs.getString(3));
                    pendingAcc.setAccountType(rs.getString(4));
                    pendingAcc.setAccountBalance(rs.getDouble(5));
                    pendingAcc.setFirstName(rs.getString(7));
                    pendingAcc.setLastName(rs.getString(8));
                    pendingAcc.setAddress(rs.getString(9));
                    pendingAcc.setSocialNum(rs.getLong(10));
                    pendingAcc.setPhoneNum((rs.getLong(11)));


                    Statement sDelPend = c2.createStatement();
                    sDelPend.execute(delSql);  //After Retrieval, Remove pending account entry in the pending database
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (pendingAcc);
    }
}
