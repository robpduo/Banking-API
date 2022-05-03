package com.revature;
import com.revature.models.Transactions;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/*Tests the getters and setters in the transaction model*/
public class TestTransactions {

    @Test //Test getter/setter functions
    public void testGetters() {
        Transactions t1 = new Transactions();

        t1.setTransactionId(001);
        t1.setTransactionType("Withdrawal");
        t1.setAmount(100.62);

        assertEquals(1, t1.getTransactionId());
        assertEquals("Withdrawal", t1.getTransactionType());
        assertEquals(100.62, t1.getAmount());

    }
}
