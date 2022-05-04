package com.revature.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.services.OperationInterface;
import com.revature.models.Account;
import com.revature.models.CreateAccountHelper;
import com.revature.models.LoginHelper;
import com.revature.models.TransferHelper;
import io.javalin.http.Handler;

import java.sql.SQLClientInfoException;
import java.sql.SQLData;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*reads and decides what to do with packets sent from Javalin*/
public class AccountController {
    private OperationInterface opi;
    private ObjectMapper om;

    public AccountController (OperationInterface opi) {
        this.opi = opi;
        this.om = new ObjectMapper();
    }

    /*user to create a new account in the Database, performs logic related to accountType*/
    public Handler handleCreator = (ctx) -> {
        CreateAccountHelper ac = om.readValue(ctx.body(), CreateAccountHelper.class);

        //insert logic to set accountType
        //if authorization code matches 777666555, then accountType is changed to manager
        if(ac.authorization == 777666555) {
            ac.accountType = "Manager";
            ac.isApproved = true;   //Managers accounts are automatically approved
        } else {
            ac.accountType = "Client";
        }

        Account createAcc = new Account(ac.email, ac.firstName, ac.lastName, ac.address, ac.socialNum,
                ac.phoneNum, ac.accountType, ac.password, 0, ac.isApproved);

        opi.createAccount(createAcc);
        ctx.status(201);

        /*Approved via  authorization code*/
        if (ac.isApproved) {
            ctx.result("Account is Approved via Authorization Code, and Created");
        } else {
            ctx.result("Account is Pending Approval");
        }
    };

    public Handler loginHandler = ctx -> {
        LoginHelper lh = om.readValue(ctx.body(), LoginHelper.class);

        try {
            String allUserAccounts = opi.loginUser(lh.email, lh.password);

            /*Welcome message for the account holder*/
            ctx.result("Welcome Back, Here are your open accounts: \n" + allUserAccounts);
            ctx.status(200);

            //Setup a session where they will have access to other functions
            ctx.req.getSession().setAttribute("LoggedIn", lh.email);

        } catch (SQLException e) {
            ctx.result("Username or Password is incorrect");
            ctx.status(403);
        }

    };

    public Handler deleteAccountsHandler = ctx -> {
        /*is the current user that is logged in a manager?*/
        try {
            if (opi.verifyManagerStatus(ctx.req.getSession().getAttribute("LoggedIn").toString())) {

                TransferHelper userAccount = om.readValue(ctx.body(), TransferHelper.class);

                opi.deleteAccount(userAccount.delete);

                ctx.result("Account and User Have been Deleted");
                ctx.status(200);

            } else {
                ctx.result("Please Login to a Managers Account to Access this Function");
                ctx.status(200);

            }
        } catch (NullPointerException e) {
            ctx.result("Please Log in to use this function");
            ctx.status(401);
        } catch (SQLException e) {
            ctx.result("Account is no longer in the system");
            ctx.status(200);
        }
    };

    public Handler depositHandler = ctx -> {
        TransferHelper th = om.readValue(ctx.body(), TransferHelper.class);
        double nBalance = 0;

        try {
            //Check to determine if there is a current session
            ctx.req.getSession().getAttribute("LoggedIn");  //Possible Refactor to allow multiple sessions to be created

            //check if account belongs to the current session owner
            if (opi.verifyAccountWithSession(th.recipient, ctx.req.getSession().getAttribute("LoggedIn").toString())) {
                //deposit
                nBalance = opi.deposit(th.amount, th.recipient);

                ctx.status(200);
                ctx.result("Deposit Successful, New Balance: $" + nBalance);
                opi.updateTransaction(th.amount, th.recipient, th.recipient, "Deposit");
            } else {
                ctx.status(401);
                ctx.result("Error Verifying Account ID with Email, unable to Deposit");
            }

        } catch (NullPointerException e) {
            ctx.status(401);
            ctx.result("Please Log in to perform this function, Deposit Cancelled");

        } catch (RuntimeException e) {
            ctx.status(404);
            ctx.result("Unable to Retrieve Account Info, Check Account Entry");
        } catch (SQLException e) {
            ctx.status(406);
            ctx.result("Invalid Entry, Please Enter an Amount > $0");
        }
    };

    public Handler withdrawHandler = ctx -> {
        TransferHelper th = om.readValue(ctx.body(), TransferHelper.class);
        double nBalance = 0;

        try {
            //Check to determine if there is a current session
            ctx.req.getSession().getAttribute("LoggedIn");

            /*check if account is registered to the user*/
            if (opi.verifyAccountWithSession(th.recipient, ctx.req.getSession().getAttribute("LoggedIn").toString())) {
                /*withdraw*/
                nBalance = opi.withdraw(th.amount, th.recipient);

                ctx.status(201);
                ctx.result("Withdraw Successful, New Balance: $" + nBalance);
                opi.updateTransaction(th.amount, th.recipient, th.recipient, "Withdraw");

            } else {
                ctx.status(401);
                ctx.result("Error Verifying Account ID with Email, unable to withdraw");
            }

        } catch (NullPointerException e) {
            ctx.status(401);
            ctx.result("Please Log in to perform this function, Withdrawal Cancelled");

        } catch (SQLException e){
            ctx.status(402);
            ctx.result("Insufficient Funds OR Invalid Withdrawal Amount");

        } catch (RuntimeException e) {
            ctx.status(404);
            ctx.result("Connection Error");
        }
    };

    public Handler logoutHandler = ctx -> {

        try {
            ctx.req.getSession().removeAttribute("LoggedIn");
            ctx.result("You have logged out, Thank you for banking with Revature Training Co.");
            ctx.status(200);

        } catch (NullPointerException e) {
            ctx.result("Logged out incomplete");
            ctx.status(404);
        }
    };

    public Handler transferHandler = ctx -> {
        TransferHelper th = om.readValue(ctx.body(), TransferHelper.class);
        double nBalance = 0;

        try {
            //verify that the account sending money is the account that is logged in*/
            if (opi.verifyAccountWithSession(th.sender, ctx.req.getSession().getAttribute("LoggedIn").toString())) {
                //verify that the recipient account exists in the Database
                if (opi.checkId(th.recipient, 1, "BankAccounts")) {
                    ctx.req.getSession().getAttribute("LoggedIn");
                    nBalance = opi.transfer(th.amount, th.sender, th.recipient);

                    ctx.result("Transfer Successful, New Balance for "+ th.sender + " is: " + nBalance);
                    opi.updateTransaction(th.amount, th.sender, th.recipient, "Transfer");
                    ctx.status(200);

                } else {
                    ctx.result("Recipient Account Not Found");
                    ctx.status(404);
                }
            } else {
                ctx.result("Sender Account Not Found in Current Session");
                ctx.status(404);
            }

        } catch (NullPointerException e) {
            ctx.result("Please Log onto Your Account To Access This Feature");
            ctx.status(401);

        } catch (RuntimeException e) {
            ctx.result("Error Connecting to Transaction Database");
            ctx.status(404);

        } catch (SQLException e) {
            nBalance = opi.getBalance(th.sender);
            ctx.result("Insufficient Funds In Account Number: " + th.sender + ", Balance: " + nBalance);
            ctx.status(406);
        }
    };

    public Handler approveHandler = ctx -> {
        TransferHelper th = om.readValue(ctx.body(), TransferHelper.class);

        try {
            ctx.req.getSession().getAttribute("LoggedIn"); // Test to see if you're logged in

            if (opi.verifyManagerStatus(ctx.req.getSession().getAttribute("LoggedIn").toString())) {

                opi.approveAccount(th.approve);


                ctx.result(th.approve + " Has Been Approved");
                ctx.status(200);
            } else {
                ctx.result("This Account Cannot Does Not Have Access To This Function");
                ctx.status(403);
            }

        } catch (NullPointerException e) {
            ctx.result("Please Log in to Use This Function");
            ctx.status(401);

        } catch (SQLClientInfoException e) {
            e.printStackTrace();

        } catch (SQLDataException e) {
            ctx.result("Account Does Not Exist");
            ctx.status(404);
        }
    };

    public Handler displayAllHandler = ctx -> {

        try {
            if (opi.verifyManagerStatus(ctx.req.getSession().getAttribute("LoggedIn").toString())) {
                String allAccounts = opi.displayAll("Manager");

                ctx.result(allAccounts);
                ctx.status(200);

            } else {
                ctx.result("This Account Cannot Does Not Have Access To This Function");
                ctx.status(403);
            }

        }   catch (SQLException e) {
            ctx.result("Please Login to a Managers Account to Access this Function");
            ctx.status(401);
        } catch (NullPointerException e) {
            ctx.result("Please Login to a Managers Account to Access this Function");
            ctx.status(401);
        }
    };

    public Handler pendingHandler = ctx -> {
        try {
            if (opi.verifyManagerStatus(ctx.req.getSession().getAttribute("LoggedIn").toString())) {
                String allAccounts = opi.displayAll("pending");

                ctx.result(allAccounts);
                ctx.status(200);
            } else {
                ctx.result("This Account Cannot Does Not Have Access To This Function");
                ctx.status(403);
            }
        } catch (SQLException e) {
            ctx.result("Please Login to a Managers Account to Access this Function");
            ctx.status(402);

        } catch (NullPointerException e ) {
            ctx.result("Please Login to a Managers Account to Access this Function");
            ctx.status(401);

        }
    };

}
