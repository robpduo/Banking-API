package com.revature;
import com.revature.models.LoginInfo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestLoginInfo {
    @Test //Test the all args constructor
    public void TestAllArgs() {
        LoginInfo t1 = new LoginInfo("robpduo@gmail.com", "password");

        assertEquals("robpduo@gmail.com", t1.email);
        assertEquals("password", t1.password);
    }
}