package com.company.Connection;

import com.company.Model.EmpType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    public Connection connection = null;

    public DatabaseConnection(String nome, String psw) throws SQLException {
        try {
            String driver = "org.postgresql.Driver";
            String url = "jdbc:postgresql://localhost:5432/company";
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

    /**
     * Crea un istanza in base all'empType dell'utente loggato
     */
    public static DatabaseConnection baseEmpInstance(EmpType empType) throws SQLException {
        return switch (empType) {
            case junior -> getInstance("junior_user", "junior");
            case middle -> getInstance("middle_user", "middle");
            case senior -> getInstance("senior_user", "senior");
            case manager -> getInstance("manager_user", "manager");
        };
    }

    public static DatabaseConnection projSalariedInstance() throws SQLException {
        return getInstance("project_salaried_user", "proj_sal");
    }
    public static DatabaseConnection ProjAdminInstance() throws SQLException {
        return getInstance("project_admin", "proj_admin");
    }
}