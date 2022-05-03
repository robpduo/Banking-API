package com.revature.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionSingleton {
    public static final String URL = "jdbc:postgresql://localhost:5432/bankapi";
    public static final String USERNAME = "robert";
    public static final String PASSWORD = "password";

    private static ConnectionSingleton cs;

    //Singleton design pattern to be called for creating a connection with the database
    public static synchronized ConnectionSingleton getConnectionSingleton() {
        if(cs == null) {
            cs = new ConnectionSingleton();
        }
        return cs;
    }

    public Connection getConnection () {
        Connection con = null;

        try {
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return con;
    }

}
