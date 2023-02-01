package com.company.Connection;

import com.company.Model.EmpType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    public Connection connection = null;

    private DatabaseConnection(String nome, String psw) throws SQLException {
        try {
            String driver = "org.postgresql.Driver";
            String url = "jdbc:postgresql://localhost:5432/company";
            Class.forName(driver);

            connection = DriverManager.getConnection(url, nome, psw);
        } catch (ClassNotFoundException err) {
            System.out.println("database connection failed.." + err.getMessage());
        }
    }

    /**
     * Effettua la connessione al database con le credenziali passate in input
     * @param userName Username dell'utente con il quale si intende effettuare la connessione al database
     * @param psw Password dell'utente con il quale si intende effettuare la connessione al database
     */
    public static DatabaseConnection getInstance(String userName, String psw) throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection(userName, psw);
        } else if (instance.connection.isClosed()) {
            instance = new DatabaseConnection(userName, psw);
        }
        return instance;
    }

    /**
     * Crea un istanza in base all'empType dell'utente loggato
     * @param empType Tipo dell'impiegato loggato con il quale si intende effettuare la connessione al database
     */
    public static DatabaseConnection baseEmpInstance(EmpType empType) throws SQLException {
        return switch (empType) {
            case junior -> getInstance("junior_user", "junior");
            case middle -> getInstance("middle_user", "middle");
            case senior -> getInstance("senior_user", "senior");
            case manager -> getInstance("manager_user", "manager");
        };
    }

    /**
     * Effettua la connessione al database con le credenziali dell'utente project salaried
     */
    public static DatabaseConnection projSalariedInstance() throws SQLException {
        return getInstance("project_salaried_user", "proj_sal");
    }

    /**
     * Effettua la connessione al database con le credenziali dell'admin dello schema del database
     */
    public static DatabaseConnection projAdminInstance() throws SQLException {
        return getInstance("project_admin", "proj_admin");
    }
}