package com.company.Connection;

import com.company.GUI.EmployeeDashboard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    public Connection connection = null;
    private String url = "jdbc:postgresql://localhost:5432/company";
    private String driver = "org.postgresql.Driver";

    public DatabaseConnection(String nome, String psw) throws SQLException {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, nome, psw);
        } catch (ClassNotFoundException err) {
            System.out.println("database connection failed.." + err.getMessage());
        }
    }

    public static DatabaseConnection getInstance(String nome, String psw) throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection(nome, psw);
        } else if (instance.connection.isClosed()) {
            instance = new DatabaseConnection(nome, psw);
        }
        return instance;
    }

}
