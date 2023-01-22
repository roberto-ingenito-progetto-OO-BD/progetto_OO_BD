package com.company.PostgresDAO;

import com.company.Connection.DatabaseConnection;
import com.company.DAO.EmployeeDAO;
import com.company.GUI.EmployeeDashboard;
import com.company.Model.EmpType;
import com.company.Model.Employee;

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


    public Employee getEmployeeInformation(EmpType empTypeConnection,  String email) {
        // creare parametri della connessione in base al tipo settato in fase di login
        String user = empTypeConnection.toString() + "_user";
        String password = empTypeConnection.toString();

        ResultSet resultSet;
        Employee loggedEmployee;

        try {
            // cercare le informazioni di employee nel database
            DatabaseConnection db = DatabaseConnection.getInstance(user, password);
            resultSet = db.connection.createStatement().executeQuery("SELECT base_emp.cf, first_name, last_name , email, base_emp.type, base_emp.role , salary , new_role_date as hire_date " +
                    "FROM base_emp , career_log " +
                    "WHERE base_emp.cf = career_log.cf AND ex_role = '' AND email = '" + email + "'");

            resultSet.next();

            EmpType currentEmployeeType = switch (resultSet.getString("type")) {
                case "junior" -> EmpType.junior;
                case "middle" -> EmpType.middle;
                case "senior" -> EmpType.senior;
                case "manager" -> EmpType.manager;
                default -> null;
            };

            // chiamare il costruttore di employee passando i dati ottenuti dal db
            loggedEmployee = new Employee(
                    resultSet.getString("cf"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("email"),
                    resultSet.getString("role"),
                    resultSet.getFloat("salary"),
                    currentEmployeeType,
                    resultSet.getDate("hire_date").toLocalDate()
            );

            // settare employee da restituire

            System.out.println(resultSet);
            db.connection.close();
            return loggedEmployee;
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
