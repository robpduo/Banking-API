package com.revature;
import com.revature.controllers.AccountController;
import com.revature.services.Operations;

import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.*;

public class BankingDriver {

    public static void main(String[] args) {

        Operations op = new Operations();
        AccountController ac = new AccountController(op);

        //establish a connection to the local server to receive HTTPs requests and calls
        Javalin server = Javalin.create(config -> {
            config.enableCorsForAllOrigins();
        });

        //Sever Routes Belong in this section
        server.routes( () -> {
            path("users", () -> {
                post("/register", ac.handleCreator);
                post("/login", ac.loginHandler);
                post("/withdraw", ac.withdrawHandler);
                post("/logout", ac.logoutHandler);
                post("/deposit", ac.depositHandler);
                post("/transfer", ac.transferHandler);
            });

            path("manager", () -> {
                post("/display", ac.displayAllHandler);
                post("/delete", ac.deleteAccountsHandler);
            });
        });


        server.start(8000);
    }
}
