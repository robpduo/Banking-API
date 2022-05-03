package com.revature;
import com.revature.models.Account;
import com.revature.utils.services.AccountDirectory;
import com.revature.models.Login;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestLogin {

    @Test //create a test that logins in correctly and incorrectly
    public void testLogin () {
        AccountDirectory dirTest = new AccountDirectory();
        Account ac1 = new Account(123, "Regular", "password", 500, "rEmail", "Robert", "Duong");
        Account ac2 = new Account(456, "Regular", "password", 700, "vEmail", "Vanessa", "Lau");
        Account ac3 = new Account(526, "Regular", "password", 700, "jEmail", "James", "Que");
        Login t1 = new Login();

        dirTest.addToDirectory(ac1);
        dirTest.addToDirectory(ac2);
        dirTest.addToDirectory(ac3);

        assertEquals(true, t1.isPassword("vEmail", "Password"));
        assertFalse(t1.isPassword("rEmail", "Wrong"));
    }
}
