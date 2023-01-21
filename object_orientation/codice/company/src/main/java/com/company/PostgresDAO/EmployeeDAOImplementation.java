package com.company.PostgresDAO;

import com.company.Connection.DatabaseConnection;
import com.company.DAO.EmployeeDAO;
import com.company.Model.EmpType;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeDAOImplementation implements EmployeeDAO {
    @Override
    public EmpType baseEmpLogin(String email, String password) {
        try {
            DatabaseConnection db = DatabaseConnection.getInstance("login_user", "login");
            ResultSet resultSet = db.connection.createStatement().executeQuery("select base_emp_login('" + email + "', '" + password + "')");
            resultSet.next();

            String baseEmpLogin = resultSet.getString("base_emp_login");

            if (baseEmpLogin == null) {
                System.out.println("Credenziali errate");
                return null;
            }

            db.connection.close();
            return switch (baseEmpLogin) {
                case "junior" -> EmpType.junior;
                case "middle" -> EmpType.middle;
                case "senior" -> EmpType.senior;
                case "manager" -> EmpType.manager;
                default -> null;
            };
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean projectSalariedLogin(String email, String password) {
        // TODO: Implementare dashboard
        try {
            DatabaseConnection db = DatabaseConnection.getInstance("login_user", "login");
            ResultSet resultSet = db.connection.createStatement().executeQuery("select project_salaried_login('" + email + "', '" + password + "')");
            resultSet.next();

            db.connection.close();
            return resultSet.getString("project_salaried_login").equals("t");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
