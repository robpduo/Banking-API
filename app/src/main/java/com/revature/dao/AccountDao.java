package com.revature.dao;

import com.revature.models.Account;
import com.revature.models.Transactions;
import com.revature.utils.ConnectionSingleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDao implements DaoInterface {
    public ConnectionSingleton cs = ConnectionSingleton.getConnectionSingleton();

    /*open connection to database and create a new entry*/
    /*Determine which database the account is added to based on account type (Pending vs main directory)*/
    public void CreateUserAccount(Account newAcc) {
        Connection c = cs.getConnection();
        String database;

        if (newAcc.getAccountType().equals("Manager")) {
            database = "BankAccounts";
        } else {
            database = "pending";
        }
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
            /*------End of Accounts Database-------*/


            /*------End of User Database-------*/

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*Inserts a nnew entry into the transaction database*/
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

    /*Used to set a unique account id, transfer money, delete account*/
    /*Returns True if it already exists*/
    public boolean checkDatabase (String checkValue, int columnIndex, String table) {
        Connection c = cs.getConnection();

        String sql = "SELECT * FROM " + table + ";";

        try {

            Statement s = c.createStatement();
            s.execute(sql);
            ResultSet rs = s.getResultSet();

            while (rs.next()) { //Loops through all accounts in the database and compare account_id with checkvalue
                if (rs.getString(columnIndex).equals(checkValue)) {
                    return true;
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }


        return false;   //The key already exists
    }

    /*removes an account from the Db, no questions asked*/
    public void deleteAccountDb (double accountId) throws SQLException {
        Connection c = cs.getConnection();
        //If all accounts have been remove, delete the user entry in users database
        String getEmailSql = "Select email FROM BankAccounts WHERE account_id = " + accountId + ";";
        String delSql = "DROP FROM BankAccounts Where account_id = " + accountId + ";";

        try {
            Statement s = c.createStatement();
            s.execute(getEmailSql);
            ResultSet rs = s.getResultSet(); //Get the email

            String email =rs.getString(1);

            s.execute(delSql);  // delete the account

            //check if the user is still in the BankAccounts Database, if not, delete user from the account database
            if (checkDatabase(email,accountId)) {
                System.out.println("Still Exists");
            } else {
                System.out.println("GONE");
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

    /*searches through BankAccount Database for a matching email and accountID, and determine if the balance
    is equal or over amt, overloaded method, returns true if a matching pair is found and amt to be withdrawn is
    equal or greater*/
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
    }

    /*displays all the accounts in the database*/
    public String displayAll (String requestType) throws SQLException {
        StringBuilder allAccounts = new StringBuilder();

        Connection c = cs.getConnection();
        String sql = "SELECT account_id, users.email, users.name_first, users.name_last, BankAccounts.balance FROM BankAccounts FULL OUTER JOIN users ON BankAccounts.email = users.email;";

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
                    allAccounts.append("---------------------------------}\n\n");
                }
            } else {
                while (rs.next()) {
                    if (requestType.equals(rs.getString(2))) {  //User Email pull request
                        allAccounts.append(("{---------------------------------\n"));
                        allAccounts.append("\tAccount Number: " + rs.getDouble(1) + "\n");
                        allAccounts.append("\tEmail: " + rs.getString(2) + "\n");
                        allAccounts.append("\tFirst Name: " + rs.getString(3) + "\n");
                        allAccounts.append("\tLast Name: " + rs.getString(4) + " \n");
                        allAccounts.append("\tBalance: " + rs.getDouble(5) + "\n");
                        allAccounts.append("---------------------------------}\n\n");
                    }
                }
            }

        } catch (SQLException e) {
            throw new SQLException();
        }

        return allAccounts.toString();
    }
}