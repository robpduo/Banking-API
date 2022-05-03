package com.revature;
import com.revature.models.Account;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestAccountModel {

    @Test //Assign using setter and getter methods
    public void testSetter () {
        Account t1 = new Account();
        t1.setAccountBalance(0.0);
        t1.setPassword("password");
        t1.setEmail("robpduo@gmail.com");
        t1.setFirstName("Robert");
        t1.setLastName(("Duong"));
        t1.setAccountType("Regular");

        assertEquals("Regular", t1.getAccountType());
        assertEquals("password", t1.getPassword());
        assertEquals(0.0, t1.getAccountBalance());
        assertEquals("robpduo@gmail.com", t1.getEmail());
        assertEquals("Robert", t1.getFirstName());
        assertEquals("Duong", t1.getLastName());
    }
}
