package com.revature;
import com.revature.utils.services.AccountDirectory;
import com.revature.models.Account;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestAccountDirectory {

    @Test //adds 3 entries to the accounts directory and test directory size and key values
    public void testDirectionExpansion () {
        AccountDirectory dirTest = new AccountDirectory();
        Account ac1 = new Account(123, "Regular", "password", 500, "rEmail", "Robert", "Duong");
        Account ac2 = new Account(456, "Regular", "password", 700, "vEmail", "Vanessa", "Lau");
        Account ac3 = new Account(526, "Regular", "password", 700, "jEmail", "James", "Que");

        dirTest.addToDirectory(ac1);
        dirTest.addToDirectory(ac2);
        dirTest.addToDirectory(ac3);

        assertEquals(3, dirTest.getAcDriectory().size());

        assertEquals("Robert", dirTest.getAcDriectory().get("rEmail").getFirstName());
        assertEquals("Vanessa", dirTest.getAcDriectory().get("vEmail").getFirstName());
        assertEquals("James", dirTest.getAcDriectory().get("jEmail").getFirstName());
    }

    @Test   //test whether the method: retAccount returns the correct account member
    public void testAccountRetrieval () {
        AccountDirectory dirTest = new AccountDirectory();
        Account ac1 = new Account(123, "Regular", "password", 500, "rEmail", "Robert", "Duong");
        Account ac2 = new Account(456, "Regular", "password", 700, "vEmail", "Vanessa", "Lau");
        Account ac3 = new Account(526, "Regular", "password", 700, "jEmail", "James", "Que");

        dirTest.addToDirectory(ac1);
        dirTest.addToDirectory(ac2);
        dirTest.addToDirectory(ac3);

        assertEquals("Vanessa", dirTest.retAccount("vEmail").getFirstName());
    }

    @Test   //enters 3 members into the account directory and removes 2, assert size of directory and name of the remaining account holder
    public void testAccountRemoval () {
        AccountDirectory dirTest = new AccountDirectory();
        Account ac1 = new Account(123, "Regular", "password", 500, "rEmail", "Robert", "Duong");
        Account ac2 = new Account(456, "Regular", "password", 700, "vEmail", "Vanessa", "Lau");
        Account ac3 = new Account(526, "Regular", "password", 700, "jEmail", "James", "Que");

        dirTest.addToDirectory(ac1);
        dirTest.addToDirectory(ac2);
        dirTest.addToDirectory(ac3);

        dirTest.rmFromDirectory("rEmail");
        dirTest.rmFromDirectory("vEmail");

        assertEquals(1, dirTest.getAcDriectory().size());
        assertEquals("James", dirTest.retAccount("jEmail").getFirstName());
    }
}
