package com.revature.models;

/*helper class used to retrieve specific information to be retrieved from the user*/
public class CreateAccountHelper {
    public String email;
    public String password;
    public String firstName;
    public String lastName;
    public String address;
    public String accountType;
    public long phoneNum;
    public long socialNum;
    public boolean isApproved = false;

    public int authorization = 0;   //An authorization code to create manager accounts
}
