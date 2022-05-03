package com.revature.utils.services;

import com.revature.models.Account;

import java.util.HashMap;
import java.util.Map;


/*Contains a list of accounts, and includes methods that act on the directory with verification checks*/
public class AccountDirectory {
    /*holds the map of all accounts in the directory*/
    private final Map<String, Account> acDriectory = new HashMap<>();

    /*searches through the account directory for a matching account id and returns the account instead of the whole directory*/
    public Account retAccount (String loginName){

        if(acDriectory.containsKey(loginName)) {
            return acDriectory.get(loginName);
        }

        return null;
    }

    /*adds a new account to the directory, validates if there is already an existing account
    if account does not exist (i.e. unique account number) then add to the directory
    assumes all id */
    public void addToDirectory (Account nAccount) {
        if (acDriectory.containsKey(nAccount.getEmail())) {
            return; //username already found in the directory, nothing will happen
        } else {
            acDriectory.put(nAccount.getEmail(), nAccount);
        }
    }

    /*remove an account from the directory give a username*/
    public void rmFromDirectory(String userName) {
        acDriectory.remove(userName);
    }


    public Map<String, Account> getAcDriectory() {
        return acDriectory;
    }
}
