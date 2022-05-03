package com.revature.models;

/*Users with accounts that opened a bank account on the banking API*/
public class Account extends User {
    private String accountType;
    private String password;
    private double accountBalance;
    private int accountId;

    private boolean isApproved;

    public Account(String email, String firstName, String lastName) {
        super(email, firstName, lastName);
    }

    public Account(long socialNum, String accountType, String password, double balance, String email, String firstName, String lastName) {
        super(email, firstName, lastName, socialNum);
        this.accountType = accountType;
        this.password = password;
        this.accountBalance = accountBalance;
    }

    public Account(String email, String firstName, String lastName, String address, long socialNum,
                   long phoneNum, String accountType, String password, double accountBalance, boolean isApproved) {
        super(email, firstName, lastName, address, socialNum, phoneNum);
        this.accountType = accountType;
        this.password = password;
        this.accountBalance = accountBalance;
        this.isApproved = isApproved;
    }

    public Account() {
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public double getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    @Override
    public String toString() {
        return "Account {" +
                "accountType='" + accountType + '\'' +
                ", password='" + password + '\'' +
                ", accountBalance=" + accountBalance +
                ", isApproved=" + isApproved +
                '}' + "\r\n" +
                "User {" +
                "email='" + getEmail() + '\'' +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", socialNum=" + getSocialNum() +
                ", phoneNum=" + getPhoneNum()+
                '}';
    }
}
