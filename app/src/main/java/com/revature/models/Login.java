package com.revature.models;
import com.revature.utils.services.AccountDirectory;

public class Login extends AccountDirectory {

    /*determine if the username and email provided by the user matches an account in the directory*/
    public boolean isPassword (String email, String password) {
        if(password == getAcDriectory().get(email).getPassword()) {
            return true;    // email matches with account password
        }

        return false; //all other reasons
    }

}
