package com.company.Connection;

import java.sql.*;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    public Connection connection = null;
    private String user = "login_user";
    private String passw = "login";
    private String url = "jdbc:postgresql://localhost:5432/company";
    private String driver = "org.postgresql.Driver";

    private DatabaseConnection() throws SQLException {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, passw);
        } catch (ClassNotFoundException err) {
            System.out.println("database connection failed.." + err.getMessage());
        }
    }

    public static DatabaseConnection getInstance() {
        try {
            if (instance == null) {
                instance = new DatabaseConnection();
            } else if (instance.connection.isClosed()) {
                instance = new DatabaseConnection();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return instance;
    }
}
