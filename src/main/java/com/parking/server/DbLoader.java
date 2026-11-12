package com.parking.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Runs a query on the MySQL database and returns an updatable result set.
 * The connection settings can be changed with -Ddb.url, -Ddb.user and -Ddb.password.
 */
public final class DbLoader {

    private static final String URL = System.getProperty("db.url", "jdbc:mysql://127.0.0.1:3306/db1");
    private static final String USER = System.getProperty("db.user", "root");
    private static final String PASSWORD = System.getProperty("db.password", "");

    private DbLoader() {
    }

    public static ResultSet executeQuery(String sql) throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        return stmt.executeQuery(sql);
    }
}
